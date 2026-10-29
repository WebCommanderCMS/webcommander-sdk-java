package com.webcommander.resources.tax_custom_profiles;

/** Path templates for the custom tax profile endpoints. */
public final class TaxCustomProfilesUrls {

    private TaxCustomProfilesUrls() {}

    public static final String TAX_CUSTOM_PROFILES =
            "/api/{api_version}/admin/tax/custom-profiles";

    public static final String TAX_CUSTOM_PROFILE =
            "/api/{api_version}/admin/tax/custom-profiles/{custom_profile_id}";

    public static final String TRANSACTION_NO_HEADER = "X-Transaction-No";
}
