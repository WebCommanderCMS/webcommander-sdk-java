package com.webcommander.resources.blogs;

import com.webcommander.Dto;
import com.webcommander.client.ApiResponse;
import com.webcommander.client.BoundResource;
import com.webcommander.client.RequestOptions;
import com.webcommander.client.ResourceBinding;
import com.webcommander.client.WebCommanderClient;
import com.webcommander.resources.blogs.blogs_dto.BlogsDto.Blog;
import com.webcommander.resources.blogs.blogs_dto.BlogsDto.BlogCommentEnvelope;
import com.webcommander.resources.blogs.blogs_dto.BlogsDto.BlogCommentInput;
import com.webcommander.resources.blogs.blogs_dto.BlogsDto.BlogCommentList;
import com.webcommander.resources.blogs.blogs_dto.BlogsDto.BlogCount;
import com.webcommander.resources.blogs.blogs_dto.BlogsDto.BlogInput;
import com.webcommander.resources.blogs.blogs_dto.BlogsDto.BlogPage;

import java.util.LinkedHashMap;
import java.util.Map;

/** Blogs and their comments — {@code /api/{api_version}/admin/blogs}. */
public final class Blogs implements BoundResource {

    private final WebCommanderClient client;

    public Blogs(WebCommanderClient client) {
        this.client = client;
    }

    /** List the blogs, latest {@code date} first — {@code GET /admin/blogs}; 20 rows at most. */
    public ApiResponse listBlogs() {
        return listBlogs(null, null);
    }

    public ApiResponse listBlogs(Map<String, Object> queryParams) {
        return listBlogs(queryParams, null);
    }

