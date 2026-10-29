package com.webcommander.resources.variations;

import com.webcommander.Dto;
import com.webcommander.client.ApiResponse;
import com.webcommander.client.BoundResource;
import com.webcommander.client.RequestOptions;
import com.webcommander.client.ResourceBinding;
import com.webcommander.client.WebCommanderClient;
import com.webcommander.resources.variations.variations_dto.VariationsDto.ProductVariations;
import com.webcommander.resources.variations.variations_dto.VariationsDto
        .ProductVariationsBulkInput;
import com.webcommander.resources.variations.variations_dto.VariationsDto
        .ProductVariationsBulkResult;
import com.webcommander.resources.variations.variations_dto.VariationsDto
        .ProductVariationsImportInput;
import com.webcommander.resources.variations.variations_dto.VariationsDto
        .ProductVariationsImportResult;
import com.webcommander.resources.variations.variations_dto.VariationsDto.Variation;
import com.webcommander.resources.variations.variations_dto.VariationsDto.VariationInput;
import com.webcommander.resources.variations.variations_dto.VariationsDto.VariationPage;

import java.util.LinkedHashMap;
import java.util.Map;

/** Variation types and product variants — {@code /api/{api_version}/admin}. */
public final class Variations implements BoundResource {

    private final WebCommanderClient client;

    public Variations(WebCommanderClient client) {
        this.client = client;
    }

    /** List the types by ascending id — {@code GET /admin/variations}. */
    public ApiResponse listVariations() {
        return listVariations(null, null);
    }

    public ApiResponse listVariations(Map<String, Object> queryParams) {
        return listVariations(queryParams, null);
    }

