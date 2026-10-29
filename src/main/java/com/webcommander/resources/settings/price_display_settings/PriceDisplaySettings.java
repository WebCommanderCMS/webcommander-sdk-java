package com.webcommander.resources.settings.price_display_settings;

import com.webcommander.Dto;
import com.webcommander.client.ApiResponse;
import com.webcommander.client.BoundResource;
import com.webcommander.client.RequestOptions;
import com.webcommander.client.ResourceBinding;
import com.webcommander.client.WebCommanderClient;
import com.webcommander.resources.settings.price_display_settings.price_display_settings_dto
        .PriceDisplaySettingsDto;

import java.util.LinkedHashMap;
import java.util.Map;

/** Price display settings — {@code /api/{api_version}/admin/settings/price_display_settings}. */
public final class PriceDisplaySettings implements BoundResource {

    private final WebCommanderClient client;

    public PriceDisplaySettings(WebCommanderClient client) {
        this.client = client;
    }

    /** Read the whole area — {@code GET /admin/settings/price_display_settings}. */
    public ApiResponse getPriceDisplaySettings() {
        return getPriceDisplaySettings(null, null);
    }

    public ApiResponse getPriceDisplaySettings(Map<String, Object> queryParams) {
        return getPriceDisplaySettings(queryParams, null);
    }

    public ApiResponse getPriceDisplaySettings(Map<String, Object> queryParams,
            RequestOptions options) {
        return client.sendGetRequest(PriceDisplaySettingsUrls.PRICE_DISPLAY_SETTINGS,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(
                                PriceDisplaySettingsDto.PriceDisplaySettings.class, body)).build());
    }

    /** Write fields — {@code PATCH .../price_display_settings}, in its facade wrapper. */
    public ApiResponse patchPriceDisplaySettings(Object payload) {
        return patchPriceDisplaySettings(payload, null);
    }

    public ApiResponse patchPriceDisplaySettings(Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPatchRequest(PriceDisplaySettingsUrls.PRICE_DISPLAY_SETTINGS,
                RequestOptions.builder(options).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(
                                PriceDisplaySettingsDto.PriceDisplaySettings.class, body)).build());
    }

    /** Ask whether the area is reachable — {@code HEAD .../price_display_settings}. */
    public ApiResponse headPriceDisplaySettings() {
        return headPriceDisplaySettings(null);
    }

    public ApiResponse headPriceDisplaySettings(RequestOptions options) {
        return client.sendApiRequest("HEAD", PriceDisplaySettingsUrls.PRICE_DISPLAY_SETTINGS,
                RequestOptions.builder(options).dtoFactory(null).build());
    }

    /** Read one section — {@code GET .../price_display_settings/{section}}. */
    public ApiResponse getPriceDisplaySettingsSection(String section) {
        return getPriceDisplaySettingsSection(section, null, null);
    }

    public ApiResponse getPriceDisplaySettingsSection(String section,
            Map<String, Object> queryParams) {
        return getPriceDisplaySettingsSection(section, queryParams, null);
    }

    public ApiResponse getPriceDisplaySettingsSection(String section,
            Map<String, Object> queryParams, RequestOptions options) {
        return client.sendGetRequest(PriceDisplaySettingsUrls.PRICE_DISPLAY_SETTINGS_SECTION,
                RequestOptions.builder(options).pathParams(pathParams("section", section))
                        .queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(
                                PriceDisplaySettingsDto.PriceDisplaySettings.class, body)).build());
    }

    /** Write one section — {@code PATCH .../price_display_settings/{section}}. */
    public ApiResponse patchPriceDisplaySettingsSection(String section, Object payload) {
        return patchPriceDisplaySettingsSection(section, payload, null);
    }

    public ApiResponse patchPriceDisplaySettingsSection(String section, Object payload,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPatchRequest(PriceDisplaySettingsUrls.PRICE_DISPLAY_SETTINGS_SECTION,
                RequestOptions.builder(options).pathParams(pathParams("section", section))
                        .jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(
                                PriceDisplaySettingsDto.PriceDisplaySettings.class, body)).build());
    }

    private static Map<String, Object> pathParams(String name, Object value) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put(name, value);
        return params;
    }
}
