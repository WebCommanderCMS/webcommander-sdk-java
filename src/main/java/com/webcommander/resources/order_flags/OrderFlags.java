package com.webcommander.resources.order_flags;

import com.webcommander.Dto;
import com.webcommander.client.ApiResponse;
import com.webcommander.client.BoundResource;
import com.webcommander.client.RequestOptions;
import com.webcommander.client.ResourceBinding;
import com.webcommander.client.WebCommanderClient;
import com.webcommander.resources.order_flags.order_flags_dto.OrderFlagsDto.OrderFlag;
import com.webcommander.resources.order_flags.order_flags_dto.OrderFlagsDto.OrderFlagInput;
import com.webcommander.resources.order_flags.order_flags_dto.OrderFlagsDto.OrderFlagPage;

import java.util.LinkedHashMap;
import java.util.Map;

/** Order flags — {@code /api/{api_version}/admin/order_flags}. */
public final class OrderFlags implements BoundResource {

    private final WebCommanderClient client;

    public OrderFlags(WebCommanderClient client) {
        this.client = client;
    }

    /** List the flags — {@code GET /admin/order_flags}. */
    public ApiResponse listOrderFlags() {
        return listOrderFlags(null, null);
    }

    public ApiResponse listOrderFlags(Map<String, Object> queryParams) {
        return listOrderFlags(queryParams, null);
    }

    public ApiResponse listOrderFlags(Map<String, Object> queryParams, RequestOptions options) {
        return client.sendGetRequest(OrderFlagsUrls.ORDER_FLAGS,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(OrderFlagPage.class, body)).build());
    }

    /** Ask whether the collection is reachable — {@code HEAD /admin/order_flags}. */
    public ApiResponse headOrderFlags() {
        return headOrderFlags(null);
    }

    public ApiResponse headOrderFlags(RequestOptions options) {
        return client.sendApiRequest("HEAD", OrderFlagsUrls.ORDER_FLAGS,
                RequestOptions.builder(options).dtoFactory(null).build());
    }

    /** Create a flag — {@code POST /admin/order_flags}, wrapped in {@code order_flag}. */
    public ApiResponse createOrderFlag(Object payload) {
        return createOrderFlag(payload, null);
    }

    public ApiResponse createOrderFlag(Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPostRequest(OrderFlagsUrls.ORDER_FLAGS,
                RequestOptions.builder(options).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(OrderFlag.class, body)).build());
    }

    public ApiResponse createOrderFlag(OrderFlagInput payload) {
        return createOrderFlag((Object) payload, null);
    }

    public ApiResponse createOrderFlag(OrderFlagInput payload, RequestOptions options) {
        return createOrderFlag((Object) payload, options);
    }

    /** Retrieve one flag — {@code GET /admin/order_flags/{order_flag_id}}. */
    public ApiResponse getOrderFlag(Object orderFlagId) {
        return getOrderFlag(orderFlagId, null);
    }

    public ApiResponse getOrderFlag(Object orderFlagId, RequestOptions options) {
        return client.sendGetRequest(OrderFlagsUrls.ORDER_FLAG,
                RequestOptions.builder(options).pathParams(pathParams(orderFlagId))
                        .dtoFactory(body -> Dto.fromMap(OrderFlag.class, body)).build());
    }

    /** Probe one flag — {@code HEAD /admin/order_flags/{order_flag_id}}. */
    public ApiResponse headOrderFlag(Object orderFlagId) {
        return headOrderFlag(orderFlagId, null);
    }

    public ApiResponse headOrderFlag(Object orderFlagId, RequestOptions options) {
        return client.sendApiRequest("HEAD", OrderFlagsUrls.ORDER_FLAG,
                RequestOptions.builder(options).pathParams(pathParams(orderFlagId)).dtoFactory(null)
                        .build());
    }

    /** Replace one flag — {@code PUT /admin/order_flags/{order_flag_id}}. */
    public ApiResponse updateOrderFlag(Object orderFlagId, Object payload) {
        return updateOrderFlag(orderFlagId, payload, null);
    }

    public ApiResponse updateOrderFlag(Object orderFlagId, Object payload,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPutRequest(OrderFlagsUrls.ORDER_FLAG,
                RequestOptions.builder(options).pathParams(pathParams(orderFlagId))
                        .jsonBody(payload).dtoFactory(body -> Dto.fromMap(OrderFlag.class, body))
                        .build());
    }

    public ApiResponse updateOrderFlag(Object orderFlagId, OrderFlagInput payload) {
        return updateOrderFlag(orderFlagId, (Object) payload, null);
    }

    public ApiResponse updateOrderFlag(Object orderFlagId, OrderFlagInput payload,
            RequestOptions options) {
        return updateOrderFlag(orderFlagId, (Object) payload, options);
    }

    /** Delete a flag — {@code DELETE /admin/order_flags/{order_flag_id}}. */
    public ApiResponse deleteOrderFlag(Object orderFlagId) {
        return deleteOrderFlag(orderFlagId, null);
    }

    public ApiResponse deleteOrderFlag(Object orderFlagId, RequestOptions options) {
        return client.sendDeleteRequest(OrderFlagsUrls.ORDER_FLAG,
                RequestOptions.builder(options).pathParams(pathParams(orderFlagId)).dtoFactory(null)
                        .build());
    }

    private static Map<String, Object> pathParams(Object orderFlagId) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("order_flag_id", orderFlagId);
        return params;
    }
}
