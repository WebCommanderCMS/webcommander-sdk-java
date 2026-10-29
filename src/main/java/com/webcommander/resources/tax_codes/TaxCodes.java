package com.webcommander.resources.tax_codes;

import com.webcommander.Dto;
import com.webcommander.client.ApiResponse;
import com.webcommander.client.BoundResource;
import com.webcommander.client.RequestOptions;
import com.webcommander.client.ResourceBinding;
import com.webcommander.client.WebCommanderClient;
import com.webcommander.resources.tax_codes.tax_codes_dto.TaxCodesDto.TaxCode;
import com.webcommander.resources.tax_codes.tax_codes_dto.TaxCodesDto.TaxCodeInput;
import com.webcommander.resources.tax_codes.tax_codes_dto.TaxCodesDto.TaxCodePage;

import java.util.LinkedHashMap;
import java.util.Map;

/** Tax codes — {@code /api/{api_version}/admin/tax_codes}. */
public final class TaxCodes implements BoundResource {

    private final WebCommanderClient client;

    public TaxCodes(WebCommanderClient client) {
        this.client = client;
    }

    /** List the tax codes — {@code GET /admin/tax_codes}; {@code limit} is capped at 100. */
    public ApiResponse listTaxCodes() {
        return listTaxCodes(null, null);
    }

    public ApiResponse listTaxCodes(Map<String, Object> queryParams) {
        return listTaxCodes(queryParams, null);
    }

    public ApiResponse listTaxCodes(Map<String, Object> queryParams, RequestOptions options) {
        return client.sendGetRequest(TaxCodesUrls.TAX_CODES,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(TaxCodePage.class, body)).build());
    }

    /** Ask whether the collection is reachable — {@code HEAD /admin/tax_codes}. */
    public ApiResponse headTaxCodes() {
        return headTaxCodes(null);
    }

    public ApiResponse headTaxCodes(RequestOptions options) {
        return client.sendApiRequest("HEAD", TaxCodesUrls.TAX_CODES,
                RequestOptions.builder(options).dtoFactory(null).build());
    }

    /** Create a tax code — {@code POST /admin/tax_codes}, wrapped in {@code tax_code}. */
    public ApiResponse createTaxCode(Object payload) {
        return createTaxCode(payload, null);
    }

    public ApiResponse createTaxCode(Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPostRequest(TaxCodesUrls.TAX_CODES,
                RequestOptions.builder(options).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(TaxCode.class, body)).build());
    }

    public ApiResponse createTaxCode(TaxCodeInput payload) {
        return createTaxCode((Object) payload, null);
    }

    public ApiResponse createTaxCode(TaxCodeInput payload, RequestOptions options) {
        return createTaxCode((Object) payload, options);
    }

    /** Retrieve one tax code — {@code GET /admin/tax_codes/{tax_code_id}}. */
    public ApiResponse getTaxCode(Object taxCodeId) {
        return getTaxCode(taxCodeId, null);
    }

    public ApiResponse getTaxCode(Object taxCodeId, RequestOptions options) {
        return client.sendGetRequest(TaxCodesUrls.TAX_CODE,
                RequestOptions.builder(options).pathParams(pathParams(taxCodeId))
                        .dtoFactory(body -> Dto.fromMap(TaxCode.class, body)).build());
    }

    /** Probe one tax code — {@code HEAD /admin/tax_codes/{tax_code_id}}. */
    public ApiResponse headTaxCode(Object taxCodeId) {
        return headTaxCode(taxCodeId, null);
    }

    public ApiResponse headTaxCode(Object taxCodeId, RequestOptions options) {
        return client.sendApiRequest("HEAD", TaxCodesUrls.TAX_CODE,
                RequestOptions.builder(options).pathParams(pathParams(taxCodeId)).dtoFactory(null)
                        .build());
    }

    /** Update one tax code — {@code PUT /admin/tax_codes/{tax_code_id}}. */
    public ApiResponse updateTaxCode(Object taxCodeId, Object payload) {
        return updateTaxCode(taxCodeId, payload, null);
    }

    public ApiResponse updateTaxCode(Object taxCodeId, Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPutRequest(TaxCodesUrls.TAX_CODE,
                RequestOptions.builder(options).pathParams(pathParams(taxCodeId)).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(TaxCode.class, body)).build());
    }

    public ApiResponse updateTaxCode(Object taxCodeId, TaxCodeInput payload) {
        return updateTaxCode(taxCodeId, (Object) payload, null);
    }

    public ApiResponse updateTaxCode(Object taxCodeId, TaxCodeInput payload,
            RequestOptions options) {
        return updateTaxCode(taxCodeId, (Object) payload, options);
    }

    /** Delete one tax code — {@code DELETE /admin/tax_codes/{tax_code_id}}. */
    public ApiResponse deleteTaxCode(Object taxCodeId) {
        return deleteTaxCode(taxCodeId, null);
    }

    public ApiResponse deleteTaxCode(Object taxCodeId, RequestOptions options) {
        return client.sendDeleteRequest(TaxCodesUrls.TAX_CODE,
                RequestOptions.builder(options).pathParams(pathParams(taxCodeId)).dtoFactory(null)
                        .build());
    }

    private static Map<String, Object> pathParams(Object taxCodeId) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("tax_code_id", taxCodeId);
        return params;
    }
}
