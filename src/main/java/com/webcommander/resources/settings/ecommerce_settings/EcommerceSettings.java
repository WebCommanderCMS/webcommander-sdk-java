package com.webcommander.resources.settings.ecommerce_settings;

import com.webcommander.Dto;
import com.webcommander.client.ApiResponse;
import com.webcommander.client.BoundResource;
import com.webcommander.client.RequestOptions;
import com.webcommander.client.ResourceBinding;
import com.webcommander.client.WebCommanderClient;
import com.webcommander.resources.settings.ecommerce_settings.ecommerce_settings_dto
        .EcommerceSettingsDto;

import java.util.LinkedHashMap;
import java.util.Map;

/** Ecommerce settings — {@code /api/{api_version}/admin/settings/ecommerce_settings}. */
public final class EcommerceSettings implements BoundResource {

    private final WebCommanderClient client;

    public EcommerceSettings(WebCommanderClient client) {
        this.client = client;
    }

    /** Read the whole area — {@code GET /admin/settings/ecommerce_settings}. */
    public ApiResponse getEcommerceSettings() {
        return getEcommerceSettings(null, null);
    }

    public ApiResponse getEcommerceSettings(Map<String, Object> queryParams) {
        return getEcommerceSettings(queryParams, null);
    }

    public ApiResponse getEcommerceSettings(Map<String, Object> queryParams,
            RequestOptions options) {
        return client.sendGetRequest(EcommerceSettingsUrls.ECOMMERCE_SETTINGS,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(
                                EcommerceSettingsDto.EcommerceSettings.class, body)).build());
    }

    /** Write fields — {@code PATCH /admin/settings/ecommerce_settings}, in its facade wrapper. */
    public ApiResponse patchEcommerceSettings(Object payload) {
        return patchEcommerceSettings(payload, null);
    }

    public ApiResponse patchEcommerceSettings(Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPatchRequest(EcommerceSettingsUrls.ECOMMERCE_SETTINGS,
                RequestOptions.builder(options).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(
                                EcommerceSettingsDto.EcommerceSettings.class, body)).build());
    }

    /** Ask whether the area is reachable — {@code HEAD /admin/settings/ecommerce_settings}. */
    public ApiResponse headEcommerceSettings() {
        return headEcommerceSettings(null);
    }

    public ApiResponse headEcommerceSettings(RequestOptions options) {
        return client.sendApiRequest("HEAD", EcommerceSettingsUrls.ECOMMERCE_SETTINGS,
                RequestOptions.builder(options).dtoFactory(null).build());
    }

    /** Read one section — {@code GET /admin/settings/ecommerce_settings/{section}}. */
    public ApiResponse getEcommerceSettingsSection(String section) {
        return getEcommerceSettingsSection(section, null, null);
    }

    public ApiResponse getEcommerceSettingsSection(String section,
            Map<String, Object> queryParams) {
        return getEcommerceSettingsSection(section, queryParams, null);
    }

    public ApiResponse getEcommerceSettingsSection(String section,
            Map<String, Object> queryParams, RequestOptions options) {
        return client.sendGetRequest(EcommerceSettingsUrls.ECOMMERCE_SETTINGS_SECTION,
                RequestOptions.builder(options).pathParams(pathParams("section", section))
                        .queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(
                                EcommerceSettingsDto.EcommerceSettings.class, body)).build());
    }

    /** Write one section — {@code PATCH .../ecommerce_settings/{section}}, keyed or bare. */
    public ApiResponse patchEcommerceSettingsSection(String section, Object payload) {
        return patchEcommerceSettingsSection(section, payload, null);
    }

    public ApiResponse patchEcommerceSettingsSection(String section, Object payload,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPatchRequest(EcommerceSettingsUrls.ECOMMERCE_SETTINGS_SECTION,
                RequestOptions.builder(options).pathParams(pathParams("section", section))
                        .jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(
                                EcommerceSettingsDto.EcommerceSettings.class, body)).build());
    }

    private static Map<String, Object> pathParams(String name, Object value) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put(name, value);
        return params;
    }
}
