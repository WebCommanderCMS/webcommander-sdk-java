package com.webcommander.resources.settings.page_settings;

import com.webcommander.Dto;
import com.webcommander.client.ApiResponse;
import com.webcommander.client.BoundResource;
import com.webcommander.client.RequestOptions;
import com.webcommander.client.ResourceBinding;
import com.webcommander.client.WebCommanderClient;
import com.webcommander.resources.settings.page_settings.product_page_dto.ProductPageDto;

import java.util.LinkedHashMap;
import java.util.Map;

/** Product page settings — {@code .../page_settings/product_page}. */
public final class ProductPage implements BoundResource {

    private final WebCommanderClient client;

    public ProductPage(WebCommanderClient client) {
        this.client = client;
    }

    /** Read the whole area — {@code GET /admin/settings/page_settings/product_page}. */
    public ApiResponse getProductPage() {
        return getProductPage(null, null);
    }

    public ApiResponse getProductPage(Map<String, Object> queryParams) {
        return getProductPage(queryParams, null);
    }

    public ApiResponse getProductPage(Map<String, Object> queryParams, RequestOptions options) {
        return client.sendGetRequest(ProductPageUrls.PRODUCT_PAGE,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(
                                ProductPageDto.ProductPage.class, body)).build());
    }

    /** Write fields in any section — {@code PATCH /admin/settings/page_settings/product_page}. */
    public ApiResponse patchProductPage(Object payload) {
        return patchProductPage(payload, null);
    }

    public ApiResponse patchProductPage(Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPatchRequest(ProductPageUrls.PRODUCT_PAGE,
                RequestOptions.builder(options).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(
                                ProductPageDto.ProductPage.class, body)).build());
    }

    /** Ask whether the area is reachable — {@code HEAD .../page_settings/product_page}. */
    public ApiResponse headProductPage() {
        return headProductPage(null);
    }

    public ApiResponse headProductPage(RequestOptions options) {
        return client.sendApiRequest("HEAD", ProductPageUrls.PRODUCT_PAGE,
                RequestOptions.builder(options).dtoFactory(null).build());
    }

    /** Read one section — {@code GET .../product_page/{section}}. */
    public ApiResponse getProductPageSection(String section) {
        return getProductPageSection(section, null, null);
    }

    public ApiResponse getProductPageSection(String section, Map<String, Object> queryParams) {
        return getProductPageSection(section, queryParams, null);
    }

    public ApiResponse getProductPageSection(String section, Map<String, Object> queryParams,
            RequestOptions options) {
        return client.sendGetRequest(ProductPageUrls.PRODUCT_PAGE_SECTION,
                RequestOptions.builder(options).pathParams(pathParams("section", section))
                        .queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(
                                ProductPageDto.ProductPage.class, body)).build());
    }

    /** Write one section — {@code PATCH .../product_page/{section}}, under either key. */
    public ApiResponse patchProductPageSection(String section, Object payload) {
        return patchProductPageSection(section, payload, null);
    }

    public ApiResponse patchProductPageSection(String section, Object payload,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPatchRequest(ProductPageUrls.PRODUCT_PAGE_SECTION,
                RequestOptions.builder(options).pathParams(pathParams("section", section))
                        .jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(ProductPageDto.ProductPage.class, body))
                        .build());
    }

    private static Map<String, Object> pathParams(String name, Object value) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put(name, value);
        return params;
    }
}
