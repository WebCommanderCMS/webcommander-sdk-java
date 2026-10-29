package com.webcommander.resources.carts;

import com.webcommander.Dto;
import com.webcommander.client.ApiResponse;
import com.webcommander.client.BoundResource;
import com.webcommander.client.RequestOptions;
import com.webcommander.client.ResourceBinding;
import com.webcommander.client.WebCommanderClient;
import com.webcommander.resources.carts.carts_dto.CartsDto.Cart;
import com.webcommander.resources.carts.carts_dto.CartsDto.CartItemInput;
import com.webcommander.resources.carts.carts_dto.CartsDto.CartItemUpdateInput;
import com.webcommander.resources.carts.carts_dto.CartsDto.CartOrder;
import com.webcommander.resources.carts.carts_dto.CartsDto.CartOrderInput;
import com.webcommander.resources.carts.carts_dto.CartsDto.CartSession;
import com.webcommander.resources.carts.carts_dto.CartsDto.CartSessionInput;

import java.util.LinkedHashMap;
import java.util.Map;

/** Carts — {@code /api/{api_version}/carts}, and the order write under {@code /admin}. */
public final class Carts implements BoundResource {

    private final WebCommanderClient client;

    public Carts(WebCommanderClient client) {
        this.client = client;
    }

    /** Add a product to the cart {@code session_id} names, creating it — {@code POST /carts}. */
    public ApiResponse addCartItem(Object payload) {
        return addCartItem(payload, null);
    }

