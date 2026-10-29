package com.webcommander.resources.discount_profiles;

/** Path templates for the discount profile endpoints. */
public final class DiscountProfilesUrls {

    private DiscountProfilesUrls() {}

    public static final String DISCOUNT_PROFILES = "/api/{api_version}/admin/discount_profiles";

    public static final String DISCOUNT_PROFILE =
            "/api/{api_version}/admin/discount_profiles/{discount_profile_id}";

    public static final String DISCOUNT_PROFILE_CREATE_COUPONS =
            "/api/{api_version}/admin/discount_profiles/{discount_profile_id}/create-coupons";

    /** Under {@code /admin/discount_coupons}, a path with no listing of its own. */
    public static final String DISCOUNT_COUPONS_ASSIGN_CUSTOMER =
            "/api/{api_version}/admin/discount_coupons/assign-customer";
}
