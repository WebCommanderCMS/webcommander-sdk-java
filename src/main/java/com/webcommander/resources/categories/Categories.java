package com.webcommander.resources.categories;

import com.webcommander.Dto;
import com.webcommander.client.ApiResponse;
import com.webcommander.client.BoundResource;
import com.webcommander.client.RequestOptions;
import com.webcommander.client.ResourceBinding;
import com.webcommander.client.WebCommanderClient;
import com.webcommander.resources.categories.categories_dto.CategoriesDto.Category;
import com.webcommander.resources.categories.categories_dto.CategoriesDto.CategoryCount;
import com.webcommander.resources.categories.categories_dto.CategoriesDto.CategoryInput;
import com.webcommander.resources.categories.categories_dto.CategoriesDto.CategoryPage;

import java.util.LinkedHashMap;
import java.util.Map;

/** Product categories — {@code /api/{api_version}/admin/categories}. */
public final class Categories implements BoundResource {

    private final WebCommanderClient client;

    public Categories(WebCommanderClient client) {
        this.client = client;
    }

    /** List the categories — {@code GET /admin/categories}, newest update first, 20 at most. */
    public ApiResponse listCategories() {
        return listCategories(null, null);
    }

    public ApiResponse listCategories(Map<String, Object> queryParams) {
        return listCategories(queryParams, null);
    }

