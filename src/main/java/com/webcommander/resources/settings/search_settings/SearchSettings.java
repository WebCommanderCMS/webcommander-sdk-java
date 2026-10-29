package com.webcommander.resources.settings.search_settings;

import com.webcommander.Dto;
import com.webcommander.client.ApiResponse;
import com.webcommander.client.BoundResource;
import com.webcommander.client.RequestOptions;
import com.webcommander.client.ResourceBinding;
import com.webcommander.client.WebCommanderClient;
import com.webcommander.resources.settings.search_settings.search_settings_dto
        .SearchSettingsDto;

import java.util.LinkedHashMap;
import java.util.Map;

/** Search settings — {@code /api/{api_version}/admin/settings/search_settings}. */
public final class SearchSettings implements BoundResource {

    private final WebCommanderClient client;

    public SearchSettings(WebCommanderClient client) {
        this.client = client;
    }

    /** Read the whole area — {@code GET /admin/settings/search_settings}. */
    public ApiResponse getSearchSettings() {
        return getSearchSettings(null, null);
    }

    public ApiResponse getSearchSettings(Map<String, Object> queryParams) {
        return getSearchSettings(queryParams, null);
    }

    public ApiResponse getSearchSettings(Map<String, Object> queryParams, RequestOptions options) {
        return client.sendGetRequest(SearchSettingsUrls.SEARCH_SETTINGS,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(
                                SearchSettingsDto.SearchSettings.class, body)).build());
    }

    /** Write fields — {@code PATCH /admin/settings/search_settings}. */
    public ApiResponse patchSearchSettings(Object payload) {
        return patchSearchSettings(payload, null);
    }

    public ApiResponse patchSearchSettings(Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPatchRequest(SearchSettingsUrls.SEARCH_SETTINGS,
                RequestOptions.builder(options).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(
                                SearchSettingsDto.SearchSettings.class, body)).build());
    }

    /** Replace the whole area — {@code PUT /admin/settings/search_settings}. */
    public ApiResponse putSearchSettings(Object payload) {
        return putSearchSettings(payload, null);
    }

    public ApiResponse putSearchSettings(Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPutRequest(SearchSettingsUrls.SEARCH_SETTINGS,
                RequestOptions.builder(options).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(
                                SearchSettingsDto.SearchSettings.class, body)).build());
    }

    /** Ask whether the area is reachable — {@code HEAD /admin/settings/search_settings}. */
    public ApiResponse headSearchSettings() {
        return headSearchSettings(null);
    }

    public ApiResponse headSearchSettings(RequestOptions options) {
        return client.sendApiRequest("HEAD", SearchSettingsUrls.SEARCH_SETTINGS,
                RequestOptions.builder(options).dtoFactory(null).build());
    }

    /** Read one section — {@code GET /admin/settings/search_settings/{section}}. */
    public ApiResponse getSearchSettingsSection(String section) {
        return getSearchSettingsSection(section, null, null);
    }

    public ApiResponse getSearchSettingsSection(String section, Map<String, Object> queryParams) {
        return getSearchSettingsSection(section, queryParams, null);
    }

    public ApiResponse getSearchSettingsSection(String section, Map<String, Object> queryParams,
            RequestOptions options) {
        return client.sendGetRequest(SearchSettingsUrls.SEARCH_SETTINGS_SECTION,
                RequestOptions.builder(options).pathParams(pathParams("section", section))
                        .queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(
                                SearchSettingsDto.SearchSettings.class, body)).build());
    }

    /** Write one section — {@code PATCH .../search_settings/{section}}, wrapped in its key. */
    public ApiResponse patchSearchSettingsSection(String section, Object payload) {
        return patchSearchSettingsSection(section, payload, null);
    }

    public ApiResponse patchSearchSettingsSection(String section, Object payload,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPatchRequest(SearchSettingsUrls.SEARCH_SETTINGS_SECTION,
                RequestOptions.builder(options).pathParams(pathParams("section", section))
                        .jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(
                                SearchSettingsDto.SearchSettings.class, body)).build());
    }

    /** Replace one section — {@code PUT .../search_settings/{section}}, wrapped in its key. */
    public ApiResponse putSearchSettingsSection(String section, Object payload) {
        return putSearchSettingsSection(section, payload, null);
    }

    public ApiResponse putSearchSettingsSection(String section, Object payload,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPutRequest(SearchSettingsUrls.SEARCH_SETTINGS_SECTION,
                RequestOptions.builder(options).pathParams(pathParams("section", section))
                        .jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(
                                SearchSettingsDto.SearchSettings.class, body)).build());
    }

    /** Read where the reindex stands — {@code GET .../search_settings/reindex_status}. */
    public ApiResponse getSearchSettingsReindexStatus() {
        return getSearchSettingsReindexStatus(null, null);
    }

    public ApiResponse getSearchSettingsReindexStatus(Map<String, Object> queryParams) {
        return getSearchSettingsReindexStatus(queryParams, null);
    }

    public ApiResponse getSearchSettingsReindexStatus(Map<String, Object> queryParams,
            RequestOptions options) {
        return client.sendGetRequest(SearchSettingsUrls.SEARCH_SETTINGS_REINDEX_STATUS,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(
                                SearchSettingsDto.ReindexStatus.class, body)).build());
    }

    /** Ask whether the reindex status is reachable — {@code HEAD .../reindex_status}; no body. */
    public ApiResponse headSearchSettingsReindexStatus() {
        return headSearchSettingsReindexStatus(null);
    }

    public ApiResponse headSearchSettingsReindexStatus(RequestOptions options) {
        return client.sendApiRequest("HEAD", SearchSettingsUrls.SEARCH_SETTINGS_REINDEX_STATUS,
                RequestOptions.builder(options).dtoFactory(null).build());
    }

    private static Map<String, Object> pathParams(String name, Object value) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put(name, value);
        return params;
    }
}
