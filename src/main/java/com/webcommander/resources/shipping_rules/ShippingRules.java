package com.webcommander.resources.shipping_rules;

import com.webcommander.Dto;
import com.webcommander.client.ApiResponse;
import com.webcommander.client.BoundResource;
import com.webcommander.client.RequestOptions;
import com.webcommander.client.ResourceBinding;
import com.webcommander.client.WebCommanderClient;
import com.webcommander.resources.shipping_rules.shipping_rules_dto.ShippingRulesDto.ShippingRule;
import com.webcommander.resources.shipping_rules.shipping_rules_dto.ShippingRulesDto.ShippingRuleCount;
import com.webcommander.resources.shipping_rules.shipping_rules_dto.ShippingRulesDto.ShippingRuleInput;
import com.webcommander.resources.shipping_rules.shipping_rules_dto.ShippingRulesDto.ShippingRuleListing;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shipping rules — {@code /api/{api_version}/admin/shipping_rules}; each ties a class and
 * zones to a rate.
 */
public final class ShippingRules implements BoundResource {

    private final WebCommanderClient client;

    public ShippingRules(WebCommanderClient client) {
        this.client = client;
    }

    /**
     * List the rules, newest first — {@code GET /admin/shipping_rules}. {@code search} (part
     * of the name), {@code shipping_class_id}, {@code zone_id}, {@code method} (the method
     * of the rule's rate) and {@code handling_cost} ({@code with_handling} or
     * {@code without_handling}) filter, each rule listed once; {@code sort} is {@code id},
     * {@code name}, {@code description}, {@code created_at}, {@code updated_at} or
     * {@code transaction_no} with {@code order} {@code asc} or {@code desc}; {@code limit}
     * is 1 to 100 and defaults to 20, {@code offset} pages. An unknown value or an id that
     * names no record is {@code 400}.
     */
    public ApiResponse listShippingRules() {
        return listShippingRules(null, null);
    }

    public ApiResponse listShippingRules(Map<String, Object> queryParams) {
        return listShippingRules(queryParams, null);
    }

