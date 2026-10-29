package com.webcommander.resources.orders;

import com.webcommander.Dto;
import com.webcommander.client.ApiResponse;
import com.webcommander.client.BoundResource;
import com.webcommander.client.RequestOptions;
import com.webcommander.client.ResourceBinding;
import com.webcommander.client.WebCommanderClient;
import com.webcommander.resources.orders.orders_dto.OrdersDto.CustomOrderStatusInput;
import com.webcommander.resources.orders.orders_dto.OrdersDto.CustomerOrders;
import com.webcommander.resources.orders.orders_dto.OrdersDto.FlatOrder;
import com.webcommander.resources.orders.orders_dto.OrdersDto.InvoiceDispatch;
import com.webcommander.resources.orders.orders_dto.OrdersDto.Order;
import com.webcommander.resources.orders.orders_dto.OrdersDto.OrderAddresses;
import com.webcommander.resources.orders.orders_dto.OrdersDto.OrderAddressesInput;
import com.webcommander.resources.orders.orders_dto.OrdersDto.OrderCancelInput;
import com.webcommander.resources.orders.orders_dto.OrdersDto.OrderComment;
import com.webcommander.resources.orders.orders_dto.OrdersDto.OrderCommentInput;
import com.webcommander.resources.orders.orders_dto.OrdersDto.OrderCommentList;
import com.webcommander.resources.orders.orders_dto.OrdersDto.OrderCount;
import com.webcommander.resources.orders.orders_dto.OrdersDto.OrderCustomFields;
import com.webcommander.resources.orders.orders_dto.OrdersDto.OrderInput;
import com.webcommander.resources.orders.orders_dto.OrdersDto.OrderPage;
import com.webcommander.resources.orders.orders_dto.OrdersDto.OrderPatchInput;
import com.webcommander.resources.orders.orders_dto.OrdersDto.OrderPaymentInput;
import com.webcommander.resources.orders.orders_dto.OrdersDto.OrderRefund;
import com.webcommander.resources.orders.orders_dto.OrdersDto.OrderRefundInput;
import com.webcommander.resources.orders.orders_dto.OrdersDto.OrderRefundPage;
import com.webcommander.resources.orders.orders_dto.OrdersDto.OrderShipment;
import com.webcommander.resources.orders.orders_dto.OrdersDto.OrderShipmentInput;
import com.webcommander.resources.orders.orders_dto.OrdersDto.OrderShipmentList;
import com.webcommander.resources.orders.orders_dto.OrdersDto.OrderStatusInput;
import com.webcommander.resources.orders.orders_dto.OrdersDto.OrderUpdateInput;
import com.webcommander.resources.orders.orders_dto.OrdersDto.OrderWithPaymentInput;
import com.webcommander.resources.orders.orders_dto.OrdersDto.PaymentReceipt;
import com.webcommander.resources.orders.orders_dto.OrdersDto.PaymentRefund;
import com.webcommander.resources.orders.orders_dto.OrdersDto.PaymentRefundInput;
import com.webcommander.resources.orders.orders_dto.OrdersDto.ShipmentStatus;

import java.util.LinkedHashMap;
import java.util.Map;

/** Orders — {@code /api/{api_version}/admin/orders}, with three paths outside it. */
public final class Orders implements BoundResource {

    private final WebCommanderClient client;

    public Orders(WebCommanderClient client) {
        this.client = client;
    }

    /** List orders — {@code GET /admin/orders}, newest first and cancelled ones left out. */
    public ApiResponse listOrders() {
        return listOrders(null, null);
    }

    public ApiResponse listOrders(Map<String, Object> queryParams) {
        return listOrders(queryParams, null);
    }

