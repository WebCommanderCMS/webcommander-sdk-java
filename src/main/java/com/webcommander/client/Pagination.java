package com.webcommander.client;

import com.webcommander.errors.SdkError;
import com.webcommander.errors.SdkUsageError;
import com.webcommander.http.QueryParams;
import com.webcommander.http.Transport;
import okhttp3.HttpUrl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/** A lazy walk of a paginated listing, following {@code pagination.next_page} to its end. */
public final class Pagination {

    /** Between a cycle key's parts. */
    private static final String SEPARATOR = String.valueOf((char) 0);

    /** Between one cycle key's query pairs. */
    private static final String PAIR_SEPARATOR = String.valueOf((char) 1);

    private Pagination() {}

    /** One {@link ApiResponse} per page; {@code queryParams} go with the first request only. */
    public static Iterable<ApiResponse> iterPages(
            WebCommanderClient client, String path, Map<String, Object> queryParams) {
        return iterPages(client, path, queryParams, null, true);
    }

    /** As above, capped at {@code maxPages}; a value below 1 is refused. */
    public static Iterable<ApiResponse> iterPages(
            WebCommanderClient client, String path, Map<String, Object> queryParams,
            Integer maxPages, boolean raiseOnError) {
        if (maxPages != null && maxPages < 1) {
            throw new SdkUsageError("maxPages: expected a positive integer or null, got " + maxPages);
        }
        return () -> new PageIterator(client, path, queryParams, maxPages, raiseOnError);
    }

    /** Each record of a listing across every page, the envelope stripped. */
    public static Iterable<Map<String, Object>> iterRecords(
            WebCommanderClient client, String path, String recordKey,
            Map<String, Object> queryParams) {
        return iterRecords(client, path, recordKey, queryParams, null);
    }

    /** As above, capped at {@code maxPages}; a value below 1 is refused. */
    public static Iterable<Map<String, Object>> iterRecords(
            WebCommanderClient client, String path, String recordKey,
            Map<String, Object> queryParams, Integer maxPages) {
        if (recordKey == null || recordKey.strip().isEmpty()) {
            throw new SdkUsageError("recordKey: expected the envelope key holding the "
                    + "records, like 'customers', got '" + recordKey + "'");
        }
        Iterable<ApiResponse> pages = iterPages(client, path, queryParams, maxPages, true);
        return () -> new RecordIterator(pages.iterator(), recordKey);
    }

    private static final class PageIterator implements Iterator<ApiResponse> {
        private final WebCommanderClient client;
        private final String firstPath;
        private final Map<String, Object> firstQuery;
        private final Integer maxPages;
        private final boolean raiseOnError;
        private final Set<String> visited = new HashSet<>();

        private ApiResponse pending;
        private ApiResponse lastYielded;
        private boolean started;
        private boolean done;
        private int yielded;

        private PageIterator(WebCommanderClient client, String firstPath,
                Map<String, Object> firstQuery, Integer maxPages, boolean raiseOnError) {
            this.client = client;
            this.firstPath = firstPath;
            this.firstQuery = firstQuery;
            this.maxPages = maxPages;
            this.raiseOnError = raiseOnError;
        }

        @Override
        public boolean hasNext() {
            if (pending != null) {
                return true;
            }
            if (done) {
                return false;
            }
            if (!started) {
                pending = client.sendGetRequest(firstPath, RequestOptions.builder()
                        .queryParams(firstQuery).raiseOnError(raiseOnError).build());
                started = true;
                visited.add(cycleKey(firstPath, firstQuery));
                return true;
            }
            if (maxPages != null && yielded >= maxPages) {
                done = true;
                return false;
            }
            String nextPage = readNextPage(lastYielded.getMap());
            if (nextPage == null) {
                done = true;
                return false;
            }
            String nextKey = cycleKey(nextPage, null);
            if (visited.contains(nextKey)) {
                done = true;
                throw new SdkError("pagination loop: next_page repeats an earlier page");
            }
            pending = client.sendGetRequest(nextPage,
                    RequestOptions.builder().raiseOnError(raiseOnError).build());
            // Recorded after a successful fetch, so a retried page is not called a loop.
            visited.add(nextKey);
            return true;
        }

        /** One page's cycle key: the URL actually requested, in the form two spellings share. */
        private String cycleKey(String pathOrUrl, Map<String, Object> queryParams) {
            HttpUrl full = Transport.buildRequestUrl(client.resolvePageUrl(pathOrUrl),
                    QueryParams.buildQueryParams(queryParams));
            List<String> pairs = new ArrayList<>(full.querySize());
            for (int i = 0; i < full.querySize(); i++) {
                String value = full.queryParameterValue(i);
                pairs.add(full.queryParameterName(i) + SEPARATOR + (value == null ? "" : value));
            }
            pairs.sort(null);
            return full.scheme() + "://" + full.host() + ":" + full.port()
                    + full.encodedPath() + SEPARATOR + String.join(PAIR_SEPARATOR, pairs);
        }

        @Override
        public ApiResponse next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            lastYielded = pending;
            pending = null;
            yielded++;
            if (!lastYielded.succeeded()) {
                done = true;
            }
            return lastYielded;
        }

    }

    /** The next page's URL; anything but a non-blank string reads as the end. */
    private static String readNextPage(Object data) {
        if (!(data instanceof Map<?, ?> body)
                || !(body.get("pagination") instanceof Map<?, ?> pagination)
                || !(pagination.get("next_page") instanceof String nextPage)
                || nextPage.strip().isEmpty()) {
            return null;
        }
        return nextPage;
    }

    private static final class RecordIterator implements Iterator<Map<String, Object>> {
        private final Iterator<ApiResponse> pages;
        private final String recordKey;
        private Iterator<Map<String, Object>> currentPage = Collections.emptyIterator();

        private RecordIterator(Iterator<ApiResponse> pages, String recordKey) {
            this.pages = pages;
            this.recordKey = recordKey;
        }

        @Override
        public boolean hasNext() {
            while (!currentPage.hasNext()) {
                if (!pages.hasNext()) {
                    return false;
                }
                currentPage = readRecords(pages.next().getMap(), recordKey).iterator();
            }
            return true;
        }

        @Override
        public Map<String, Object> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return currentPage.next();
        }
    }

    /** One page's record list, never {@code null}, or a refusal naming the bad shape. */
    @SuppressWarnings("unchecked")
    private static List<Map<String, Object>> readRecords(Object body, String recordKey) {
        if (body instanceof Map<?, ?> envelope
                && envelope.get(recordKey) instanceof List<?> records) {
            requireRecordObjects(records, recordKey);
            return (List<Map<String, Object>>) records;
        }
        String available = body instanceof Map<?, ?> envelope
                ? String.join(", ", envelope.keySet().stream().map(String::valueOf).sorted().toList())
                : "a " + (body == null ? "null" : body.getClass().getSimpleName()) + " body";
        throw new SdkUsageError("recordKey '" + recordKey + "': this page has no "
                + "list under that key; it carries " + available);
    }

    /** Refused before the unchecked cast above hands the list on as records. */
    private static void requireRecordObjects(List<?> records, String recordKey) {
        int position = 0;
        for (Object record : records) {
            if (record != null && !(record instanceof Map<?, ?>)) {
                throw new SdkUsageError("recordKey '" + recordKey + "': element "
                        + position + " of the list under that key is a "
                        + record.getClass().getSimpleName() + " rather than a record "
                        + "object; iterPages walks a listing whose entries are not "
                        + "objects, iterRecords cannot");
            }
            position++;
        }
    }
}
