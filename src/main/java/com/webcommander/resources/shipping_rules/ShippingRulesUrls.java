package com.webcommander.resources.shipping_rules;

/** Path templates for the shipping rule endpoints. */
public final class ShippingRulesUrls {

    private ShippingRulesUrls() {}

    public static final String SHIPPING_RULES = "/api/{api_version}/admin/shipping_rules";

    public static final String SHIPPING_RULES_COUNT =
            "/api/{api_version}/admin/shipping_rules/count";

    public static final String SHIPPING_RULE =
            "/api/{api_version}/admin/shipping_rules/{shipping_rule_id}";

    /** Files the record under a transaction number; it wins over a {@code transaction_no} in the body. */
    public static final String TRANSACTION_NO_HEADER = "X-Transaction-No";
}
