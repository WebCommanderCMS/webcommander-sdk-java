package com.webcommander.resources.categories;

/** Path templates for the category endpoints. */
public final class CategoriesUrls {

    private CategoriesUrls() {}

    public static final String CATEGORIES = "/api/{api_version}/admin/categories";

    public static final String CATEGORIES_COUNT = "/api/{api_version}/admin/categories/count";

    public static final String CATEGORIES_CHECK_NAME =
            "/api/{api_version}/admin/categories/check-name";

    public static final String CATEGORY = "/api/{api_version}/admin/categories/{category_id}";

    public static final String CATEGORY_SECTION =
            "/api/{api_version}/admin/categories/{category_id}/{section}";

    public static final String TRANSACTION_NO_HEADER = "X-Transaction-No";
}
