package com.webcommander.resources.products;

/** Path templates, body keys and the one header name for the product endpoints. */
public final class ProductsUrls {

    private ProductsUrls() {}

    public static final String PRODUCTS = "/api/{api_version}/admin/products";

    public static final String PRODUCT = "/api/{api_version}/admin/products/{product_id}";

    public static final String PRODUCT_REVIEWS =
            "/api/{api_version}/admin/products/{product_id}/reviews";

    public static final String PRODUCT_STOCK =
            "/api/{api_version}/admin/products/{product_id}/stock";

    public static final String PRODUCT_STOCK_LOCATION =
            "/api/{api_version}/admin/products/{product_id}/stock/locations/"
                    + "{inventory_location_id}";

    /** The key every product write answers under; only the review import takes it as a wrapper. */
    public static final String PRODUCT_KEY = "product";

    /** Tags a create with a transaction number; it wins over a body one it disagrees with. */
    public static final String TRANSACTION_NO_HEADER = "X-Transaction-No";
}
