package com.webcommander.resources.carts;

import com.webcommander.Dto;
import com.webcommander.client.ApiResponse;
import com.webcommander.client.BoundResource;
import com.webcommander.client.RequestOptions;
import com.webcommander.client.ResourceBinding;
import com.webcommander.client.WebCommanderClient;
import com.webcommander.resources.carts.admin_carts_dto.AdminCartsDto.AdminCart;
import com.webcommander.resources.carts.admin_carts_dto.AdminCartsDto.AdminCartCustomerInput;
import com.webcommander.resources.carts.admin_carts_dto.AdminCartsDto.AdminCartInput;
import com.webcommander.resources.carts.admin_carts_dto.AdminCartsDto.AdminCartItemUpdateInput;
import com.webcommander.resources.carts.admin_carts_dto.AdminCartsDto.AdminCartItemsInput;
import com.webcommander.resources.carts.admin_carts_dto.AdminCartsDto.AdminCheckout;
import com.webcommander.resources.carts.admin_carts_dto.AdminCartsDto.AdminCheckoutCodeInput;
import com.webcommander.resources.carts.admin_carts_dto.AdminCartsDto.AdminCheckoutConfirmInput;
import com.webcommander.resources.carts.admin_carts_dto.AdminCartsDto.AdminCheckoutDeliveryScheduleInput;
import com.webcommander.resources.carts.admin_carts_dto.AdminCartsDto.AdminCheckoutInput;
import com.webcommander.resources.orders.orders_dto.OrdersDto.FlatOrder;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Admin carts and checkout — {@code /api/{api_version}/admin/orders}: a cart and its checkout are
 * one session, and {@code confirmCheckout} places the order from it.
 */
public final class AdminCarts implements BoundResource {

    private final WebCommanderClient client;

    public AdminCarts(WebCommanderClient client) {
        this.client = client;
    }

    /**
     * Start a cart for a customer — {@code POST /admin/orders/carts}, {@code 201}. {@code customer_id} is
     * the customer's {@code customer_id} and {@code products} ({@code product_id} and
     * {@code quantity}) is required. Keep {@code cart.session.session_id}: every other call takes it
     * as {@code session_id}. The session lasts 30 minutes from the last write.
     */
    public ApiResponse createAdminCart(Object payload) {
        return createAdminCart(payload, null);
    }

