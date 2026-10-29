package com.webcommander.resources.orders;

/** Path templates and the refund header for the order endpoints. */
public final class OrdersUrls {

    private OrdersUrls() {}

    public static final String ORDERS = "/api/{api_version}/admin/orders";

    public static final String ORDERS_COUNT = "/api/{api_version}/admin/orders/count";

    public static final String ORDERS_WITH_PAYMENT = "/api/{api_version}/admin/orders/with-payment";

    public static final String ORDER = "/api/{api_version}/admin/orders/{order_id}";

    public static final String ORDER_ADDRESSES =
            "/api/{api_version}/admin/orders/{order_id}/addresses";

    public static final String ORDER_CANCEL = "/api/{api_version}/admin/orders/{order_id}/cancel";

    public static final String ORDER_COMMENTS =
            "/api/{api_version}/admin/orders/{order_id}/comments";

    public static final String ORDER_PAYMENTS =
            "/api/{api_version}/admin/orders/{order_id}/payments";

    public static final String ORDER_REFUNDS = "/api/{api_version}/admin/orders/{order_id}/refunds";

    public static final String ORDER_SHIPMENT_STATUS =
            "/api/{api_version}/admin/orders/{order_id}/shipment-status";

    public static final String ORDER_SHIPMENTS =
            "/api/{api_version}/admin/orders/{order_id}/shipments";

    public static final String ORDER_SHIPMENT =
            "/api/{api_version}/admin/orders/{order_id}/shipments/{shipment_id}";

    public static final String ORDER_UPDATE_STATUS =
            "/api/{api_version}/admin/orders/{order_id}/update-status";

    public static final String ORDER_CUSTOM_FIELDS =
            "/api/{api_version}/admin/orders/{order_internal_id}/custom_fields";

    public static final String ORDER_SEND_INVOICE =
            "/api/{api_version}/admin/orders/{order_internal_id}/send-invoice";

    public static final String PAYMENT_REFUNDS =
            "/api/{api_version}/admin/payment/{payment_id}/refunds";

    public static final String ORDER_STATUS_ADD = "/api/{api_version}/admin/order/add-order-status";

    public static final String CUSTOMER_ORDERS =
            "/api/{api_version}/admin/customers/{customer_id}/orders";

    public static final String IDEMPOTENCY_KEY_HEADER = "Idempotency-Key";

    public static final String TRANSACTION_NO_HEADER = "X-Transaction-No";
}
