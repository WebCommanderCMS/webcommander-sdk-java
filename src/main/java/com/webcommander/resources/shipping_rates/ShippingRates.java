package com.webcommander.resources.shipping_rates;

import com.webcommander.Dto;
import com.webcommander.client.ApiResponse;
import com.webcommander.client.BoundResource;
import com.webcommander.client.RequestOptions;
import com.webcommander.client.ResourceBinding;
import com.webcommander.client.WebCommanderClient;
import com.webcommander.resources.shipping_rates.shipping_rates_dto.ShippingRatesDto.ShippingRate;
import com.webcommander.resources.shipping_rates.shipping_rates_dto.ShippingRatesDto.ShippingRateCount;
import com.webcommander.resources.shipping_rates.shipping_rates_dto.ShippingRatesDto.ShippingRateInput;
import com.webcommander.resources.shipping_rates.shipping_rates_dto.ShippingRatesDto.ShippingRateListing;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shipping rates — {@code /api/{api_version}/admin/shipping_rates}; the price a shipping rule
 * applies.
 */
public final class ShippingRates implements BoundResource {

    private final WebCommanderClient client;

    public ShippingRates(WebCommanderClient client) {
        this.client = client;
    }

    /**
     * List the rates, newest first — {@code GET /admin/shipping_rates}. {@code search} (part of the
     * name), {@code method} (such as {@code flat_rate} or {@code ship_by_weight}) and
     * {@code handling_cost} ({@code with_handling} or {@code without_handling}) filter;
     * {@code sort} is {@code id}, {@code name}, {@code method}, {@code created_at},
     * {@code updated_at} or {@code transaction_no} with {@code order} {@code asc} or
     * {@code desc}; {@code limit} is 1 to 100 and defaults to 20, {@code offset} pages. An unknown
     * value is {@code 400}.
     */
    public ApiResponse listShippingRates() {
        return listShippingRates(null, null);
    }

    public ApiResponse listShippingRates(Map<String, Object> queryParams) {
        return listShippingRates(queryParams, null);
    }

