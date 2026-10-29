package com.webcommander.resources.settings.price_display_settings;

import java.util.List;

/** Path templates for the price display settings endpoints. */
public final class PriceDisplaySettingsUrls {

    private PriceDisplaySettingsUrls() {}

    public static final String PRICE_DISPLAY_SETTINGS =
            "/api/{api_version}/admin/settings/price_display_settings";

    public static final String PRICE_DISPLAY_SETTINGS_SECTION =
            "/api/{api_version}/admin/settings/price_display_settings/{section}";

    /** The two values {@code {section}} takes, in the order the facade declares them. */
    public static final List<String> PRICE_DISPLAY_SETTINGS_SECTIONS =
            List.of("order_item", "order_summary");
}
