package com.webcommander.resources.blogs;

/** Path templates for the blog and blog comment endpoints. */
public final class BlogsUrls {

    private BlogsUrls() {}

    public static final String BLOGS = "/api/{api_version}/admin/blogs";

    public static final String BLOGS_COUNT = "/api/{api_version}/admin/blogs/count";

    public static final String BLOG = "/api/{api_version}/admin/blogs/{blog_id}";

    public static final String BLOG_COMMENTS = "/api/{api_version}/admin/blogs/{blog_id}/comments";

    public static final String BLOG_COMMENT =
            "/api/{api_version}/admin/blogs/{blog_id}/comments/{blog_comment_id}";

    public static final String BLOG_COMMENT_APPROVE =
            "/api/{api_version}/admin/blogs/{blog_id}/comments/{blog_comment_id}/approve";

    public static final String BLOG_COMMENT_MARK_SPAM =
            "/api/{api_version}/admin/blogs/{blog_id}/comments/{blog_comment_id}/mark-spam";
}
