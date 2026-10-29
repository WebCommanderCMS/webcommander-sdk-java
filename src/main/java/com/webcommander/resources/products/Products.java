package com.webcommander.resources.products;

import com.webcommander.Dto;
import com.webcommander.client.ApiResponse;
import com.webcommander.client.BoundResource;
import com.webcommander.client.RequestOptions;
import com.webcommander.client.ResourceBinding;
import com.webcommander.client.WebCommanderClient;
import com.webcommander.resources.products.products_dto.ProductsDto.Product;
import com.webcommander.resources.products.products_dto.ProductsDto.ProductInput;
import com.webcommander.resources.products.products_dto.ProductsDto.ProductPage;
import com.webcommander.resources.products.products_dto.ProductsDto.ProductReview;
import com.webcommander.resources.products.products_dto.ProductsDto.ProductReviews;
import com.webcommander.resources.products.products_dto.ProductsDto
        .ProductReviewsImportInput;
import com.webcommander.resources.products.products_dto.ProductsDto.ProductStock;
import com.webcommander.resources.products.products_dto.ProductsDto.ProductStockInput;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Products — {@code /api/{api_version}/admin/products}, a row, its reviews and its stock. */
public final class Products implements BoundResource {

    private final WebCommanderClient client;

    public Products(WebCommanderClient client) {
        this.client = client;
    }

    /**
     * List products, the most recently updated first — {@code GET /admin/products}. Each row is
     * the full record the row read answers, and a trashed product is not listed. {@code limit} is
     * 1 to 100 (default 20, above 100 is {@code 400}), with {@code offset} or a 1-based
     * {@code page}. {@code search_text} matches {@code name} or {@code sku}; {@code name},
     * {@code sku}, {@code ids} (repeat the name for more; a comma list is {@code 500}),
     * {@code parent} (a category id), {@code product_type}, {@code stock} ({@code in},
     * {@code out} or {@code low}), {@code is_available}, {@code is_inventory_enabled},
     * {@code created_by}, {@code price_from}, {@code price_to}, {@code created_from},
     * {@code created_to}, {@code updated_from} and {@code updated_to} narrow it. {@code sort}
     * takes {@code name}, {@code isAvailable}, {@code sku}, {@code parent}, {@code basePrice},
     * {@code isInventoryEnabled}, {@code createdBy}, {@code created}, {@code updated} or
     * {@code id}, with {@code dir}; {@code field_metadata} adds a schema. Any other name is
     * {@code 400}.
     */
    public ApiResponse listProducts() {
        return listProducts(null, null);
    }

    public ApiResponse listProducts(Map<String, Object> queryParams) {
        return listProducts(queryParams, null);
    }

