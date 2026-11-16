package com.webcommander.resources.shipping_rules;

/** Path templates for the shipping rule endpoints. */
public final class ShippingRulesUrls {

    private ShippingRulesUrls() {}

    public static final String SHIPPING_RULES = "/api/{api_version}/admin/shipping_rules";

    public static final String SHIPPING_RULES_COUNT =
            "/api/{api_version}/admin/shipping_rules/count";

    public static final String SHIPPING_RULE =
            "/api/{api_version}/admin/shipping_rules/{shipping_rule_id}";

    /**
     * Files the record under a transaction number; a different {@code transaction_no} beside the
     * {@code shipping_rule} wrapper is refused with {@code 400 header_body_mismatch}, and one
     * inside the wrapper is ignored.
     */
    public static final String TRANSACTION_NO_HEADER = "X-Transaction-No";
}
