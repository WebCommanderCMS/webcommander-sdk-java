package com.webcommander.resources.cities;

import com.webcommander.Dto;
import com.webcommander.client.ApiResponse;
import com.webcommander.client.BoundResource;
import com.webcommander.client.RequestOptions;
import com.webcommander.client.WebCommanderClient;
import com.webcommander.resources.cities.cities_dto.CitiesDto.CityPage;

import java.util.Map;

/** Cities — {@code /api/{api_version}/app/cities}, read-only. */
public final class Cities implements BoundResource {

    private final WebCommanderClient client;

    public Cities(WebCommanderClient client) {
        this.client = client;
    }

    /**
     * List the cities, by state, post code and name unless {@code sort} says otherwise —
     * {@code GET /app/cities}. {@code limit} is the page size, 1 to 100 and 100 when absent,
     * and {@code offset} the start. {@code country} takes a country id or two-letter code,
     * {@code state} a state id or, beside {@code country}, a state code; {@code post_code}
     * matches exactly and {@code search} a substring of the name in any case. {@code sort} is
     * {@code state}, {@code name} or {@code post_code}, {@code order} {@code asc} or
     * {@code desc}.
     */
    public ApiResponse listCities() {
        return listCities(null, null);
    }

    public ApiResponse listCities(Map<String, Object> queryParams) {
        return listCities(queryParams, null);
    }

    public ApiResponse listCities(Map<String, Object> queryParams, RequestOptions options) {
        return client.sendGetRequest(CitiesUrls.CITIES,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(CityPage.class, body)).build());
    }

    /**
     * Ask whether the listing is reachable — {@code HEAD /app/cities}; the listing's headers,
     * its {@code ETag} among them, and no body.
     */
    public ApiResponse headCities() {
        return headCities(null);
    }

    public ApiResponse headCities(RequestOptions options) {
        return client.sendApiRequest("HEAD", CitiesUrls.CITIES,
                RequestOptions.builder(options).dtoFactory(null).build());
    }
}
