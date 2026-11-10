package com.webcommander.resources.shipping_profiles;

import com.webcommander.Dto;
import com.webcommander.client.ApiResponse;
import com.webcommander.client.BoundResource;
import com.webcommander.client.RequestOptions;
import com.webcommander.client.ResourceBinding;
import com.webcommander.client.WebCommanderClient;
import com.webcommander.resources.shipping_profiles.shipping_profiles_dto.ShippingProfilesDto.ShippingProfile;
import com.webcommander.resources.shipping_profiles.shipping_profiles_dto.ShippingProfilesDto.ShippingProfileCount;
import com.webcommander.resources.shipping_profiles.shipping_profiles_dto.ShippingProfilesDto.ShippingProfileInput;
import com.webcommander.resources.shipping_profiles.shipping_profiles_dto.ShippingProfilesDto.ShippingProfileListing;
import com.webcommander.resources.shipping_profiles.shipping_profiles_dto.ShippingProfilesDto.ShippingProfileWizardInput;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shipping profiles — {@code /api/{api_version}/admin/shipping_profiles}; one of them is the
 * store default.
 */
public final class ShippingProfiles implements BoundResource {

    private final WebCommanderClient client;

    public ShippingProfiles(WebCommanderClient client) {
        this.client = client;
    }

    /**
     * List the profiles, newest first — {@code GET /admin/shipping_profiles}. {@code search}
     * (part of the name) and {@code default} ({@code true} or {@code false}) filter;
     * {@code sort} is {@code id}, {@code name}, {@code description},
     * {@code rule_preference}, {@code created_at}, {@code updated_at} or
     * {@code transaction_no} with {@code order} {@code asc} or {@code desc}; {@code limit}
     * is 1 to 100 and defaults to 20, {@code offset} pages. An unknown value is {@code 400};
     * an unknown name is ignored.
     */
    public ApiResponse listShippingProfiles() {
        return listShippingProfiles(null, null);
    }

    public ApiResponse listShippingProfiles(Map<String, Object> queryParams) {
        return listShippingProfiles(queryParams, null);
    }

