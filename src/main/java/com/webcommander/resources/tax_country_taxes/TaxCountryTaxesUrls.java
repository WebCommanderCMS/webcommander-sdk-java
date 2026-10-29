package com.webcommander.resources.tax_country_taxes;

/** Path templates for the country tax endpoints. */
public final class TaxCountryTaxesUrls {

    private TaxCountryTaxesUrls() {}

    public static final String TAX_COUNTRY_TAXES = "/api/{api_version}/admin/tax/country-taxes";

    public static final String TAX_COUNTRY_TAX =
            "/api/{api_version}/admin/tax/country-taxes/{country_tax_id}";

    public static final String TRANSACTION_NO_HEADER = "X-Transaction-No";
}