    public ApiResponse listVariations(Map<String, Object> queryParams, RequestOptions options) {
        return client.sendGetRequest(VariationsUrls.VARIATIONS,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(VariationPage.class, body)).build());
    }

    /** Ask whether the collection is reachable — {@code HEAD /admin/variations}; no body. */
    public ApiResponse headVariations() {
        return headVariations(null);
    }

    public ApiResponse headVariations(RequestOptions options) {
        return client.sendApiRequest("HEAD", VariationsUrls.VARIATIONS,
                RequestOptions.builder(options).dtoFactory(null).build());
    }

    /** Create a type — {@code POST /admin/variations}, wrapped in {@code variation}. */
    public ApiResponse createVariation(Object payload) {
        return createVariation(payload, null);
    }

    public ApiResponse createVariation(Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPostRequest(VariationsUrls.VARIATIONS,
                RequestOptions.builder(options).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(Variation.class, body)).build());
    }

    public ApiResponse createVariation(VariationInput payload) {
        return createVariation((Object) payload, null);
    }

    public ApiResponse createVariation(VariationInput payload, RequestOptions options) {
        return createVariation((Object) payload, options);
    }

    /** Retrieve one type — {@code GET /admin/variations/{variation_id}}. */
    public ApiResponse getVariation(Object variationId) {
        return getVariation(variationId, null, null);
    }

    public ApiResponse getVariation(Object variationId, Map<String, Object> queryParams) {
        return getVariation(variationId, queryParams, null);
    }

    public ApiResponse getVariation(Object variationId, Map<String, Object> queryParams,
            RequestOptions options) {
        return client.sendGetRequest(VariationsUrls.VARIATION,
                RequestOptions.builder(options).pathParams(pathParams("variation_id", variationId))
                        .queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(Variation.class, body)).build());
    }

    /** Probe one type — {@code HEAD /admin/variations/{variation_id}}; the row read's ETag. */
    public ApiResponse headVariation(Object variationId) {
        return headVariation(variationId, null);
    }

    public ApiResponse headVariation(Object variationId, RequestOptions options) {
        return client.sendApiRequest("HEAD", VariationsUrls.VARIATION,
                RequestOptions.builder(options).pathParams(pathParams("variation_id", variationId))
                        .dtoFactory(null).build());
    }

    /** Replace one type and its options — {@code PUT /admin/variations/{variation_id}}. */
    public ApiResponse updateVariation(Object variationId, Object payload) {
        return updateVariation(variationId, payload, null);
    }

    public ApiResponse updateVariation(Object variationId, Object payload,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPutRequest(VariationsUrls.VARIATION,
                RequestOptions.builder(options).pathParams(pathParams("variation_id", variationId))
                        .jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(Variation.class, body)).build());
    }

    public ApiResponse updateVariation(Object variationId, VariationInput payload) {
        return updateVariation(variationId, (Object) payload, null);
    }

    public ApiResponse updateVariation(Object variationId, VariationInput payload,
            RequestOptions options) {
        return updateVariation(variationId, (Object) payload, options);
    }

    /** Merge into one type — {@code PATCH /admin/variations/{variation_id}}. */
    public ApiResponse patchVariation(Object variationId, Object payload) {
        return patchVariation(variationId, payload, null);
    }

    public ApiResponse patchVariation(Object variationId, Object payload,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPatchRequest(VariationsUrls.VARIATION,
                RequestOptions.builder(options).pathParams(pathParams("variation_id", variationId))
                        .jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(Variation.class, body)).build());
    }

    public ApiResponse patchVariation(Object variationId, VariationInput payload) {
        return patchVariation(variationId, (Object) payload, null);
    }

    public ApiResponse patchVariation(Object variationId, VariationInput payload,
            RequestOptions options) {
        return patchVariation(variationId, (Object) payload, options);
    }

    /** Delete one type — {@code DELETE /admin/variations/{variation_id}}. */
    public ApiResponse deleteVariation(Object variationId) {
        return deleteVariation(variationId, null, null);
    }

    public ApiResponse deleteVariation(Object variationId, Map<String, Object> queryParams) {
        return deleteVariation(variationId, queryParams, null);
    }

    public ApiResponse deleteVariation(Object variationId, Map<String, Object> queryParams,
            RequestOptions options) {
        return client.sendDeleteRequest(VariationsUrls.VARIATION,
                RequestOptions.builder(options).pathParams(pathParams("variation_id", variationId))
                        .queryParams(queryParams).dtoFactory(null).build());
    }

    /** Delete one option of a type — {@code DELETE .../options/{variation_option_id}}. */
    public ApiResponse deleteVariationOption(Object variationId, Object variationOptionId) {
        return deleteVariationOption(variationId, variationOptionId, null);
    }

    public ApiResponse deleteVariationOption(Object variationId, Object variationOptionId,
            RequestOptions options) {
        Map<String, Object> params = pathParams("variation_id", variationId);
        params.put("variation_option_id", variationOptionId);
        return client.sendDeleteRequest(VariationsUrls.VARIATION_OPTION,
                RequestOptions.builder(options).pathParams(params).dtoFactory(null).build());
    }

    /** One product's variants, grouped — {@code GET /admin/products/{product_id}/variations}. */
    public ApiResponse listProductVariations(Object productId) {
        return listProductVariations(productId, null);
    }

    public ApiResponse listProductVariations(Object productId, RequestOptions options) {
        return client.sendGetRequest(VariationsUrls.PRODUCT_VARIATIONS,
                RequestOptions.builder(options).pathParams(pathParams("product_id", productId))
                        .dtoFactory(body -> Dto.fromMap(ProductVariations.class, body))
                        .build());
    }

    /**
     * Add or change variants by {@code key} — {@code POST .../{product_id}/variations}; 201. A
     * {@code product_key} naming other types than the product's is 409; a null
     * {@code transactionNo} sends no header.
     */
    public ApiResponse importProductVariations(Object productId, Object payload) {
        return importProductVariations(productId, payload, null, null);
    }

    public ApiResponse importProductVariations(Object productId, Object payload,
            String transactionNo) {
        return importProductVariations(productId, payload, transactionNo, null);
    }

    public ApiResponse importProductVariations(Object productId, Object payload,
            RequestOptions options) {
        return importProductVariations(productId, payload, null, options);
    }

    public ApiResponse importProductVariations(Object productId, Object payload,
            String transactionNo, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        RequestOptions given = options == null ? RequestOptions.none() : options;
        Map<String, String> headers = given.getHeaders();
        if (transactionNo != null) {
            headers = new LinkedHashMap<>();
            if (given.getHeaders() != null) {
                headers.putAll(given.getHeaders());
            }
            headers.put(VariationsUrls.TRANSACTION_NO_HEADER, transactionNo);
        }
        return client.sendPostRequest(VariationsUrls.PRODUCT_VARIATIONS,
                RequestOptions.builder(options).pathParams(pathParams("product_id", productId))
                        .jsonBody(payload).headers(headers)
                        .dtoFactory(body -> Dto.fromMap(ProductVariationsImportResult.class, body))
                        .build());
    }

    public ApiResponse importProductVariations(Object productId,
            ProductVariationsImportInput payload) {
        return importProductVariations(productId, (Object) payload, null, null);
    }

    public ApiResponse importProductVariations(Object productId,
            ProductVariationsImportInput payload, String transactionNo) {
        return importProductVariations(productId, (Object) payload, transactionNo, null);
    }

    public ApiResponse importProductVariations(Object productId,
            ProductVariationsImportInput payload, RequestOptions options) {
        return importProductVariations(productId, (Object) payload, null, options);
    }

    public ApiResponse importProductVariations(Object productId,
            ProductVariationsImportInput payload, String transactionNo,
            RequestOptions options) {
        return importProductVariations(productId, (Object) payload, transactionNo, options);
    }

    /** Set fields on several variants — {@code PATCH .../{product_id}/variations/bulk}. */
    public ApiResponse updateProductVariations(Object productId, Object payload) {
        return updateProductVariations(productId, payload, null, null);
    }

    public ApiResponse updateProductVariations(Object productId, Object payload,
            Map<String, Object> queryParams) {
        return updateProductVariations(productId, payload, queryParams, null);
    }

    public ApiResponse updateProductVariations(Object productId, Object payload,
            Map<String, Object> queryParams, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPatchRequest(VariationsUrls.PRODUCT_VARIATIONS_BULK,
                RequestOptions.builder(options).pathParams(pathParams("product_id", productId))
                        .queryParams(queryParams).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(ProductVariationsBulkResult.class, body))
                        .build());
    }

    public ApiResponse updateProductVariations(Object productId,
            ProductVariationsBulkInput payload) {
        return updateProductVariations(productId, (Object) payload, null, null);
    }

    public ApiResponse updateProductVariations(Object productId,
            ProductVariationsBulkInput payload, Map<String, Object> queryParams,
            RequestOptions options) {
        return updateProductVariations(productId, (Object) payload, queryParams, options);
    }

    private static Map<String, Object> pathParams(String name, Object value) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put(name, value);
        return params;
    }
}
