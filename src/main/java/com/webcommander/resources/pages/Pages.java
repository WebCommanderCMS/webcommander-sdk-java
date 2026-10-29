package com.webcommander.resources.pages;

import com.webcommander.Dto;
import com.webcommander.client.ApiResponse;
import com.webcommander.client.BoundResource;
import com.webcommander.client.RequestOptions;
import com.webcommander.client.ResourceBinding;
import com.webcommander.client.WebCommanderClient;
import com.webcommander.resources.pages.pages_dto.PagesDto.Page;
import com.webcommander.resources.pages.pages_dto.PagesDto.PageCount;
import com.webcommander.resources.pages.pages_dto.PagesDto.PageDeleteBlockers;
import com.webcommander.resources.pages.pages_dto.PagesDto.PageInput;
import com.webcommander.resources.pages.pages_dto.PagesDto.PageListing;
import com.webcommander.resources.pages.pages_dto.PagesDto.PageUrlInput;

import java.util.LinkedHashMap;
import java.util.Map;

/** Pages — {@code /api/{api_version}/admin/pages}; one of them is the store's landing page. */
public final class Pages implements BoundResource {

    private final WebCommanderClient client;

    public Pages(WebCommanderClient client) {
        this.client = client;
    }

    /**
     * List the pages outside the trash, newest change first — {@code GET /admin/pages}.
     * {@code search}, {@code active}, {@code has_layout}, {@code visibility},
     * {@code created_from}/{@code created_to}, {@code updated_from}/{@code updated_to}
     * ({@code YYYY-MM-DD}), {@code author}, {@code store} and {@code not_in_store} filter;
     * {@code sort} and {@code order} order; {@code limit} is 1 to 100 or {@code all}. Unknown
     * names are {@code 400}.
     */
    public ApiResponse listPages() {
        return listPages(null, null);
    }

    public ApiResponse listPages(Map<String, Object> queryParams) {
        return listPages(queryParams, null);
    }

