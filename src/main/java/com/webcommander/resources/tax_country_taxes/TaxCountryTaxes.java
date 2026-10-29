package com.webcommander.resources.tax_country_taxes;

import com.webcommander.Dto;
import com.webcommander.client.ApiResponse;
import com.webcommander.client.BoundResource;
import com.webcommander.client.RequestOptions;
import com.webcommander.client.ResourceBinding;
import com.webcommander.client.WebCommanderClient;
import com.webcommander.resources.tax_country_taxes.tax_country_taxes_dto
        .TaxCountryTaxesDto.CountryTax;
import com.webcommander.resources.tax_country_taxes.tax_country_taxes_dto
        .TaxCountryTaxesDto.CountryTaxInput;
import com.webcommander.resources.tax_country_taxes.tax_country_taxes_dto
        .TaxCountryTaxesDto.CountryTaxPage;

import java.util.LinkedHashMap;
import java.util.Map;

/** Country taxes — {@code /api/{api_version}/admin/tax/country-taxes}; no HEAD. */
public final class TaxCountryTaxes implements BoundResource {

    private final WebCommanderClient client;

    public TaxCountryTaxes(WebCommanderClient client) {
        this.client = client;
    }

    /** List the country taxes — {@code GET /admin/tax/country-taxes}, newest first. */
    public ApiResponse listTaxCountryTaxes() {
        return listTaxCountryTaxes(null, null);
    }

    public ApiResponse listTaxCountryTaxes(Map<String, Object> queryParams) {
        return listTaxCountryTaxes(queryParams, null);
    }

    public ApiResponse listTaxCountryTaxes(Map<String, Object> queryParams,
            RequestOptions options) {
        return client.sendGetRequest(TaxCountryTaxesUrls.TAX_COUNTRY_TAXES,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(CountryTaxPage.class, body)).build());
    }

    /** Create a country tax — {@code POST /admin/tax/country-taxes}; {@code 201}. */
    public ApiResponse createTaxCountryTax(Object payload) {
        return createTaxCountryTax(payload, null, null);
    }

    public ApiResponse createTaxCountryTax(Object payload, RequestOptions options) {
        return createTaxCountryTax(payload, null, options);
    }

    public ApiResponse createTaxCountryTax(Object payload, String transactionNo) {
        return createTaxCountryTax(payload, transactionNo, null);
    }

    public ApiResponse createTaxCountryTax(Object payload, String transactionNo,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        RequestOptions given = options == null ? RequestOptions.none() : options;
        Map<String, String> headers = new LinkedHashMap<>();
        if (given.getHeaders() != null) {
            headers.putAll(given.getHeaders());
        }
        if (transactionNo != null) {
            headers.put(TaxCountryTaxesUrls.TRANSACTION_NO_HEADER, transactionNo);
        }
        return client.sendPostRequest(TaxCountryTaxesUrls.TAX_COUNTRY_TAXES,
                RequestOptions.builder(options).jsonBody(payload).headers(headers)
                        .dtoFactory(body -> Dto.fromMap(CountryTax.class, body)).build());
    }

    public ApiResponse createTaxCountryTax(CountryTaxInput payload) {
        return createTaxCountryTax((Object) payload, null, null);
    }

    public ApiResponse createTaxCountryTax(CountryTaxInput payload, RequestOptions options) {
        return createTaxCountryTax((Object) payload, null, options);
    }

    public ApiResponse createTaxCountryTax(CountryTaxInput payload, String transactionNo) {
        return createTaxCountryTax((Object) payload, transactionNo, null);
    }

    public ApiResponse createTaxCountryTax(CountryTaxInput payload, String transactionNo,
            RequestOptions options) {
        return createTaxCountryTax((Object) payload, transactionNo, options);
    }

    /** Retrieve one country tax — {@code GET /admin/tax/country-taxes/{country_tax_id}}. */
    public ApiResponse getTaxCountryTax(Object countryTaxId) {
        return getTaxCountryTax(countryTaxId, null);
    }

    public ApiResponse getTaxCountryTax(Object countryTaxId, RequestOptions options) {
        return client.sendGetRequest(TaxCountryTaxesUrls.TAX_COUNTRY_TAX,
                RequestOptions.builder(options).pathParams(pathParams(countryTaxId))
                        .dtoFactory(body -> Dto.fromMap(CountryTax.class, body)).build());
    }

    /** Merge into one country tax — {@code PUT /admin/tax/country-taxes/{country_tax_id}}. */
    public ApiResponse updateTaxCountryTax(Object countryTaxId, Object payload) {
        return updateTaxCountryTax(countryTaxId, payload, null);
    }

    public ApiResponse updateTaxCountryTax(Object countryTaxId, Object payload,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPutRequest(TaxCountryTaxesUrls.TAX_COUNTRY_TAX,
                RequestOptions.builder(options).pathParams(pathParams(countryTaxId))
                        .jsonBody(payload).dtoFactory(body -> Dto.fromMap(CountryTax.class, body))
                        .build());
    }

    public ApiResponse updateTaxCountryTax(Object countryTaxId, CountryTaxInput payload) {
        return updateTaxCountryTax(countryTaxId, (Object) payload, null);
    }

    public ApiResponse updateTaxCountryTax(Object countryTaxId, CountryTaxInput payload,
            RequestOptions options) {
        return updateTaxCountryTax(countryTaxId, (Object) payload, options);
    }

    /** Delete one country tax — {@code DELETE .../country-taxes/{country_tax_id}}; {@code 204}. */
    public ApiResponse deleteTaxCountryTax(Object countryTaxId) {
        return deleteTaxCountryTax(countryTaxId, null);
    }

    public ApiResponse deleteTaxCountryTax(Object countryTaxId, RequestOptions options) {
        return client.sendDeleteRequest(TaxCountryTaxesUrls.TAX_COUNTRY_TAX,
                RequestOptions.builder(options).pathParams(pathParams(countryTaxId))
                        .dtoFactory(null).build());
    }

    private static Map<String, Object> pathParams(Object countryTaxId) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("country_tax_id", countryTaxId);
        return params;
    }
}
