package com.webcommander.resources.order_custom_fields;

import com.webcommander.Dto;
import com.webcommander.client.ApiResponse;
import com.webcommander.client.BoundResource;
import com.webcommander.client.RequestOptions;
import com.webcommander.client.ResourceBinding;
import com.webcommander.client.WebCommanderClient;
import com.webcommander.resources.order_custom_fields.order_custom_fields_dto
        .OrderCustomFieldsDto.OrderCustomField;
import com.webcommander.resources.order_custom_fields.order_custom_fields_dto
        .OrderCustomFieldsDto.OrderCustomFieldPage;
import com.webcommander.resources.order_custom_fields.order_custom_fields_dto
        .OrderCustomFieldsDto.OrderCustomFieldTimeslotInput;

import java.util.LinkedHashMap;
import java.util.Map;

/** Order custom fields — {@code /api/{api_version}/admin/order_custom_fields}. */
public final class OrderCustomFields implements BoundResource {

    private final WebCommanderClient client;

    public OrderCustomFields(WebCommanderClient client) {
        this.client = client;
    }

    /** List the fields — {@code GET /admin/order_custom_fields}. */
    public ApiResponse listOrderCustomFields() {
        return listOrderCustomFields(null, null);
    }

    public ApiResponse listOrderCustomFields(Map<String, Object> queryParams) {
        return listOrderCustomFields(queryParams, null);
    }

    public ApiResponse listOrderCustomFields(Map<String, Object> queryParams,
            RequestOptions options) {
        return client.sendGetRequest(OrderCustomFieldsUrls.ORDER_CUSTOM_FIELDS,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(OrderCustomFieldPage.class, body)).build());
    }

    /** Count the fields — {@code GET /admin/order_custom_fields/count}. */
    public ApiResponse countOrderCustomFields() {
        return countOrderCustomFields(null, null);
    }

    public ApiResponse countOrderCustomFields(Map<String, Object> queryParams) {
        return countOrderCustomFields(queryParams, null);
    }

    public ApiResponse countOrderCustomFields(Map<String, Object> queryParams,
            RequestOptions options) {
        return client.sendGetRequest(OrderCustomFieldsUrls.ORDER_CUSTOM_FIELDS_COUNT,
                RequestOptions.builder(options).queryParams(queryParams).dtoFactory(null).build());
    }

    /** Retrieve one field — {@code GET /admin/order_custom_fields/{order_custom_field_id}}. */
    public ApiResponse getOrderCustomField(Object orderCustomFieldId) {
        return getOrderCustomField(orderCustomFieldId, null, null);
    }

    public ApiResponse getOrderCustomField(Object orderCustomFieldId,
            Map<String, Object> queryParams) {
        return getOrderCustomField(orderCustomFieldId, queryParams, null);
    }

    public ApiResponse getOrderCustomField(Object orderCustomFieldId,
            Map<String, Object> queryParams, RequestOptions options) {
        return client.sendGetRequest(OrderCustomFieldsUrls.ORDER_CUSTOM_FIELD,
                RequestOptions.builder(options).pathParams(pathParams(orderCustomFieldId))
                        .queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(OrderCustomField.class, body)).build());
    }

    /** Check a timeslot — {@code POST .../{order_custom_field_id}/validate_timeslot}. */
    public ApiResponse validateOrderCustomFieldTimeslot(Object orderCustomFieldId,
            Object payload) {
        return validateOrderCustomFieldTimeslot(orderCustomFieldId, payload, null);
    }

    public ApiResponse validateOrderCustomFieldTimeslot(Object orderCustomFieldId,
            Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPostRequest(OrderCustomFieldsUrls.ORDER_CUSTOM_FIELD_VALIDATE_TIMESLOT,
                RequestOptions.builder(options).pathParams(pathParams(orderCustomFieldId))
                        .jsonBody(payload).dtoFactory(null).build());
    }

    public ApiResponse validateOrderCustomFieldTimeslot(Object orderCustomFieldId,
            OrderCustomFieldTimeslotInput payload) {
        return validateOrderCustomFieldTimeslot(orderCustomFieldId, (Object) payload, null);
    }

    public ApiResponse validateOrderCustomFieldTimeslot(Object orderCustomFieldId,
            OrderCustomFieldTimeslotInput payload, RequestOptions options) {
        return validateOrderCustomFieldTimeslot(orderCustomFieldId, (Object) payload, options);
    }

    private static Map<String, Object> pathParams(Object orderCustomFieldId) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("order_custom_field_id", orderCustomFieldId);
        return params;
    }
}
