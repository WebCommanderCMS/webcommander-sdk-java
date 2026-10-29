package com.webcommander.resources.payment_gateways;

/** Path templates for the payment gateway endpoints. */
public final class PaymentGatewaysUrls {

    private PaymentGatewaysUrls() {}

    public static final String PAYMENT_GATEWAYS = "/api/{api_version}/admin/payment_gateways";

    public static final String PAYMENT_GATEWAYS_COUNT =
            "/api/{api_version}/admin/payment_gateways/count";

    public static final String PAYMENT_GATEWAY =
            "/api/{api_version}/admin/payment_gateways/{payment_gateway_id}";
}
