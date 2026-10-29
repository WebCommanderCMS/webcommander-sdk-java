package com.webcommander.resources.settings.page_settings;

/** Path templates for the shopping cart settings endpoints. */
public final class ShoppingCartUrls {

    private ShoppingCartUrls() {}

    public static final String SHOPPING_CART =
            "/api/{api_version}/admin/settings/page_settings/shopping_cart";

    public static final String SHOPPING_CART_SECTION =
            "/api/{api_version}/admin/settings/page_settings/shopping_cart/{section}";
}