    public ApiResponse createAdminCart(Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPostRequest(AdminCartsUrls.ADMIN_CARTS,
                RequestOptions.builder(options).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(AdminCart.class, body)).build());
    }

    public ApiResponse createAdminCart(AdminCartInput payload) {
        return createAdminCart((Object) payload, null);
    }

    public ApiResponse createAdminCart(AdminCartInput payload, RequestOptions options) {
        return createAdminCart((Object) payload, options);
    }

    /**
     * Read the cart — {@code GET /admin/orders/carts/{session_id}}, where the slot takes
     * {@code cart.session.session_id}. A deleted cart is {@code 410 session_deleted} and one an order
     * was placed from is {@code 410 session_confirmed}.
     */
    public ApiResponse getAdminCart(String sessionId) {
        return getAdminCart(sessionId, null);
    }

    public ApiResponse getAdminCart(String sessionId, RequestOptions options) {
        return client.sendGetRequest(AdminCartsUrls.ADMIN_CART,
                RequestOptions.builder(options).pathParams(pathParams(sessionId))
                        .dtoFactory(body -> Dto.fromMap(AdminCart.class, body)).build());
    }

    /**
     * Replace the cart's customer and lines — {@code PUT /admin/orders/carts/{session_id}},
     * {@code 200}. {@code customer_id} and {@code products} are both sent. The lines are put on
     * again, so each gets a new {@code cart_item_id}.
     */
    public ApiResponse replaceAdminCart(String sessionId, Object payload) {
        return replaceAdminCart(sessionId, payload, null);
    }

    public ApiResponse replaceAdminCart(String sessionId, Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPutRequest(AdminCartsUrls.ADMIN_CART,
                RequestOptions.builder(options).pathParams(pathParams(sessionId)).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(AdminCart.class, body)).build());
    }

    public ApiResponse replaceAdminCart(String sessionId, AdminCartInput payload) {
        return replaceAdminCart(sessionId, (Object) payload, null);
    }

    public ApiResponse replaceAdminCart(String sessionId, AdminCartInput payload, RequestOptions options) {
        return replaceAdminCart(sessionId, (Object) payload, options);
    }

    /**
     * Change the cart's customer — {@code PATCH /admin/orders/carts/{session_id}}, {@code 200}. Only
     * {@code customer_id} is taken, and it is required: {@code products} is {@code 400 unknown_field}.
     */
    public ApiResponse updateAdminCart(String sessionId, Object payload) {
        return updateAdminCart(sessionId, payload, null);
    }

    public ApiResponse updateAdminCart(String sessionId, Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPatchRequest(AdminCartsUrls.ADMIN_CART,
                RequestOptions.builder(options).pathParams(pathParams(sessionId)).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(AdminCart.class, body)).build());
    }

    public ApiResponse updateAdminCart(String sessionId, AdminCartCustomerInput payload) {
        return updateAdminCart(sessionId, (Object) payload, null);
    }

    public ApiResponse updateAdminCart(String sessionId, AdminCartCustomerInput payload, RequestOptions options) {
        return updateAdminCart(sessionId, (Object) payload, options);
    }

    /**
     * Delete the cart — {@code DELETE /admin/orders/carts/{session_id}}; {@code 204} with no body,
     * after which every call on the session is {@code 410}.
     */
    public ApiResponse deleteAdminCart(String sessionId) {
        return deleteAdminCart(sessionId, null);
    }

    public ApiResponse deleteAdminCart(String sessionId, RequestOptions options) {
        return client.sendDeleteRequest(AdminCartsUrls.ADMIN_CART,
                RequestOptions.builder(options).pathParams(pathParams(sessionId))
                        .dtoFactory(null).build());
    }

    /**
     * Put products on the cart — {@code POST /admin/orders/carts/{session_id}/cart_items},
     * {@code 201} with the whole cart. {@code products} is required; one already on the cart raises
     * that line's quantity.
     */
    public ApiResponse addAdminCartItems(String sessionId, Object payload) {
        return addAdminCartItems(sessionId, payload, null);
    }

    public ApiResponse addAdminCartItems(String sessionId, Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPostRequest(AdminCartsUrls.ADMIN_CART_ITEMS,
                RequestOptions.builder(options).pathParams(pathParams(sessionId)).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(AdminCart.class, body)).build());
    }

    public ApiResponse addAdminCartItems(String sessionId, AdminCartItemsInput payload) {
        return addAdminCartItems(sessionId, (Object) payload, null);
    }

    public ApiResponse addAdminCartItems(String sessionId, AdminCartItemsInput payload, RequestOptions options) {
        return addAdminCartItems(sessionId, (Object) payload, options);
    }

    /**
     * Take every line off the cart — {@code DELETE /admin/orders/carts/{session_id}/cart_items},
     * {@code 200} with the empty cart. The session stays.
     */
    public ApiResponse clearAdminCartItems(String sessionId) {
        return clearAdminCartItems(sessionId, null);
    }

    public ApiResponse clearAdminCartItems(String sessionId, RequestOptions options) {
        return client.sendDeleteRequest(AdminCartsUrls.ADMIN_CART_ITEMS,
                RequestOptions.builder(options).pathParams(pathParams(sessionId))
                        .dtoFactory(body -> Dto.fromMap(AdminCart.class, body)).build());
    }

    /**
     * Set one line's quantity —
     * {@code PATCH /admin/orders/carts/{session_id}/cart_items/{cart_item_id}}, {@code 200} with the
     * cart. {@code cart_item_id} is a line's {@code cart_item_id} and {@code quantity} is required.
     * A line the cart does not hold is {@code 404}.
     */
    public ApiResponse updateAdminCartItem(String sessionId, Object cartItemId, Object payload) {
        return updateAdminCartItem(sessionId, cartItemId, payload, null);
    }

    public ApiResponse updateAdminCartItem(String sessionId, Object cartItemId, Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        Map<String, Object> params = pathParams(sessionId);
        params.put("cart_item_id", cartItemId);
        return client.sendPatchRequest(AdminCartsUrls.ADMIN_CART_ITEM,
                RequestOptions.builder(options).pathParams(params).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(AdminCart.class, body)).build());
    }

    public ApiResponse updateAdminCartItem(String sessionId, Object cartItemId, AdminCartItemUpdateInput payload) {
        return updateAdminCartItem(sessionId, cartItemId, (Object) payload, null);
    }

    public ApiResponse updateAdminCartItem(String sessionId, Object cartItemId, AdminCartItemUpdateInput payload, RequestOptions options) {
        return updateAdminCartItem(sessionId, cartItemId, (Object) payload, options);
    }

    /**
     * Take one line off the cart —
     * {@code DELETE /admin/orders/carts/{session_id}/cart_items/{cart_item_id}}, {@code 200} with
     * the cart. {@code cart_item_id} is a line's {@code cart_item_id}; one the cart does not hold is
     * {@code 404}.
     */
    public ApiResponse deleteAdminCartItem(String sessionId, Object cartItemId) {
        return deleteAdminCartItem(sessionId, cartItemId, null);
    }

    public ApiResponse deleteAdminCartItem(String sessionId, Object cartItemId, RequestOptions options) {
        Map<String, Object> params = pathParams(sessionId);
        params.put("cart_item_id", cartItemId);
        return client.sendDeleteRequest(AdminCartsUrls.ADMIN_CART_ITEM,
                RequestOptions.builder(options).pathParams(params)
                        .dtoFactory(body -> Dto.fromMap(AdminCart.class, body)).build());
    }

    /**
     * Read the checkout of a cart — {@code GET /admin/orders/checkout/{session_id}}.
     * {@code issues} lists what still blocks a confirm, {@code delivery_type} {@code required} until
     * one is set; the addresses start as the customer's.
     */
    public ApiResponse getCheckout(String sessionId) {
        return getCheckout(sessionId, null);
    }

    public ApiResponse getCheckout(String sessionId, RequestOptions options) {
        return client.sendGetRequest(AdminCartsUrls.ADMIN_CHECKOUT,
                RequestOptions.builder(options).pathParams(pathParams(sessionId))
                        .dtoFactory(body -> Dto.fromMap(AdminCheckout.class, body)).build());
    }

    /**
     * Set the delivery and addresses — {@code PATCH /admin/orders/checkout/{session_id}},
     * {@code 200} with the priced checkout. Only {@code delivery_type}, {@code billing_address} and
     * {@code shipping_address} are taken. The shipping and handling cost follow the shipping
     * address, so set it with {@code delivery_type}.
     */
    public ApiResponse updateCheckout(String sessionId, Object payload) {
        return updateCheckout(sessionId, payload, null);
    }

    public ApiResponse updateCheckout(String sessionId, Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPatchRequest(AdminCartsUrls.ADMIN_CHECKOUT,
                RequestOptions.builder(options).pathParams(pathParams(sessionId)).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(AdminCheckout.class, body)).build());
    }

    public ApiResponse updateCheckout(String sessionId, AdminCheckoutInput payload) {
        return updateCheckout(sessionId, (Object) payload, null);
    }

    public ApiResponse updateCheckout(String sessionId, AdminCheckoutInput payload, RequestOptions options) {
        return updateCheckout(sessionId, (Object) payload, options);
    }

    /**
     * Place the order — {@code POST /admin/orders/checkout/{session_id}/confirm}; {@code 201} with
     * the order, and the body optional. This creates a real order and closes the session, so every
     * later call on it is {@code 410 session_confirmed}. A checkout with {@code issues} is
     * {@code 422 checkout_incomplete}. Without a {@code payment} the order is unpaid.
     * {@code transactionNo} is sent as {@code X-Transaction-No} and files the order under that
     * number.
     */
    public ApiResponse confirmCheckout(String sessionId) {
        return confirmCheckout(sessionId, (Object) null, null, null);
    }

    public ApiResponse confirmCheckout(String sessionId, Object payload) {
        return confirmCheckout(sessionId, payload, null, null);
    }

    public ApiResponse confirmCheckout(String sessionId, Object payload,
            RequestOptions options) {
        return confirmCheckout(sessionId, payload, null, options);
    }

    public ApiResponse confirmCheckout(String sessionId, Object payload,
            String transactionNo) {
        return confirmCheckout(sessionId, payload, transactionNo, null);
    }

    public ApiResponse confirmCheckout(String sessionId, Object payload,
            String transactionNo, RequestOptions options) {
        RequestOptions given = options == null ? RequestOptions.none() : options;
        Map<String, String> headers = new LinkedHashMap<>();
        if (given.getHeaders() != null) {
            headers.putAll(given.getHeaders());
        }
        if (transactionNo != null) {
            headers.put(AdminCartsUrls.TRANSACTION_NO_HEADER, transactionNo);
        }
        return client.sendPostRequest(AdminCartsUrls.ADMIN_CHECKOUT_CONFIRM,
                RequestOptions.builder(options).pathParams(pathParams(sessionId)).jsonBody(payload)
                        .headers(headers).dtoFactory(body -> Dto.fromMap(FlatOrder.class, body))
                        .build());
    }

    public ApiResponse confirmCheckout(String sessionId, AdminCheckoutConfirmInput payload) {
        return confirmCheckout(sessionId, (Object) payload, null, null);
    }

    public ApiResponse confirmCheckout(String sessionId, AdminCheckoutConfirmInput payload,
            RequestOptions options) {
        return confirmCheckout(sessionId, (Object) payload, null, options);
    }

    public ApiResponse confirmCheckout(String sessionId, AdminCheckoutConfirmInput payload,
            String transactionNo) {
        return confirmCheckout(sessionId, (Object) payload, transactionNo, null);
    }

    public ApiResponse confirmCheckout(String sessionId, AdminCheckoutConfirmInput payload,
            String transactionNo, RequestOptions options) {
        return confirmCheckout(sessionId, (Object) payload, transactionNo, options);
    }

    /**
     * Apply a coupon — {@code POST /admin/orders/checkout/{session_id}/coupon}, {@code 200}.
     * {@code code} is required. One coupon at a time: a second is {@code 409}, and a code the store
     * does not accept is {@code 422 invalid_coupon}.
     */
    public ApiResponse applyCheckoutCoupon(String sessionId, Object payload) {
        return applyCheckoutCoupon(sessionId, payload, null);
    }

    public ApiResponse applyCheckoutCoupon(String sessionId, Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPostRequest(AdminCartsUrls.ADMIN_CHECKOUT_COUPON,
                RequestOptions.builder(options).pathParams(pathParams(sessionId)).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(AdminCheckout.class, body)).build());
    }

    public ApiResponse applyCheckoutCoupon(String sessionId, AdminCheckoutCodeInput payload) {
        return applyCheckoutCoupon(sessionId, (Object) payload, null);
    }

    public ApiResponse applyCheckoutCoupon(String sessionId, AdminCheckoutCodeInput payload, RequestOptions options) {
        return applyCheckoutCoupon(sessionId, (Object) payload, options);
    }

    /**
     * Remove the coupon — {@code DELETE /admin/orders/checkout/{session_id}/coupon}. With none
     * applied it is {@code 404 coupon_not_applied}.
     */
    public ApiResponse removeCheckoutCoupon(String sessionId) {
        return removeCheckoutCoupon(sessionId, null);
    }

    public ApiResponse removeCheckoutCoupon(String sessionId, RequestOptions options) {
        return client.sendDeleteRequest(AdminCartsUrls.ADMIN_CHECKOUT_COUPON,
                RequestOptions.builder(options).pathParams(pathParams(sessionId))
                        .dtoFactory(body -> Dto.fromMap(AdminCheckout.class, body)).build());
    }

    /**
     * Apply a gift card — {@code POST /admin/orders/checkout/{session_id}/gift_card}, {@code 200}.
     * {@code code} is required. The card must be usable by the cart's customer, else
     * {@code 422 invalid_gift_card}; a second card is {@code 409}. It lowers {@code due} and spends
     * nothing until an order is placed.
     */
    public ApiResponse applyCheckoutGiftCard(String sessionId, Object payload) {
        return applyCheckoutGiftCard(sessionId, payload, null);
    }

    public ApiResponse applyCheckoutGiftCard(String sessionId, Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPostRequest(AdminCartsUrls.ADMIN_CHECKOUT_GIFT_CARD,
                RequestOptions.builder(options).pathParams(pathParams(sessionId)).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(AdminCheckout.class, body)).build());
    }

    public ApiResponse applyCheckoutGiftCard(String sessionId, AdminCheckoutCodeInput payload) {
        return applyCheckoutGiftCard(sessionId, (Object) payload, null);
    }

    public ApiResponse applyCheckoutGiftCard(String sessionId, AdminCheckoutCodeInput payload, RequestOptions options) {
        return applyCheckoutGiftCard(sessionId, (Object) payload, options);
    }

    /**
     * Remove the gift card — {@code DELETE /admin/orders/checkout/{session_id}/gift_card}. With none
     * applied it is {@code 404 gift_card_not_applied}.
     */
    public ApiResponse removeCheckoutGiftCard(String sessionId) {
        return removeCheckoutGiftCard(sessionId, null);
    }

    public ApiResponse removeCheckoutGiftCard(String sessionId, RequestOptions options) {
        return client.sendDeleteRequest(AdminCartsUrls.ADMIN_CHECKOUT_GIFT_CARD,
                RequestOptions.builder(options).pathParams(pathParams(sessionId))
                        .dtoFactory(body -> Dto.fromMap(AdminCheckout.class, body)).build());
    }

    /**
     * Read the chosen delivery schedule —
     * {@code GET /admin/orders/checkout/{session_id}/delivery_schedules}, with {@code session} and
     * {@code delivery_schedule}, which is {@code null} until one is set.
     */
    public ApiResponse getCheckoutDeliverySchedules(String sessionId) {
        return getCheckoutDeliverySchedules(sessionId, null);
    }

    public ApiResponse getCheckoutDeliverySchedules(String sessionId, RequestOptions options) {
        return client.sendGetRequest(AdminCartsUrls.ADMIN_CHECKOUT_DELIVERY_SCHEDULES,
                RequestOptions.builder(options).pathParams(pathParams(sessionId))
                        .dtoFactory(body -> Dto.fromMap(AdminCheckout.class, body)).build());
    }

    /**
     * Choose a delivery schedule and date —
     * {@code PUT /admin/orders/checkout/{session_id}/delivery_schedules}. {@code id} and
     * {@code date} are both required. Only a {@code delivery_type} of {@code delivery} takes one
     * (else {@code 422}), an unknown {@code id} is {@code 404} and one that does not apply to the
     * order is {@code 422 schedule_not_applicable}.
     */
    public ApiResponse setCheckoutDeliverySchedule(String sessionId, Object payload) {
        return setCheckoutDeliverySchedule(sessionId, payload, null);
    }

    public ApiResponse setCheckoutDeliverySchedule(String sessionId, Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPutRequest(AdminCartsUrls.ADMIN_CHECKOUT_DELIVERY_SCHEDULES,
                RequestOptions.builder(options).pathParams(pathParams(sessionId)).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(AdminCheckout.class, body)).build());
    }

    public ApiResponse setCheckoutDeliverySchedule(String sessionId, AdminCheckoutDeliveryScheduleInput payload) {
        return setCheckoutDeliverySchedule(sessionId, (Object) payload, null);
    }

    public ApiResponse setCheckoutDeliverySchedule(String sessionId, AdminCheckoutDeliveryScheduleInput payload, RequestOptions options) {
        return setCheckoutDeliverySchedule(sessionId, (Object) payload, options);
    }

    /**
     * Clear the delivery schedule —
     * {@code DELETE /admin/orders/checkout/{session_id}/delivery_schedules}; {@code 204} with no
     * body, and also when none was set.
     */
    public ApiResponse clearCheckoutDeliverySchedule(String sessionId) {
        return clearCheckoutDeliverySchedule(sessionId, null);
    }

    public ApiResponse clearCheckoutDeliverySchedule(String sessionId, RequestOptions options) {
        return client.sendDeleteRequest(AdminCartsUrls.ADMIN_CHECKOUT_DELIVERY_SCHEDULES,
                RequestOptions.builder(options).pathParams(pathParams(sessionId))
                        .dtoFactory(null).build());
    }

    private static Map<String, Object> pathParams(String sessionId) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("session_id", sessionId);
        return params;
    }
}
