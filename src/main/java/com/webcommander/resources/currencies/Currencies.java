package com.webcommander.resources.currencies;

import com.webcommander.Dto;
import com.webcommander.client.ApiResponse;
import com.webcommander.client.BoundResource;
import com.webcommander.client.RequestOptions;
import com.webcommander.client.ResourceBinding;
import com.webcommander.client.WebCommanderClient;
import com.webcommander.resources.currencies.currencies_dto.CurrenciesDto.Currency;
import com.webcommander.resources.currencies.currencies_dto.CurrenciesDto.CurrencyBaseChange;
import com.webcommander.resources.currencies.currencies_dto.CurrenciesDto.CurrencyCount;
import com.webcommander.resources.currencies.currencies_dto.CurrenciesDto.CurrencyInput;
import com.webcommander.resources.currencies.currencies_dto.CurrenciesDto.CurrencyPage;

import java.util.LinkedHashMap;
import java.util.Map;

/** Currencies — {@code /api/{api_version}/admin/currencies}. */
public final class Currencies implements BoundResource {

    private final WebCommanderClient client;

    public Currencies(WebCommanderClient client) {
        this.client = client;
    }

    /** List the currencies — {@code GET /admin/currencies}. */
    public ApiResponse listCurrencies() {
        return listCurrencies(null, null);
    }

    public ApiResponse listCurrencies(Map<String, Object> queryParams) {
        return listCurrencies(queryParams, null);
    }

