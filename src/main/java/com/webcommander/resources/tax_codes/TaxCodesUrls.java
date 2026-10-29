package com.webcommander.resources.tax_codes;

/** Path templates for the tax code endpoints. */
public final class TaxCodesUrls {

    private TaxCodesUrls() {}

    public static final String TAX_CODES = "/api/{api_version}/admin/tax_codes";

    public static final String TAX_CODE = "/api/{api_version}/admin/tax_codes/{tax_code_id}";
}
