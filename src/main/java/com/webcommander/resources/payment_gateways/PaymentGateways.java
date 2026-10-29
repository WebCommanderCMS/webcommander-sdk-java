package com.webcommander.resources.payment_gateways;

import com.webcommander.Dto;
import com.webcommander.client.ApiResponse;
import com.webcommander.client.BoundResource;
import com.webcommander.client.RequestOptions;
import com.webcommander.client.ResourceBinding;
import com.webcommander.client.WebCommanderClient;
import com.webcommander.resources.payment_gateways.payment_gateways_dto.PaymentGatewaysDto
        .PaymentGateway;
import com.webcommander.resources.payment_gateways.payment_gateways_dto.PaymentGatewaysDto
        .PaymentGatewayCount;
import com.webcommander.resources.payment_gateways.payment_gateways_dto.PaymentGatewaysDto
        .PaymentGatewayInput;
import com.webcommander.resources.payment_gateways.payment_gateways_dto.PaymentGatewaysDto
        .PaymentGatewayPage;

import java.util.LinkedHashMap;
import java.util.Map;

/** Payment gateways — {@code /api/{api_version}/admin/payment_gateways}; no create, no delete. */
public final class PaymentGateways implements BoundResource {

    private final WebCommanderClient client;

    public PaymentGateways(WebCommanderClient client) {
        this.client = client;
    }

    /**
     * List the gateways by ascending id, rows without {@code settings} —
     * {@code GET /admin/payment_gateways}. {@code limit} is 1 to 100, default 20; {@code 0} or
     * above 100 is {@code 400} {@code out_of_range}. {@code search}, {@code exclude},
     * {@code promotional} and {@code added} filter.
     */
    public ApiResponse listPaymentGateways() {
        return listPaymentGateways(null, null);
    }

    public ApiResponse listPaymentGateways(Map<String, Object> queryParams) {
        return listPaymentGateways(queryParams, null);
    }

    public ApiResponse listPaymentGateways(Map<String, Object> queryParams,
            RequestOptions options) {
        return client.sendGetRequest(PaymentGatewaysUrls.PAYMENT_GATEWAYS,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(PaymentGatewayPage.class, body)).build());
    }

    /** Ask whether the collection is reachable — {@code HEAD /admin/payment_gateways}. */
    public ApiResponse headPaymentGateways() {
        return headPaymentGateways(null);
    }

    public ApiResponse headPaymentGateways(RequestOptions options) {
        return client.sendApiRequest("HEAD", PaymentGatewaysUrls.PAYMENT_GATEWAYS,
                RequestOptions.builder(options).dtoFactory(null).build());
    }

    /**
     * How many gateways match the listing's filters — {@code GET .../payment_gateways/count}.
     * {@code limit} is checked as on the listing, {@code 0} or above 100 {@code 400}
     * {@code out_of_range}, but changes nothing.
     */
    public ApiResponse countPaymentGateways() {
        return countPaymentGateways(null, null);
    }

    public ApiResponse countPaymentGateways(Map<String, Object> queryParams) {
        return countPaymentGateways(queryParams, null);
    }

    public ApiResponse countPaymentGateways(Map<String, Object> queryParams,
            RequestOptions options) {
        return client.sendGetRequest(PaymentGatewaysUrls.PAYMENT_GATEWAYS_COUNT,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(PaymentGatewayCount.class, body)).build());
    }

    /** Ask whether the count is reachable — {@code HEAD /admin/payment_gateways/count}. */
    public ApiResponse headPaymentGatewaysCount() {
        return headPaymentGatewaysCount(null);
    }

    public ApiResponse headPaymentGatewaysCount(RequestOptions options) {
        return client.sendApiRequest("HEAD", PaymentGatewaysUrls.PAYMENT_GATEWAYS_COUNT,
                RequestOptions.builder(options).dtoFactory(null).build());
    }

    /**
     * Retrieve one gateway with its {@code settings}, secrets reading {@code ********} —
     * {@code GET /admin/payment_gateways/{payment_gateway_id}}. Every query name is ignored.
     */
    public ApiResponse getPaymentGateway(Object paymentGatewayId) {
        return getPaymentGateway(paymentGatewayId, null);
    }

    public ApiResponse getPaymentGateway(Object paymentGatewayId, RequestOptions options) {
        return client.sendGetRequest(PaymentGatewaysUrls.PAYMENT_GATEWAY,
                RequestOptions.builder(options).pathParams(pathParams(paymentGatewayId))
                        .dtoFactory(body -> Dto.fromMap(PaymentGateway.class, body)).build());
    }

    /** Ask whether one gateway is there — {@code HEAD .../{payment_gateway_id}}; no body. */
    public ApiResponse headPaymentGateway(Object paymentGatewayId) {
        return headPaymentGateway(paymentGatewayId, null);
    }

    public ApiResponse headPaymentGateway(Object paymentGatewayId, RequestOptions options) {
        return client.sendApiRequest("HEAD", PaymentGatewaysUrls.PAYMENT_GATEWAY,
                RequestOptions.builder(options).pathParams(pathParams(paymentGatewayId))
                        .dtoFactory(null).build());
    }

    /**
     * Write one gateway — {@code PUT /admin/payment_gateways/{payment_gateway_id}}. A bare
     * record merged like the {@code PATCH}: the fields left out keep their value.
     */
    public ApiResponse updatePaymentGateway(Object paymentGatewayId, Object payload) {
        return updatePaymentGateway(paymentGatewayId, payload, null);
    }

    public ApiResponse updatePaymentGateway(Object paymentGatewayId, Object payload,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPutRequest(PaymentGatewaysUrls.PAYMENT_GATEWAY,
                RequestOptions.builder(options).pathParams(pathParams(paymentGatewayId))
                        .jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(PaymentGateway.class, body)).build());
    }

    public ApiResponse updatePaymentGateway(Object paymentGatewayId,
            PaymentGatewayInput payload) {
        return updatePaymentGateway(paymentGatewayId, (Object) payload, null);
    }

    public ApiResponse updatePaymentGateway(Object paymentGatewayId,
            PaymentGatewayInput payload, RequestOptions options) {
        return updatePaymentGateway(paymentGatewayId, (Object) payload, options);
    }

    /**
     * Change only the fields sent — {@code PATCH /admin/payment_gateways/{payment_gateway_id}}.
     * A bare record; an empty body is {@code 400}.
     */
    public ApiResponse patchPaymentGateway(Object paymentGatewayId, Object payload) {
        return patchPaymentGateway(paymentGatewayId, payload, null);
    }

    public ApiResponse patchPaymentGateway(Object paymentGatewayId, Object payload,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPatchRequest(PaymentGatewaysUrls.PAYMENT_GATEWAY,
                RequestOptions.builder(options).pathParams(pathParams(paymentGatewayId))
                        .jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(PaymentGateway.class, body)).build());
    }

    public ApiResponse patchPaymentGateway(Object paymentGatewayId,
            PaymentGatewayInput payload) {
        return patchPaymentGateway(paymentGatewayId, (Object) payload, null);
    }

    public ApiResponse patchPaymentGateway(Object paymentGatewayId,
            PaymentGatewayInput payload, RequestOptions options) {
        return patchPaymentGateway(paymentGatewayId, (Object) payload, options);
    }

    private static Map<String, Object> pathParams(Object paymentGatewayId) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("payment_gateway_id", paymentGatewayId);
        return params;
    }
}
