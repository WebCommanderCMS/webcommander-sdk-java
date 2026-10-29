package com.webcommander.resources.variations;

/** Path templates and the one header name for the variation type and variant endpoints. */
public final class VariationsUrls {

    private VariationsUrls() {}

    public static final String VARIATIONS = "/api/{api_version}/admin/variations";

    public static final String VARIATION = "/api/{api_version}/admin/variations/{variation_id}";

    public static final String VARIATION_OPTION =
            "/api/{api_version}/admin/variations/{variation_id}/options/{variation_option_id}";

    public static final String PRODUCT_VARIATIONS =
            "/api/{api_version}/admin/products/{product_id}/variations";

    public static final String PRODUCT_VARIATIONS_BULK =
            "/api/{api_version}/admin/products/{product_id}/variations/bulk";

    /** Tags a create with a transaction number; it wins over a body one it disagrees with. */
    public static final String TRANSACTION_NO_HEADER = "X-Transaction-No";
}
