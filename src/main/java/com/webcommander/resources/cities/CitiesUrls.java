package com.webcommander.resources.cities;

/** Path templates for the city endpoints. */
public final class CitiesUrls {

    private CitiesUrls() {}

    /** Under {@code /app}, not {@code /admin}: the storefront address lookup. */
    public static final String CITIES = "/api/{api_version}/app/cities";
}
