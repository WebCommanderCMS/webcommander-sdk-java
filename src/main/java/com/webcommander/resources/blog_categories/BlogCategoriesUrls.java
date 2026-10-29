package com.webcommander.resources.blog_categories;

/** Path templates for the blog category endpoints. */
public final class BlogCategoriesUrls {

    private BlogCategoriesUrls() {}

    public static final String BLOG_CATEGORIES = "/api/{api_version}/admin/blog_categories";

    public static final String BLOG_CATEGORY =
            "/api/{api_version}/admin/blog_categories/{blog_category_id}";
}
