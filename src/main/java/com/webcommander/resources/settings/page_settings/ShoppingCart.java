package com.webcommander.resources.settings.page_settings;

import com.webcommander.Dto;
import com.webcommander.client.ApiResponse;
import com.webcommander.client.BoundResource;
import com.webcommander.client.RequestOptions;
import com.webcommander.client.ResourceBinding;
import com.webcommander.client.WebCommanderClient;
import com.webcommander.resources.settings.page_settings.shopping_cart_dto.ShoppingCartDto;

import java.util.LinkedHashMap;
import java.util.Map;

/** Shopping cart settings — {@code .../page_settings/shopping_cart}. */
public final class ShoppingCart implements BoundResource {

    private final WebCommanderClient client;

    public ShoppingCart(WebCommanderClient client) {
        this.client = client;
    }

    /** Read the whole area — {@code GET /admin/settings/page_settings/shopping_cart}. */
    public ApiResponse getShoppingCart() {
        return getShoppingCart(null, null);
    }

    public ApiResponse getShoppingCart(Map<String, Object> queryParams) {
        return getShoppingCart(queryParams, null);
    }

    public ApiResponse getShoppingCart(Map<String, Object> queryParams, RequestOptions options) {
        return client.sendGetRequest(ShoppingCartUrls.SHOPPING_CART,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(
                                ShoppingCartDto.ShoppingCart.class, body)).build());
    }

    /** Write fields in any section — {@code PATCH /admin/settings/page_settings/shopping_cart}. */
    public ApiResponse patchShoppingCart(Object payload) {
        return patchShoppingCart(payload, null);
    }

    public ApiResponse patchShoppingCart(Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPatchRequest(ShoppingCartUrls.SHOPPING_CART,
                RequestOptions.builder(options).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(
                                ShoppingCartDto.ShoppingCart.class, body)).build());
    }

    /** Ask whether the area is reachable — {@code HEAD .../page_settings/shopping_cart}. */
    public ApiResponse headShoppingCart() {
        return headShoppingCart(null);
    }

    public ApiResponse headShoppingCart(RequestOptions options) {
        return client.sendApiRequest("HEAD", ShoppingCartUrls.SHOPPING_CART,
                RequestOptions.builder(options).dtoFactory(null).build());
    }

    /** Read one section — {@code GET .../shopping_cart/{section}}. */
    public ApiResponse getShoppingCartSection(String section) {
        return getShoppingCartSection(section, null, null);
    }

    public ApiResponse getShoppingCartSection(String section, Map<String, Object> queryParams) {
        return getShoppingCartSection(section, queryParams, null);
    }

    public ApiResponse getShoppingCartSection(String section, Map<String, Object> queryParams,
            RequestOptions options) {
        return client.sendGetRequest(ShoppingCartUrls.SHOPPING_CART_SECTION,
                RequestOptions.builder(options).pathParams(pathParams("section", section))
                        .queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(
                                ShoppingCartDto.ShoppingCart.class, body)).build());
    }

    /** Write one section — {@code PATCH .../shopping_cart/{section}}, wrapped in its key. */
    public ApiResponse patchShoppingCartSection(String section, Object payload) {
        return patchShoppingCartSection(section, payload, null);
    }

    public ApiResponse patchShoppingCartSection(String section, Object payload,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPatchRequest(ShoppingCartUrls.SHOPPING_CART_SECTION,
                RequestOptions.builder(options).pathParams(pathParams("section", section))
                        .jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(ShoppingCartDto.ShoppingCart.class, body))
                        .build());
    }

    private static Map<String, Object> pathParams(String name, Object value) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put(name, value);
        return params;
    }
}
