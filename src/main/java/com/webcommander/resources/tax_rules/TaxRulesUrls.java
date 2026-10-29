package com.webcommander.resources.tax_rules;

/** Path templates for the tax rule endpoints. */
public final class TaxRulesUrls {

    private TaxRulesUrls() {}

    public static final String TAX_RULES = "/api/{api_version}/admin/tax_rules";

    public static final String TAX_RULE = "/api/{api_version}/admin/tax_rules/{tax_rule_id}";
}
