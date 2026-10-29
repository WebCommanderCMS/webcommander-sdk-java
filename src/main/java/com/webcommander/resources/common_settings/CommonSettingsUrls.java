package com.webcommander.resources.common_settings;

/** Path templates for the common settings endpoints. */
public final class CommonSettingsUrls {

    private CommonSettingsUrls() {}

    /** Not under {@code /admin}: the store-wide settings reader and writer. */
    public static final String SETTINGS = "/api/{api_version}/settings";

    /** Not under {@code /admin}: the customer registration form. */
    public static final String CREATE_CUSTOMER_FIELDS = "/api/{api_version}/create-customer-fields";
}