    public ApiResponse listCategories(Map<String, Object> queryParams, RequestOptions options) {
        return client.sendGetRequest(CategoriesUrls.CATEGORIES,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(CategoryPage.class, body)).build());
    }

    /** Count the categories — {@code GET /admin/categories/count}; the trash is not counted. */
    public ApiResponse countCategories() {
        return countCategories(null, null);
    }

    public ApiResponse countCategories(Map<String, Object> queryParams) {
        return countCategories(queryParams, null);
    }

    public ApiResponse countCategories(Map<String, Object> queryParams, RequestOptions options) {
        return client.sendGetRequest(CategoriesUrls.CATEGORIES_COUNT,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(CategoryCount.class, body)).build());
    }

    /** Ask whether a name is free — {@code GET /admin/categories/check-name}; read available. */
    public ApiResponse checkCategoryName(String name) {
        return checkCategoryName(name, null);
    }

    public ApiResponse checkCategoryName(String name, RequestOptions options) {
        Map<String, Object> query = new LinkedHashMap<>();
        query.put("name", name);
        return client.sendGetRequest(CategoriesUrls.CATEGORIES_CHECK_NAME,
                RequestOptions.builder(options).queryParams(query).dtoFactory(null).build());
    }

    /** Create a category — {@code POST /admin/categories}, wrapped in category; answered 201. */
    public ApiResponse createCategory(Object payload) {
        return createCategory(payload, null, null);
    }

    public ApiResponse createCategory(Object payload, RequestOptions options) {
        return createCategory(payload, null, options);
    }

    public ApiResponse createCategory(Object payload, String transactionNo) {
        return createCategory(payload, transactionNo, null);
    }

    public ApiResponse createCategory(Object payload, String transactionNo,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        RequestOptions given = options == null ? RequestOptions.none() : options;
        Map<String, String> headers = new LinkedHashMap<>();
        if (given.getHeaders() != null) {
            headers.putAll(given.getHeaders());
        }
        if (transactionNo != null) {
            headers.put(CategoriesUrls.TRANSACTION_NO_HEADER, transactionNo);
        }
        return client.sendPostRequest(CategoriesUrls.CATEGORIES,
                RequestOptions.builder(options).jsonBody(payload).headers(headers)
                        .dtoFactory(body -> Dto.fromMap(Category.class, body)).build());
    }

    public ApiResponse createCategory(CategoryInput payload) {
        return createCategory((Object) payload, null, null);
    }

    public ApiResponse createCategory(CategoryInput payload, RequestOptions options) {
        return createCategory((Object) payload, null, options);
    }

    public ApiResponse createCategory(CategoryInput payload, String transactionNo) {
        return createCategory((Object) payload, transactionNo, null);
    }

    public ApiResponse createCategory(CategoryInput payload, String transactionNo,
            RequestOptions options) {
        return createCategory((Object) payload, transactionNo, options);
    }

    /** Retrieve one category — {@code GET /admin/categories/{category_id}}, trashed or not. */
    public ApiResponse getCategory(Object categoryId) {
        return getCategory(categoryId, null, null);
    }

    public ApiResponse getCategory(Object categoryId, Map<String, Object> queryParams) {
        return getCategory(categoryId, queryParams, null);
    }

    public ApiResponse getCategory(Object categoryId, Map<String, Object> queryParams,
            RequestOptions options) {
        return client.sendGetRequest(CategoriesUrls.CATEGORY,
                RequestOptions.builder(options).pathParams(pathParams(categoryId))
                        .queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(Category.class, body)).build());
    }

    /** Replace one category — {@code PUT /admin/categories/{category_id}}. */
    public ApiResponse updateCategory(Object categoryId, Object payload) {
        return updateCategory(categoryId, payload, null);
    }

    public ApiResponse updateCategory(Object categoryId, Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPutRequest(CategoriesUrls.CATEGORY,
                RequestOptions.builder(options).pathParams(pathParams(categoryId)).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(Category.class, body)).build());
    }

    public ApiResponse updateCategory(Object categoryId, CategoryInput payload) {
        return updateCategory(categoryId, (Object) payload, null);
    }

    public ApiResponse updateCategory(Object categoryId, CategoryInput payload,
            RequestOptions options) {
        return updateCategory(categoryId, (Object) payload, options);
    }

    /** Merge into one category — {@code PATCH /admin/categories/{category_id}}, any sections. */
    public ApiResponse patchCategory(Object categoryId, Object payload) {
        return patchCategory(categoryId, payload, null);
    }

    public ApiResponse patchCategory(Object categoryId, Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPatchRequest(CategoriesUrls.CATEGORY,
                RequestOptions.builder(options).pathParams(pathParams(categoryId)).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(Category.class, body)).build());
    }

    public ApiResponse patchCategory(Object categoryId, CategoryInput payload) {
        return patchCategory(categoryId, (Object) payload, null);
    }

    public ApiResponse patchCategory(Object categoryId, CategoryInput payload,
            RequestOptions options) {
        return patchCategory(categoryId, (Object) payload, options);
    }

    /** Move one category to the trash — {@code DELETE /admin/categories/{category_id}}; 204. */
    public ApiResponse deleteCategory(Object categoryId) {
        return deleteCategory(categoryId, null);
    }

    public ApiResponse deleteCategory(Object categoryId, RequestOptions options) {
        return client.sendDeleteRequest(CategoriesUrls.CATEGORY,
                RequestOptions.builder(options).pathParams(pathParams(categoryId)).dtoFactory(null)
                        .build());
    }

    /** Read one section — {@code GET .../{category_id}/{section}}, answered inside category. */
    public ApiResponse getCategorySection(Object categoryId, String section) {
        return getCategorySection(categoryId, section, null);
    }

    public ApiResponse getCategorySection(Object categoryId, String section,
            RequestOptions options) {
        return client.sendGetRequest(CategoriesUrls.CATEGORY_SECTION,
                RequestOptions.builder(options).pathParams(pathParams(categoryId, section))
                        .dtoFactory(body -> Dto.fromMap(Category.class, body)).build());
    }

    /** Merge into one section — {@code PATCH .../{category_id}/{section}}, inside category. */
    public ApiResponse patchCategorySection(Object categoryId, String section, Object payload) {
        return patchCategorySection(categoryId, section, payload, null);
    }

    public ApiResponse patchCategorySection(Object categoryId, String section, Object payload,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPatchRequest(CategoriesUrls.CATEGORY_SECTION,
                RequestOptions.builder(options).pathParams(pathParams(categoryId, section))
                        .jsonBody(payload).dtoFactory(body -> Dto.fromMap(Category.class, body))
                        .build());
    }

    public ApiResponse patchCategorySection(Object categoryId, String section,
            CategoryInput payload) {
        return patchCategorySection(categoryId, section, (Object) payload, null);
    }

    public ApiResponse patchCategorySection(Object categoryId, String section,
            CategoryInput payload, RequestOptions options) {
        return patchCategorySection(categoryId, section, (Object) payload, options);
    }

    private static Map<String, Object> pathParams(Object categoryId) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("category_id", categoryId);
        return params;
    }

    private static Map<String, Object> pathParams(Object categoryId, String section) {
        Map<String, Object> params = pathParams(categoryId);
        params.put("section", section);
        return params;
    }
}
