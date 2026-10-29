package com.webcommander.resources.carts;

/** Path templates and the session key for the cart endpoints. */
public final class CartsUrls {

    private CartsUrls() {}

    public static final String CARTS = "/api/{api_version}/carts";

    public static final String CART = "/api/{api_version}/carts/{session_id}";

    public static final String CART_ITEM =
            "/api/{api_version}/carts/{session_id}/cart_items/{cart_item_id}";

    /** The one cart path under {@code /admin}. */
    public static final String CART_ORDER = "/api/{api_version}/admin/orders/carts/{session_id}";

    /** The key the session write answers under, spelled as the store spells it. */
    public static final String CART_SESSION_KEY = "sessionId";

    public static final String TRANSACTION_NO_HEADER = "X-Transaction-No";
}
