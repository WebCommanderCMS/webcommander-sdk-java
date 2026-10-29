package com.webcommander.resources.settings.shipping_settings;

import com.webcommander.Dto;
import com.webcommander.client.ApiResponse;
import com.webcommander.client.BoundResource;
import com.webcommander.client.RequestOptions;
import com.webcommander.client.ResourceBinding;
import com.webcommander.client.WebCommanderClient;
import com.webcommander.resources.settings.shipping_settings.shipping_settings_dto
        .ShippingSettingsDto;

import java.util.Map;

/** Shipping settings — {@code /api/{api_version}/admin/settings/shipping_settings}. */
public final class ShippingSettings implements BoundResource {

    private final WebCommanderClient client;

    public ShippingSettings(WebCommanderClient client) {
        this.client = client;
    }

    /** Read the whole area — {@code GET /admin/settings/shipping_settings}. */
    public ApiResponse getShippingSettings() {
        return getShippingSettings(null, null);
    }

    public ApiResponse getShippingSettings(Map<String, Object> queryParams) {
        return getShippingSettings(queryParams, null);
    }

    public ApiResponse getShippingSettings(Map<String, Object> queryParams,
            RequestOptions options) {
        return client.sendGetRequest(ShippingSettingsUrls.SHIPPING_SETTINGS,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(
                                ShippingSettingsDto.ShippingSettings.class, body)).build());
    }

    /** Write one field or several — {@code PATCH /admin/settings/shipping_settings}. */
    public ApiResponse patchShippingSettings(Object payload) {
        return patchShippingSettings(payload, null);
    }

    public ApiResponse patchShippingSettings(Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPatchRequest(ShippingSettingsUrls.SHIPPING_SETTINGS,
                RequestOptions.builder(options).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(
                                ShippingSettingsDto.ShippingSettings.class, body)).build());
    }

    /** Ask whether the area is reachable — {@code HEAD /admin/settings/shipping_settings}. */
    public ApiResponse headShippingSettings() {
        return headShippingSettings(null);
    }

    public ApiResponse headShippingSettings(RequestOptions options) {
        return client.sendApiRequest("HEAD", ShippingSettingsUrls.SHIPPING_SETTINGS,
                RequestOptions.builder(options).dtoFactory(null).build());
    }
}
