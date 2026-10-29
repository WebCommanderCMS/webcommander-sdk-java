package com.webcommander.resources.settings.site_settings;

/** Path templates for the site settings endpoints. */
public final class SiteSettingsUrls {

    private SiteSettingsUrls() {}

    public static final String SITE_SETTINGS =
            "/api/{api_version}/admin/settings/site_settings";

    public static final String SITE_SETTINGS_SECTION =
            "/api/{api_version}/admin/settings/site_settings/{section}";
}
