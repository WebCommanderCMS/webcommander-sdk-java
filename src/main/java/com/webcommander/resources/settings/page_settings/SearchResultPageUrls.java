package com.webcommander.resources.settings.page_settings;

import java.util.List;

/** Path templates for the search result page settings endpoints. */
public final class SearchResultPageUrls {

    private SearchResultPageUrls() {}

    public static final String SEARCH_RESULT_PAGE =
            "/api/{api_version}/admin/settings/page_settings/search_result_page";

    public static final String SEARCH_RESULT_PAGE_SECTION =
            "/api/{api_version}/admin/settings/page_settings/search_result_page/{section}";

    public static final String SEARCH_RESULT_PAGE_PRODUCT_PROMOTIONS =
            "/api/{api_version}/admin/settings/page_settings/search_result_page/product_promotion";

    public static final String SEARCH_RESULT_PAGE_PRODUCT_PROMOTION =
            "/api/{api_version}/admin/settings/page_settings/search_result_page/product_promotion"
                    + "/{promotion_id}";

    public static final String SEARCH_RESULT_PAGE_PRODUCT_PROMOTION_ACTIVE =
            "/api/{api_version}/admin/settings/page_settings/search_result_page/product_promotion"
                    + "/{promotion_id}/active";

    /** The three values {@code {section}} takes; {@code product_promotion} is a collection. */
    public static final List<String> SEARCH_RESULT_PAGE_SECTIONS =
            List.of("product_information", "seo", "reinitialise");
}
