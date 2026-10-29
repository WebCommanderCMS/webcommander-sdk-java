package com.webcommander.resources.shipping_classes;

import com.webcommander.Dto;
import com.webcommander.client.ApiResponse;
import com.webcommander.client.BoundResource;
import com.webcommander.client.RequestOptions;
import com.webcommander.client.ResourceBinding;
import com.webcommander.client.WebCommanderClient;
import com.webcommander.resources.shipping_classes.shipping_classes_dto.ShippingClassesDto
        .ShippingClass;
import com.webcommander.resources.shipping_classes.shipping_classes_dto.ShippingClassesDto
        .ShippingClassCount;
import com.webcommander.resources.shipping_classes.shipping_classes_dto.ShippingClassesDto
        .ShippingClassInput;
import com.webcommander.resources.shipping_classes.shipping_classes_dto.ShippingClassesDto
        .ShippingClassPage;

import java.util.LinkedHashMap;
import java.util.Map;

/** Shipping classes — {@code /api/{api_version}/admin/shipping_classes}. */
public final class ShippingClasses implements BoundResource {

    private final WebCommanderClient client;

    public ShippingClasses(WebCommanderClient client) {
        this.client = client;
    }

    /** List the classes — {@code GET /admin/shipping_classes}; a created row is listed first. */
    public ApiResponse listShippingClasses() {
        return listShippingClasses(null, null);
    }

    public ApiResponse listShippingClasses(Map<String, Object> queryParams) {
        return listShippingClasses(queryParams, null);
    }

    public ApiResponse listShippingClasses(Map<String, Object> queryParams,
            RequestOptions options) {
        return client.sendGetRequest(ShippingClassesUrls.SHIPPING_CLASSES,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(ShippingClassPage.class, body)).build());
    }

    /** Ask whether the collection answers — {@code HEAD /admin/shipping_classes}. */
    public ApiResponse headShippingClasses() {
        return headShippingClasses(null);
    }

    public ApiResponse headShippingClasses(RequestOptions options) {
        return client.sendApiRequest("HEAD", ShippingClassesUrls.SHIPPING_CLASSES,
                RequestOptions.builder(options).dtoFactory(null).build());
    }

    /** How many classes the store holds — {@code GET /admin/shipping_classes/count}. */
    public ApiResponse countShippingClasses() {
        return countShippingClasses(null, null);
    }

    public ApiResponse countShippingClasses(Map<String, Object> queryParams) {
        return countShippingClasses(queryParams, null);
    }

    public ApiResponse countShippingClasses(Map<String, Object> queryParams,
            RequestOptions options) {
        return client.sendGetRequest(ShippingClassesUrls.SHIPPING_CLASSES_COUNT,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(ShippingClassCount.class, body)).build());
    }

    /** Create a class — {@code POST /admin/shipping_classes}. */
    public ApiResponse createShippingClass(Object payload) {
        return createShippingClass(payload, null);
    }

    public ApiResponse createShippingClass(Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPostRequest(ShippingClassesUrls.SHIPPING_CLASSES,
                RequestOptions.builder(options).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(ShippingClass.class, body)).build());
    }

    public ApiResponse createShippingClass(ShippingClassInput payload) {
        return createShippingClass((Object) payload, null);
    }

    public ApiResponse createShippingClass(ShippingClassInput payload, RequestOptions options) {
        return createShippingClass((Object) payload, options);
    }

    /** Retrieve one class — {@code GET /admin/shipping_classes/{shipping_class_id}}. */
    public ApiResponse getShippingClass(Object shippingClassId) {
        return getShippingClass(shippingClassId, null, null);
    }

    public ApiResponse getShippingClass(Object shippingClassId, Map<String, Object> queryParams) {
        return getShippingClass(shippingClassId, queryParams, null);
    }

    public ApiResponse getShippingClass(Object shippingClassId, Map<String, Object> queryParams,
            RequestOptions options) {
        return client.sendGetRequest(ShippingClassesUrls.SHIPPING_CLASS,
                RequestOptions.builder(options).pathParams(pathParams(shippingClassId))
                        .queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(ShippingClass.class, body)).build());
    }

    /** Probe one class — {@code HEAD /admin/shipping_classes/{shipping_class_id}}. */
    public ApiResponse headShippingClass(Object shippingClassId) {
        return headShippingClass(shippingClassId, null);
    }

    public ApiResponse headShippingClass(Object shippingClassId, RequestOptions options) {
        return client.sendApiRequest("HEAD", ShippingClassesUrls.SHIPPING_CLASS,
                RequestOptions.builder(options).pathParams(pathParams(shippingClassId))
                        .dtoFactory(null).build());
    }

    /** Replace one class — {@code PUT /admin/shipping_classes/{shipping_class_id}}. */
    public ApiResponse updateShippingClass(Object shippingClassId, Object payload) {
        return updateShippingClass(shippingClassId, payload, null);
    }

    public ApiResponse updateShippingClass(Object shippingClassId, Object payload,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPutRequest(ShippingClassesUrls.SHIPPING_CLASS,
                RequestOptions.builder(options).pathParams(pathParams(shippingClassId))
                        .jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(ShippingClass.class, body)).build());
    }

    public ApiResponse updateShippingClass(Object shippingClassId, ShippingClassInput payload) {
        return updateShippingClass(shippingClassId, (Object) payload, null);
    }

    public ApiResponse updateShippingClass(Object shippingClassId, ShippingClassInput payload,
            RequestOptions options) {
        return updateShippingClass(shippingClassId, (Object) payload, options);
    }

    /** Delete one class — {@code DELETE /admin/shipping_classes/{shipping_class_id}}. */
    public ApiResponse deleteShippingClass(Object shippingClassId) {
        return deleteShippingClass(shippingClassId, null);
    }

    public ApiResponse deleteShippingClass(Object shippingClassId, RequestOptions options) {
        return client.sendDeleteRequest(ShippingClassesUrls.SHIPPING_CLASS,
                RequestOptions.builder(options).pathParams(pathParams(shippingClassId))
                        .dtoFactory(null).build());
    }

    /** Make one class the default — {@code PUT .../{shipping_class_id}/default}. */
    public ApiResponse setShippingClassDefault(Object shippingClassId) {
        return setShippingClassDefault(shippingClassId, null);
    }

    public ApiResponse setShippingClassDefault(Object shippingClassId, RequestOptions options) {
        return client.sendPutRequest(ShippingClassesUrls.SHIPPING_CLASS_DEFAULT,
                RequestOptions.builder(options).pathParams(pathParams(shippingClassId))
                        .dtoFactory(body -> Dto.fromMap(ShippingClass.class, body)).build());
    }

    private static Map<String, Object> pathParams(Object shippingClassId) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("shipping_class_id", shippingClassId);
        return params;
    }
}
