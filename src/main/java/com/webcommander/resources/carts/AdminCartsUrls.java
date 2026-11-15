package com.webcommander.resources.carts;

/** Path templates for the admin cart and checkout endpoints. */
public final class AdminCartsUrls {

    private AdminCartsUrls() {}

    public static final String ADMIN_CARTS = "/api/{api_version}/admin/orders/carts";

    public static final String ADMIN_CART = "/api/{api_version}/admin/orders/carts/{session_id}";

    public static final String ADMIN_CART_ITEMS =
            "/api/{api_version}/admin/orders/carts/{session_id}/cart_items";

    public static final String ADMIN_CART_ITEM =
            "/api/{api_version}/admin/orders/carts/{session_id}/cart_items/{cart_item_id}";

    public static final String ADMIN_CHECKOUT =
            "/api/{api_version}/admin/orders/checkout/{session_id}";

    public static final String ADMIN_CHECKOUT_CONFIRM =
            "/api/{api_version}/admin/orders/checkout/{session_id}/confirm";

    public static final String ADMIN_CHECKOUT_COUPON =
            "/api/{api_version}/admin/orders/checkout/{session_id}/coupon";

    public static final String ADMIN_CHECKOUT_GIFT_CARD =
            "/api/{api_version}/admin/orders/checkout/{session_id}/gift_card";

    public static final String ADMIN_CHECKOUT_DELIVERY_SCHEDULES =
            "/api/{api_version}/admin/orders/checkout/{session_id}/delivery_schedules";

    /**
     * Files the order a checkout places under this transaction number; it beats
     * {@code transaction_no}.
     */
    public static final String TRANSACTION_NO_HEADER = "X-Transaction-No";
}
