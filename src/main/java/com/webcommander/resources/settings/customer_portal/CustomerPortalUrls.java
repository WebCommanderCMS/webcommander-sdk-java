package com.webcommander.resources.settings.customer_portal;

/** Path templates for the customer portal endpoints. */
public final class CustomerPortalUrls {

    private CustomerPortalUrls() {}

    public static final String CUSTOMER_PORTAL =
            "/api/{api_version}/admin/settings/customer_portal";

    public static final String CUSTOMER_PORTAL_SECTION =
            "/api/{api_version}/admin/settings/customer_portal/{section}";
}
