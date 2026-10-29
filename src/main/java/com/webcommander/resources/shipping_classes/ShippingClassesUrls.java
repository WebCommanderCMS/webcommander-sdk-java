package com.webcommander.resources.shipping_classes;

/** Path templates for the shipping class endpoints. */
public final class ShippingClassesUrls {

    private ShippingClassesUrls() {}

    public static final String SHIPPING_CLASSES = "/api/{api_version}/admin/shipping_classes";

    public static final String SHIPPING_CLASSES_COUNT =
            "/api/{api_version}/admin/shipping_classes/count";

    public static final String SHIPPING_CLASS =
            "/api/{api_version}/admin/shipping_classes/{shipping_class_id}";

    public static final String SHIPPING_CLASS_DEFAULT =
            "/api/{api_version}/admin/shipping_classes/{shipping_class_id}/default";
}
