package com.webcommander.resources.settings.product_listing;

import java.util.List;

/** Path templates for the product listing endpoints. */
public final class ProductListingUrls {

    private ProductListingUrls() {}

    public static final String PRODUCT_LISTING =
            "/api/{api_version}/admin/settings/product_listing";

    /** The four sections the area declares, in the order the facade sends them. */
    public static final List<String> PRODUCT_LISTING_SECTIONS = List.of("product_information",
            "load", "sorting", "sortable_items");
}
