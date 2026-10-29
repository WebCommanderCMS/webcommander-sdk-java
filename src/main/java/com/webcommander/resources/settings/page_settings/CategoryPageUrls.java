package com.webcommander.resources.settings.page_settings;

/** Path templates for the category page settings endpoints. */
public final class CategoryPageUrls {

    private CategoryPageUrls() {}

    public static final String CATEGORY_PAGE =
            "/api/{api_version}/admin/settings/page_settings/category_page";

    public static final String CATEGORY_PAGE_SECTION =
            "/api/{api_version}/admin/settings/page_settings/category_page/{section}";
}
