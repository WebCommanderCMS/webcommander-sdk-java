package com.webcommander.resources.order_previews;

/** Path templates for the order preview endpoints. */
public final class OrderPreviewsUrls {

    private OrderPreviewsUrls() {}

    public static final String ORDER_PREVIEWS = "/api/{api_version}/admin/orders/preview";

    public static final String ORDER_PREVIEW =
            "/api/{api_version}/admin/orders/preview/{preview_session_id}";

    public static final String ORDER_PREVIEW_CONFIRM =
            "/api/{api_version}/admin/orders/preview/{preview_session_id}/confirm";

    public static final String ORDER_PREVIEW_COUPON =
            "/api/{api_version}/admin/orders/preview/{preview_session_id}/coupon";

    public static final String ORDER_PREVIEW_GIFT_CARD =
            "/api/{api_version}/admin/orders/preview/{preview_session_id}/gift-card";
}
