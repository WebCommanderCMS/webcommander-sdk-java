package com.webcommander.resources.shipping_rates;

/** Path templates for the shipping rate endpoints. */
public final class ShippingRatesUrls {

    private ShippingRatesUrls() {}

    public static final String SHIPPING_RATES = "/api/{api_version}/admin/shipping_rates";

    public static final String SHIPPING_RATES_COUNT =
            "/api/{api_version}/admin/shipping_rates/count";

    public static final String SHIPPING_RATE =
            "/api/{api_version}/admin/shipping_rates/{shipping_rate_id}";

    /**
     * Files the record under a transaction number; a different {@code transaction_no} beside the
     * {@code shipping_rate} wrapper is refused with {@code 400 header_body_mismatch}, and one
     * inside the wrapper is ignored.
     */
    public static final String TRANSACTION_NO_HEADER = "X-Transaction-No";
}
