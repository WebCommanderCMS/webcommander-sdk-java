package com.webcommander.resources.blog_categories;

import com.webcommander.Dto;
import com.webcommander.client.ApiResponse;
import com.webcommander.client.BoundResource;
import com.webcommander.client.RequestOptions;
import com.webcommander.client.ResourceBinding;
import com.webcommander.client.WebCommanderClient;
import com.webcommander.resources.blog_categories.blog_categories_dto.BlogCategoriesDto
        .BlogCategory;
import com.webcommander.resources.blog_categories.blog_categories_dto.BlogCategoriesDto
        .BlogCategoryInput;
import com.webcommander.resources.blog_categories.blog_categories_dto.BlogCategoriesDto
        .BlogCategoryPage;

import java.util.LinkedHashMap;
import java.util.Map;

/** Blog categories — {@code /api/{api_version}/admin/blog_categories}. */
public final class BlogCategories implements BoundResource {

    private final WebCommanderClient client;

    public BlogCategories(WebCommanderClient client) {
        this.client = client;
    }

    /** List the categories, ordered by {@code name} — {@code GET /admin/blog_categories}. */
    public ApiResponse listBlogCategories() {
        return listBlogCategories(null, null);
    }

    public ApiResponse listBlogCategories(Map<String, Object> queryParams) {
        return listBlogCategories(queryParams, null);
    }

    public ApiResponse listBlogCategories(Map<String, Object> queryParams,
            RequestOptions options) {
        return client.sendGetRequest(BlogCategoriesUrls.BLOG_CATEGORIES,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(BlogCategoryPage.class, body)).build());
    }

    /** Create a category — {@code POST /admin/blog_categories}, wrapped in {@code blog_category}. */
    public ApiResponse createBlogCategory(Object payload) {
        return createBlogCategory(payload, null);
    }

    public ApiResponse createBlogCategory(Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPostRequest(BlogCategoriesUrls.BLOG_CATEGORIES,
                RequestOptions.builder(options).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(BlogCategory.class, body)).build());
    }

    public ApiResponse createBlogCategory(BlogCategoryInput payload) {
        return createBlogCategory((Object) payload, null);
    }

    public ApiResponse createBlogCategory(BlogCategoryInput payload, RequestOptions options) {
        return createBlogCategory((Object) payload, options);
    }

    /** Retrieve one category — {@code GET /admin/blog_categories/{blog_category_id}}. */
    public ApiResponse getBlogCategory(Object blogCategoryId) {
        return getBlogCategory(blogCategoryId, null);
    }

    public ApiResponse getBlogCategory(Object blogCategoryId, RequestOptions options) {
        return client.sendGetRequest(BlogCategoriesUrls.BLOG_CATEGORY,
                RequestOptions.builder(options).pathParams(pathParams(blogCategoryId))
                        .dtoFactory(body -> Dto.fromMap(BlogCategory.class, body)).build());
    }

    /** Update a category — {@code PUT /admin/blog_categories/{blog_category_id}}. */
    public ApiResponse updateBlogCategory(Object blogCategoryId, Object payload) {
        return updateBlogCategory(blogCategoryId, payload, null);
    }

    public ApiResponse updateBlogCategory(Object blogCategoryId, Object payload,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPutRequest(BlogCategoriesUrls.BLOG_CATEGORY,
                RequestOptions.builder(options).pathParams(pathParams(blogCategoryId))
                        .jsonBody(payload).dtoFactory(body -> Dto.fromMap(BlogCategory.class, body))
                        .build());
    }

    public ApiResponse updateBlogCategory(Object blogCategoryId, BlogCategoryInput payload) {
        return updateBlogCategory(blogCategoryId, (Object) payload, null);
    }

    public ApiResponse updateBlogCategory(Object blogCategoryId, BlogCategoryInput payload,
            RequestOptions options) {
        return updateBlogCategory(blogCategoryId, (Object) payload, options);
    }

    /** Delete a category — {@code DELETE /admin/blog_categories/{blog_category_id}}. */
    public ApiResponse deleteBlogCategory(Object blogCategoryId) {
        return deleteBlogCategory(blogCategoryId, null);
    }

    public ApiResponse deleteBlogCategory(Object blogCategoryId, RequestOptions options) {
        return client.sendDeleteRequest(BlogCategoriesUrls.BLOG_CATEGORY,
                RequestOptions.builder(options).pathParams(pathParams(blogCategoryId))
                        .dtoFactory(null).build());
    }

    private static Map<String, Object> pathParams(Object blogCategoryId) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("blog_category_id", blogCategoryId);
        return params;
    }
}
