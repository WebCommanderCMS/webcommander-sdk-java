package com.webcommander.resources.shipping_profiles;

/** Path templates for the shipping profile endpoints. */
public final class ShippingProfilesUrls {

    private ShippingProfilesUrls() {}

    public static final String SHIPPING_PROFILES = "/api/{api_version}/admin/shipping_profiles";

    public static final String SHIPPING_PROFILES_COUNT =
            "/api/{api_version}/admin/shipping_profiles/count";

    public static final String SHIPPING_PROFILES_WIZARD =
            "/api/{api_version}/admin/shipping_profiles/wizard";

    public static final String SHIPPING_PROFILE =
            "/api/{api_version}/admin/shipping_profiles/{shipping_profile_id}";

    /**
     * Files the record under a transaction number; a different {@code transaction_no} beside the
     * {@code shipping_profile} wrapper is refused with {@code 400 header_body_mismatch}, and one
     * inside the wrapper is ignored.
     */
    public static final String TRANSACTION_NO_HEADER = "X-Transaction-No";
}
