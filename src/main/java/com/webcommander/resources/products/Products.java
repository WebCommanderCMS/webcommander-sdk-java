package com.webcommander.resources.products;

import com.webcommander.Dto;
import com.webcommander.client.ApiResponse;
import com.webcommander.client.BoundResource;
import com.webcommander.client.RequestOptions;
import com.webcommander.client.ResourceBinding;
import com.webcommander.client.WebCommanderClient;
import com.webcommander.errors.SdkUsageError;
import com.webcommander.resources.products.products_dto.ProductsDto.Product;
import com.webcommander.resources.products.products_dto.ProductsDto.ProductCount;
import com.webcommander.resources.products.products_dto.ProductsDto.ProductImageUploadInput;
import com.webcommander.resources.products.products_dto.ProductsDto.ProductInventoryInput;
import com.webcommander.resources.products.products_dto.ProductsDto.ProductInventoryResult;
import com.webcommander.resources.products.products_dto.ProductsDto.ProductMediaImage;
import com.webcommander.resources.products.products_dto.ProductsDto.ProductMediaImagePatchInput;
import com.webcommander.resources.products.products_dto.ProductsDto.ProductMediaOrder;
import com.webcommander.resources.products.products_dto.ProductsDto.ProductMediaOrderInput;
import com.webcommander.resources.products.products_dto.ProductsDto.ProductMediaUploadInput;
import com.webcommander.resources.products.products_dto.ProductsDto.ProductMediaUploaded;
import com.webcommander.resources.products.products_dto.ProductsDto.ProductReviewList;
import com.webcommander.resources.products.products_dto.ProductsDto.ProductReviewPatchInput;
import com.webcommander.resources.products.products_dto.ProductsDto.ProductSeo;
import com.webcommander.resources.products.products_dto.ProductsDto.ProductSkuCheck;
import com.webcommander.resources.products.products_dto.ProductsDto.ProductSpecialPage;
import com.webcommander.resources.products.products_dto.ProductsDto.ProductStockAdjustmentPage;
import com.webcommander.resources.products.products_dto.ProductsDto.ProductStockPatchInput;
import com.webcommander.resources.products.products_dto.ProductsDto.ProductStockPatchResult;
import com.webcommander.resources.products.products_dto.ProductsDto.ProductsBulkDeleteInput;
import com.webcommander.resources.products.products_dto.ProductsDto.ProductsBulkDeleteResult;
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

    /**
     * Count products — {@code GET /admin/products/count}; {@code {"total": n, "count": n}}. It
     * takes the list's filters ({@code search_text}, {@code name}, {@code sku}, {@code ids},
     * {@code parent}, {@code product_type}, {@code stock}, {@code is_available},
     * {@code is_inventory_enabled}, {@code created_by}, {@code price_from}, {@code price_to} and
     * the four date bounds) and refuses its paging and sorting, and {@code status}, as
     * {@code 400 unknown_parameter}. A trashed product is not counted.
     */
    public ApiResponse countProducts() {
        return countProducts(null, null);
    }

    public ApiResponse countProducts(Map<String, Object> queryParams) {
        return countProducts(queryParams, null);
    }

    public ApiResponse countProducts(Map<String, Object> queryParams, RequestOptions options) {
        return client.sendGetRequest(ProductsUrls.PRODUCTS_COUNT,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(ProductCount.class, body)).build());
    }

    /**
     * Ask whether a SKU is free — {@code GET /admin/products/check-sku}. The match ignores case
     * and sees a SKU held by a trashed product or by a variant; {@code excludeProductId} is the
     * {@code id} of a product that may hold it, as when that product is being edited. Any other
     * query name, and {@code SKU} in capitals, is {@code 400 unknown_parameter}.
     */
    public ApiResponse checkProductSku(String sku) {
        return checkProductSku(sku, null, null);
    }

    public ApiResponse checkProductSku(String sku, Object excludeProductId) {
        return checkProductSku(sku, excludeProductId, null);
    }

    public ApiResponse checkProductSku(String sku, Object excludeProductId,
            RequestOptions options) {
        if (sku == null || sku.isEmpty()) {
            throw new SdkUsageError("sku is required");
        }
        Map<String, Object> query = new LinkedHashMap<>();
        query.put("sku", sku);
        query.put("exclude_product_id", excludeProductId);
        return client.sendGetRequest(ProductsUrls.PRODUCTS_CHECK_SKU,
                RequestOptions.builder(options).queryParams(query)
                        .dtoFactory(body -> Dto.fromMap(ProductSkuCheck.class, body)).build());
    }

    /**
     * List a special selection of products — {@code GET /admin/products/special}. {@code type}
     * is required: {@code onSale}, {@code featured}, {@code new}, {@code topSelling},
     * {@code lastBought} or {@code lastViewed}. {@code max} (1 to 100, default 10) and
     * {@code offset} page it. The last two are per customer and need {@code customerId} (the
     * {@code customer_id} or {@code internal_id} of a customer) or {@code customer_email};
     * {@code customer_id} is not read, and an unknown customer is {@code 400 unknown_reference}.
     * A name it does not know is ignored.
     */
    public ApiResponse listSpecialProducts(Map<String, Object> queryParams) {
        return listSpecialProducts(queryParams, null);
    }

    public ApiResponse listSpecialProducts(Map<String, Object> queryParams,
            RequestOptions options) {
        if (queryParams == null) {
            throw new SdkUsageError("queryParams is required: type names the selection");
        }
        return client.sendGetRequest(ProductsUrls.PRODUCTS_SPECIAL,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(ProductSpecialPage.class, body))
                        .build());
    }

    /**
     * Move several products to the trash — {@code POST /admin/products/bulk-delete}, answered
     * {@code 200} even when an id failed. The body is {@code {"ids": [...]}} (the collection
     * sends {@code product_ids}, which the store answers {@code 400 required} for {@code ids});
     * an empty list is {@code 400}. Each id that is not a product is counted in {@code failed}
     * and listed in {@code errors}.
     */
    public ApiResponse bulkDeleteProducts(Object payload) {
        return bulkDeleteProducts(payload, null);
    }

    public ApiResponse bulkDeleteProducts(Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPostRequest(ProductsUrls.PRODUCTS_BULK_DELETE,
                RequestOptions.builder(options).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(ProductsBulkDeleteResult.class, body))
                        .build());
    }

    public ApiResponse bulkDeleteProducts(ProductsBulkDeleteInput payload) {
        return bulkDeleteProducts((Object) payload, null);
    }

    public ApiResponse bulkDeleteProducts(ProductsBulkDeleteInput payload,
            RequestOptions options) {
        return bulkDeleteProducts((Object) payload, options);
    }

    /**
     * List a product's reviews — {@code GET /admin/products/{product_id}/reviews};
     * {@code {"reviews": [...]}} with no paging block. {@code status}, {@code limit} and
     * {@code offset} are in the collection and this store ignores them, as it does any other
     * name.
     */
    public ApiResponse listProductReviews(Object productId) {
        return listProductReviews(productId, null, null);
    }

    public ApiResponse listProductReviews(Object productId, Map<String, Object> queryParams) {
        return listProductReviews(productId, queryParams, null);
    }

    public ApiResponse listProductReviews(Object productId, Map<String, Object> queryParams,
            RequestOptions options) {
        return client.sendGetRequest(ProductsUrls.PRODUCT_REVIEWS,
                RequestOptions.builder(options).pathParams(pathParams(productId))
                        .queryParams(queryParams)
                        .dtoFactory(Products::readProductReviewList).build());
    }

    /**
     * Read one review under {@code review} —
     * {@code GET /admin/products/{product_id}/reviews/{review_id}}. {@code reviewId} is a
     * review's {@code id}; an unknown one is {@code 404}.
     */
    public ApiResponse getProductReview(Object productId, Object reviewId) {
        return getProductReview(productId, reviewId, null);
    }

    public ApiResponse getProductReview(Object productId, Object reviewId,
            RequestOptions options) {
        return client.sendGetRequest(ProductsUrls.PRODUCT_REVIEW,
                RequestOptions.builder(options).pathParams(reviewPathParams(productId, reviewId))
                        .dtoFactory(Products::readProductReview).build());
    }

    /**
     * Change one review — {@code PATCH /admin/products/{product_id}/reviews/{review_id}}, a bare
     * body answered {@code 200} with the review under {@code review}. {@code status} is
     * {@code pending} (what an imported review without one reads) or {@code approved};
     * {@code rejected}, which the collection uses, is {@code 400 invalid_enum}. {@code rating}
     * and {@code comment} change too.
     */
    public ApiResponse patchProductReview(Object productId, Object reviewId, Object payload) {
        return patchProductReview(productId, reviewId, payload, null);
    }

    public ApiResponse patchProductReview(Object productId, Object reviewId, Object payload,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPatchRequest(ProductsUrls.PRODUCT_REVIEW,
                RequestOptions.builder(options).pathParams(reviewPathParams(productId, reviewId))
                        .jsonBody(payload).dtoFactory(Products::readProductReview).build());
    }

    public ApiResponse patchProductReview(Object productId, Object reviewId,
            ProductReviewPatchInput payload) {
        return patchProductReview(productId, reviewId, (Object) payload, null);
    }

    public ApiResponse patchProductReview(Object productId, Object reviewId,
            ProductReviewPatchInput payload, RequestOptions options) {
        return patchProductReview(productId, reviewId, (Object) payload, options);
    }

    /**
     * Delete one review for good; {@code 204}, and a second delete is {@code 404}.
     * {@code DELETE /admin/products/{product_id}/reviews/{review_id}}
     */
    public ApiResponse deleteProductReview(Object productId, Object reviewId) {
        return deleteProductReview(productId, reviewId, null);
    }

    public ApiResponse deleteProductReview(Object productId, Object reviewId,
            RequestOptions options) {
        return client.sendDeleteRequest(ProductsUrls.PRODUCT_REVIEW,
                RequestOptions.builder(options).pathParams(reviewPathParams(productId, reviewId))
                        .dtoFactory(null).build());
    }

    /**
     * Set a product's stock figure — {@code POST /admin/products/{product_id}/inventories},
     * answered {@code 201}. The body is {@code {"product": {"product_pricing_and_stock":
     * {"track_inventory": true, "available_stock": n}}}}; {@code low_level_stock} sets the low
     * level. It records a history row and a variant keeps its own figure. The header is stored as
     * the product's {@code transaction_no} when it has none, and the answer echoes the value
     * from before the write; a null {@code transactionNo} sends no header.
     */
    public ApiResponse createProductInventory(Object productId, Object payload) {
        return createProductInventory(productId, payload, null, null);
    }

    public ApiResponse createProductInventory(Object productId, Object payload,
            String transactionNo) {
        return createProductInventory(productId, payload, transactionNo, null);
    }

    public ApiResponse createProductInventory(Object productId, Object payload,
            RequestOptions options) {
        return createProductInventory(productId, payload, null, options);
    }

    public ApiResponse createProductInventory(Object productId, Object payload,
            String transactionNo, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        Map<String, String> headers = withTransactionNo(options, transactionNo);
        return client.sendPostRequest(ProductsUrls.PRODUCT_INVENTORIES,
                RequestOptions.builder(options).pathParams(pathParams(productId)).jsonBody(payload)
                        .headers(headers)
                        .dtoFactory(body -> Dto.fromMap(ProductInventoryResult.class, body))
                        .build());
    }

    public ApiResponse createProductInventory(Object productId, ProductInventoryInput payload) {
        return createProductInventory(productId, (Object) payload, null, null);
    }

    public ApiResponse createProductInventory(Object productId, ProductInventoryInput payload,
            String transactionNo) {
        return createProductInventory(productId, (Object) payload, transactionNo, null);
    }

    public ApiResponse createProductInventory(Object productId, ProductInventoryInput payload,
            RequestOptions options) {
        return createProductInventory(productId, (Object) payload, null, options);
    }

    public ApiResponse createProductInventory(Object productId, ProductInventoryInput payload,
            String transactionNo, RequestOptions options) {
        return createProductInventory(productId, (Object) payload, transactionNo, options);
    }

    /**
     * Store images on a product — {@code POST /admin/products/{product_id}/image}, answered
     * {@code 201} with the files just stored. The body is JSON,
     * {@code {"product": {"image_and_video": {"images": [{"filename": ..., "base64": ...}]}}}};
     * a multipart body is {@code 415}.
     */
    public ApiResponse createProductImage(Object productId, Object payload) {
        return createProductImage(productId, payload, null);
    }

    public ApiResponse createProductImage(Object productId, Object payload,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPostRequest(ProductsUrls.PRODUCT_IMAGE,
                RequestOptions.builder(options).pathParams(pathParams(productId)).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(ProductMediaUploaded.class, body))
                        .build());
    }

    public ApiResponse createProductImage(Object productId, ProductImageUploadInput payload) {
        return createProductImage(productId, (Object) payload, null);
    }

    public ApiResponse createProductImage(Object productId, ProductImageUploadInput payload,
            RequestOptions options) {
        return createProductImage(productId, (Object) payload, options);
    }

    /**
     * Store one file on a product — {@code POST /admin/products/{product_id}/media}, answered
     * {@code 201} with the file just stored, flat ({@code id}, {@code filename}, {@code url}),
     * and a {@code Location}. A bare JSON body of {@code type}, {@code filename}
     * and {@code content} (base64 or a {@code data:} URI). Only {@code type} {@code images} is
     * accepted; {@code videos} and {@code specification_file} are {@code 400 not_supported}. An
     * optional 1-based {@code position} places it.
     */
    public ApiResponse createProductMedia(Object productId, Object payload) {
        return createProductMedia(productId, payload, null);
    }

    public ApiResponse createProductMedia(Object productId, Object payload,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPostRequest(ProductsUrls.PRODUCT_MEDIA,
                RequestOptions.builder(options).pathParams(pathParams(productId)).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(ProductMediaImage.class, body))
                        .build());
    }

    public ApiResponse createProductMedia(Object productId, ProductMediaUploadInput payload) {
        return createProductMedia(productId, (Object) payload, null);
    }

    public ApiResponse createProductMedia(Object productId, ProductMediaUploadInput payload,
            RequestOptions options) {
        return createProductMedia(productId, (Object) payload, options);
    }

    /**
     * Delete one image from a product; {@code 204}, and a second delete is {@code 404}.
     * {@code imageId} is an image's {@code id} on the product.
     * {@code DELETE /admin/products/{product_id}/media/images/{image_id}}
     */
    public ApiResponse deleteProductMediaImage(Object productId, Object imageId) {
        return deleteProductMediaImage(productId, imageId, null);
    }

    public ApiResponse deleteProductMediaImage(Object productId, Object imageId,
            RequestOptions options) {
        Map<String, Object> params = pathParams(productId);
        params.put("image_id", imageId);
        return client.sendDeleteRequest(ProductsUrls.PRODUCT_MEDIA_IMAGE,
                RequestOptions.builder(options).pathParams(params).dtoFactory(null).build());
    }

    /**
     * Set the order of a product's images; the first becomes {@code primary}. A bare
     * {@code {"type": "images", "ids": [...]}} naming every image once, answered with
     * {@code images} in the new order. {@code PUT /admin/products/{product_id}/media/order}
     */
    public ApiResponse updateProductMediaOrder(Object productId, Object payload) {
        return updateProductMediaOrder(productId, payload, null);
    }

    public ApiResponse updateProductMediaOrder(Object productId, Object payload,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPutRequest(ProductsUrls.PRODUCT_MEDIA_ORDER,
                RequestOptions.builder(options).pathParams(pathParams(productId)).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(ProductMediaOrder.class, body)).build());
    }

    public ApiResponse updateProductMediaOrder(Object productId, ProductMediaOrderInput payload) {
        return updateProductMediaOrder(productId, (Object) payload, null);
    }

    public ApiResponse updateProductMediaOrder(Object productId, ProductMediaOrderInput payload,
            RequestOptions options) {
        return updateProductMediaOrder(productId, (Object) payload, options);
    }

    /**
     * Move one image to a 1-based {@code position}; a bare {@code {"position": n}} is the only
     * body, and the image is answered as one flat row. {@code imageId} is an image's {@code id}
     * on the product. {@code PATCH /admin/products/{product_id}/media/images/{image_id}}
     */
    public ApiResponse patchProductMediaImage(Object productId, Object imageId, Object payload) {
        return patchProductMediaImage(productId, imageId, payload, null);
    }

    public ApiResponse patchProductMediaImage(Object productId, Object imageId, Object payload,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        Map<String, Object> params = pathParams(productId);
        params.put("image_id", imageId);
        return client.sendPatchRequest(ProductsUrls.PRODUCT_MEDIA_IMAGE,
                RequestOptions.builder(options).pathParams(params).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(ProductMediaImage.class, body)).build());
    }

    public ApiResponse patchProductMediaImage(Object productId, Object imageId,
            ProductMediaImagePatchInput payload) {
        return patchProductMediaImage(productId, imageId, (Object) payload, null);
    }

    public ApiResponse patchProductMediaImage(Object productId, Object imageId,
            ProductMediaImagePatchInput payload, RequestOptions options) {
        return patchProductMediaImage(productId, imageId, (Object) payload, options);
    }

    /**
     * A product's SEO, analytics, custom code, social and AI visibility settings under
     * {@code seo}. {@code GET /admin/products/{product_id}/seo}
     */
    public ApiResponse getProductSeo(Object productId) {
        return getProductSeo(productId, null);
    }

    public ApiResponse getProductSeo(Object productId, RequestOptions options) {
        return client.sendGetRequest(ProductsUrls.PRODUCT_SEO,
                RequestOptions.builder(options).pathParams(pathParams(productId))
                        .dtoFactory(body -> Dto.fromMap(ProductSeo.class, body)).build());
    }

    /**
     * Change a product's stock; a bare body of {@code tracking}, {@code locations}, {@code note}
     * and {@code transaction_no}, at least one of them. Answered with the {@code stock} now and
     * the {@code adjustment} it recorded, {@code null} when the figure did not change.
     * {@code PATCH /admin/products/{product_id}/stock}
     */
    public ApiResponse patchProductStock(Object productId, Object payload) {
        return patchProductStock(productId, payload, null);
    }

    public ApiResponse patchProductStock(Object productId, Object payload,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPatchRequest(ProductsUrls.PRODUCT_STOCK,
                RequestOptions.builder(options).pathParams(pathParams(productId)).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(ProductStockPatchResult.class, body))
                        .build());
    }

    public ApiResponse patchProductStock(Object productId, ProductStockPatchInput payload) {
        return patchProductStock(productId, (Object) payload, null);
    }

    public ApiResponse patchProductStock(Object productId, ProductStockPatchInput payload,
            RequestOptions options) {
        return patchProductStock(productId, (Object) payload, options);
    }

    /**
     * A product's stock history, newest first, paged by {@code limit} and {@code offset}.
     * {@code GET /admin/products/{product_id}/stock/adjustments}
     */
    public ApiResponse listProductStockAdjustments(Object productId) {
        return listProductStockAdjustments(productId, null, null);
    }

    public ApiResponse listProductStockAdjustments(Object productId,
            Map<String, Object> queryParams) {
        return listProductStockAdjustments(productId, queryParams, null);
    }

    public ApiResponse listProductStockAdjustments(Object productId,
            Map<String, Object> queryParams, RequestOptions options) {
        return client.sendGetRequest(ProductsUrls.PRODUCT_STOCK_ADJUSTMENTS,
                RequestOptions.builder(options).pathParams(pathParams(productId))
                        .queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(ProductStockAdjustmentPage.class, body))
                        .build());
    }

    private static ProductReviewList readProductReviewList(Object body) {
        ProductReviewList list = Dto.fromMap(ProductReviewList.class, body);
        copyReviewProductIds(list.reviews, ((Map<?, ?>) body).get("reviews"));
        return list;
    }

    private static ProductReview readProductReview(Object body) {
        ProductReview review = Dto.fromMap(ProductReview.class, body);
        if (((Map<?, ?>) body).get("review") instanceof Map<?, ?> row
                && row.get("productId") instanceof Number value) {
            review.productId = value.longValue();
        }
        return review;
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

    private static void readReviewProductIds(List<ProductReview> reviews, Object body) {
        Object record = ((Map<?, ?>) body).get(ProductsUrls.PRODUCT_KEY);
        if (record instanceof Map<?, ?> row) {
            copyReviewProductIds(reviews, row.get("reviews"));
        }
    }

    /** By hand after the walk, as no field name derives a review's camel-case {@code productId}. */
    private static void copyReviewProductIds(List<ProductReview> reviews, Object sentReviews) {
        if (reviews == null || !(sentReviews instanceof List<?> sent)) {
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

    private static Map<String, Object> reviewPathParams(Object productId, Object reviewId) {
        Map<String, Object> params = pathParams(productId);
        params.put("review_id", reviewId);
        return params;
    }

    private static Map<String, Object> pathParams(Object productId, Object inventoryLocationId) {
        Map<String, Object> params = pathParams(productId);
        params.put("inventory_location_id", inventoryLocationId);
        return params;
    }
}
