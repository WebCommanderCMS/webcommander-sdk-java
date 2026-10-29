package com.webcommander.resources.settings.page_settings;

import java.util.List;

/** Path templates for the product page settings endpoints. */
public final class ProductPageUrls {

    private ProductPageUrls() {}

    public static final String PRODUCT_PAGE =
            "/api/{api_version}/admin/settings/page_settings/product_page";

    public static final String PRODUCT_PAGE_SECTION =
            "/api/{api_version}/admin/settings/page_settings/product_page/{section}";

    /** The seven values {@code {section}} takes, in the order the facade declares them. */
    public static final List<String> PRODUCT_PAGE_SECTIONS = List.of(
            "configuration",
            "variations",
            "image_and_video",
            "up_sell",
            "related_products",
            "review_and_ratings",
            "quick_view");
}
