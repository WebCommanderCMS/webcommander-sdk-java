package com.webcommander.resources.shipping_methods;

import com.webcommander.Dto;
import com.webcommander.client.ApiResponse;
import com.webcommander.client.BoundResource;
import com.webcommander.client.RequestOptions;
import com.webcommander.client.ResourceBinding;
import com.webcommander.client.WebCommanderClient;
import com.webcommander.resources.shipping_methods.shipping_methods_dto.ShippingMethodsDto
        .ShippingMethod;
import com.webcommander.resources.shipping_methods.shipping_methods_dto.ShippingMethodsDto
        .ShippingMethodCount;
import com.webcommander.resources.shipping_methods.shipping_methods_dto.ShippingMethodsDto
        .ShippingMethodInput;
import com.webcommander.resources.shipping_methods.shipping_methods_dto.ShippingMethodsDto
        .ShippingMethodPage;

import java.util.LinkedHashMap;
import java.util.Map;

/** Shipping methods — {@code /api/{api_version}/admin/shipping_methods}. */
public final class ShippingMethods implements BoundResource {

    private final WebCommanderClient client;

    public ShippingMethods(WebCommanderClient client) {
        this.client = client;
    }

    /** List the methods — {@code GET /admin/shipping_methods}. */
    public ApiResponse listShippingMethods() {
        return listShippingMethods(null, null);
    }

    public ApiResponse listShippingMethods(Map<String, Object> queryParams) {
        return listShippingMethods(queryParams, null);
    }

    public ApiResponse listShippingMethods(Map<String, Object> queryParams,
            RequestOptions options) {
        return client.sendGetRequest(ShippingMethodsUrls.SHIPPING_METHODS,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(ShippingMethodPage.class, body)).build());
    }

    /** Ask whether the collection is reachable — {@code HEAD /admin/shipping_methods}. */
    public ApiResponse headShippingMethods() {
        return headShippingMethods(null);
    }

    public ApiResponse headShippingMethods(RequestOptions options) {
        return client.sendApiRequest("HEAD", ShippingMethodsUrls.SHIPPING_METHODS,
                RequestOptions.builder(options).dtoFactory(null).build());
    }

    /** How many methods the store holds — {@code GET /admin/shipping_methods/count}. */
    public ApiResponse countShippingMethods() {
        return countShippingMethods(null, null);
    }

    public ApiResponse countShippingMethods(Map<String, Object> queryParams) {
        return countShippingMethods(queryParams, null);
    }

    public ApiResponse countShippingMethods(Map<String, Object> queryParams,
            RequestOptions options) {
        return client.sendGetRequest(ShippingMethodsUrls.SHIPPING_METHODS_COUNT,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(ShippingMethodCount.class, body)).build());
    }

    /** Create a method — {@code POST /admin/shipping_methods}. */
    public ApiResponse createShippingMethod(Object payload) {
        return createShippingMethod(payload, null);
    }

    public ApiResponse createShippingMethod(Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPostRequest(ShippingMethodsUrls.SHIPPING_METHODS,
                RequestOptions.builder(options).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(ShippingMethod.class, body)).build());
    }

    public ApiResponse createShippingMethod(ShippingMethodInput payload) {
        return createShippingMethod((Object) payload, null);
    }

    public ApiResponse createShippingMethod(ShippingMethodInput payload, RequestOptions options) {
        return createShippingMethod((Object) payload, options);
    }

    /** Retrieve one method — {@code GET /admin/shipping_methods/{shipping_method_id}}. */
    public ApiResponse getShippingMethod(Object shippingMethodId) {
        return getShippingMethod(shippingMethodId, null, null);
    }

    public ApiResponse getShippingMethod(Object shippingMethodId,
            Map<String, Object> queryParams) {
        return getShippingMethod(shippingMethodId, queryParams, null);
    }

    public ApiResponse getShippingMethod(Object shippingMethodId, Map<String, Object> queryParams,
            RequestOptions options) {
        return client.sendGetRequest(ShippingMethodsUrls.SHIPPING_METHOD,
                RequestOptions.builder(options).pathParams(pathParams(shippingMethodId))
                        .queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(ShippingMethod.class, body)).build());
    }

    /** Probe one method — {@code HEAD /admin/shipping_methods/{shipping_method_id}}. */
    public ApiResponse headShippingMethod(Object shippingMethodId) {
        return headShippingMethod(shippingMethodId, null);
    }

    public ApiResponse headShippingMethod(Object shippingMethodId, RequestOptions options) {
        return client.sendApiRequest("HEAD", ShippingMethodsUrls.SHIPPING_METHOD,
                RequestOptions.builder(options).pathParams(pathParams(shippingMethodId))
                        .dtoFactory(null).build());
    }

    /** Rename one method — {@code PUT /admin/shipping_methods/{shipping_method_id}}. */
    public ApiResponse updateShippingMethod(Object shippingMethodId, Object payload) {
        return updateShippingMethod(shippingMethodId, payload, null);
    }

    public ApiResponse updateShippingMethod(Object shippingMethodId, Object payload,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPutRequest(ShippingMethodsUrls.SHIPPING_METHOD,
                RequestOptions.builder(options).pathParams(pathParams(shippingMethodId))
                        .jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(ShippingMethod.class, body)).build());
    }

    public ApiResponse updateShippingMethod(Object shippingMethodId, ShippingMethodInput payload) {
        return updateShippingMethod(shippingMethodId, (Object) payload, null);
    }

    public ApiResponse updateShippingMethod(Object shippingMethodId, ShippingMethodInput payload,
            RequestOptions options) {
        return updateShippingMethod(shippingMethodId, (Object) payload, options);
    }

    /** Delete a method — {@code DELETE /admin/shipping_methods/{shipping_method_id}}. */
    public ApiResponse deleteShippingMethod(Object shippingMethodId) {
        return deleteShippingMethod(shippingMethodId, null);
    }

    public ApiResponse deleteShippingMethod(Object shippingMethodId, RequestOptions options) {
        return client.sendDeleteRequest(ShippingMethodsUrls.SHIPPING_METHOD,
                RequestOptions.builder(options).pathParams(pathParams(shippingMethodId))
                        .dtoFactory(null).build());
    }

    /** Make one method the store default — {@code PUT .../{shipping_method_id}/default}. */
    public ApiResponse setShippingMethodDefault(Object shippingMethodId) {
        return setShippingMethodDefault(shippingMethodId, null);
    }

    public ApiResponse setShippingMethodDefault(Object shippingMethodId, RequestOptions options) {
        return client.sendPutRequest(ShippingMethodsUrls.SHIPPING_METHOD_DEFAULT,
                RequestOptions.builder(options).pathParams(pathParams(shippingMethodId))
                        .dtoFactory(body -> Dto.fromMap(ShippingMethod.class, body)).build());
    }

    private static Map<String, Object> pathParams(Object shippingMethodId) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("shipping_method_id", shippingMethodId);
        return params;
    }
}
