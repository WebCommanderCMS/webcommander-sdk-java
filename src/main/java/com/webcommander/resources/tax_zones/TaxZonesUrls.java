package com.webcommander.resources.tax_zones;

/** Path templates for the tax zone endpoints. */
public final class TaxZonesUrls {

    private TaxZonesUrls() {}

    public static final String TAX_ZONES = "/api/{api_version}/admin/tax_zones";

    public static final String TAX_ZONE = "/api/{api_version}/admin/tax_zones/{tax_zone_id}";
}
