package com.webcommander.resources.tax_profiles;

import com.webcommander.Dto;
import com.webcommander.client.ApiResponse;
import com.webcommander.client.BoundResource;
import com.webcommander.client.RequestOptions;
import com.webcommander.client.ResourceBinding;
import com.webcommander.client.WebCommanderClient;
import com.webcommander.resources.tax_profiles.tax_profiles_dto.TaxProfilesDto.TaxProfile;
import com.webcommander.resources.tax_profiles.tax_profiles_dto.TaxProfilesDto.TaxProfileInput;
import com.webcommander.resources.tax_profiles.tax_profiles_dto.TaxProfilesDto.TaxProfilePage;

import java.util.LinkedHashMap;
import java.util.Map;

/** Tax profiles — {@code /api/{api_version}/admin/tax_profiles}. */
public final class TaxProfiles implements BoundResource {

    private final WebCommanderClient client;

    public TaxProfiles(WebCommanderClient client) {
        this.client = client;
    }

    /** List the tax profiles — {@code GET /admin/tax_profiles}. */
    public ApiResponse listTaxProfiles() {
        return listTaxProfiles(null, null);
    }

    public ApiResponse listTaxProfiles(Map<String, Object> queryParams) {
        return listTaxProfiles(queryParams, null);
    }

    public ApiResponse listTaxProfiles(Map<String, Object> queryParams, RequestOptions options) {
        return client.sendGetRequest(TaxProfilesUrls.TAX_PROFILES,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(TaxProfilePage.class, body)).build());
    }

    /** Ask whether the collection is reachable — {@code HEAD /admin/tax_profiles}. */
    public ApiResponse headTaxProfiles() {
        return headTaxProfiles(null);
    }

    public ApiResponse headTaxProfiles(RequestOptions options) {
        return client.sendApiRequest("HEAD", TaxProfilesUrls.TAX_PROFILES,
                RequestOptions.builder(options).dtoFactory(null).build());
    }

    /** Create a tax profile — {@code POST /admin/tax_profiles}, wrapped in {@code tax_profile}. */
    public ApiResponse createTaxProfile(Object payload) {
        return createTaxProfile(payload, null);
    }

    public ApiResponse createTaxProfile(Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPostRequest(TaxProfilesUrls.TAX_PROFILES,
                RequestOptions.builder(options).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(TaxProfile.class, body)).build());
    }

    public ApiResponse createTaxProfile(TaxProfileInput payload) {
        return createTaxProfile((Object) payload, null);
    }

    public ApiResponse createTaxProfile(TaxProfileInput payload, RequestOptions options) {
        return createTaxProfile((Object) payload, options);
    }

    /** Retrieve one tax profile — {@code GET /admin/tax_profiles/{tax_profile_id}}. */
    public ApiResponse getTaxProfile(Object taxProfileId) {
        return getTaxProfile(taxProfileId, null);
    }

    public ApiResponse getTaxProfile(Object taxProfileId, RequestOptions options) {
        return client.sendGetRequest(TaxProfilesUrls.TAX_PROFILE,
                RequestOptions.builder(options).pathParams(pathParams(taxProfileId))
                        .dtoFactory(body -> Dto.fromMap(TaxProfile.class, body)).build());
    }

    /** Probe one tax profile — {@code HEAD /admin/tax_profiles/{tax_profile_id}}. */
    public ApiResponse headTaxProfile(Object taxProfileId) {
        return headTaxProfile(taxProfileId, null);
    }

    public ApiResponse headTaxProfile(Object taxProfileId, RequestOptions options) {
        return client.sendApiRequest("HEAD", TaxProfilesUrls.TAX_PROFILE,
                RequestOptions.builder(options).pathParams(pathParams(taxProfileId))
                        .dtoFactory(null).build());
    }

    /** Update one tax profile — {@code PUT /admin/tax_profiles/{tax_profile_id}}. */
    public ApiResponse updateTaxProfile(Object taxProfileId, Object payload) {
        return updateTaxProfile(taxProfileId, payload, null);
    }

    public ApiResponse updateTaxProfile(Object taxProfileId, Object payload,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPutRequest(TaxProfilesUrls.TAX_PROFILE,
                RequestOptions.builder(options).pathParams(pathParams(taxProfileId))
                        .jsonBody(payload).dtoFactory(body -> Dto.fromMap(TaxProfile.class, body))
                        .build());
    }

    public ApiResponse updateTaxProfile(Object taxProfileId, TaxProfileInput payload) {
        return updateTaxProfile(taxProfileId, (Object) payload, null);
    }

    public ApiResponse updateTaxProfile(Object taxProfileId, TaxProfileInput payload,
            RequestOptions options) {
        return updateTaxProfile(taxProfileId, (Object) payload, options);
    }

    /** Delete one tax profile — {@code DELETE /admin/tax_profiles/{tax_profile_id}}. */
    public ApiResponse deleteTaxProfile(Object taxProfileId) {
        return deleteTaxProfile(taxProfileId, null);
    }

    public ApiResponse deleteTaxProfile(Object taxProfileId, RequestOptions options) {
        return client.sendDeleteRequest(TaxProfilesUrls.TAX_PROFILE,
                RequestOptions.builder(options).pathParams(pathParams(taxProfileId))
                        .dtoFactory(null).build());
    }

    private static Map<String, Object> pathParams(Object taxProfileId) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("tax_profile_id", taxProfileId);
        return params;
    }
}
