package com.webcommander.resources.products;

/** Path templates, body keys and the one header name for the product endpoints. */
public final class ProductsUrls {

    private ProductsUrls() {}

    public static final String PRODUCTS = "/api/{api_version}/admin/products";

    public static final String PRODUCTS_COUNT = "/api/{api_version}/admin/products/count";

    public static final String PRODUCTS_CHECK_SKU =
            "/api/{api_version}/admin/products/check-sku";

    public static final String PRODUCTS_SPECIAL = "/api/{api_version}/admin/products/special";

    public static final String PRODUCTS_BULK_DELETE =
            "/api/{api_version}/admin/products/bulk-delete";

    public static final String PRODUCT = "/api/{api_version}/admin/products/{product_id}";

    public static final String PRODUCT_REVIEWS =
            "/api/{api_version}/admin/products/{product_id}/reviews";

    public static final String PRODUCT_REVIEW =
            "/api/{api_version}/admin/products/{product_id}/reviews/{review_id}";

    public static final String PRODUCT_IMAGE =
            "/api/{api_version}/admin/products/{product_id}/image";

    public static final String PRODUCT_MEDIA =
            "/api/{api_version}/admin/products/{product_id}/media";

    public static final String PRODUCT_MEDIA_IMAGE =
            "/api/{api_version}/admin/products/{product_id}/media/images/{image_id}";

    public static final String PRODUCT_INVENTORIES =
            "/api/{api_version}/admin/products/{product_id}/inventories";

    public static final String PRODUCT_STOCK =
            "/api/{api_version}/admin/products/{product_id}/stock";

    public static final String PRODUCT_STOCK_ADJUSTMENTS =
            "/api/{api_version}/admin/products/{product_id}/stock/adjustments";

    public static final String PRODUCT_SEO =
            "/api/{api_version}/admin/products/{product_id}/seo";

    public static final String PRODUCT_MEDIA_ORDER =
            "/api/{api_version}/admin/products/{product_id}/media/order";

    public static final String PRODUCT_STOCK_LOCATION =
            "/api/{api_version}/admin/products/{product_id}/stock/locations/"
                    + "{inventory_location_id}";

    /** The key every product write answers under; only the review import takes it as a wrapper. */
    public static final String PRODUCT_KEY = "product";

    /** Tags a create with a transaction number; it wins over a body one it disagrees with. */
    public static final String TRANSACTION_NO_HEADER = "X-Transaction-No";
}
