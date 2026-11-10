package com.webcommander.resources.order_previews;

import com.webcommander.Dto;
import com.webcommander.client.ApiResponse;
import com.webcommander.client.BoundResource;
import com.webcommander.client.RequestOptions;
import com.webcommander.client.ResourceBinding;
import com.webcommander.client.WebCommanderClient;
import com.webcommander.resources.order_previews.order_previews_dto.OrderPreviewsDto.OrderPreview;
import com.webcommander.resources.order_previews.order_previews_dto.OrderPreviewsDto.OrderPreviewCodeInput;
import com.webcommander.resources.order_previews.order_previews_dto.OrderPreviewsDto.OrderPreviewConfirmInput;
import com.webcommander.resources.order_previews.order_previews_dto.OrderPreviewsDto.OrderPreviewInput;
import com.webcommander.resources.orders.orders_dto.OrdersDto.FlatOrder;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Order previews — {@code /api/{api_version}/admin/orders/preview}; a priced session that places
 * no order until it is confirmed.
 */
public final class OrderPreviews implements BoundResource {

    private final WebCommanderClient client;

    public OrderPreviews(WebCommanderClient client) {
        this.client = client;
    }

    /**
     * Start an order preview — {@code POST /admin/orders/preview}, {@code 201}, a priced session
     * that places no order. {@code products} is required; read {@code order.session.session_id},
     * which every other call takes as {@code preview_session_id}. The session lasts 30 minutes.
     */
    public ApiResponse createOrderPreview(Object payload) {
        return createOrderPreview(payload, null);
    }

