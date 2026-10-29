package com.webcommander.resources.order_flags;

import java.util.List;

/** Path templates and body keys for the order flag endpoints. */
public final class OrderFlagsUrls {

    private OrderFlagsUrls() {}

    public static final String ORDER_FLAGS = "/api/{api_version}/admin/order_flags";

    public static final String ORDER_FLAG = "/api/{api_version}/admin/order_flags/{order_flag_id}";

    /** The key the listing's rows arrive under — the resource's own plural name. */
    public static final String ORDER_FLAGS_KEY = "order_flags";

    /** The key one row arrives under, and the wrapper both writes require. */
    public static final String ORDER_FLAG_KEY = "order_flag";

    /** The four names the listing accepts; anything else is {@code 400 invalid_request}. */
    public static final List<String> ORDER_FLAGS_QUERY_PARAMS =
            List.of("limit", "offset", "page", "field_metadata");
}
