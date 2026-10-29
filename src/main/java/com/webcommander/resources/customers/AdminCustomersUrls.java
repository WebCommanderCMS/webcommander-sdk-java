package com.webcommander.resources.customers;

/** Path templates for the customer endpoints; the client fills {@code {api_version}}. */
public final class AdminCustomersUrls {

    private AdminCustomersUrls() {}

    public static final String ADMIN_CUSTOMERS = "/api/{api_version}/admin/customers";

    public static final String ADMIN_CUSTOMER = "/api/{api_version}/admin/customers/{customer_id}";

    public static final String ADMIN_CUSTOMERS_COUNT = "/api/{api_version}/admin/customers/count";

    public static final String ADMIN_CUSTOMERS_CHECK_EMAIL =
            "/api/{api_version}/admin/customers/check-email";

    public static final String ADMIN_CUSTOMERS_EXPORT = "/api/{api_version}/admin/customers/export";

    public static final String ADMIN_CUSTOMERS_IMPORT = "/api/{api_version}/admin/customers/import";

    public static final String ADMIN_CUSTOMERS_BULK_RESET_PASSWORD =
            "/api/{api_version}/admin/customers/bulk-reset-password";

    public static final String ADMIN_CUSTOMER_ADDRESSES =
            "/api/{api_version}/admin/customers/{customer_id}/addresses";

    public static final String ADMIN_CUSTOMER_RESET_PASSWORD =
            "/api/{api_version}/admin/customers/{customer_id}/reset-password";

    public static final String ADMIN_CUSTOMER_CHANGE_PASSWORD =
            "/api/{api_version}/admin/customers/{customer_id}/change-password";

    public static final String ADMIN_CUSTOMER_STORE_CREDIT_ADJUSTMENTS =
            "/api/{api_version}/admin/customers/{customer_id}/store-credit/adjustments";

    public static final String TRANSACTION_NO_HEADER = "X-Transaction-No";
}
