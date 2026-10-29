package com.webcommander.resources.settings.ecommerce_settings;

import java.util.List;

/** Path templates for the ecommerce settings endpoints. */
public final class EcommerceSettingsUrls {

    private EcommerceSettingsUrls() {}

    public static final String ECOMMERCE_SETTINGS =
            "/api/{api_version}/admin/settings/ecommerce_settings";

    public static final String ECOMMERCE_SETTINGS_SECTION =
            "/api/{api_version}/admin/settings/ecommerce_settings/{section}";

    /** The seven keys the facade's body and its {@code group_meta} both carry. */
    public static final List<String> ECOMMERCE_SETTINGS_AREA_KEYS = List.of("continue_shopping",
            "product_inventory", "pricing", "product_restriction", "order_filters",
            "location_settings", "order_flags");

    /** The six values {@code {section}} takes. */
    public static final List<String> ECOMMERCE_SETTINGS_SECTIONS = List.of("continue_shopping",
            "product_inventory", "pricing", "product_restriction", "order_filters",
            "location_settings");

    /** The one area key that is not a section of this area. */
    public static final String ECOMMERCE_SETTINGS_COLLECTION_KEY = "order_flags";
}
