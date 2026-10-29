package com.webcommander.resources.countries;

/** Path templates for the country endpoints. */
public final class CountriesUrls {

    private CountriesUrls() {}

    public static final String COUNTRIES = "/api/{api_version}/admin/countries";

    public static final String COUNTRY = "/api/{api_version}/admin/countries/{country_id}";

    public static final String COUNTRY_STATES =
            "/api/{api_version}/admin/countries/{country_id}/states";

    public static final String COUNTRY_STATE =
            "/api/{api_version}/admin/countries/{country_id}/states/{state_id}";
}