    public ApiResponse listShippingRules(Map<String, Object> queryParams,
            RequestOptions options) {
        return client.sendGetRequest(ShippingRulesUrls.SHIPPING_RULES,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(ShippingRuleListing.class, body)).build());
    }

    /** Ask whether the collection is reachable — {@code HEAD /admin/shipping_rules}; no body. */
    public ApiResponse headShippingRules() {
        return headShippingRules(null);
    }

    public ApiResponse headShippingRules(RequestOptions options) {
        return client.sendApiRequest("HEAD", ShippingRulesUrls.SHIPPING_RULES,
                RequestOptions.builder(options).dtoFactory(null).build());
    }

    /**
     * How many rules match — {@code GET /admin/shipping_rules/count}, a bare {@code {"total":
     * N}}. The listing's filters narrow it.
     */
    public ApiResponse countShippingRules() {
        return countShippingRules(null, null);
    }

    public ApiResponse countShippingRules(Map<String, Object> queryParams) {
        return countShippingRules(queryParams, null);
    }

    public ApiResponse countShippingRules(Map<String, Object> queryParams,
            RequestOptions options) {
        return client.sendGetRequest(ShippingRulesUrls.SHIPPING_RULES_COUNT,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(ShippingRuleCount.class, body)).build());
    }

    /** Ask whether the count is reachable — {@code HEAD /admin/shipping_rules/count}; no body. */
    public ApiResponse headShippingRulesCount() {
        return headShippingRulesCount(null);
    }

    public ApiResponse headShippingRulesCount(RequestOptions options) {
        return client.sendApiRequest("HEAD", ShippingRulesUrls.SHIPPING_RULES_COUNT,
                RequestOptions.builder(options).dtoFactory(null).build());
    }

    /**
     * Create a rule — {@code POST /admin/shipping_rules}, answered {@code 201} with a
     * {@code Location}. The body is wrapped in {@code shipping_rule} and needs {@code name},
     * unique without regard to case ({@code 409}). A class, a rate and every zone must exist,
     * {@code zones} without a repeat; a {@code profile_id} adds the rule to that profile. An
     * unknown field, an {@code id} or an unwrapped body is {@code 400}. A non-null
     * {@code transactionNo} is sent as {@code X-Transaction-No}.
     */
    public ApiResponse createShippingRule(Object payload) {
        return createShippingRule(payload, null, null);
    }

    public ApiResponse createShippingRule(Object payload, RequestOptions options) {
        return createShippingRule(payload, null, options);
    }

    public ApiResponse createShippingRule(Object payload, String transactionNo) {
        return createShippingRule(payload, transactionNo, null);
    }

    public ApiResponse createShippingRule(Object payload, String transactionNo,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        RequestOptions given = options == null ? RequestOptions.none() : options;
        Map<String, String> headers = new LinkedHashMap<>();
        if (given.getHeaders() != null) {
            headers.putAll(given.getHeaders());
        }
        if (transactionNo != null) {
            headers.put(ShippingRulesUrls.TRANSACTION_NO_HEADER, transactionNo);
        }
        return client.sendPostRequest(ShippingRulesUrls.SHIPPING_RULES,
                RequestOptions.builder(options).jsonBody(payload).headers(headers)
                        .dtoFactory(body -> Dto.fromMap(ShippingRule.class, body)).build());
    }

    public ApiResponse createShippingRule(ShippingRuleInput payload) {
        return createShippingRule((Object) payload, null, null);
    }

    public ApiResponse createShippingRule(ShippingRuleInput payload, RequestOptions options) {
        return createShippingRule((Object) payload, null, options);
    }

    public ApiResponse createShippingRule(ShippingRuleInput payload, String transactionNo) {
        return createShippingRule((Object) payload, transactionNo, null);
    }

    public ApiResponse createShippingRule(ShippingRuleInput payload, String transactionNo,
            RequestOptions options) {
        return createShippingRule((Object) payload, transactionNo, options);
    }

    /**
     * Retrieve one rule by its {@code id} — {@code GET
     * /admin/shipping_rules/{shipping_rule_id}}, the row under {@code shipping_rule}. An id
     * that is not plain digits, or unknown, is {@code 404}.
     */
    public ApiResponse getShippingRule(Object shippingRuleId) {
        return getShippingRule(shippingRuleId, null);
    }

    public ApiResponse getShippingRule(Object shippingRuleId, RequestOptions options) {
        return client.sendGetRequest(ShippingRulesUrls.SHIPPING_RULE,
                RequestOptions.builder(options).pathParams(pathParams(shippingRuleId))
                        .dtoFactory(body -> Dto.fromMap(ShippingRule.class, body)).build());
    }

    /**
     * Ask whether a rule exists — {@code HEAD /admin/shipping_rules/{shipping_rule_id}}; 200
     * with no body, 404 for an unknown id.
     */
    public ApiResponse headShippingRule(Object shippingRuleId) {
        return headShippingRule(shippingRuleId, null);
    }

    public ApiResponse headShippingRule(Object shippingRuleId, RequestOptions options) {
        return client.sendApiRequest("HEAD", ShippingRulesUrls.SHIPPING_RULE,
                RequestOptions.builder(options).pathParams(pathParams(shippingRuleId))
                        .dtoFactory(null).build());
    }

    /**
     * Replace one rule — {@code PUT /admin/shipping_rules/{shipping_rule_id}}. {@code name},
     * {@code description}, {@code shipping_class}, {@code shipping_rate} and {@code zones}
     * are all required; the first three may be {@code null} and {@code zones} {@code []},
     * which takes a {@code Map}. The rate and the zones themselves stay.
     */
    public ApiResponse updateShippingRule(Object shippingRuleId, Object payload) {
        return updateShippingRule(shippingRuleId, payload, null);
    }

    public ApiResponse updateShippingRule(Object shippingRuleId, Object payload,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPutRequest(ShippingRulesUrls.SHIPPING_RULE,
                RequestOptions.builder(options).pathParams(pathParams(shippingRuleId))
                        .jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(ShippingRule.class, body)).build());
    }

    public ApiResponse updateShippingRule(Object shippingRuleId, ShippingRuleInput payload) {
        return updateShippingRule(shippingRuleId, (Object) payload, null);
    }

    public ApiResponse updateShippingRule(Object shippingRuleId, ShippingRuleInput payload,
            RequestOptions options) {
        return updateShippingRule(shippingRuleId, (Object) payload, options);
    }

    /**
     * Change only the fields sent — {@code PATCH /admin/shipping_rules/{shipping_rule_id}}.
     * {@code zones} is the full list, so {@code []} takes every zone off;
     * {@code shipping_rate} and {@code shipping_class} {@code null} take the rate or the
     * class off. The records themselves stay.
     */
    public ApiResponse patchShippingRule(Object shippingRuleId, Object payload) {
        return patchShippingRule(shippingRuleId, payload, null);
    }

    public ApiResponse patchShippingRule(Object shippingRuleId, Object payload,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPatchRequest(ShippingRulesUrls.SHIPPING_RULE,
                RequestOptions.builder(options).pathParams(pathParams(shippingRuleId))
                        .jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(ShippingRule.class, body)).build());
    }

    public ApiResponse patchShippingRule(Object shippingRuleId, ShippingRuleInput payload) {
        return patchShippingRule(shippingRuleId, (Object) payload, null);
    }

    public ApiResponse patchShippingRule(Object shippingRuleId, ShippingRuleInput payload,
            RequestOptions options) {
        return patchShippingRule(shippingRuleId, (Object) payload, options);
    }

    /**
     * Delete one rule — {@code DELETE /admin/shipping_rules/{shipping_rule_id}}, {@code 204}.
     * Its rate and zones stay. A rule on a profile is {@code 409} until it is taken off.
     */
    public ApiResponse deleteShippingRule(Object shippingRuleId) {
        return deleteShippingRule(shippingRuleId, null);
    }

    public ApiResponse deleteShippingRule(Object shippingRuleId, RequestOptions options) {
        return client.sendDeleteRequest(ShippingRulesUrls.SHIPPING_RULE,
                RequestOptions.builder(options).pathParams(pathParams(shippingRuleId))
                        .dtoFactory(null).build());
    }

    private static Map<String, Object> pathParams(Object shippingRuleId) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("shipping_rule_id", shippingRuleId);
        return params;
    }
}
