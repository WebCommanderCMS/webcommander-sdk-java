package com.webcommander.resources.inventory_locations;

import java.util.List;

/** Path templates and body keys for the inventory location endpoints. */
public final class InventoryLocationsUrls {

    private InventoryLocationsUrls() {}

    public static final String INVENTORY_LOCATIONS = "/api/{api_version}/admin/inventory_locations";

    public static final String INVENTORY_LOCATION =
            "/api/{api_version}/admin/inventory_locations/{inventory_location_id}";

    public static final String INVENTORY_LOCATION_STOCK =
            "/api/{api_version}/admin/inventory_locations/{inventory_location_id}/stock";

    public static final String INVENTORY_LOCATION_DEFAULT =
            "/api/{api_version}/admin/inventory_locations/{inventory_location_id}/default";

    public static final String INVENTORY_LOCATION_STATUS =
            "/api/{api_version}/admin/inventory_locations/{inventory_location_id}/status";

    /** The key the listing's rows arrive under — the resource's own plural name. */
    public static final String INVENTORY_LOCATIONS_KEY = "inventory_locations";

    /** The key one row arrives under; no write takes it as a wrapper. */
    public static final String INVENTORY_LOCATION_KEY = "inventory_location";

    /** The key the stock sub-path's rows arrive under, beside the location and the pagination. */
    public static final String INVENTORY_LOCATION_STOCK_KEY = "items";

    /** The three query names the listing accepts; any other name is refused with {@code 400}. */
    public static final List<String> INVENTORY_LOCATIONS_QUERY_PARAMS =
            List.of("q", "limit", "offset");

    /** The page size the listing applies when no {@code limit} is sent; 100 is the most. */
    public static final int INVENTORY_LOCATIONS_PAGE_SIZE = 20;
}
