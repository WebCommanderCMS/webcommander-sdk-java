package com.webcommander.resources.settings.page_settings;

/** Path templates for the checkout page settings endpoints. */
public final class CheckoutPageUrls {

    private CheckoutPageUrls() {}

    public static final String CHECKOUT_PAGE =
            "/api/{api_version}/admin/settings/page_settings/checkout_page";

    public static final String CHECKOUT_PAGE_SECTION =
            "/api/{api_version}/admin/settings/page_settings/checkout_page/{section}";
}
