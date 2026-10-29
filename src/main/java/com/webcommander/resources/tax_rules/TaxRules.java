package com.webcommander.resources.tax_rules;

import com.webcommander.Dto;
import com.webcommander.client.ApiResponse;
import com.webcommander.client.BoundResource;
import com.webcommander.client.RequestOptions;
import com.webcommander.client.ResourceBinding;
import com.webcommander.client.WebCommanderClient;
import com.webcommander.resources.tax_rules.tax_rules_dto.TaxRulesDto.TaxRule;
import com.webcommander.resources.tax_rules.tax_rules_dto.TaxRulesDto.TaxRuleInput;
import com.webcommander.resources.tax_rules.tax_rules_dto.TaxRulesDto.TaxRulePage;

import java.util.LinkedHashMap;
import java.util.Map;

/** Tax rules — {@code /api/{api_version}/admin/tax_rules}. */
public final class TaxRules implements BoundResource {

    private final WebCommanderClient client;

    public TaxRules(WebCommanderClient client) {
        this.client = client;
    }

    /** List the tax rules — {@code GET /admin/tax_rules}; {@code limit} is capped at 100. */
    public ApiResponse listTaxRules() {
        return listTaxRules(null, null);
    }

    public ApiResponse listTaxRules(Map<String, Object> queryParams) {
        return listTaxRules(queryParams, null);
    }

    public ApiResponse listTaxRules(Map<String, Object> queryParams, RequestOptions options) {
        return client.sendGetRequest(TaxRulesUrls.TAX_RULES,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(TaxRulePage.class, body)).build());
    }

    /** Ask whether the collection is reachable — {@code HEAD /admin/tax_rules}. */
    public ApiResponse headTaxRules() {
        return headTaxRules(null);
    }

    public ApiResponse headTaxRules(RequestOptions options) {
        return client.sendApiRequest("HEAD", TaxRulesUrls.TAX_RULES,
                RequestOptions.builder(options).dtoFactory(null).build());
    }

    /** Create a tax rule — {@code POST /admin/tax_rules}, wrapped in {@code tax_rule}. */
    public ApiResponse createTaxRule(Object payload) {
        return createTaxRule(payload, null);
    }

    public ApiResponse createTaxRule(Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPostRequest(TaxRulesUrls.TAX_RULES,
                RequestOptions.builder(options).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(TaxRule.class, body)).build());
    }

    public ApiResponse createTaxRule(TaxRuleInput payload) {
        return createTaxRule((Object) payload, null);
    }

    public ApiResponse createTaxRule(TaxRuleInput payload, RequestOptions options) {
        return createTaxRule((Object) payload, options);
    }

    /** Retrieve one tax rule — {@code GET /admin/tax_rules/{tax_rule_id}}. */
    public ApiResponse getTaxRule(Object taxRuleId) {
        return getTaxRule(taxRuleId, null);
    }

    public ApiResponse getTaxRule(Object taxRuleId, RequestOptions options) {
        return client.sendGetRequest(TaxRulesUrls.TAX_RULE,
                RequestOptions.builder(options).pathParams(pathParams(taxRuleId))
                        .dtoFactory(body -> Dto.fromMap(TaxRule.class, body)).build());
    }

    /** Probe one tax rule — {@code HEAD /admin/tax_rules/{tax_rule_id}}. */
    public ApiResponse headTaxRule(Object taxRuleId) {
        return headTaxRule(taxRuleId, null);
    }

    public ApiResponse headTaxRule(Object taxRuleId, RequestOptions options) {
        return client.sendApiRequest("HEAD", TaxRulesUrls.TAX_RULE,
                RequestOptions.builder(options).pathParams(pathParams(taxRuleId)).dtoFactory(null)
                        .build());
    }

    /** Update one tax rule — {@code PUT /admin/tax_rules/{tax_rule_id}}. */
    public ApiResponse updateTaxRule(Object taxRuleId, Object payload) {
        return updateTaxRule(taxRuleId, payload, null);
    }

    public ApiResponse updateTaxRule(Object taxRuleId, Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPutRequest(TaxRulesUrls.TAX_RULE,
                RequestOptions.builder(options).pathParams(pathParams(taxRuleId)).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(TaxRule.class, body)).build());
    }

    public ApiResponse updateTaxRule(Object taxRuleId, TaxRuleInput payload) {
        return updateTaxRule(taxRuleId, (Object) payload, null);
    }

    public ApiResponse updateTaxRule(Object taxRuleId, TaxRuleInput payload,
            RequestOptions options) {
        return updateTaxRule(taxRuleId, (Object) payload, options);
    }

    /** Delete one tax rule — {@code DELETE /admin/tax_rules/{tax_rule_id}}. */
    public ApiResponse deleteTaxRule(Object taxRuleId) {
        return deleteTaxRule(taxRuleId, null);
    }

    public ApiResponse deleteTaxRule(Object taxRuleId, RequestOptions options) {
        return client.sendDeleteRequest(TaxRulesUrls.TAX_RULE,
                RequestOptions.builder(options).pathParams(pathParams(taxRuleId)).dtoFactory(null)
                        .build());
    }

    private static Map<String, Object> pathParams(Object taxRuleId) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("tax_rule_id", taxRuleId);
        return params;
    }
}
