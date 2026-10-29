package com.webcommander.resources.settings.product_listing;

import com.webcommander.Dto;
import com.webcommander.client.ApiResponse;
import com.webcommander.client.BoundResource;
import com.webcommander.client.RequestOptions;
import com.webcommander.client.ResourceBinding;
import com.webcommander.client.WebCommanderClient;
import com.webcommander.resources.settings.product_listing.product_listing_dto
        .ProductListingDto;

import java.util.Map;

/** Product listing settings — {@code /api/{api_version}/admin/settings/product_listing}. */
public final class ProductListing implements BoundResource {

    private final WebCommanderClient client;

    public ProductListing(WebCommanderClient client) {
        this.client = client;
    }

    /** Read the whole area — {@code GET /admin/settings/product_listing}. */
    public ApiResponse getProductListing() {
        return getProductListing(null, null);
    }

    public ApiResponse getProductListing(Map<String, Object> queryParams) {
        return getProductListing(queryParams, null);
    }

    public ApiResponse getProductListing(Map<String, Object> queryParams, RequestOptions options) {
        return client.sendGetRequest(ProductListingUrls.PRODUCT_LISTING,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(
                                ProductListingDto.ProductListing.class, body)).build());
    }

    /** Write fields — {@code PATCH /admin/settings/product_listing}, in its facade wrapper. */
    public ApiResponse patchProductListing(Object payload) {
        return patchProductListing(payload, null);
    }

    public ApiResponse patchProductListing(Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPatchRequest(ProductListingUrls.PRODUCT_LISTING,
                RequestOptions.builder(options).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(
                                ProductListingDto.ProductListing.class, body)).build());
    }

    /** Ask whether the area is reachable — {@code HEAD /admin/settings/product_listing}. */
    public ApiResponse headProductListing() {
        return headProductListing(null);
    }

    public ApiResponse headProductListing(RequestOptions options) {
        return client.sendApiRequest("HEAD", ProductListingUrls.PRODUCT_LISTING,
                RequestOptions.builder(options).dtoFactory(null).build());
    }
}