    public ApiResponse createOrderPreview(Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPostRequest(OrderPreviewsUrls.ORDER_PREVIEWS,
                RequestOptions.builder(options).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(OrderPreview.class, body)).build());
    }

    public ApiResponse createOrderPreview(OrderPreviewInput payload) {
        return createOrderPreview((Object) payload, null);
    }

    public ApiResponse createOrderPreview(OrderPreviewInput payload, RequestOptions options) {
        return createOrderPreview((Object) payload, options);
    }

    /**
     * Read an order preview — {@code GET /admin/orders/preview/{preview_session_id}}, where the
     * slot takes the {@code session_id} the start answered. A read does not extend the session.
     * An unknown id is {@code 404}, and a session already confirmed is {@code 410}
     * {@code session_confirmed}.
     */
    public ApiResponse getOrderPreview(Object previewSessionId) {
        return getOrderPreview(previewSessionId, null);
    }

    public ApiResponse getOrderPreview(Object previewSessionId, RequestOptions options) {
        return client.sendGetRequest(OrderPreviewsUrls.ORDER_PREVIEW,
                RequestOptions.builder(options).pathParams(pathParams(previewSessionId))
                        .dtoFactory(body -> Dto.fromMap(OrderPreview.class, body)).build());
    }

    /**
     * Place the order — {@code POST /admin/orders/preview/{preview_session_id}/confirm};
     * {@code 201} with the created order, and the body optional. This creates a real order and
     * closes the session, so a second confirm, or any other call on it, is {@code 410}
     * {@code session_confirmed}. Without a {@code payment} the order is unpaid, and so it is after
     * a {@code payment_gateway} alone.
     */
    public ApiResponse confirmOrderPreview(Object previewSessionId) {
        return confirmOrderPreview(previewSessionId, (Object) null, null);
    }

    public ApiResponse confirmOrderPreview(Object previewSessionId, Object payload) {
        return confirmOrderPreview(previewSessionId, payload, null);
    }

    public ApiResponse confirmOrderPreview(Object previewSessionId, Object payload,
            RequestOptions options) {
        return client.sendPostRequest(OrderPreviewsUrls.ORDER_PREVIEW_CONFIRM,
                RequestOptions.builder(options).pathParams(pathParams(previewSessionId))
                        .jsonBody(payload).dtoFactory(body -> Dto.fromMap(FlatOrder.class, body))
                        .build());
    }

    public ApiResponse confirmOrderPreview(Object previewSessionId,
            OrderPreviewConfirmInput payload) {
        return confirmOrderPreview(previewSessionId, (Object) payload, null);
    }

    public ApiResponse confirmOrderPreview(Object previewSessionId,
            OrderPreviewConfirmInput payload, RequestOptions options) {
        return confirmOrderPreview(previewSessionId, (Object) payload, options);
    }

    /**
     * Apply a coupon — {@code POST /admin/orders/preview/{preview_session_id}/coupon},
     * {@code 200} with {@code session}, {@code discounts} and {@code totals}. One coupon at a
     * time: a second is {@code 409}, and a code the store does not accept is {@code 422}
     * {@code invalid_coupon}.
     */
    public ApiResponse applyOrderPreviewCoupon(Object previewSessionId, Object payload) {
        return applyOrderPreviewCoupon(previewSessionId, payload, null);
    }

    public ApiResponse applyOrderPreviewCoupon(Object previewSessionId, Object payload,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPostRequest(OrderPreviewsUrls.ORDER_PREVIEW_COUPON,
                RequestOptions.builder(options).pathParams(pathParams(previewSessionId))
                        .jsonBody(payload).dtoFactory(body -> Dto.fromMap(OrderPreview.class, body))
                        .build());
    }

    public ApiResponse applyOrderPreviewCoupon(Object previewSessionId,
            OrderPreviewCodeInput payload) {
        return applyOrderPreviewCoupon(previewSessionId, (Object) payload, null);
    }

    public ApiResponse applyOrderPreviewCoupon(Object previewSessionId,
            OrderPreviewCodeInput payload, RequestOptions options) {
        return applyOrderPreviewCoupon(previewSessionId, (Object) payload, options);
    }

    /**
     * Remove the coupon — {@code DELETE /admin/orders/preview/{preview_session_id}/coupon},
     * {@code 204} with no body. With none applied it is {@code 404} {@code coupon_not_applied}.
     */
    public ApiResponse removeOrderPreviewCoupon(Object previewSessionId) {
        return removeOrderPreviewCoupon(previewSessionId, null);
    }

    public ApiResponse removeOrderPreviewCoupon(Object previewSessionId, RequestOptions options) {
        return client.sendDeleteRequest(OrderPreviewsUrls.ORDER_PREVIEW_COUPON,
                RequestOptions.builder(options).pathParams(pathParams(previewSessionId))
                        .dtoFactory(null).build());
    }

    /**
     * Apply a gift card — {@code POST /admin/orders/preview/{preview_session_id}/gift-card},
     * {@code 200} with {@code session}, {@code discounts} and {@code totals}. The card must be the
     * previewed customer's, active, paid, in date and with a balance, else {@code 422}
     * {@code invalid_gift_card}; a second card is {@code 409}. It lowers {@code due} and spends
     * nothing until an order is placed with it applied.
     */
    public ApiResponse applyOrderPreviewGiftCard(Object previewSessionId, Object payload) {
        return applyOrderPreviewGiftCard(previewSessionId, payload, null);
    }

    public ApiResponse applyOrderPreviewGiftCard(Object previewSessionId, Object payload,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPostRequest(OrderPreviewsUrls.ORDER_PREVIEW_GIFT_CARD,
                RequestOptions.builder(options).pathParams(pathParams(previewSessionId))
                        .jsonBody(payload).dtoFactory(body -> Dto.fromMap(OrderPreview.class, body))
                        .build());
    }

    public ApiResponse applyOrderPreviewGiftCard(Object previewSessionId,
            OrderPreviewCodeInput payload) {
        return applyOrderPreviewGiftCard(previewSessionId, (Object) payload, null);
    }

    public ApiResponse applyOrderPreviewGiftCard(Object previewSessionId,
            OrderPreviewCodeInput payload, RequestOptions options) {
        return applyOrderPreviewGiftCard(previewSessionId, (Object) payload, options);
    }

    /**
     * Remove the gift card — {@code DELETE /admin/orders/preview/{preview_session_id}/gift-card},
     * {@code 204} with no body. With none applied it is {@code 404}
     * {@code gift_card_not_applied}.
     */
    public ApiResponse removeOrderPreviewGiftCard(Object previewSessionId) {
        return removeOrderPreviewGiftCard(previewSessionId, null);
    }

    public ApiResponse removeOrderPreviewGiftCard(Object previewSessionId,
            RequestOptions options) {
        return client.sendDeleteRequest(OrderPreviewsUrls.ORDER_PREVIEW_GIFT_CARD,
                RequestOptions.builder(options).pathParams(pathParams(previewSessionId))
                        .dtoFactory(null).build());
    }

    private static Map<String, Object> pathParams(Object previewSessionId) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("preview_session_id", previewSessionId);
        return params;
    }
}
