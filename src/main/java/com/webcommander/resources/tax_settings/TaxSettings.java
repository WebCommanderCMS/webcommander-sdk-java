package com.webcommander.resources.tax_settings;

import com.webcommander.Dto;
import com.webcommander.client.ApiResponse;
import com.webcommander.client.BoundResource;
import com.webcommander.client.RequestOptions;
import com.webcommander.client.ResourceBinding;
import com.webcommander.client.WebCommanderClient;
import com.webcommander.resources.tax_settings.tax_settings_dto.TaxSettingsDto;

import java.util.Map;

/** The tax settings facade — {@code /api/{api_version}/admin/tax/settings}, flat. */
public final class TaxSettings implements BoundResource {

    private final WebCommanderClient client;

    public TaxSettings(WebCommanderClient client) {
        this.client = client;
    }

    /** Read the tax settings — {@code GET /admin/tax/settings}. */
    public ApiResponse getTaxSettings() {
        return getTaxSettings(null, null);
    }

    public ApiResponse getTaxSettings(Map<String, Object> queryParams) {
        return getTaxSettings(queryParams, null);
    }

    public ApiResponse getTaxSettings(Map<String, Object> queryParams, RequestOptions options) {
        return client.sendGetRequest(TaxSettingsUrls.TAX_SETTINGS,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(TaxSettingsDto.TaxSettings.class, body))
                        .build());
    }

    /** Write the tax settings — {@code PUT /admin/tax/settings}, wrapped in {@code settings}. */
    public ApiResponse putTaxSettings(Object payload) {
        return putTaxSettings(payload, null);
    }

    public ApiResponse putTaxSettings(Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPutRequest(TaxSettingsUrls.TAX_SETTINGS,
                RequestOptions.builder(options).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(TaxSettingsDto.TaxSettings.class, body))
                        .build());
    }

    public ApiResponse putTaxSettings(TaxSettingsDto.TaxSettingsInput payload) {
        return putTaxSettings((Object) payload, null);
    }

    public ApiResponse putTaxSettings(TaxSettingsDto.TaxSettingsInput payload,
            RequestOptions options) {
        return putTaxSettings((Object) payload, options);
    }
}