    public ApiResponse listShippingProfiles(Map<String, Object> queryParams,
            RequestOptions options) {
        return client.sendGetRequest(ShippingProfilesUrls.SHIPPING_PROFILES,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(ShippingProfileListing.class, body)).build());
    }

    /** Ask whether the collection is reachable — {@code HEAD /admin/shipping_profiles}; no body. */
    public ApiResponse headShippingProfiles() {
        return headShippingProfiles(null);
    }

    public ApiResponse headShippingProfiles(RequestOptions options) {
        return client.sendApiRequest("HEAD", ShippingProfilesUrls.SHIPPING_PROFILES,
                RequestOptions.builder(options).dtoFactory(null).build());
    }

    /**
     * How many profiles match — {@code GET /admin/shipping_profiles/count}, a bare
     * {@code {"total": N}}. The listing's filters narrow it.
     */
    public ApiResponse countShippingProfiles() {
        return countShippingProfiles(null, null);
    }

    public ApiResponse countShippingProfiles(Map<String, Object> queryParams) {
        return countShippingProfiles(queryParams, null);
    }

    public ApiResponse countShippingProfiles(Map<String, Object> queryParams,
            RequestOptions options) {
        return client.sendGetRequest(ShippingProfilesUrls.SHIPPING_PROFILES_COUNT,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(ShippingProfileCount.class, body)).build());
    }

    /** Ask whether the count is reachable — {@code HEAD /admin/shipping_profiles/count}; no body. */
    public ApiResponse headShippingProfilesCount() {
        return headShippingProfilesCount(null);
    }

    public ApiResponse headShippingProfilesCount(RequestOptions options) {
        return client.sendApiRequest("HEAD", ShippingProfilesUrls.SHIPPING_PROFILES_COUNT,
                RequestOptions.builder(options).dtoFactory(null).build());
    }

    /**
     * Create a profile — {@code POST /admin/shipping_profiles}, answered {@code 201} with a
     * {@code Location}. The body is wrapped in {@code shipping_profile} and needs
     * {@code name}, unique without regard to case ({@code 409}); {@code rule_preference}
     * defaults to {@code product_assigned_rule}. An unknown field, an {@code id} or an
     * unwrapped body is {@code 400}. A non-null {@code transactionNo} is sent as
     * {@code X-Transaction-No}.
     */
    public ApiResponse createShippingProfile(Object payload) {
        return createShippingProfile(payload, null, null);
    }

    public ApiResponse createShippingProfile(Object payload, RequestOptions options) {
        return createShippingProfile(payload, null, options);
    }

    public ApiResponse createShippingProfile(Object payload, String transactionNo) {
        return createShippingProfile(payload, transactionNo, null);
    }

    public ApiResponse createShippingProfile(Object payload, String transactionNo,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        RequestOptions given = options == null ? RequestOptions.none() : options;
        Map<String, String> headers = new LinkedHashMap<>();
        if (given.getHeaders() != null) {
            headers.putAll(given.getHeaders());
        }
        if (transactionNo != null) {
            headers.put(ShippingProfilesUrls.TRANSACTION_NO_HEADER, transactionNo);
        }
        return client.sendPostRequest(ShippingProfilesUrls.SHIPPING_PROFILES,
                RequestOptions.builder(options).jsonBody(payload).headers(headers)
                        .dtoFactory(body -> Dto.fromMap(ShippingProfile.class, body)).build());
    }

    public ApiResponse createShippingProfile(ShippingProfileInput payload) {
        return createShippingProfile((Object) payload, null, null);
    }

    public ApiResponse createShippingProfile(ShippingProfileInput payload, RequestOptions options) {
        return createShippingProfile((Object) payload, null, options);
    }

    public ApiResponse createShippingProfile(ShippingProfileInput payload, String transactionNo) {
        return createShippingProfile((Object) payload, transactionNo, null);
    }

    public ApiResponse createShippingProfile(ShippingProfileInput payload, String transactionNo,
            RequestOptions options) {
        return createShippingProfile((Object) payload, transactionNo, options);
    }

    /**
     * Create a profile with its rules and their rates in one call — {@code POST
     * /admin/shipping_profiles/wizard}. A new profile name is {@code 201}; a name that exists
     * adds the rules to that profile and is {@code 200}. Every rule's rate, class and zone is
     * checked first, and one bad value saves nothing. A zone given as {@code {"name": ...}} is
     * created.
     */
    public ApiResponse createShippingProfileWizard(Object payload) {
        return createShippingProfileWizard(payload, null);
    }

    public ApiResponse createShippingProfileWizard(Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPostRequest(ShippingProfilesUrls.SHIPPING_PROFILES_WIZARD,
                RequestOptions.builder(options).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(ShippingProfile.class, body)).build());
    }

    public ApiResponse createShippingProfileWizard(ShippingProfileWizardInput payload) {
        return createShippingProfileWizard((Object) payload, null);
    }

    public ApiResponse createShippingProfileWizard(ShippingProfileWizardInput payload,
            RequestOptions options) {
        return createShippingProfileWizard((Object) payload, options);
    }

    /**
     * Retrieve one profile by its {@code id} — {@code GET
     * /admin/shipping_profiles/{shipping_profile_id}}, the row under {@code shipping_profile}.
     * An id that is not plain digits, or unknown, is {@code 404}.
     */
    public ApiResponse getShippingProfile(Object shippingProfileId) {
        return getShippingProfile(shippingProfileId, null);
    }

    public ApiResponse getShippingProfile(Object shippingProfileId, RequestOptions options) {
        return client.sendGetRequest(ShippingProfilesUrls.SHIPPING_PROFILE,
                RequestOptions.builder(options).pathParams(pathParams(shippingProfileId))
                        .dtoFactory(body -> Dto.fromMap(ShippingProfile.class, body)).build());
    }

    /**
     * Ask whether a profile exists — {@code HEAD
     * /admin/shipping_profiles/{shipping_profile_id}}; 200 with no body, 404 for an unknown id.
     */
    public ApiResponse headShippingProfile(Object shippingProfileId) {
        return headShippingProfile(shippingProfileId, null);
    }

    public ApiResponse headShippingProfile(Object shippingProfileId, RequestOptions options) {
        return client.sendApiRequest("HEAD", ShippingProfilesUrls.SHIPPING_PROFILE,
                RequestOptions.builder(options).pathParams(pathParams(shippingProfileId))
                        .dtoFactory(null).build());
    }

    /**
     * Replace one profile — {@code PUT /admin/shipping_profiles/{shipping_profile_id}}.
     * {@code name}, {@code description}, {@code rule_preference} and {@code shipping_rules}
     * are all required ({@code description} may be {@code null}, which takes a {@code Map});
     * the rules not listed are taken off the profile and kept.
     */
    public ApiResponse updateShippingProfile(Object shippingProfileId, Object payload) {
        return updateShippingProfile(shippingProfileId, payload, null);
    }

    public ApiResponse updateShippingProfile(Object shippingProfileId, Object payload,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPutRequest(ShippingProfilesUrls.SHIPPING_PROFILE,
                RequestOptions.builder(options).pathParams(pathParams(shippingProfileId))
                        .jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(ShippingProfile.class, body)).build());
    }

    public ApiResponse updateShippingProfile(Object shippingProfileId, ShippingProfileInput payload) {
        return updateShippingProfile(shippingProfileId, (Object) payload, null);
    }

    public ApiResponse updateShippingProfile(Object shippingProfileId, ShippingProfileInput payload,
            RequestOptions options) {
        return updateShippingProfile(shippingProfileId, (Object) payload, options);
    }

    /**
     * Change only the fields sent — {@code PATCH
     * /admin/shipping_profiles/{shipping_profile_id}}. {@code shipping_rules} is the full
     * list, so {@code []} takes every rule off. {@code default} true moves the store default
     * to this profile; the default cannot be switched off directly ({@code 409}).
     */
    public ApiResponse patchShippingProfile(Object shippingProfileId, Object payload) {
        return patchShippingProfile(shippingProfileId, payload, null);
    }

    public ApiResponse patchShippingProfile(Object shippingProfileId, Object payload,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPatchRequest(ShippingProfilesUrls.SHIPPING_PROFILE,
                RequestOptions.builder(options).pathParams(pathParams(shippingProfileId))
                        .jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(ShippingProfile.class, body)).build());
    }

    public ApiResponse patchShippingProfile(Object shippingProfileId, ShippingProfileInput payload) {
        return patchShippingProfile(shippingProfileId, (Object) payload, null);
    }

    public ApiResponse patchShippingProfile(Object shippingProfileId, ShippingProfileInput payload,
            RequestOptions options) {
        return patchShippingProfile(shippingProfileId, (Object) payload, options);
    }

    /**
     * Delete one profile — {@code DELETE /admin/shipping_profiles/{shipping_profile_id}},
     * {@code 204}. Its rules and zones stay. The store default is {@code 409} until another
     * profile holds the flag.
     */
    public ApiResponse deleteShippingProfile(Object shippingProfileId) {
        return deleteShippingProfile(shippingProfileId, null);
    }

    public ApiResponse deleteShippingProfile(Object shippingProfileId, RequestOptions options) {
        return client.sendDeleteRequest(ShippingProfilesUrls.SHIPPING_PROFILE,
                RequestOptions.builder(options).pathParams(pathParams(shippingProfileId))
                        .dtoFactory(null).build());
    }

    private static Map<String, Object> pathParams(Object shippingProfileId) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("shipping_profile_id", shippingProfileId);
        return params;
    }
}
