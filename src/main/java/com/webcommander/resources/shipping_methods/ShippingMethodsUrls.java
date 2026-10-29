package com.webcommander.resources.shipping_methods;

/** Path templates for the shipping method endpoints. */
public final class ShippingMethodsUrls {

    private ShippingMethodsUrls() {}

    public static final String SHIPPING_METHODS = "/api/{api_version}/admin/shipping_methods";

    public static final String SHIPPING_METHODS_COUNT =
            "/api/{api_version}/admin/shipping_methods/count";

    public static final String SHIPPING_METHOD =
            "/api/{api_version}/admin/shipping_methods/{shipping_method_id}";

    public static final String SHIPPING_METHOD_DEFAULT =
            "/api/{api_version}/admin/shipping_methods/{shipping_method_id}/default";
}