    public ApiResponse listPages(Map<String, Object> queryParams, RequestOptions options) {
        return client.sendGetRequest(PagesUrls.PAGES,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(PageListing.class, body)).build());
    }

    /** Ask whether the collection is reachable — {@code HEAD /admin/pages}; no body. */
    public ApiResponse headPages() {
        return headPages(null);
    }

    public ApiResponse headPages(RequestOptions options) {
        return client.sendApiRequest("HEAD", PagesUrls.PAGES,
                RequestOptions.builder(options).dtoFactory(null).build());
    }

    /**
     * How many pages match — {@code GET /admin/pages/count}, a bare {@code {"total": N}}. The
     * listing's filters narrow it; {@code sort}, {@code order}, {@code limit} and {@code offset}
     * are ignored.
     */
    public ApiResponse countPages() {
        return countPages(null, null);
    }

    public ApiResponse countPages(Map<String, Object> queryParams) {
        return countPages(queryParams, null);
    }

    public ApiResponse countPages(Map<String, Object> queryParams, RequestOptions options) {
        return client.sendGetRequest(PagesUrls.PAGES_COUNT,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(PageCount.class, body)).build());
    }

    /** Ask whether the count is reachable — {@code HEAD /admin/pages/count}; no body. */
    public ApiResponse headPagesCount() {
        return headPagesCount(null);
    }

    public ApiResponse headPagesCount(RequestOptions options) {
        return client.sendApiRequest("HEAD", PagesUrls.PAGES_COUNT,
                RequestOptions.builder(options).dtoFactory(null).build());
    }

    /**
     * Create a page — {@code POST /admin/pages} with {@code {"page": {...}}}, answered
     * {@code 201}. {@code name} and {@code visibility} are required; the URL is made from the name.
     */
    public ApiResponse createPage(Object payload) {
        return createPage(payload, null);
    }

    public ApiResponse createPage(Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPostRequest(PagesUrls.PAGES,
                RequestOptions.builder(options).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(Page.class, body)).build());
    }

    public ApiResponse createPage(PageInput payload) {
        return createPage((Object) payload, null);
    }

    public ApiResponse createPage(PageInput payload, RequestOptions options) {
        return createPage((Object) payload, options);
    }

    /**
     * Retrieve one page, with an {@code ETag} — {@code GET /admin/pages/{page_id}}; a page in the
     * trash still reads, {@code in_trash} true.
     */
    public ApiResponse getPage(Object pageId) {
        return getPage(pageId, null);
    }

    public ApiResponse getPage(Object pageId, RequestOptions options) {
        return client.sendGetRequest(PagesUrls.PAGE,
                RequestOptions.builder(options).pathParams(pathParams(pageId))
                        .dtoFactory(body -> Dto.fromMap(Page.class, body)).build());
    }

    /** Probe one page — {@code HEAD /admin/pages/{page_id}}; its read's ETag, no body. */
    public ApiResponse headPage(Object pageId) {
        return headPage(pageId, null);
    }

    public ApiResponse headPage(Object pageId, RequestOptions options) {
        return client.sendApiRequest("HEAD", PagesUrls.PAGE,
                RequestOptions.builder(options).pathParams(pathParams(pageId)).dtoFactory(null)
                        .build());
    }

    /**
     * Replace one page — {@code PUT /admin/pages/{page_id}}. {@code name}, {@code title},
     * {@code subtitle}, {@code url}, {@code layout}, {@code visibility}, {@code body_class},
     * {@code active}, {@code disable_tracking} and {@code password_protection} are required, a
     * partial body is {@code 400}. A non-null {@code ifMatch} is sent as {@code If-Match}: a
     * stale {@code ETag} is {@code 412}, checked before the body.
     */
    public ApiResponse updatePage(Object pageId, Object payload) {
        return updatePage(pageId, payload, null, null);
    }

    public ApiResponse updatePage(Object pageId, Object payload, String ifMatch) {
        return updatePage(pageId, payload, ifMatch, null);
    }

    public ApiResponse updatePage(Object pageId, Object payload, RequestOptions options) {
        return updatePage(pageId, payload, null, options);
    }

    public ApiResponse updatePage(Object pageId, Object payload, String ifMatch,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPutRequest(PagesUrls.PAGE,
                RequestOptions.builder(options).pathParams(pathParams(pageId)).jsonBody(payload)
                        .headers(withIfMatch(options, ifMatch))
                        .dtoFactory(body -> Dto.fromMap(Page.class, body)).build());
    }

    public ApiResponse updatePage(Object pageId, PageInput payload) {
        return updatePage(pageId, (Object) payload, null, null);
    }

    public ApiResponse updatePage(Object pageId, PageInput payload, String ifMatch) {
        return updatePage(pageId, (Object) payload, ifMatch, null);
    }

    public ApiResponse updatePage(Object pageId, PageInput payload, RequestOptions options) {
        return updatePage(pageId, (Object) payload, null, options);
    }

    public ApiResponse updatePage(Object pageId, PageInput payload, String ifMatch,
            RequestOptions options) {
        return updatePage(pageId, (Object) payload, ifMatch, options);
    }

    /**
     * Change only the fields sent — {@code PATCH /admin/pages/{page_id}}; an empty {@code page}
     * is {@code 400} and a page in the trash {@code 409}. A non-null {@code ifMatch} is sent as
     * {@code If-Match}.
     */
    public ApiResponse patchPage(Object pageId, Object payload) {
        return patchPage(pageId, payload, null, null);
    }

    public ApiResponse patchPage(Object pageId, Object payload, String ifMatch) {
        return patchPage(pageId, payload, ifMatch, null);
    }

    public ApiResponse patchPage(Object pageId, Object payload, RequestOptions options) {
        return patchPage(pageId, payload, null, options);
    }

    public ApiResponse patchPage(Object pageId, Object payload, String ifMatch,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPatchRequest(PagesUrls.PAGE,
                RequestOptions.builder(options).pathParams(pathParams(pageId)).jsonBody(payload)
                        .headers(withIfMatch(options, ifMatch))
                        .dtoFactory(body -> Dto.fromMap(Page.class, body)).build());
    }

    public ApiResponse patchPage(Object pageId, PageInput payload) {
        return patchPage(pageId, (Object) payload, null, null);
    }

    public ApiResponse patchPage(Object pageId, PageInput payload, String ifMatch) {
        return patchPage(pageId, (Object) payload, ifMatch, null);
    }

    public ApiResponse patchPage(Object pageId, PageInput payload, RequestOptions options) {
        return patchPage(pageId, (Object) payload, null, options);
    }

    public ApiResponse patchPage(Object pageId, PageInput payload, String ifMatch,
            RequestOptions options) {
        return patchPage(pageId, (Object) payload, ifMatch, options);
    }

    /**
     * Move a page to the trash — {@code DELETE /admin/pages/{page_id}}; {@code 204}.
     * {@code force} {@code true} purges it, from the trash or live; a second plain delete is
     * {@code 400}, the landing page {@code 409}. A non-null {@code ifMatch} is sent as
     * {@code If-Match}.
     */
    public ApiResponse deletePage(Object pageId) {
        return deletePage(pageId, null, null, null);
    }

    public ApiResponse deletePage(Object pageId, Map<String, Object> queryParams) {
        return deletePage(pageId, queryParams, null, null);
    }

    public ApiResponse deletePage(Object pageId, Map<String, Object> queryParams,
            String ifMatch) {
        return deletePage(pageId, queryParams, ifMatch, null);
    }

    public ApiResponse deletePage(Object pageId, Map<String, Object> queryParams,
            RequestOptions options) {
        return deletePage(pageId, queryParams, null, options);
    }

    public ApiResponse deletePage(Object pageId, Map<String, Object> queryParams, String ifMatch,
            RequestOptions options) {
        return client.sendDeleteRequest(PagesUrls.PAGE,
                RequestOptions.builder(options).pathParams(pathParams(pageId))
                        .queryParams(queryParams).headers(withIfMatch(options, ifMatch))
                        .dtoFactory(null).build());
    }

    /**
     * Move a page to a new URL — {@code PUT /admin/pages/{page_id}/url} with a bare
     * {@code {"url": ...}}; answers the page. A URL another page holds is {@code 409}.
     */
    public ApiResponse updatePageUrl(Object pageId, Object payload) {
        return updatePageUrl(pageId, payload, null);
    }

    public ApiResponse updatePageUrl(Object pageId, Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPutRequest(PagesUrls.PAGE_URL,
                RequestOptions.builder(options).pathParams(pathParams(pageId)).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(Page.class, body)).build());
    }

    public ApiResponse updatePageUrl(Object pageId, PageUrlInput payload) {
        return updatePageUrl(pageId, (Object) payload, null);
    }

    public ApiResponse updatePageUrl(Object pageId, PageUrlInput payload,
            RequestOptions options) {
        return updatePageUrl(pageId, (Object) payload, options);
    }

    /**
     * Make a page the store's landing page — {@code PATCH .../{page_id}/landing}; takes no body.
     * The previous landing page stops being it.
     */
    public ApiResponse setLandingPage(Object pageId) {
        return setLandingPage(pageId, null);
    }

    public ApiResponse setLandingPage(Object pageId, RequestOptions options) {
        return client.sendPatchRequest(PagesUrls.PAGE_LANDING,
                RequestOptions.builder(options).pathParams(pathParams(pageId))
                        .dtoFactory(body -> Dto.fromMap(Page.class, body)).build());
    }

    /**
     * Whether a delete would pass and what blocks it, such as {@code landing_page} —
     * {@code GET /admin/pages/{page_id}/delete-blockers}.
     */
    public ApiResponse getPageDeleteBlockers(Object pageId) {
        return getPageDeleteBlockers(pageId, null);
    }

    public ApiResponse getPageDeleteBlockers(Object pageId, RequestOptions options) {
        return client.sendGetRequest(PagesUrls.PAGE_DELETE_BLOCKERS,
                RequestOptions.builder(options).pathParams(pathParams(pageId))
                        .dtoFactory(body -> Dto.fromMap(PageDeleteBlockers.class, body)).build());
    }

    /** Ask whether the delete check is reachable — {@code HEAD .../{page_id}/delete-blockers}. */
    public ApiResponse headPageDeleteBlockers(Object pageId) {
        return headPageDeleteBlockers(pageId, null);
    }

    public ApiResponse headPageDeleteBlockers(Object pageId, RequestOptions options) {
        return client.sendApiRequest("HEAD", PagesUrls.PAGE_DELETE_BLOCKERS,
                RequestOptions.builder(options).pathParams(pathParams(pageId)).dtoFactory(null)
                        .build());
    }

    /**
     * Copy a page — {@code POST /admin/pages/{page_id}/duplicate}, answered {@code 201}; sends no
     * body. The copy is {@code "<name> - Copy"} at {@code <url>-copy}, then {@code - Copy 1}.
     */
    public ApiResponse duplicatePage(Object pageId) {
        return duplicatePage(pageId, null);
    }

    public ApiResponse duplicatePage(Object pageId, RequestOptions options) {
        return client.sendPostRequest(PagesUrls.PAGE_DUPLICATE,
                RequestOptions.builder(options).pathParams(pathParams(pageId))
                        .dtoFactory(body -> Dto.fromMap(Page.class, body)).build());
    }

    private static Map<String, String> withIfMatch(RequestOptions options, String ifMatch) {
        Map<String, String> given = options == null ? null : options.getHeaders();
        if (ifMatch == null) {
            return given;
        }
        Map<String, String> headers =
                given == null ? new LinkedHashMap<>() : new LinkedHashMap<>(given);
        headers.put(PagesUrls.IF_MATCH_HEADER, ifMatch);
        return headers;
    }

    private static Map<String, Object> pathParams(Object pageId) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("page_id", pageId);
        return params;
    }
}