    public ApiResponse listShippingRates(Map<String, Object> queryParams,
            RequestOptions options) {
        return client.sendGetRequest(ShippingRatesUrls.SHIPPING_RATES,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(ShippingRateListing.class, body)).build());
    }

    /** Ask whether the collection is reachable — {@code HEAD /admin/shipping_rates}; no body. */
    public ApiResponse headShippingRates() {
        return headShippingRates(null);
    }

    public ApiResponse headShippingRates(RequestOptions options) {
        return client.sendApiRequest("HEAD", ShippingRatesUrls.SHIPPING_RATES,
                RequestOptions.builder(options).dtoFactory(null).build());
    }

    /**
     * How many rates match — {@code GET /admin/shipping_rates/count}, a bare {@code {"total": N}}.
     * The listing's filters narrow it.
     */
    public ApiResponse countShippingRates() {
        return countShippingRates(null, null);
    }

    public ApiResponse countShippingRates(Map<String, Object> queryParams) {
        return countShippingRates(queryParams, null);
    }

    public ApiResponse countShippingRates(Map<String, Object> queryParams,
            RequestOptions options) {
        return client.sendGetRequest(ShippingRatesUrls.SHIPPING_RATES_COUNT,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(ShippingRateCount.class, body)).build());
    }

    /** Ask whether the count is reachable — {@code HEAD /admin/shipping_rates/count}; no body. */
    public ApiResponse headShippingRatesCount() {
        return headShippingRatesCount(null);
    }

    public ApiResponse headShippingRatesCount(RequestOptions options) {
        return client.sendApiRequest("HEAD", ShippingRatesUrls.SHIPPING_RATES_COUNT,
                RequestOptions.builder(options).dtoFactory(null).build());
    }

    /**
     * Create a rate — {@code POST /admin/shipping_rates}, answered {@code 201} with a
     * {@code Location}. The body is wrapped in {@code shipping_rate} and needs {@code name},
     * unique without regard to case ({@code 409}), and {@code method}. A flat rate needs
     * {@code shipping_cost}; {@code ship_by_price}, {@code ship_by_quantity},
     * {@code ship_by_weight} and {@code ship_by_variation} take their costs per row in
     * {@code conditions} and refuse a rate-level {@code shipping_cost}. {@code free_shipping}
     * always saves {@code calculate_for_entire_order} false. {@code shipping_rule_id} attaches the
     * rate to a rule that has none ({@code 409} otherwise). An unknown field, an {@code id} or an
     * unwrapped body is {@code 400}. A non-null {@code transactionNo} is sent as
     * {@code X-Transaction-No}.
     */
    public ApiResponse createShippingRate(Object payload) {
        return createShippingRate(payload, null, null);
    }

    public ApiResponse createShippingRate(Object payload, RequestOptions options) {
        return createShippingRate(payload, null, options);
    }

    public ApiResponse createShippingRate(Object payload, String transactionNo) {
        return createShippingRate(payload, transactionNo, null);
    }

    public ApiResponse createShippingRate(Object payload, String transactionNo,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        RequestOptions given = options == null ? RequestOptions.none() : options;
        Map<String, String> headers = new LinkedHashMap<>();
        if (given.getHeaders() != null) {
            headers.putAll(given.getHeaders());
        }
        if (transactionNo != null) {
            headers.put(ShippingRatesUrls.TRANSACTION_NO_HEADER, transactionNo);
        }
        return client.sendPostRequest(ShippingRatesUrls.SHIPPING_RATES,
                RequestOptions.builder(options).jsonBody(payload).headers(headers)
                        .dtoFactory(body -> Dto.fromMap(ShippingRate.class, body)).build());
    }

    public ApiResponse createShippingRate(ShippingRateInput payload) {
        return createShippingRate((Object) payload, null, null);
    }

    public ApiResponse createShippingRate(ShippingRateInput payload, RequestOptions options) {
        return createShippingRate((Object) payload, null, options);
    }

    public ApiResponse createShippingRate(ShippingRateInput payload, String transactionNo) {
        return createShippingRate((Object) payload, transactionNo, null);
    }

    public ApiResponse createShippingRate(ShippingRateInput payload, String transactionNo,
            RequestOptions options) {
        return createShippingRate((Object) payload, transactionNo, options);
    }

    /**
     * Retrieve one rate by its {@code id} — {@code GET /admin/shipping_rates/{shipping_rate_id}},
     * the row under {@code shipping_rate}. An id that is not plain digits, or unknown, is
     * {@code 404}.
     */
    public ApiResponse getShippingRate(Object shippingRateId) {
        return getShippingRate(shippingRateId, null);
    }

    public ApiResponse getShippingRate(Object shippingRateId, RequestOptions options) {
        return client.sendGetRequest(ShippingRatesUrls.SHIPPING_RATE,
                RequestOptions.builder(options).pathParams(pathParams(shippingRateId))
                        .dtoFactory(body -> Dto.fromMap(ShippingRate.class, body)).build());
    }

    /**
     * Ask whether a rate exists — {@code HEAD /admin/shipping_rates/{shipping_rate_id}}; 200 with
     * no body, 404 for an unknown id.
     */
    public ApiResponse headShippingRate(Object shippingRateId) {
        return headShippingRate(shippingRateId, null);
    }

    public ApiResponse headShippingRate(Object shippingRateId, RequestOptions options) {
        return client.sendApiRequest("HEAD", ShippingRatesUrls.SHIPPING_RATE,
                RequestOptions.builder(options).pathParams(pathParams(shippingRateId))
                        .dtoFactory(null).build());
    }

    /**
     * Replace one rate — {@code PUT /admin/shipping_rates/{shipping_rate_id}}. Every field is
     * required: {@code name}, {@code method}, {@code calculate_for_entire_order},
     * {@code price_entered_with_tax}, the costs the method uses and {@code additional_rule}; a
     * {@code handling_cost} or {@code additional_rule} the rate does not use is sent as
     * {@code null}, which takes a {@code Map}. A field left out is {@code 400} naming it.
     */
    public ApiResponse updateShippingRate(Object shippingRateId, Object payload) {
        return updateShippingRate(shippingRateId, payload, null);
    }

    public ApiResponse updateShippingRate(Object shippingRateId, Object payload,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPutRequest(ShippingRatesUrls.SHIPPING_RATE,
                RequestOptions.builder(options).pathParams(pathParams(shippingRateId))
                        .jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(ShippingRate.class, body)).build());
    }

    public ApiResponse updateShippingRate(Object shippingRateId, ShippingRateInput payload) {
        return updateShippingRate(shippingRateId, (Object) payload, null);
    }

    public ApiResponse updateShippingRate(Object shippingRateId, ShippingRateInput payload,
            RequestOptions options) {
        return updateShippingRate(shippingRateId, (Object) payload, options);
    }

    /**
     * Change only the fields sent — {@code PATCH /admin/shipping_rates/{shipping_rate_id}}; the
     * costs, rows and boxes not sent are kept. An empty {@code shipping_rate} changes nothing and
     * is {@code 200}.
     */
    public ApiResponse patchShippingRate(Object shippingRateId, Object payload) {
        return patchShippingRate(shippingRateId, payload, null);
    }

    public ApiResponse patchShippingRate(Object shippingRateId, Object payload,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPatchRequest(ShippingRatesUrls.SHIPPING_RATE,
                RequestOptions.builder(options).pathParams(pathParams(shippingRateId))
                        .jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(ShippingRate.class, body)).build());
    }

    public ApiResponse patchShippingRate(Object shippingRateId, ShippingRateInput payload) {
        return patchShippingRate(shippingRateId, (Object) payload, null);
    }

    public ApiResponse patchShippingRate(Object shippingRateId, ShippingRateInput payload,
            RequestOptions options) {
        return patchShippingRate(shippingRateId, (Object) payload, options);
    }

    /**
     * Delete one rate — {@code DELETE /admin/shipping_rates/{shipping_rate_id}}, {@code 204}. A
     * rate a rule uses is {@code 409} until the rule lets it go.
     */
    public ApiResponse deleteShippingRate(Object shippingRateId) {
        return deleteShippingRate(shippingRateId, null);
    }

    public ApiResponse deleteShippingRate(Object shippingRateId, RequestOptions options) {
        return client.sendDeleteRequest(ShippingRatesUrls.SHIPPING_RATE,
                RequestOptions.builder(options).pathParams(pathParams(shippingRateId))
                        .dtoFactory(null).build());
    }

    private static Map<String, Object> pathParams(Object shippingRateId) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("shipping_rate_id", shippingRateId);
        return params;
    }
}
