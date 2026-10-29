package com.webcommander.resources.countries;

import com.webcommander.Dto;
import com.webcommander.client.ApiResponse;
import com.webcommander.client.BoundResource;
import com.webcommander.client.RequestOptions;
import com.webcommander.client.WebCommanderClient;
import com.webcommander.resources.countries.countries_dto.CountriesDto.Country;
import com.webcommander.resources.countries.countries_dto.CountriesDto.CountryPage;
import com.webcommander.resources.countries.countries_dto.CountriesDto.CountryState;
import com.webcommander.resources.countries.countries_dto.CountriesDto.CountryStatePage;

import java.util.LinkedHashMap;
import java.util.Map;

/** Countries and their states — {@code /api/{api_version}/admin/countries}. */
public final class Countries implements BoundResource {

    private final WebCommanderClient client;

    public Countries(WebCommanderClient client) {
        this.client = client;
    }

    /** List the countries — {@code GET /admin/countries}. */
    public ApiResponse listCountries() {
        return listCountries(null, null);
    }

    public ApiResponse listCountries(Map<String, Object> queryParams) {
        return listCountries(queryParams, null);
    }

    public ApiResponse listCountries(Map<String, Object> queryParams, RequestOptions options) {
        return client.sendGetRequest(CountriesUrls.COUNTRIES,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(CountryPage.class, body)).build());
    }

    /** Ask whether the collection is reachable — {@code HEAD /admin/countries}; it sends an ETag. */
    public ApiResponse headCountries() {
        return headCountries(null);
    }

    public ApiResponse headCountries(RequestOptions options) {
        return client.sendApiRequest("HEAD", CountriesUrls.COUNTRIES,
                RequestOptions.builder(options).dtoFactory(null).build());
    }

    /** Retrieve one country by id, ISO code or name — {@code GET /admin/countries/{country_id}}. */
    public ApiResponse getCountry(Object countryId) {
        return getCountry(countryId, null);
    }

    public ApiResponse getCountry(Object countryId, RequestOptions options) {
        return client.sendGetRequest(CountriesUrls.COUNTRY,
                RequestOptions.builder(options).pathParams(pathParams(countryId))
                        .dtoFactory(body -> Dto.fromMap(Country.class, body)).build());
    }

    /** Probe one country — {@code HEAD /admin/countries/{country_id}}; its read's ETag, no body. */
    public ApiResponse headCountry(Object countryId) {
        return headCountry(countryId, null);
    }

    public ApiResponse headCountry(Object countryId, RequestOptions options) {
        return client.sendApiRequest("HEAD", CountriesUrls.COUNTRY,
                RequestOptions.builder(options).pathParams(pathParams(countryId)).dtoFactory(null)
                        .build());
    }

    /** List one country's states — {@code GET /admin/countries/{country_id}/states}. */
    public ApiResponse listCountryStates(Object countryId) {
        return listCountryStates(countryId, null, null);
    }

    public ApiResponse listCountryStates(Object countryId, Map<String, Object> queryParams) {
        return listCountryStates(countryId, queryParams, null);
    }

    public ApiResponse listCountryStates(Object countryId, Map<String, Object> queryParams,
            RequestOptions options) {
        return client.sendGetRequest(CountriesUrls.COUNTRY_STATES,
                RequestOptions.builder(options).pathParams(pathParams(countryId))
                        .queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(CountryStatePage.class, body)).build());
    }

    /** Probe one country's states — {@code HEAD /admin/countries/{country_id}/states}. */
    public ApiResponse headCountryStates(Object countryId) {
        return headCountryStates(countryId, null);
    }

    public ApiResponse headCountryStates(Object countryId, RequestOptions options) {
        return client.sendApiRequest("HEAD", CountriesUrls.COUNTRY_STATES,
                RequestOptions.builder(options).pathParams(pathParams(countryId)).dtoFactory(null)
                        .build());
    }

    /** Retrieve one state by id, code or name — {@code GET .../{country_id}/states/{state_id}}. */
    public ApiResponse getCountryState(Object countryId, Object stateId) {
        return getCountryState(countryId, stateId, null);
    }

    public ApiResponse getCountryState(Object countryId, Object stateId, RequestOptions options) {
        return client.sendGetRequest(CountriesUrls.COUNTRY_STATE,
                RequestOptions.builder(options).pathParams(pathParams(countryId, stateId))
                        .dtoFactory(body -> Dto.fromMap(CountryState.class, body)).build());
    }

    /** Probe one state of one country — {@code HEAD .../{country_id}/states/{state_id}}. */
    public ApiResponse headCountryState(Object countryId, Object stateId) {
        return headCountryState(countryId, stateId, null);
    }

    public ApiResponse headCountryState(Object countryId, Object stateId, RequestOptions options) {
        return client.sendApiRequest("HEAD", CountriesUrls.COUNTRY_STATE,
                RequestOptions.builder(options).pathParams(pathParams(countryId, stateId))
                        .dtoFactory(null).build());
    }

    private static Map<String, Object> pathParams(Object countryId) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("country_id", countryId);
        return params;
    }

    private static Map<String, Object> pathParams(Object countryId, Object stateId) {
        Map<String, Object> params = pathParams(countryId);
        params.put("state_id", stateId);
        return params;
    }
}
