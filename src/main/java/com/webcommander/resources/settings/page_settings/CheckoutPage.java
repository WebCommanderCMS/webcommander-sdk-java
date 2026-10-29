package com.webcommander.resources.settings.page_settings;

import com.webcommander.Dto;
import com.webcommander.client.ApiResponse;
import com.webcommander.client.BoundResource;
import com.webcommander.client.RequestOptions;
import com.webcommander.client.ResourceBinding;
import com.webcommander.client.WebCommanderClient;
import com.webcommander.resources.settings.page_settings.checkout_page_dto.CheckoutPageDto;

import java.util.LinkedHashMap;
import java.util.Map;

/** Checkout page settings — {@code .../page_settings/checkout_page}. */
public final class CheckoutPage implements BoundResource {

    private final WebCommanderClient client;

    public CheckoutPage(WebCommanderClient client) {
        this.client = client;
    }

    /** Read the whole area — {@code GET /admin/settings/page_settings/checkout_page}. */
    public ApiResponse getCheckoutPage() {
        return getCheckoutPage(null, null);
    }

    public ApiResponse getCheckoutPage(Map<String, Object> queryParams) {
        return getCheckoutPage(queryParams, null);
    }

    public ApiResponse getCheckoutPage(Map<String, Object> queryParams, RequestOptions options) {
        return client.sendGetRequest(CheckoutPageUrls.CHECKOUT_PAGE,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(
                                CheckoutPageDto.CheckoutPage.class, body)).build());
    }

    /** Write fields in any writable section — {@code PATCH .../page_settings/checkout_page}. */
    public ApiResponse patchCheckoutPage(Object payload) {
        return patchCheckoutPage(payload, null);
    }

    public ApiResponse patchCheckoutPage(Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPatchRequest(CheckoutPageUrls.CHECKOUT_PAGE,
                RequestOptions.builder(options).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(
                                CheckoutPageDto.CheckoutPage.class, body)).build());
    }

    /** Ask whether the area is reachable — {@code HEAD .../page_settings/checkout_page}. */
    public ApiResponse headCheckoutPage() {
        return headCheckoutPage(null);
    }

    public ApiResponse headCheckoutPage(RequestOptions options) {
        return client.sendApiRequest("HEAD", CheckoutPageUrls.CHECKOUT_PAGE,
                RequestOptions.builder(options).dtoFactory(null).build());
    }

    /** Read one section — {@code GET .../checkout_page/{section}}. */
    public ApiResponse getCheckoutPageSection(String section) {
        return getCheckoutPageSection(section, null, null);
    }

    public ApiResponse getCheckoutPageSection(String section, Map<String, Object> queryParams) {
        return getCheckoutPageSection(section, queryParams, null);
    }

    public ApiResponse getCheckoutPageSection(String section, Map<String, Object> queryParams,
            RequestOptions options) {
        return client.sendGetRequest(CheckoutPageUrls.CHECKOUT_PAGE_SECTION,
                RequestOptions.builder(options).pathParams(pathParams("section", section))
                        .queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(
                                CheckoutPageDto.CheckoutPage.class, body)).build());
    }

    /** Write one section — {@code PATCH .../checkout_page/{section}}, wrapped in its key. */
    public ApiResponse patchCheckoutPageSection(String section, Object payload) {
        return patchCheckoutPageSection(section, payload, null);
    }

    public ApiResponse patchCheckoutPageSection(String section, Object payload,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPatchRequest(CheckoutPageUrls.CHECKOUT_PAGE_SECTION,
                RequestOptions.builder(options).pathParams(pathParams("section", section))
                        .jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(CheckoutPageDto.CheckoutPage.class, body))
                        .build());
    }

    private static Map<String, Object> pathParams(String name, Object value) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put(name, value);
        return params;
    }
}