    public ApiResponse addCartItem(Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPostRequest(CartsUrls.CARTS,
                RequestOptions.builder(options).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(Cart.class, body)).build());
    }

    public ApiResponse addCartItem(CartItemInput payload) {
        return addCartItem((Object) payload, null);
    }

    public ApiResponse addCartItem(CartItemInput payload, RequestOptions options) {
        return addCartItem((Object) payload, options);
    }

    /** Retrieve the cart — {@code GET /carts/{session_id}}. */
    public ApiResponse getCart(String sessionId) {
        return getCart(sessionId, null);
    }

    public ApiResponse getCart(String sessionId, RequestOptions options) {
        return client.sendGetRequest(CartsUrls.CART,
                RequestOptions.builder(options).pathParams(pathParams(sessionId))
                        .dtoFactory(body -> Dto.fromMap(Cart.class, body)).build());
    }

    /** Ask which session id to use — {@code PUT /carts/{session_id}}; nothing is stored. */
    public ApiResponse resolveCartSession(String sessionId, Object payload) {
        return resolveCartSession(sessionId, payload, null);
    }

    public ApiResponse resolveCartSession(String sessionId, Object payload,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        RequestOptions given = options == null ? RequestOptions.none() : options;
        Map<String, String> headers = new LinkedHashMap<>();
        if (given.getHeaders() != null) {
            headers.putAll(given.getHeaders());
        }
        // No charset: required by the endpoint.
        headers.put("Content-Type", "application/json");
        return client.sendPutRequest(CartsUrls.CART,
                RequestOptions.builder(options).pathParams(pathParams(sessionId)).jsonBody(payload)
                        .headers(headers).dtoFactory(Carts::readSession).build());
    }

    public ApiResponse resolveCartSession(String sessionId, CartSessionInput payload) {
        return resolveCartSession(sessionId, (Object) payload, null);
    }

    public ApiResponse resolveCartSession(String sessionId, CartSessionInput payload,
            RequestOptions options) {
        return resolveCartSession(sessionId, (Object) payload, options);
    }

    /** Change one line's quantity — {@code PATCH /carts/{session_id}}. */
    public ApiResponse updateCartItem(String sessionId, Object payload) {
        return updateCartItem(sessionId, payload, null);
    }

    public ApiResponse updateCartItem(String sessionId, Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPatchRequest(CartsUrls.CART,
                RequestOptions.builder(options).pathParams(pathParams(sessionId)).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(Cart.class, body)).build());
    }

    public ApiResponse updateCartItem(String sessionId, CartItemUpdateInput payload) {
        return updateCartItem(sessionId, (Object) payload, null);
    }

    public ApiResponse updateCartItem(String sessionId, CartItemUpdateInput payload,
            RequestOptions options) {
        return updateCartItem(sessionId, (Object) payload, options);
    }

    /** Empty the cart — {@code DELETE /carts/{session_id}}. */
    public ApiResponse deleteCart(String sessionId) {
        return deleteCart(sessionId, null);
    }

    public ApiResponse deleteCart(String sessionId, RequestOptions options) {
        return client.sendDeleteRequest(CartsUrls.CART,
                RequestOptions.builder(options).pathParams(pathParams(sessionId)).dtoFactory(null)
                        .build());
    }

    /** Remove one line — {@code DELETE /carts/{session_id}/cart_items/{cart_item_id}}. */
    public ApiResponse deleteCartItem(String sessionId, Object cartItemId) {
        return deleteCartItem(sessionId, cartItemId, null);
    }

    public ApiResponse deleteCartItem(String sessionId, Object cartItemId,
            RequestOptions options) {
        Map<String, Object> params = pathParams(sessionId);
        params.put("cart_item_id", cartItemId);
        return client.sendDeleteRequest(CartsUrls.CART_ITEM,
                RequestOptions.builder(options).pathParams(params).dtoFactory(null).build());
    }

    /** Turn the cart into an order — {@code POST /admin/orders/carts/{session_id}}. */
    public ApiResponse createOrderFromCart(String sessionId, Object payload) {
        return createOrderFromCart(sessionId, payload, null, null);
    }

    public ApiResponse createOrderFromCart(String sessionId, Object payload,
            RequestOptions options) {
        return createOrderFromCart(sessionId, payload, null, options);
    }

    public ApiResponse createOrderFromCart(String sessionId, Object payload,
            String transactionNo) {
        return createOrderFromCart(sessionId, payload, transactionNo, null);
    }

    public ApiResponse createOrderFromCart(String sessionId, Object payload,
            String transactionNo, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        RequestOptions given = options == null ? RequestOptions.none() : options;
        Map<String, String> headers = new LinkedHashMap<>();
        if (given.getHeaders() != null) {
            headers.putAll(given.getHeaders());
        }
        if (transactionNo != null) {
            headers.put(CartsUrls.TRANSACTION_NO_HEADER, transactionNo);
        }
        return client.sendPostRequest(CartsUrls.CART_ORDER,
                RequestOptions.builder(options).pathParams(pathParams(sessionId)).jsonBody(payload)
                        .headers(headers)
                        .dtoFactory(body -> Dto.fromMap(CartOrder.class, body)).build());
    }

    public ApiResponse createOrderFromCart(String sessionId, CartOrderInput payload) {
        return createOrderFromCart(sessionId, (Object) payload, null, null);
    }

    public ApiResponse createOrderFromCart(String sessionId, CartOrderInput payload,
            RequestOptions options) {
        return createOrderFromCart(sessionId, (Object) payload, null, options);
    }

    public ApiResponse createOrderFromCart(String sessionId, CartOrderInput payload,
            String transactionNo) {
        return createOrderFromCart(sessionId, (Object) payload, transactionNo, null);
    }

    public ApiResponse createOrderFromCart(String sessionId, CartOrderInput payload,
            String transactionNo, RequestOptions options) {
        return createOrderFromCart(sessionId, (Object) payload, transactionNo, options);
    }

    /** By hand, as no field name derives the camel-case {@code sessionId}. */
    private static CartSession readSession(Object body) {
        CartSession session = new CartSession();
        Object value = ((Map<?, ?>) body).get(CartsUrls.CART_SESSION_KEY);
        session.sessionId = value instanceof String text ? text : null;
        return session;
    }

    private static Map<String, Object> pathParams(String sessionId) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("session_id", sessionId);
        return params;
    }
}