    public ApiResponse listProducts(Map<String, Object> queryParams, RequestOptions options) {
        return client.sendGetRequest(ProductsUrls.PRODUCTS,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(ProductPage.class, body)).build());
    }

    /**
     * Create a product — {@code POST /admin/products}, {@code 201}. A bare record, {@code name}
     * and {@code base_price} required; a null {@code transactionNo} sends no header.
     */
    public ApiResponse createProduct(Object payload) {
        return createProduct(payload, null, null);
    }

    public ApiResponse createProduct(Object payload, String transactionNo) {
        return createProduct(payload, transactionNo, null);
    }

    public ApiResponse createProduct(Object payload, RequestOptions options) {
        return createProduct(payload, null, options);
    }

    public ApiResponse createProduct(Object payload, String transactionNo,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        Map<String, String> headers = withTransactionNo(options, transactionNo);
        return client.sendPostRequest(ProductsUrls.PRODUCTS,
                RequestOptions.builder(options).jsonBody(payload).headers(headers)
                        .dtoFactory(Products::readProduct).build());
    }

    public ApiResponse createProduct(ProductInput payload) {
        return createProduct((Object) payload, null, null);
    }

    public ApiResponse createProduct(ProductInput payload, String transactionNo) {
        return createProduct((Object) payload, transactionNo, null);
    }

    public ApiResponse createProduct(ProductInput payload, RequestOptions options) {
        return createProduct((Object) payload, null, options);
    }

    public ApiResponse createProduct(ProductInput payload, String transactionNo,
            RequestOptions options) {
        return createProduct((Object) payload, transactionNo, options);
    }

    /** Retrieve one product — {@code GET /admin/products/{product_id}}. */
    public ApiResponse getProduct(Object productId) {
        return getProduct(productId, null, null);
    }

    public ApiResponse getProduct(Object productId, Map<String, Object> queryParams) {
        return getProduct(productId, queryParams, null);
    }

    public ApiResponse getProduct(Object productId, Map<String, Object> queryParams,
            RequestOptions options) {
        return client.sendGetRequest(ProductsUrls.PRODUCT,
                RequestOptions.builder(options).pathParams(pathParams(productId))
                        .queryParams(queryParams).dtoFactory(Products::readProduct).build());
    }

    /**
     * Update one product — {@code PUT /admin/products/{product_id}}. A bare record merged at every
     * level; it must carry {@code name}, {@code sku}, {@code base_price}, {@code product_type},
     * {@code available}, {@code is_on_sale}, {@code available_on_date_range},
     * {@code available_on_weekdays}, {@code available_for} and {@code selected_customers}
     * ({@code 400 required} otherwise), and fields left out keep their values.
     */
    public ApiResponse updateProduct(Object productId, Object payload) {
        return updateProduct(productId, payload, null);
    }

    public ApiResponse updateProduct(Object productId, Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPutRequest(ProductsUrls.PRODUCT,
                RequestOptions.builder(options).pathParams(pathParams(productId)).jsonBody(payload)
                        .dtoFactory(Products::readProduct).build());
    }

    public ApiResponse updateProduct(Object productId, ProductInput payload) {
        return updateProduct(productId, (Object) payload, null);
    }

    public ApiResponse updateProduct(Object productId, ProductInput payload,
            RequestOptions options) {
        return updateProduct(productId, (Object) payload, options);
    }

    /** Merge into one product — {@code PATCH /admin/products/{product_id}}; trashed is 409. */
    public ApiResponse patchProduct(Object productId, Object payload) {
        return patchProduct(productId, payload, null);
    }

    public ApiResponse patchProduct(Object productId, Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPatchRequest(ProductsUrls.PRODUCT,
                RequestOptions.builder(options).pathParams(pathParams(productId)).jsonBody(payload)
                        .dtoFactory(Products::readProduct).build());
    }

    public ApiResponse patchProduct(Object productId, ProductInput payload) {
        return patchProduct(productId, (Object) payload, null);
    }

    public ApiResponse patchProduct(Object productId, ProductInput payload,
            RequestOptions options) {
        return patchProduct(productId, (Object) payload, options);
    }

    /**
     * Move a product to the trash — {@code DELETE /admin/products/{product_id}}; 204. The
     * product still reads, {@code status} {@code trashed}; a second delete is 404.
     */
    public ApiResponse deleteProduct(Object productId) {
        return deleteProduct(productId, null);
    }

    public ApiResponse deleteProduct(Object productId, RequestOptions options) {
        return client.sendDeleteRequest(ProductsUrls.PRODUCT,
                RequestOptions.builder(options).pathParams(pathParams(productId)).dtoFactory(null)
                        .build());
    }

    /**
     * Add reviews to a product — {@code POST /admin/products/{product_id}/reviews}; 201. It
     * answers every review the product holds, the new ones last; a null {@code transactionNo}
     * sends no header.
     */
    public ApiResponse importProductReviews(Object productId, Object payload) {
        return importProductReviews(productId, payload, null, null);
    }

    public ApiResponse importProductReviews(Object productId, Object payload,
            String transactionNo) {
        return importProductReviews(productId, payload, transactionNo, null);
    }

    public ApiResponse importProductReviews(Object productId, Object payload,
            RequestOptions options) {
        return importProductReviews(productId, payload, null, options);
    }

    public ApiResponse importProductReviews(Object productId, Object payload,
            String transactionNo, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        Map<String, String> headers = withTransactionNo(options, transactionNo);
        return client.sendPostRequest(ProductsUrls.PRODUCT_REVIEWS,
                RequestOptions.builder(options).pathParams(pathParams(productId)).jsonBody(payload)
                        .headers(headers).dtoFactory(Products::readProductReviews).build());
    }

    public ApiResponse importProductReviews(Object productId,
            ProductReviewsImportInput payload) {
        return importProductReviews(productId, (Object) payload, null, null);
    }

    public ApiResponse importProductReviews(Object productId,
            ProductReviewsImportInput payload, String transactionNo) {
        return importProductReviews(productId, (Object) payload, transactionNo, null);
    }

    public ApiResponse importProductReviews(Object productId,
            ProductReviewsImportInput payload, RequestOptions options) {
        return importProductReviews(productId, (Object) payload, null, options);
    }

    public ApiResponse importProductReviews(Object productId,
            ProductReviewsImportInput payload, String transactionNo, RequestOptions options) {
        return importProductReviews(productId, (Object) payload, transactionNo, options);
    }

    /** One product's stock at each location — {@code GET /admin/products/{product_id}/stock}. */
    public ApiResponse getProductStock(Object productId) {
        return getProductStock(productId, null);
    }

    public ApiResponse getProductStock(Object productId, RequestOptions options) {
        return client.sendGetRequest(ProductsUrls.PRODUCT_STOCK,
                RequestOptions.builder(options).pathParams(pathParams(productId))
                        .dtoFactory(body -> Dto.fromMap(ProductStock.class, body)).build());
    }

    /** Set the stock; {@code locations} replaces the whole set — {@code PUT .../stock}. */
    public ApiResponse updateProductStock(Object productId, Object payload) {
        return updateProductStock(productId, payload, null);
    }

    public ApiResponse updateProductStock(Object productId, Object payload,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPutRequest(ProductsUrls.PRODUCT_STOCK,
                RequestOptions.builder(options).pathParams(pathParams(productId)).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(ProductStock.class, body)).build());
    }

    public ApiResponse updateProductStock(Object productId, ProductStockInput payload) {
        return updateProductStock(productId, (Object) payload, null);
    }

    public ApiResponse updateProductStock(Object productId, ProductStockInput payload,
            RequestOptions options) {
        return updateProductStock(productId, (Object) payload, options);
    }

    /** Unassign a location — {@code DELETE .../stock/locations/{inventory_location_id}}. */
    public ApiResponse deleteProductStockLocation(Object productId, Object inventoryLocationId) {
        return deleteProductStockLocation(productId, inventoryLocationId, null);
    }

    public ApiResponse deleteProductStockLocation(Object productId, Object inventoryLocationId,
            RequestOptions options) {
        return client.sendDeleteRequest(ProductsUrls.PRODUCT_STOCK_LOCATION,
                RequestOptions.builder(options)
                        .pathParams(pathParams(productId, inventoryLocationId)).dtoFactory(null)
                        .build());
    }

    private static Product readProduct(Object body) {
        Product product = Dto.fromMap(Product.class, body);
        readReviewProductIds(product.reviews, body);
        return product;
    }

    private static ProductReviews readProductReviews(Object body) {
        ProductReviews productReviews = Dto.fromMap(ProductReviews.class, body);
        readReviewProductIds(productReviews.reviews, body);
        return productReviews;
    }

    /** By hand after the walk, as no field name derives a review's camel-case {@code productId}. */
    private static void readReviewProductIds(List<ProductReview> reviews, Object body) {
        Object record = ((Map<?, ?>) body).get(ProductsUrls.PRODUCT_KEY);
        if (reviews == null || !(record instanceof Map<?, ?> row)
                || !(row.get("reviews") instanceof List<?> sent)) {
            return;
        }
        for (int index = 0; index < sent.size() && index < reviews.size(); index++) {
            ProductReview review = reviews.get(index);
            if (review != null && sent.get(index) instanceof Map<?, ?> given
                    && given.get("productId") instanceof Number value) {
                review.productId = value.longValue();
            }
        }
    }

    private static Map<String, String> withTransactionNo(RequestOptions options,
            String transactionNo) {
        Map<String, String> given = options == null ? null : options.getHeaders();
        if (transactionNo == null) {
            return given;
        }
        Map<String, String> headers =
                given == null ? new LinkedHashMap<>() : new LinkedHashMap<>(given);
        headers.put(ProductsUrls.TRANSACTION_NO_HEADER, transactionNo);
        return headers;
    }

    private static Map<String, Object> pathParams(Object productId) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("product_id", productId);
        return params;
    }

    private static Map<String, Object> pathParams(Object productId, Object inventoryLocationId) {
        Map<String, Object> params = pathParams(productId);
        params.put("inventory_location_id", inventoryLocationId);
        return params;
    }
}
