package com.webcommander.resources.settings.page_settings;

import com.webcommander.Dto;
import com.webcommander.client.ApiResponse;
import com.webcommander.client.BoundResource;
import com.webcommander.client.RequestOptions;
import com.webcommander.client.ResourceBinding;
import com.webcommander.client.WebCommanderClient;
import com.webcommander.resources.settings.page_settings.category_page_dto.CategoryPageDto;

import java.util.LinkedHashMap;
import java.util.Map;

/** Category page settings — {@code .../page_settings/category_page}. */
public final class CategoryPage implements BoundResource {

    private final WebCommanderClient client;

    public CategoryPage(WebCommanderClient client) {
        this.client = client;
    }

    /** Read the whole area — {@code GET /admin/settings/page_settings/category_page}. */
    public ApiResponse getCategoryPage() {
        return getCategoryPage(null, null);
    }

    public ApiResponse getCategoryPage(Map<String, Object> queryParams) {
        return getCategoryPage(queryParams, null);
    }

    public ApiResponse getCategoryPage(Map<String, Object> queryParams, RequestOptions options) {
        return client.sendGetRequest(CategoryPageUrls.CATEGORY_PAGE,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(
                                CategoryPageDto.CategoryPage.class, body)).build());
    }

    /** Write fields in any section — {@code PATCH /admin/settings/page_settings/category_page}. */
    public ApiResponse patchCategoryPage(Object payload) {
        return patchCategoryPage(payload, null);
    }

    public ApiResponse patchCategoryPage(Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPatchRequest(CategoryPageUrls.CATEGORY_PAGE,
                RequestOptions.builder(options).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(
                                CategoryPageDto.CategoryPage.class, body)).build());
    }

    /** Ask whether the area is reachable — {@code HEAD .../page_settings/category_page}. */
    public ApiResponse headCategoryPage() {
        return headCategoryPage(null);
    }

    public ApiResponse headCategoryPage(RequestOptions options) {
        return client.sendApiRequest("HEAD", CategoryPageUrls.CATEGORY_PAGE,
                RequestOptions.builder(options).dtoFactory(null).build());
    }

    /** Read one section — {@code GET .../category_page/{section}}. */
    public ApiResponse getCategoryPageSection(String section) {
        return getCategoryPageSection(section, null, null);
    }

    public ApiResponse getCategoryPageSection(String section, Map<String, Object> queryParams) {
        return getCategoryPageSection(section, queryParams, null);
    }

    public ApiResponse getCategoryPageSection(String section, Map<String, Object> queryParams,
            RequestOptions options) {
        return client.sendGetRequest(CategoryPageUrls.CATEGORY_PAGE_SECTION,
                RequestOptions.builder(options).pathParams(pathParams("section", section))
                        .queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(
                                CategoryPageDto.CategoryPage.class, body)).build());
    }

    /** Write one section — {@code PATCH .../category_page/{section}}, wrapped in its key. */
    public ApiResponse patchCategoryPageSection(String section, Object payload) {
        return patchCategoryPageSection(section, payload, null);
    }

    public ApiResponse patchCategoryPageSection(String section, Object payload,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPatchRequest(CategoryPageUrls.CATEGORY_PAGE_SECTION,
                RequestOptions.builder(options).pathParams(pathParams("section", section))
                        .jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(CategoryPageDto.CategoryPage.class, body))
                        .build());
    }

    private static Map<String, Object> pathParams(String name, Object value) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put(name, value);
        return params;
    }
}
