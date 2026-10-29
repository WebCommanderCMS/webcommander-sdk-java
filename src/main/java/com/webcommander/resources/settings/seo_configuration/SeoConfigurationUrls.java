package com.webcommander.resources.settings.seo_configuration;

import java.util.List;

/** Path templates for the SEO configuration settings endpoints. */
public final class SeoConfigurationUrls {

    private SeoConfigurationUrls() {}

    public static final String SEO_CONFIGURATION =
            "/api/{api_version}/admin/settings/seo_configuration";

    public static final String SEO_CONFIGURATION_SECTION =
            "/api/{api_version}/admin/settings/seo_configuration/{section}";

    public static final String SEO_CONFIGURATION_DOCUMENT_ROOT =
            "/api/{api_version}/admin/settings/seo_configuration/document_root";

    public static final String SEO_CONFIGURATION_DOCUMENT_ROOT_FILE =
            "/api/{api_version}/admin/settings/seo_configuration/document_root/{config_key}";

    public static final String SEO_CONFIGURATION_REDIRECTS =
            "/api/{api_version}/admin/settings/seo_configuration/redirects";

    public static final String SEO_CONFIGURATION_REDIRECT =
            "/api/{api_version}/admin/settings/seo_configuration/redirects/{redirect_id}";

    /** The three values {@code {section}} takes, the settings {@code group_meta} declares. */
    public static final List<String> SEO_CONFIGURATION_SECTIONS =
            List.of("global_store_settings", "custom_code", "url_settings");
}
