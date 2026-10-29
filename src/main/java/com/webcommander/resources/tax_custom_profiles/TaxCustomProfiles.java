package com.webcommander.resources.tax_custom_profiles;

import com.webcommander.Dto;
import com.webcommander.client.ApiResponse;
import com.webcommander.client.BoundResource;
import com.webcommander.client.RequestOptions;
import com.webcommander.client.ResourceBinding;
import com.webcommander.client.WebCommanderClient;
import com.webcommander.resources.tax_custom_profiles.tax_custom_profiles_dto
        .TaxCustomProfilesDto.CustomProfile;
import com.webcommander.resources.tax_custom_profiles.tax_custom_profiles_dto
        .TaxCustomProfilesDto.CustomProfileInput;
import com.webcommander.resources.tax_custom_profiles.tax_custom_profiles_dto
        .TaxCustomProfilesDto.CustomProfilePage;

import java.util.LinkedHashMap;
import java.util.Map;

/** Custom tax profiles — {@code /api/{api_version}/admin/tax/custom-profiles}; no HEAD. */
public final class TaxCustomProfiles implements BoundResource {

    private final WebCommanderClient client;

    public TaxCustomProfiles(WebCommanderClient client) {
        this.client = client;
    }

    /** List the custom profiles — {@code GET /admin/tax/custom-profiles}, newest first. */
    public ApiResponse listTaxCustomProfiles() {
        return listTaxCustomProfiles(null, null);
    }

    public ApiResponse listTaxCustomProfiles(Map<String, Object> queryParams) {
        return listTaxCustomProfiles(queryParams, null);
    }

    public ApiResponse listTaxCustomProfiles(Map<String, Object> queryParams,
            RequestOptions options) {
        return client.sendGetRequest(TaxCustomProfilesUrls.TAX_CUSTOM_PROFILES,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(CustomProfilePage.class, body)).build());
    }

    /** Create a custom profile — {@code POST /admin/tax/custom-profiles}; {@code 201}. */
    public ApiResponse createTaxCustomProfile(Object payload) {
        return createTaxCustomProfile(payload, null, null);
    }

    public ApiResponse createTaxCustomProfile(Object payload, RequestOptions options) {
        return createTaxCustomProfile(payload, null, options);
    }

    public ApiResponse createTaxCustomProfile(Object payload, String transactionNo) {
        return createTaxCustomProfile(payload, transactionNo, null);
    }

    public ApiResponse createTaxCustomProfile(Object payload, String transactionNo,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        RequestOptions given = options == null ? RequestOptions.none() : options;
        Map<String, String> headers = new LinkedHashMap<>();
        if (given.getHeaders() != null) {
            headers.putAll(given.getHeaders());
        }
        if (transactionNo != null) {
            headers.put(TaxCustomProfilesUrls.TRANSACTION_NO_HEADER, transactionNo);
        }
        return client.sendPostRequest(TaxCustomProfilesUrls.TAX_CUSTOM_PROFILES,
                RequestOptions.builder(options).jsonBody(payload).headers(headers)
                        .dtoFactory(body -> Dto.fromMap(CustomProfile.class, body)).build());
    }

    public ApiResponse createTaxCustomProfile(CustomProfileInput payload) {
        return createTaxCustomProfile((Object) payload, null, null);
    }

    public ApiResponse createTaxCustomProfile(CustomProfileInput payload, RequestOptions options) {
        return createTaxCustomProfile((Object) payload, null, options);
    }

    public ApiResponse createTaxCustomProfile(CustomProfileInput payload, String transactionNo) {
        return createTaxCustomProfile((Object) payload, transactionNo, null);
    }

    public ApiResponse createTaxCustomProfile(CustomProfileInput payload, String transactionNo,
            RequestOptions options) {
        return createTaxCustomProfile((Object) payload, transactionNo, options);
    }

    /** Retrieve one custom profile — {@code GET .../custom-profiles/{custom_profile_id}}. */
    public ApiResponse getTaxCustomProfile(Object customProfileId) {
        return getTaxCustomProfile(customProfileId, null);
    }

    public ApiResponse getTaxCustomProfile(Object customProfileId, RequestOptions options) {
        return client.sendGetRequest(TaxCustomProfilesUrls.TAX_CUSTOM_PROFILE,
                RequestOptions.builder(options).pathParams(pathParams(customProfileId))
                        .dtoFactory(body -> Dto.fromMap(CustomProfile.class, body)).build());
    }

    /** Replace one custom profile — {@code PUT .../custom-profiles/{custom_profile_id}}. */
    public ApiResponse updateTaxCustomProfile(Object customProfileId, Object payload) {
        return updateTaxCustomProfile(customProfileId, payload, null);
    }

    public ApiResponse updateTaxCustomProfile(Object customProfileId, Object payload,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPutRequest(TaxCustomProfilesUrls.TAX_CUSTOM_PROFILE,
                RequestOptions.builder(options).pathParams(pathParams(customProfileId))
                        .jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(CustomProfile.class, body)).build());
    }

    public ApiResponse updateTaxCustomProfile(Object customProfileId, CustomProfileInput payload) {
        return updateTaxCustomProfile(customProfileId, (Object) payload, null);
    }

    public ApiResponse updateTaxCustomProfile(Object customProfileId, CustomProfileInput payload,
            RequestOptions options) {
        return updateTaxCustomProfile(customProfileId, (Object) payload, options);
    }

    /** Delete one custom profile — {@code DELETE .../custom-profiles/{custom_profile_id}}. */
    public ApiResponse deleteTaxCustomProfile(Object customProfileId) {
        return deleteTaxCustomProfile(customProfileId, null);
    }

    public ApiResponse deleteTaxCustomProfile(Object customProfileId, RequestOptions options) {
        return client.sendDeleteRequest(TaxCustomProfilesUrls.TAX_CUSTOM_PROFILE,
                RequestOptions.builder(options).pathParams(pathParams(customProfileId))
                        .dtoFactory(null).build());
    }

    private static Map<String, Object> pathParams(Object customProfileId) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("custom_profile_id", customProfileId);
        return params;
    }
}