    public ApiResponse listOrders(Map<String, Object> queryParams, RequestOptions options) {
        return client.sendGetRequest(OrdersUrls.ORDERS,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(OrderPage.class, body)).build());
    }

    /** Count orders — {@code GET /admin/orders/count}; the listing's filters narrow it. */
    public ApiResponse countOrders() {
        return countOrders(null, null);
    }

    public ApiResponse countOrders(Map<String, Object> queryParams) {
        return countOrders(queryParams, null);
    }

    public ApiResponse countOrders(Map<String, Object> queryParams, RequestOptions options) {
        return client.sendGetRequest(OrdersUrls.ORDERS_COUNT,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(OrderCount.class, body)).build());
    }

    /** Create an order — {@code POST /admin/orders}; only {@code products} is required. */
    public ApiResponse createOrder(Object payload) {
        return createOrder(payload, null, null);
    }

    public ApiResponse createOrder(Object payload, RequestOptions options) {
        return createOrder(payload, null, options);
    }

    public ApiResponse createOrder(Object payload, String transactionNo) {
        return createOrder(payload, transactionNo, null);
    }

    public ApiResponse createOrder(Object payload, String transactionNo,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        RequestOptions given = options == null ? RequestOptions.none() : options;
        Map<String, String> headers = new LinkedHashMap<>();
        if (given.getHeaders() != null) {
            headers.putAll(given.getHeaders());
        }
        if (transactionNo != null) {
            headers.put(OrdersUrls.TRANSACTION_NO_HEADER, transactionNo);
        }
        return client.sendPostRequest(OrdersUrls.ORDERS,
                RequestOptions.builder(options).jsonBody(payload).headers(headers)
                        .dtoFactory(body -> Dto.fromMap(FlatOrder.class, body)).build());
    }

    public ApiResponse createOrder(OrderInput payload) {
        return createOrder((Object) payload, null, null);
    }

    public ApiResponse createOrder(OrderInput payload, RequestOptions options) {
        return createOrder((Object) payload, null, options);
    }

    public ApiResponse createOrder(OrderInput payload, String transactionNo) {
        return createOrder((Object) payload, transactionNo, null);
    }

    public ApiResponse createOrder(OrderInput payload, String transactionNo,
            RequestOptions options) {
        return createOrder((Object) payload, transactionNo, options);
    }

    /** Create an order and pay on it — {@code POST /admin/orders/with-payment}; {@code 201}. */
    public ApiResponse createOrderWithPayment(Object payload) {
        return createOrderWithPayment(payload, null);
    }

    public ApiResponse createOrderWithPayment(Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPostRequest(OrdersUrls.ORDERS_WITH_PAYMENT,
                RequestOptions.builder(options).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(FlatOrder.class, body)).build());
    }

    public ApiResponse createOrderWithPayment(OrderWithPaymentInput payload) {
        return createOrderWithPayment((Object) payload, null);
    }

    public ApiResponse createOrderWithPayment(OrderWithPaymentInput payload,
            RequestOptions options) {
        return createOrderWithPayment((Object) payload, options);
    }

    /** Retrieve one order, in sections — {@code GET /admin/orders/{order_id}}. */
    public ApiResponse getOrder(Object orderId) {
        return getOrder(orderId, null, null);
    }

    public ApiResponse getOrder(Object orderId, Map<String, Object> queryParams) {
        return getOrder(orderId, queryParams, null);
    }

    public ApiResponse getOrder(Object orderId, Map<String, Object> queryParams,
            RequestOptions options) {
        return client.sendGetRequest(OrdersUrls.ORDER,
                RequestOptions.builder(options).pathParams(pathParams("order_id", orderId))
                        .queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(Order.class, body)).build());
    }

    /** Update an order — {@code PUT /admin/orders/{order_id}}; sections left out are kept. */
    public ApiResponse updateOrder(Object orderId, Object payload) {
        return updateOrder(orderId, payload, null);
    }

    public ApiResponse updateOrder(Object orderId, Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPutRequest(OrdersUrls.ORDER,
                RequestOptions.builder(options).pathParams(pathParams("order_id", orderId))
                        .jsonBody(payload).dtoFactory(body -> Dto.fromMap(Order.class, body))
                        .build());
    }

    public ApiResponse updateOrder(Object orderId, OrderUpdateInput payload) {
        return updateOrder(orderId, (Object) payload, null);
    }

    public ApiResponse updateOrder(Object orderId, OrderUpdateInput payload,
            RequestOptions options) {
        return updateOrder(orderId, (Object) payload, options);
    }

    /** Change an order's named fields — {@code PATCH /admin/orders/{order_id}}. */
    public ApiResponse patchOrder(Object orderId, Object payload) {
        return patchOrder(orderId, payload, null);
    }

    public ApiResponse patchOrder(Object orderId, Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPatchRequest(OrdersUrls.ORDER,
                RequestOptions.builder(options).pathParams(pathParams("order_id", orderId))
                        .jsonBody(payload).dtoFactory(body -> Dto.fromMap(Order.class, body))
                        .build());
    }

    public ApiResponse patchOrder(Object orderId, OrderPatchInput payload) {
        return patchOrder(orderId, (Object) payload, null);
    }

    public ApiResponse patchOrder(Object orderId, OrderPatchInput payload,
            RequestOptions options) {
        return patchOrder(orderId, (Object) payload, options);
    }

    /** Delete an order — {@code DELETE /admin/orders/{order_id}}; check {@code status}. */
    public ApiResponse deleteOrder(Object orderId) {
        return deleteOrder(orderId, null);
    }

    public ApiResponse deleteOrder(Object orderId, RequestOptions options) {
        return client.sendDeleteRequest(OrdersUrls.ORDER,
                RequestOptions.builder(options).pathParams(pathParams("order_id", orderId))
                        .dtoFactory(null).build());
    }

    /** Replace one or both addresses — {@code PATCH .../{order_id}/addresses}. */
    public ApiResponse updateOrderAddresses(Object orderId, Object payload) {
        return updateOrderAddresses(orderId, payload, null);
    }

    public ApiResponse updateOrderAddresses(Object orderId, Object payload,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPatchRequest(OrdersUrls.ORDER_ADDRESSES,
                RequestOptions.builder(options).pathParams(pathParams("order_id", orderId))
                        .jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(OrderAddresses.class, body)).build());
    }

    public ApiResponse updateOrderAddresses(Object orderId, OrderAddressesInput payload) {
        return updateOrderAddresses(orderId, (Object) payload, null);
    }

    public ApiResponse updateOrderAddresses(Object orderId, OrderAddressesInput payload,
            RequestOptions options) {
        return updateOrderAddresses(orderId, (Object) payload, options);
    }

    /** Cancel an order — {@code POST .../{order_id}/cancel}; the body is optional. */
    public ApiResponse cancelOrder(Object orderId) {
        return cancelOrder(orderId, (Object) null, null);
    }

    public ApiResponse cancelOrder(Object orderId, Object payload) {
        return cancelOrder(orderId, payload, null);
    }

    public ApiResponse cancelOrder(Object orderId, Object payload, RequestOptions options) {
        return client.sendPostRequest(OrdersUrls.ORDER_CANCEL,
                RequestOptions.builder(options).pathParams(pathParams("order_id", orderId))
                        .jsonBody(payload).dtoFactory(body -> Dto.fromMap(FlatOrder.class, body))
                        .build());
    }

    public ApiResponse cancelOrder(Object orderId, OrderCancelInput payload) {
        return cancelOrder(orderId, (Object) payload, null);
    }

    public ApiResponse cancelOrder(Object orderId, OrderCancelInput payload,
            RequestOptions options) {
        return cancelOrder(orderId, (Object) payload, options);
    }

    /** An order's comments, oldest first and unpaged — {@code GET .../{order_id}/comments}. */
    public ApiResponse listOrderComments(Object orderId) {
        return listOrderComments(orderId, null);
    }

    public ApiResponse listOrderComments(Object orderId, RequestOptions options) {
        return client.sendGetRequest(OrdersUrls.ORDER_COMMENTS,
                RequestOptions.builder(options).pathParams(pathParams("order_id", orderId))
                        .dtoFactory(body -> Dto.fromMap(OrderCommentList.class, body)).build());
    }

    /** Add a comment — {@code POST /admin/orders/{order_id}/comments}, a bare record. */
    public ApiResponse createOrderComment(Object orderId, Object payload) {
        return createOrderComment(orderId, payload, null);
    }

    public ApiResponse createOrderComment(Object orderId, Object payload,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPostRequest(OrdersUrls.ORDER_COMMENTS,
                RequestOptions.builder(options).pathParams(pathParams("order_id", orderId))
                        .jsonBody(payload).dtoFactory(body -> Dto.fromMap(OrderComment.class, body))
                        .build());
    }

    public ApiResponse createOrderComment(Object orderId, OrderCommentInput payload) {
        return createOrderComment(orderId, (Object) payload, null);
    }

    public ApiResponse createOrderComment(Object orderId, OrderCommentInput payload,
            RequestOptions options) {
        return createOrderComment(orderId, (Object) payload, options);
    }

    /** An order's custom fields, by its {@code internal_id} — {@code GET .../custom_fields}. */
    public ApiResponse getOrderCustomFields(Object orderInternalId) {
        return getOrderCustomFields(orderInternalId, null);
    }

    public ApiResponse getOrderCustomFields(Object orderInternalId, RequestOptions options) {
        return client.sendGetRequest(OrdersUrls.ORDER_CUSTOM_FIELDS,
                RequestOptions.builder(options)
                        .pathParams(pathParams("order_internal_id", orderInternalId))
                        .dtoFactory(body -> Dto.fromMap(OrderCustomFields.class, body)).build());
    }

    /** Record a payment — {@code POST /admin/orders/{order_id}/payments}; {@code 201}. */
    public ApiResponse createOrderPayment(Object orderId, Object payload) {
        return createOrderPayment(orderId, payload, null);
    }

    public ApiResponse createOrderPayment(Object orderId, Object payload,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPostRequest(OrdersUrls.ORDER_PAYMENTS,
                RequestOptions.builder(options).pathParams(pathParams("order_id", orderId))
                        .jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(PaymentReceipt.class, body)).build());
    }

    public ApiResponse createOrderPayment(Object orderId, OrderPaymentInput payload) {
        return createOrderPayment(orderId, (Object) payload, null);
    }

    public ApiResponse createOrderPayment(Object orderId, OrderPaymentInput payload,
            RequestOptions options) {
        return createOrderPayment(orderId, (Object) payload, options);
    }

    /**
     * List an order's refunds, newest first — {@code GET /admin/orders/{order_id}/refunds}, by the
     * order's {@code order_id}. {@code limit} is 1 to 20 (above 20 is capped, {@code 0} is
     * {@code 400}), with {@code offset} or a 1-based {@code page}; any other name is ignored.
     */
    public ApiResponse listOrderRefunds(Object orderId) {
        return listOrderRefunds(orderId, null, null);
    }

    public ApiResponse listOrderRefunds(Object orderId, Map<String, Object> queryParams) {
        return listOrderRefunds(orderId, queryParams, null);
    }

    public ApiResponse listOrderRefunds(Object orderId, Map<String, Object> queryParams,
            RequestOptions options) {
        return client.sendGetRequest(OrdersUrls.ORDER_REFUNDS,
                RequestOptions.builder(options).pathParams(pathParams("order_id", orderId))
                        .queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(OrderRefundPage.class, body)).build());
    }

    /** Refund against an order — {@code POST /admin/orders/{order_id}/refunds}; {@code 201}. */
    public ApiResponse createOrderRefund(Object orderId, Object payload) {
        return createOrderRefund(orderId, payload, null);
    }

    public ApiResponse createOrderRefund(Object orderId, Object payload,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPostRequest(OrdersUrls.ORDER_REFUNDS,
                RequestOptions.builder(options).pathParams(pathParams("order_id", orderId))
                        .jsonBody(payload).dtoFactory(body -> Dto.fromMap(OrderRefund.class, body))
                        .build());
    }

    public ApiResponse createOrderRefund(Object orderId, OrderRefundInput payload) {
        return createOrderRefund(orderId, (Object) payload, null);
    }

    public ApiResponse createOrderRefund(Object orderId, OrderRefundInput payload,
            RequestOptions options) {
        return createOrderRefund(orderId, (Object) payload, options);
    }

    /** Refund part of a payment — {@code POST /admin/payment/{payment_id}/refunds}. */
    public ApiResponse createPaymentRefund(Object paymentId, Object payload,
            String idempotencyKey) {
        return createPaymentRefund(paymentId, payload, idempotencyKey, null);
    }

    public ApiResponse createPaymentRefund(Object paymentId, Object payload,
            String idempotencyKey, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        RequestOptions given = options == null ? RequestOptions.none() : options;
        Map<String, String> headers = new LinkedHashMap<>();
        if (given.getHeaders() != null) {
            headers.putAll(given.getHeaders());
        }
        headers.put(OrdersUrls.IDEMPOTENCY_KEY_HEADER, idempotencyKey);
        return client.sendPostRequest(OrdersUrls.PAYMENT_REFUNDS,
                RequestOptions.builder(options).pathParams(pathParams("payment_id", paymentId))
                        .jsonBody(payload).headers(headers)
                        .dtoFactory(body -> Dto.fromMap(PaymentRefund.class, body)).build());
    }

    public ApiResponse createPaymentRefund(Object paymentId, PaymentRefundInput payload,
            String idempotencyKey) {
        return createPaymentRefund(paymentId, (Object) payload, idempotencyKey, null);
    }

    public ApiResponse createPaymentRefund(Object paymentId, PaymentRefundInput payload,
            String idempotencyKey, RequestOptions options) {
        return createPaymentRefund(paymentId, (Object) payload, idempotencyKey, options);
    }

    /** Email the invoice, by the order's {@code internal_id} — {@code POST .../send-invoice}. */
    public ApiResponse sendOrderInvoice(Object orderInternalId) {
        return sendOrderInvoice(orderInternalId, null);
    }

    public ApiResponse sendOrderInvoice(Object orderInternalId, RequestOptions options) {
        return client.sendPostRequest(OrdersUrls.ORDER_SEND_INVOICE,
                RequestOptions.builder(options)
                        .pathParams(pathParams("order_internal_id", orderInternalId))
                        .dtoFactory(body -> Dto.fromMap(InvoiceDispatch.class, body)).build());
    }

    /** How far an order has shipped — {@code GET /admin/orders/{order_id}/shipment-status}. */
    public ApiResponse getOrderShipmentStatus(Object orderId) {
        return getOrderShipmentStatus(orderId, null);
    }

    public ApiResponse getOrderShipmentStatus(Object orderId, RequestOptions options) {
        return client.sendGetRequest(OrdersUrls.ORDER_SHIPMENT_STATUS,
                RequestOptions.builder(options).pathParams(pathParams("order_id", orderId))
                        .dtoFactory(body -> Dto.fromMap(ShipmentStatus.class, body)).build());
    }

    /** An order's shipments, unpaged — {@code GET /admin/orders/{order_id}/shipments}. */
    public ApiResponse listOrderShipments(Object orderId) {
        return listOrderShipments(orderId, null);
    }

    public ApiResponse listOrderShipments(Object orderId, RequestOptions options) {
        return client.sendGetRequest(OrdersUrls.ORDER_SHIPMENTS,
                RequestOptions.builder(options).pathParams(pathParams("order_id", orderId))
                        .dtoFactory(body -> Dto.fromMap(OrderShipmentList.class, body))
                        .build());
    }

    /** Ship order lines — {@code POST /admin/orders/{order_id}/shipments}; {@code 201}. */
    public ApiResponse createOrderShipment(Object orderId, Object payload) {
        return createOrderShipment(orderId, payload, null);
    }

    public ApiResponse createOrderShipment(Object orderId, Object payload,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPostRequest(OrdersUrls.ORDER_SHIPMENTS,
                RequestOptions.builder(options).pathParams(pathParams("order_id", orderId))
                        .jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(OrderShipment.class, body)).build());
    }

    public ApiResponse createOrderShipment(Object orderId, OrderShipmentInput payload) {
        return createOrderShipment(orderId, (Object) payload, null);
    }

    public ApiResponse createOrderShipment(Object orderId, OrderShipmentInput payload,
            RequestOptions options) {
        return createOrderShipment(orderId, (Object) payload, options);
    }

    /** Change a shipment — {@code PATCH .../{order_id}/shipments/{shipment_id}}. */
    public ApiResponse updateOrderShipment(Object orderId, Object shipmentId, Object payload) {
        return updateOrderShipment(orderId, shipmentId, payload, null);
    }

    public ApiResponse updateOrderShipment(Object orderId, Object shipmentId, Object payload,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        Map<String, Object> params = pathParams("order_id", orderId);
        params.put("shipment_id", shipmentId);
        return client.sendPatchRequest(OrdersUrls.ORDER_SHIPMENT,
                RequestOptions.builder(options).pathParams(params).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(OrderShipment.class, body)).build());
    }

    public ApiResponse updateOrderShipment(Object orderId, Object shipmentId,
            OrderShipmentInput payload) {
        return updateOrderShipment(orderId, shipmentId, (Object) payload, null);
    }

    public ApiResponse updateOrderShipment(Object orderId, Object shipmentId,
            OrderShipmentInput payload, RequestOptions options) {
        return updateOrderShipment(orderId, shipmentId, (Object) payload, options);
    }

    /** Move an order's status — {@code PATCH /admin/orders/{order_id}/update-status}. */
    public ApiResponse updateOrderStatus(Object orderId, Object payload) {
        return updateOrderStatus(orderId, payload, null);
    }

    public ApiResponse updateOrderStatus(Object orderId, Object payload,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPatchRequest(OrdersUrls.ORDER_UPDATE_STATUS,
                RequestOptions.builder(options).pathParams(pathParams("order_id", orderId))
                        .jsonBody(payload).dtoFactory(body -> Dto.fromMap(FlatOrder.class, body))
                        .build());
    }

    public ApiResponse updateOrderStatus(Object orderId, OrderStatusInput payload) {
        return updateOrderStatus(orderId, (Object) payload, null);
    }

    public ApiResponse updateOrderStatus(Object orderId, OrderStatusInput payload,
            RequestOptions options) {
        return updateOrderStatus(orderId, (Object) payload, options);
    }

    /** Add a custom order status — {@code POST /admin/order/add-order-status}. */
    public ApiResponse createOrderStatus(Object payload) {
        return createOrderStatus(payload, null);
    }

    public ApiResponse createOrderStatus(Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPostRequest(OrdersUrls.ORDER_STATUS_ADD,
                RequestOptions.builder(options).jsonBody(payload).dtoFactory(null).build());
    }

    public ApiResponse createOrderStatus(CustomOrderStatusInput payload) {
        return createOrderStatus((Object) payload, null);
    }

    public ApiResponse createOrderStatus(CustomOrderStatusInput payload,
            RequestOptions options) {
        return createOrderStatus((Object) payload, options);
    }

    /** One customer's orders, unpaged — {@code GET /admin/customers/{customer_id}/orders}. */
    public ApiResponse listCustomerOrders(Object customerId) {
        return listCustomerOrders(customerId, null);
    }

    public ApiResponse listCustomerOrders(Object customerId, RequestOptions options) {
        return client.sendGetRequest(OrdersUrls.CUSTOMER_ORDERS,
                RequestOptions.builder(options).pathParams(pathParams("customer_id", customerId))
                        .dtoFactory(body -> Dto.fromMap(CustomerOrders.class, body)).build());
    }

    private static Map<String, Object> pathParams(String name, Object value) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put(name, value);
        return params;
    }
}