    public ApiResponse listCurrencies(Map<String, Object> queryParams, RequestOptions options) {
        return client.sendGetRequest(CurrenciesUrls.CURRENCIES,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(CurrencyPage.class, body)).build());
    }

    /** Ask whether the collection is reachable — {@code HEAD /admin/currencies}; no body. */
    public ApiResponse headCurrencies() {
        return headCurrencies(null);
    }

    public ApiResponse headCurrencies(RequestOptions options) {
        return client.sendApiRequest("HEAD", CurrenciesUrls.CURRENCIES,
                RequestOptions.builder(options).dtoFactory(null).build());
    }

    /** How many currencies match — {@code GET /admin/currencies/count}. */
    public ApiResponse countCurrencies() {
        return countCurrencies(null, null);
    }

    public ApiResponse countCurrencies(Map<String, Object> queryParams) {
        return countCurrencies(queryParams, null);
    }

    public ApiResponse countCurrencies(Map<String, Object> queryParams, RequestOptions options) {
        return client.sendGetRequest(CurrenciesUrls.CURRENCIES_COUNT,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(CurrencyCount.class, body)).build());
    }

    /** Ask whether the count is reachable — {@code HEAD /admin/currencies/count}; no body. */
    public ApiResponse headCurrenciesCount() {
        return headCurrenciesCount(null);
    }

    public ApiResponse headCurrenciesCount(RequestOptions options) {
        return client.sendApiRequest("HEAD", CurrenciesUrls.CURRENCIES_COUNT,
                RequestOptions.builder(options).dtoFactory(null).build());
    }

    /** Create a currency — {@code POST /admin/currencies}. */
    public ApiResponse createCurrency(Object payload) {
        return createCurrency(payload, null, null);
    }

    public ApiResponse createCurrency(Object payload, RequestOptions options) {
        return createCurrency(payload, null, options);
    }

    public ApiResponse createCurrency(Object payload, String transactionNo) {
        return createCurrency(payload, transactionNo, null);
    }

    public ApiResponse createCurrency(Object payload, String transactionNo,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        RequestOptions given = options == null ? RequestOptions.none() : options;
        Map<String, String> headers = new LinkedHashMap<>();
        if (given.getHeaders() != null) {
            headers.putAll(given.getHeaders());
        }
        if (transactionNo != null) {
            headers.put(CurrenciesUrls.TRANSACTION_NO_HEADER, transactionNo);
        }
        return client.sendPostRequest(CurrenciesUrls.CURRENCIES,
                RequestOptions.builder(options).jsonBody(payload).headers(headers)
                        .dtoFactory(body -> Dto.fromMap(Currency.class, body)).build());
    }

    public ApiResponse createCurrency(CurrencyInput payload) {
        return createCurrency((Object) payload, null, null);
    }

    public ApiResponse createCurrency(CurrencyInput payload, RequestOptions options) {
        return createCurrency((Object) payload, null, options);
    }

    public ApiResponse createCurrency(CurrencyInput payload, String transactionNo) {
        return createCurrency((Object) payload, transactionNo, null);
    }

    public ApiResponse createCurrency(CurrencyInput payload, String transactionNo,
            RequestOptions options) {
        return createCurrency((Object) payload, transactionNo, options);
    }

    /** Retrieve one currency — {@code GET /admin/currencies/{currency_id}}. */
    public ApiResponse getCurrency(Object currencyId) {
        return getCurrency(currencyId, null, null);
    }

    public ApiResponse getCurrency(Object currencyId, Map<String, Object> queryParams) {
        return getCurrency(currencyId, queryParams, null);
    }

    public ApiResponse getCurrency(Object currencyId, Map<String, Object> queryParams,
            RequestOptions options) {
        return client.sendGetRequest(CurrenciesUrls.CURRENCY,
                RequestOptions.builder(options).pathParams(pathParams(currencyId))
                        .queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(Currency.class, body)).build());
    }

    /** Probe one currency — {@code HEAD /admin/currencies/{currency_id}}, with its ETag. */
    public ApiResponse headCurrency(Object currencyId) {
        return headCurrency(currencyId, null);
    }

    public ApiResponse headCurrency(Object currencyId, RequestOptions options) {
        return client.sendApiRequest("HEAD", CurrenciesUrls.CURRENCY,
                RequestOptions.builder(options).pathParams(pathParams(currencyId)).dtoFactory(null)
                        .build());
    }

    /** Replace one currency — {@code PUT /admin/currencies/{currency_id}}. */
    public ApiResponse updateCurrency(Object currencyId, Object payload) {
        return updateCurrency(currencyId, payload, null);
    }

    public ApiResponse updateCurrency(Object currencyId, Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPutRequest(CurrenciesUrls.CURRENCY,
                RequestOptions.builder(options).pathParams(pathParams(currencyId)).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(Currency.class, body)).build());
    }

    public ApiResponse updateCurrency(Object currencyId, CurrencyInput payload) {
        return updateCurrency(currencyId, (Object) payload, null);
    }

    public ApiResponse updateCurrency(Object currencyId, CurrencyInput payload,
            RequestOptions options) {
        return updateCurrency(currencyId, (Object) payload, options);
    }

    /** Change only the fields sent — {@code PATCH /admin/currencies/{currency_id}}. */
    public ApiResponse patchCurrency(Object currencyId, Object payload) {
        return patchCurrency(currencyId, payload, null);
    }

    public ApiResponse patchCurrency(Object currencyId, Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPatchRequest(CurrenciesUrls.CURRENCY,
                RequestOptions.builder(options).pathParams(pathParams(currencyId)).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(Currency.class, body)).build());
    }

    public ApiResponse patchCurrency(Object currencyId, CurrencyInput payload) {
        return patchCurrency(currencyId, (Object) payload, null);
    }

    public ApiResponse patchCurrency(Object currencyId, CurrencyInput payload,
            RequestOptions options) {
        return patchCurrency(currencyId, (Object) payload, options);
    }

    /** Delete a currency — {@code DELETE /admin/currencies/{currency_id}}; the base is 409. */
    public ApiResponse deleteCurrency(Object currencyId) {
        return deleteCurrency(currencyId, null);
    }

    public ApiResponse deleteCurrency(Object currencyId, RequestOptions options) {
        return client.sendDeleteRequest(CurrenciesUrls.CURRENCY,
                RequestOptions.builder(options).pathParams(pathParams(currencyId)).dtoFactory(null)
                        .build());
    }

    /** Make an active currency the store's base — {@code PUT .../{currency_id}/base}. */
    public ApiResponse setBaseCurrency(Object currencyId) {
        return setBaseCurrency(currencyId, null);
    }

    public ApiResponse setBaseCurrency(Object currencyId, RequestOptions options) {
        return client.sendPutRequest(CurrenciesUrls.CURRENCY_BASE,
                RequestOptions.builder(options).pathParams(pathParams(currencyId))
                        .dtoFactory(body -> Dto.fromMap(CurrencyBaseChange.class, body)).build());
    }

    private static Map<String, Object> pathParams(Object currencyId) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("currency_id", currencyId);
        return params;
    }
}