    public ApiResponse listBlogs(Map<String, Object> queryParams, RequestOptions options) {
        return client.sendGetRequest(BlogsUrls.BLOGS,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(BlogPage.class, body)).build());
    }

    /** How many blogs are live — {@code GET /admin/blogs/count}, narrowed by the filters. */
    public ApiResponse countBlogs() {
        return countBlogs(null, null);
    }

    public ApiResponse countBlogs(Map<String, Object> queryParams) {
        return countBlogs(queryParams, null);
    }

    public ApiResponse countBlogs(Map<String, Object> queryParams, RequestOptions options) {
        return client.sendGetRequest(BlogsUrls.BLOGS_COUNT,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(BlogCount.class, body)).build());
    }

    /** Create a blog — {@code POST /admin/blogs}, answered {@code 201}. */
    public ApiResponse createBlog(Object payload) {
        return createBlog(payload, null);
    }

    public ApiResponse createBlog(Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPostRequest(BlogsUrls.BLOGS,
                RequestOptions.builder(options).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(Blog.class, body)).build());
    }

    public ApiResponse createBlog(BlogInput payload) {
        return createBlog((Object) payload, null);
    }

    public ApiResponse createBlog(BlogInput payload, RequestOptions options) {
        return createBlog((Object) payload, options);
    }

    /** Retrieve one blog — {@code GET /admin/blogs/{blog_id}}; a trashed blog is {@code 404}. */
    public ApiResponse getBlog(Object blogId) {
        return getBlog(blogId, null, null);
    }

    public ApiResponse getBlog(Object blogId, Map<String, Object> queryParams) {
        return getBlog(blogId, queryParams, null);
    }

    public ApiResponse getBlog(Object blogId, Map<String, Object> queryParams,
            RequestOptions options) {
        return client.sendGetRequest(BlogsUrls.BLOG,
                RequestOptions.builder(options).pathParams(pathParams(blogId))
                        .queryParams(queryParams).dtoFactory(body -> Dto.fromMap(Blog.class, body))
                        .build());
    }

    /** Replace one blog — {@code PUT /admin/blogs/{blog_id}}; a field it omits is reset. */
    public ApiResponse updateBlog(Object blogId, Object payload) {
        return updateBlog(blogId, payload, null);
    }

    public ApiResponse updateBlog(Object blogId, Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPutRequest(BlogsUrls.BLOG,
                RequestOptions.builder(options).pathParams(pathParams(blogId)).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(Blog.class, body)).build());
    }

    public ApiResponse updateBlog(Object blogId, BlogInput payload) {
        return updateBlog(blogId, (Object) payload, null);
    }

    public ApiResponse updateBlog(Object blogId, BlogInput payload, RequestOptions options) {
        return updateBlog(blogId, (Object) payload, options);
    }

    /** Change only the fields sent — {@code PATCH /admin/blogs/{blog_id}}. */
    public ApiResponse patchBlog(Object blogId, Object payload) {
        return patchBlog(blogId, payload, null);
    }

    public ApiResponse patchBlog(Object blogId, Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPatchRequest(BlogsUrls.BLOG,
                RequestOptions.builder(options).pathParams(pathParams(blogId)).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(Blog.class, body)).build());
    }

    public ApiResponse patchBlog(Object blogId, BlogInput payload) {
        return patchBlog(blogId, (Object) payload, null);
    }

    public ApiResponse patchBlog(Object blogId, BlogInput payload, RequestOptions options) {
        return patchBlog(blogId, (Object) payload, options);
    }

    /** Trash one blog — {@code DELETE /admin/blogs/{blog_id}}; its title stays taken. */
    public ApiResponse deleteBlog(Object blogId) {
        return deleteBlog(blogId, null);
    }

    public ApiResponse deleteBlog(Object blogId, RequestOptions options) {
        return client.sendDeleteRequest(BlogsUrls.BLOG,
                RequestOptions.builder(options).pathParams(pathParams(blogId)).dtoFactory(null)
                        .build());
    }

    /** Every comment on one blog, oldest first and unpaged — {@code GET .../{blog_id}/comments}. */
    public ApiResponse listBlogComments(Object blogId) {
        return listBlogComments(blogId, null);
    }

    public ApiResponse listBlogComments(Object blogId, RequestOptions options) {
        return client.sendGetRequest(BlogsUrls.BLOG_COMMENTS,
                RequestOptions.builder(options).pathParams(pathParams(blogId))
                        .dtoFactory(body -> Dto.fromMap(BlogCommentList.class, body)).build());
    }

    /** Add a comment, {@code pending} — {@code POST /admin/blogs/{blog_id}/comments}. */
    public ApiResponse createBlogComment(Object blogId, Object payload) {
        return createBlogComment(blogId, payload, null);
    }

    public ApiResponse createBlogComment(Object blogId, Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPostRequest(BlogsUrls.BLOG_COMMENTS,
                RequestOptions.builder(options).pathParams(pathParams(blogId)).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(BlogCommentEnvelope.class, body)).build());
    }

    public ApiResponse createBlogComment(Object blogId, BlogCommentInput payload) {
        return createBlogComment(blogId, (Object) payload, null);
    }

    public ApiResponse createBlogComment(Object blogId, BlogCommentInput payload,
            RequestOptions options) {
        return createBlogComment(blogId, (Object) payload, options);
    }

    /** One comment — {@code GET .../{blog_comment_id}}; the top-level {@code id} is the blog's. */
    public ApiResponse getBlogComment(Object blogId, Object blogCommentId) {
        return getBlogComment(blogId, blogCommentId, null);
    }

    public ApiResponse getBlogComment(Object blogId, Object blogCommentId,
            RequestOptions options) {
        return client.sendGetRequest(BlogsUrls.BLOG_COMMENT,
                RequestOptions.builder(options).pathParams(pathParams(blogId, blogCommentId))
                        .dtoFactory(body -> Dto.fromMap(BlogCommentEnvelope.class, body)).build());
    }

    /** Approve one comment — {@code POST .../{blog_comment_id}/approve}; {@code spam} is kept. */
    public ApiResponse approveBlogComment(Object blogId, Object blogCommentId) {
        return approveBlogComment(blogId, blogCommentId, null);
    }

    public ApiResponse approveBlogComment(Object blogId, Object blogCommentId,
            RequestOptions options) {
        return client.sendPostRequest(BlogsUrls.BLOG_COMMENT_APPROVE,
                RequestOptions.builder(options).pathParams(pathParams(blogId, blogCommentId))
                        .dtoFactory(body -> Dto.fromMap(BlogCommentEnvelope.class, body)).build());
    }

    /** Mark one comment as spam — {@code POST .../{blog_comment_id}/mark-spam}. */
    public ApiResponse markBlogCommentSpam(Object blogId, Object blogCommentId) {
        return markBlogCommentSpam(blogId, blogCommentId, null);
    }

    public ApiResponse markBlogCommentSpam(Object blogId, Object blogCommentId,
            RequestOptions options) {
        return client.sendPostRequest(BlogsUrls.BLOG_COMMENT_MARK_SPAM,
                RequestOptions.builder(options).pathParams(pathParams(blogId, blogCommentId))
                        .dtoFactory(body -> Dto.fromMap(BlogCommentEnvelope.class, body)).build());
    }

    private static Map<String, Object> pathParams(Object blogId) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("blog_id", blogId);
        return params;
    }

    private static Map<String, Object> pathParams(Object blogId, Object blogCommentId) {
        Map<String, Object> params = pathParams(blogId);
        params.put("blog_comment_id", blogCommentId);
        return params;
    }
}
