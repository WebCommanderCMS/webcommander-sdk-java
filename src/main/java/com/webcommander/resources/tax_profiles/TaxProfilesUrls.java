package com.webcommander.resources.tax_profiles;

/** Path templates for the tax profile endpoints. */
public final class TaxProfilesUrls {

    private TaxProfilesUrls() {}

    public static final String TAX_PROFILES = "/api/{api_version}/admin/tax_profiles";

    public static final String TAX_PROFILE =
            "/api/{api_version}/admin/tax_profiles/{tax_profile_id}";
}
