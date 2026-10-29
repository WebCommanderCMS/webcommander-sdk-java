package com.webcommander.resources.settings.page_settings;

import com.webcommander.Dto;
import com.webcommander.client.ApiResponse;
import com.webcommander.client.BoundResource;
import com.webcommander.client.RequestOptions;
import com.webcommander.client.ResourceBinding;
import com.webcommander.client.WebCommanderClient;
import com.webcommander.resources.customers.admin_customers_dto.AdminCustomersDto.Page;
import com.webcommander.resources.settings.page_settings.search_result_page_dto
        .SearchResultPageDto;

import java.util.LinkedHashMap;
import java.util.Map;

/** Search result page settings — {@code .../page_settings/search_result_page}. */
public final class SearchResultPage implements BoundResource {

    private final WebCommanderClient client;

    public SearchResultPage(WebCommanderClient client) {
        this.client = client;
    }

    /** Read the whole area — {@code GET /admin/settings/page_settings/search_result_page}. */
    public ApiResponse getSearchResultPage() {
        return getSearchResultPage(null, null);
    }

    public ApiResponse getSearchResultPage(Map<String, Object> queryParams) {
        return getSearchResultPage(queryParams, null);
    }

    public ApiResponse getSearchResultPage(Map<String, Object> queryParams,
            RequestOptions options) {
        return client.sendGetRequest(SearchResultPageUrls.SEARCH_RESULT_PAGE,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(
                                SearchResultPageDto.SearchResultPage.class, body)).build());
    }

    /** Write fields — {@code PATCH /admin/settings/page_settings/search_result_page}. */
    public ApiResponse patchSearchResultPage(Object payload) {
        return patchSearchResultPage(payload, null);
    }

    public ApiResponse patchSearchResultPage(Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPatchRequest(SearchResultPageUrls.SEARCH_RESULT_PAGE,
                RequestOptions.builder(options).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(
                                SearchResultPageDto.SearchResultPage.class, body)).build());
    }

    /** Ask whether the area is reachable — {@code HEAD .../page_settings/search_result_page}. */
    public ApiResponse headSearchResultPage() {
        return headSearchResultPage(null);
    }

    public ApiResponse headSearchResultPage(RequestOptions options) {
        return client.sendApiRequest("HEAD", SearchResultPageUrls.SEARCH_RESULT_PAGE,
                RequestOptions.builder(options).dtoFactory(null).build());
    }

    /** Read one section — {@code GET .../search_result_page/{section}}. */
    public ApiResponse getSearchResultPageSection(String section) {
        return getSearchResultPageSection(section, null, null);
    }

    public ApiResponse getSearchResultPageSection(String section,
            Map<String, Object> queryParams) {
        return getSearchResultPageSection(section, queryParams, null);
    }

    public ApiResponse getSearchResultPageSection(String section, Map<String, Object> queryParams,
            RequestOptions options) {
        return client.sendGetRequest(SearchResultPageUrls.SEARCH_RESULT_PAGE_SECTION,
                RequestOptions.builder(options).pathParams(pathParams("section", section))
                        .queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(
                                SearchResultPageDto.SearchResultPage.class, body)).build());
    }

    /** Write one section — {@code PATCH .../search_result_page/{section}}, wrapped in its key. */
    public ApiResponse patchSearchResultPageSection(String section, Object payload) {
        return patchSearchResultPageSection(section, payload, null);
    }

    public ApiResponse patchSearchResultPageSection(String section, Object payload,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPatchRequest(SearchResultPageUrls.SEARCH_RESULT_PAGE_SECTION,
                RequestOptions.builder(options).pathParams(pathParams("section", section))
                        .jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(
                                SearchResultPageDto.SearchResultPage.class, body)).build());
    }

    /** List the promotions — {@code GET .../search_result_page/product_promotion}. */
    public ApiResponse getSearchResultPageProductPromotions() {
        return getSearchResultPageProductPromotions(null, null);
    }

    public ApiResponse getSearchResultPageProductPromotions(Map<String, Object> queryParams) {
        return getSearchResultPageProductPromotions(queryParams, null);
    }

    public ApiResponse getSearchResultPageProductPromotions(Map<String, Object> queryParams,
            RequestOptions options) {
        return client.sendGetRequest(SearchResultPageUrls.SEARCH_RESULT_PAGE_PRODUCT_PROMOTIONS,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(Page.of(SearchResultPageDto.ProductPromotion.class)).build());
    }

    /** Ask whether the promotions are reachable — {@code HEAD .../product_promotion}; no body. */
    public ApiResponse headSearchResultPageProductPromotions() {
        return headSearchResultPageProductPromotions(null);
    }

    public ApiResponse headSearchResultPageProductPromotions(RequestOptions options) {
        return client.sendApiRequest("HEAD",
                SearchResultPageUrls.SEARCH_RESULT_PAGE_PRODUCT_PROMOTIONS,
                RequestOptions.builder(options).dtoFactory(null).build());
    }

    /** Add a promotion — {@code POST .../search_result_page/product_promotion}. */
    public ApiResponse createSearchResultPageProductPromotion(Object payload) {
        return createSearchResultPageProductPromotion(payload, null);
    }

    public ApiResponse createSearchResultPageProductPromotion(Object payload,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPostRequest(SearchResultPageUrls.SEARCH_RESULT_PAGE_PRODUCT_PROMOTIONS,
                RequestOptions.builder(options).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(
                                SearchResultPageDto.ProductPromotion.class, body)).build());
    }

    /** Read one promotion — {@code GET .../product_promotion/{promotion_id}}. */
    public ApiResponse getSearchResultPageProductPromotion(Object promotionId) {
        return getSearchResultPageProductPromotion(promotionId, null);
    }

    public ApiResponse getSearchResultPageProductPromotion(Object promotionId,
            RequestOptions options) {
        return client.sendGetRequest(SearchResultPageUrls.SEARCH_RESULT_PAGE_PRODUCT_PROMOTION,
                RequestOptions.builder(options).pathParams(pathParams("promotion_id", promotionId))
                        .dtoFactory(body -> Dto.fromMap(
                                SearchResultPageDto.ProductPromotion.class, body)).build());
    }

    /**
     * Ask whether one promotion is there — {@code HEAD .../product_promotion/{promotion_id}}; no
     * body, an {@code ETag}, and {@code 404} for an unknown id.
     */
    public ApiResponse headSearchResultPageProductPromotion(Object promotionId) {
        return headSearchResultPageProductPromotion(promotionId, null);
    }

    public ApiResponse headSearchResultPageProductPromotion(Object promotionId,
            RequestOptions options) {
        return client.sendApiRequest("HEAD",
                SearchResultPageUrls.SEARCH_RESULT_PAGE_PRODUCT_PROMOTION,
                RequestOptions.builder(options).pathParams(pathParams("promotion_id", promotionId))
                        .dtoFactory(null).build());
    }

    /** Change one promotion — {@code PATCH .../product_promotion/{promotion_id}}. */
    public ApiResponse patchSearchResultPageProductPromotion(Object promotionId, Object payload) {
        return patchSearchResultPageProductPromotion(promotionId, payload, null);
    }

    public ApiResponse patchSearchResultPageProductPromotion(Object promotionId, Object payload,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPatchRequest(SearchResultPageUrls.SEARCH_RESULT_PAGE_PRODUCT_PROMOTION,
                RequestOptions.builder(options).pathParams(pathParams("promotion_id", promotionId))
                        .jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(
                                SearchResultPageDto.ProductPromotion.class, body)).build());
    }

    /** Turn one promotion on or off — {@code PATCH .../{promotion_id}/active}. */
    public ApiResponse patchSearchResultPageProductPromotionActive(Object promotionId,
            Object payload) {
        return patchSearchResultPageProductPromotionActive(promotionId, payload, null);
    }

    public ApiResponse patchSearchResultPageProductPromotionActive(Object promotionId,
            Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPatchRequest(
                SearchResultPageUrls.SEARCH_RESULT_PAGE_PRODUCT_PROMOTION_ACTIVE,
                RequestOptions.builder(options).pathParams(pathParams("promotion_id", promotionId))
                        .jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(
                                SearchResultPageDto.ProductPromotion.class, body)).build());
    }

    /** Remove one promotion — {@code DELETE .../product_promotion/{promotion_id}}. */
    public ApiResponse deleteSearchResultPageProductPromotion(Object promotionId) {
        return deleteSearchResultPageProductPromotion(promotionId, null);
    }

    public ApiResponse deleteSearchResultPageProductPromotion(Object promotionId,
            RequestOptions options) {
        return client.sendDeleteRequest(SearchResultPageUrls.SEARCH_RESULT_PAGE_PRODUCT_PROMOTION,
                RequestOptions.builder(options).pathParams(pathParams("promotion_id", promotionId))
                        .dtoFactory(null).build());
    }

    private static Map<String, Object> pathParams(String name, Object value) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put(name, value);
        return params;
    }
}
