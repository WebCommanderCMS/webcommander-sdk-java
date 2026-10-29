package com.webcommander.resources.settings.site_settings;

import com.webcommander.Dto;
import com.webcommander.client.ApiResponse;
import com.webcommander.client.BoundResource;
import com.webcommander.client.RequestOptions;
import com.webcommander.client.ResourceBinding;
import com.webcommander.client.WebCommanderClient;
import com.webcommander.resources.settings.site_settings.site_settings_dto.SiteSettingsDto;

import java.util.LinkedHashMap;
import java.util.Map;

/** Site settings — {@code /api/{api_version}/admin/settings/site_settings}. */
public final class SiteSettings implements BoundResource {

    private final WebCommanderClient client;

    public SiteSettings(WebCommanderClient client) {
        this.client = client;
    }

    /** Read the whole area — {@code GET /admin/settings/site_settings}. */
    public ApiResponse getSiteSettings() {
        return getSiteSettings(null, null);
    }

    public ApiResponse getSiteSettings(Map<String, Object> queryParams) {
        return getSiteSettings(queryParams, null);
    }

    public ApiResponse getSiteSettings(Map<String, Object> queryParams, RequestOptions options) {
        return client.sendGetRequest(SiteSettingsUrls.SITE_SETTINGS,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(
                                SiteSettingsDto.SiteSettings.class, body)).build());
    }

    /** Write fields — {@code PATCH /admin/settings/site_settings}. */
    public ApiResponse patchSiteSettings(Object payload) {
        return patchSiteSettings(payload, null);
    }

    public ApiResponse patchSiteSettings(Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPatchRequest(SiteSettingsUrls.SITE_SETTINGS,
                RequestOptions.builder(options).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(
                                SiteSettingsDto.SiteSettings.class, body)).build());
    }

    /** Ask whether the area is reachable — {@code HEAD /admin/settings/site_settings}. */
    public ApiResponse headSiteSettings() {
        return headSiteSettings(null);
    }

    public ApiResponse headSiteSettings(RequestOptions options) {
        return client.sendApiRequest("HEAD", SiteSettingsUrls.SITE_SETTINGS,
                RequestOptions.builder(options).dtoFactory(null).build());
    }

    /** Read one section — {@code GET /admin/settings/site_settings/{section}}. */
    public ApiResponse getSiteSettingsSection(String section) {
        return getSiteSettingsSection(section, null, null);
    }

    public ApiResponse getSiteSettingsSection(String section, Map<String, Object> queryParams) {
        return getSiteSettingsSection(section, queryParams, null);
    }

    public ApiResponse getSiteSettingsSection(String section, Map<String, Object> queryParams,
            RequestOptions options) {
        return client.sendGetRequest(SiteSettingsUrls.SITE_SETTINGS_SECTION,
                RequestOptions.builder(options).pathParams(pathParams("section", section))
                        .queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(
                                SiteSettingsDto.SiteSettings.class, body)).build());
    }

    /** Write one section — {@code PATCH .../site_settings/{section}}, keyed or bare. */
    public ApiResponse patchSiteSettingsSection(String section, Object payload) {
        return patchSiteSettingsSection(section, payload, null);
    }

    public ApiResponse patchSiteSettingsSection(String section, Object payload,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPatchRequest(SiteSettingsUrls.SITE_SETTINGS_SECTION,
                RequestOptions.builder(options).pathParams(pathParams("section", section))
                        .jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(SiteSettingsDto.SiteSettings.class, body))
                        .build());
    }

    private static Map<String, Object> pathParams(String name, Object value) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put(name, value);
        return params;
    }
}
