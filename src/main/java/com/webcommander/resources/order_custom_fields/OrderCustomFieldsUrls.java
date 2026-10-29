package com.webcommander.resources.order_custom_fields;

/** Path templates for the order custom field endpoints. */
public final class OrderCustomFieldsUrls {

    private OrderCustomFieldsUrls() {}

    public static final String ORDER_CUSTOM_FIELDS = "/api/{api_version}/admin/order_custom_fields";

    public static final String ORDER_CUSTOM_FIELDS_COUNT =
            "/api/{api_version}/admin/order_custom_fields/count";

    public static final String ORDER_CUSTOM_FIELD =
            "/api/{api_version}/admin/order_custom_fields/{order_custom_field_id}";

    public static final String ORDER_CUSTOM_FIELD_VALIDATE_TIMESLOT =
            "/api/{api_version}/admin/order_custom_fields/{order_custom_field_id}"
                    + "/validate_timeslot";
}
