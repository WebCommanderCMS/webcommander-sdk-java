package com.webcommander.resources.discount_profiles;

import com.webcommander.Dto;
import com.webcommander.client.ApiResponse;
import com.webcommander.client.BoundResource;
import com.webcommander.client.RequestOptions;
import com.webcommander.client.ResourceBinding;
import com.webcommander.client.WebCommanderClient;
import com.webcommander.resources.discount_profiles.discount_profiles_dto
        .DiscountProfilesDto.DiscountCouponCustomerInput;
import com.webcommander.resources.discount_profiles.discount_profiles_dto
        .DiscountProfilesDto.DiscountProfile;
import com.webcommander.resources.discount_profiles.discount_profiles_dto
        .DiscountProfilesDto.DiscountProfileCoupons;
import com.webcommander.resources.discount_profiles.discount_profiles_dto
        .DiscountProfilesDto.DiscountProfileCouponsInput;
import com.webcommander.resources.discount_profiles.discount_profiles_dto
        .DiscountProfilesDto.DiscountProfileInput;
import com.webcommander.resources.discount_profiles.discount_profiles_dto
        .DiscountProfilesDto.DiscountProfilePage;

import java.util.LinkedHashMap;
import java.util.Map;

/** Discount profiles — {@code /api/{api_version}/admin/discount_profiles} — and their coupons. */
public final class DiscountProfiles implements BoundResource {

    private final WebCommanderClient client;

    public DiscountProfiles(WebCommanderClient client) {
        this.client = client;
    }

    /** List the profiles, newest first — {@code GET /admin/discount_profiles}. */
    public ApiResponse listDiscountProfiles() {
        return listDiscountProfiles(null, null);
    }

    public ApiResponse listDiscountProfiles(Map<String, Object> queryParams) {
        return listDiscountProfiles(queryParams, null);
    }

    public ApiResponse listDiscountProfiles(Map<String, Object> queryParams,
            RequestOptions options) {
        return client.sendGetRequest(DiscountProfilesUrls.DISCOUNT_PROFILES,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(DiscountProfilePage.class, body)).build());
    }

    /** Create a profile from a bare record — {@code POST /admin/discount_profiles}. */
    public ApiResponse createDiscountProfile(Object payload) {
        return createDiscountProfile(payload, null);
    }

    public ApiResponse createDiscountProfile(Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPostRequest(DiscountProfilesUrls.DISCOUNT_PROFILES,
                RequestOptions.builder(options).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(DiscountProfile.class, body)).build());
    }

    public ApiResponse createDiscountProfile(DiscountProfileInput payload) {
        return createDiscountProfile((Object) payload, null);
    }

    public ApiResponse createDiscountProfile(DiscountProfileInput payload,
            RequestOptions options) {
        return createDiscountProfile((Object) payload, options);
    }

    /** Retrieve one profile — {@code GET /admin/discount_profiles/{discount_profile_id}}. */
    public ApiResponse getDiscountProfile(Object discountProfileId) {
        return getDiscountProfile(discountProfileId, null, null);
    }

    public ApiResponse getDiscountProfile(Object discountProfileId,
            Map<String, Object> queryParams) {
        return getDiscountProfile(discountProfileId, queryParams, null);
    }

    public ApiResponse getDiscountProfile(Object discountProfileId,
            Map<String, Object> queryParams, RequestOptions options) {
        return client.sendGetRequest(DiscountProfilesUrls.DISCOUNT_PROFILE,
                RequestOptions.builder(options).pathParams(pathParams(discountProfileId))
                        .queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(DiscountProfile.class, body)).build());
    }

    /** Replace one profile; omitted fields reset — {@code PUT .../{discount_profile_id}}. */
    public ApiResponse updateDiscountProfile(Object discountProfileId, Object payload) {
        return updateDiscountProfile(discountProfileId, payload, null);
    }

    public ApiResponse updateDiscountProfile(Object discountProfileId, Object payload,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPutRequest(DiscountProfilesUrls.DISCOUNT_PROFILE,
                RequestOptions.builder(options).pathParams(pathParams(discountProfileId))
                        .jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(DiscountProfile.class, body)).build());
    }

    public ApiResponse updateDiscountProfile(Object discountProfileId,
            DiscountProfileInput payload) {
        return updateDiscountProfile(discountProfileId, (Object) payload, null);
    }

    public ApiResponse updateDiscountProfile(Object discountProfileId,
            DiscountProfileInput payload, RequestOptions options) {
        return updateDiscountProfile(discountProfileId, (Object) payload, options);
    }

    /** Delete one profile and its coupons — {@code DELETE .../{discount_profile_id}}. */
    public ApiResponse deleteDiscountProfile(Object discountProfileId) {
        return deleteDiscountProfile(discountProfileId, null);
    }

    public ApiResponse deleteDiscountProfile(Object discountProfileId, RequestOptions options) {
        return client.sendDeleteRequest(DiscountProfilesUrls.DISCOUNT_PROFILE,
                RequestOptions.builder(options).pathParams(pathParams(discountProfileId))
                        .dtoFactory(null).build());
    }

    /** Generate coupon codes — {@code POST .../{discount_profile_id}/create-coupons}. */
    public ApiResponse createDiscountProfileCoupons(Object discountProfileId, Object payload) {
        return createDiscountProfileCoupons(discountProfileId, payload, null);
    }

    public ApiResponse createDiscountProfileCoupons(Object discountProfileId, Object payload,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPostRequest(DiscountProfilesUrls.DISCOUNT_PROFILE_CREATE_COUPONS,
                RequestOptions.builder(options).pathParams(pathParams(discountProfileId))
                        .jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(DiscountProfileCoupons.class, body))
                        .build());
    }

    public ApiResponse createDiscountProfileCoupons(Object discountProfileId,
            DiscountProfileCouponsInput payload) {
        return createDiscountProfileCoupons(discountProfileId, (Object) payload, null);
    }

    public ApiResponse createDiscountProfileCoupons(Object discountProfileId,
            DiscountProfileCouponsInput payload, RequestOptions options) {
        return createDiscountProfileCoupons(discountProfileId, (Object) payload, options);
    }

    /** Assign a code to a customer — {@code POST /admin/discount_coupons/assign-customer}. */
    public ApiResponse assignDiscountCouponCustomer(Object payload) {
        return assignDiscountCouponCustomer(payload, null);
    }

    public ApiResponse assignDiscountCouponCustomer(Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPostRequest(DiscountProfilesUrls.DISCOUNT_COUPONS_ASSIGN_CUSTOMER,
                RequestOptions.builder(options).jsonBody(payload).dtoFactory(null).build());
    }

    public ApiResponse assignDiscountCouponCustomer(DiscountCouponCustomerInput payload) {
        return assignDiscountCouponCustomer((Object) payload, null);
    }

    public ApiResponse assignDiscountCouponCustomer(DiscountCouponCustomerInput payload,
            RequestOptions options) {
        return assignDiscountCouponCustomer((Object) payload, options);
    }

    private static Map<String, Object> pathParams(Object discountProfileId) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("discount_profile_id", discountProfileId);
        return params;
    }
}
