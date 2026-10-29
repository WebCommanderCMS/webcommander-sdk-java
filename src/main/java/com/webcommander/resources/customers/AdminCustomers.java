package com.webcommander.resources.customers;

import com.webcommander.client.ApiResponse;
import com.webcommander.client.BoundResource;
import com.webcommander.client.RequestOptions;
import com.webcommander.client.ResourceBinding;
import com.webcommander.client.WebCommanderClient;
import com.webcommander.errors.SdkUsageError;
import com.webcommander.Dto;
import com.webcommander.resources.customers.admin_customers_dto.AdminCustomersDto.Customer;
import com.webcommander.resources.customers.admin_customers_dto.AdminCustomersDto.CustomerInput;
import com.webcommander.resources.customers.admin_customers_dto.AdminCustomersDto.Page;
import com.webcommander.resources.customers.admin_customers_dto.AdminCustomersDto.StoreCreditAdjustment;
import com.webcommander.resources.customers.admin_customers_dto.AdminCustomersDto.StoreCreditAdjustmentInput;

import java.util.LinkedHashMap;
import java.util.Map;

/** Admin customer endpoints under {@code /api/{api_version}/admin/customers}. */
public final class AdminCustomers implements BoundResource {

    private final WebCommanderClient client;

    public AdminCustomers(WebCommanderClient client) {
        this.client = client;
    }

    /** List customers — {@code GET /admin/customers}; {@code limit} caps at 20. */
    public ApiResponse getCustomers() {
        return getCustomers(null, null);
    }

    public ApiResponse getCustomers(Map<String, Object> queryParams) {
        return getCustomers(queryParams, null);
    }

    public ApiResponse getCustomers(Map<String, Object> queryParams, RequestOptions options) {
        return client.sendGetRequest(AdminCustomersUrls.ADMIN_CUSTOMERS,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(Page.of(Customer.class)).build());
    }

    /** One customer — {@code GET /admin/customers/{customer_id}}. */
    public ApiResponse getCustomer(Object customerId) {
        return getCustomer(customerId, null);
    }

    public ApiResponse getCustomer(Object customerId, RequestOptions options) {
        return client.sendGetRequest(AdminCustomersUrls.ADMIN_CUSTOMER,
                RequestOptions.builder(options).pathParams(pathParams(customerId))
                        .dtoFactory(body -> Dto.fromMap(Customer.class, body)).build());
    }

    /** Count matching customers — {@code GET /admin/customers/count}. */
    public ApiResponse getCustomerCount() {
        return getCustomerCount(null, null);
    }

    public ApiResponse getCustomerCount(Map<String, Object> queryParams) {
        return getCustomerCount(queryParams, null);
    }

    public ApiResponse getCustomerCount(Map<String, Object> queryParams,
            RequestOptions options) {
        return client.sendGetRequest(AdminCustomersUrls.ADMIN_CUSTOMERS_COUNT,
                RequestOptions.builder(options).queryParams(queryParams).dtoFactory(null).build());
    }

    /** Is an email registered — {@code GET /admin/customers/check-email}. */
    public ApiResponse getCustomerEmailExists(String email) {
        return getCustomerEmailExists(email, null);
    }

    public ApiResponse getCustomerEmailExists(String email, RequestOptions options) {
        if (email == null || email.strip().isEmpty()) {
            throw new SdkUsageError("email is required and identifies the lookup, so a "
                    + "null or blank one is refused here rather than sent as a different question");
        }
        return client.sendGetRequest(AdminCustomersUrls.ADMIN_CUSTOMERS_CHECK_EMAIL,
                RequestOptions.builder(options).queryParams(Map.of("email", email))
                        .dtoFactory(null).build());
    }

    /** Export customers — {@code GET /admin/customers/export}; the body is XLSX {@code byte[]}. */
    public ApiResponse getCustomerExport() {
        return getCustomerExport(null, null);
    }

    public ApiResponse getCustomerExport(Map<String, Object> queryParams) {
        return getCustomerExport(queryParams, null);
    }

    public ApiResponse getCustomerExport(Map<String, Object> queryParams,
            RequestOptions options) {
        return client.sendGetRequest(AdminCustomersUrls.ADMIN_CUSTOMERS_EXPORT,
                RequestOptions.builder(options).queryParams(queryParams).dtoFactory(null).build());
    }

    /** Create a customer — {@code POST /admin/customers}; {@code email} must be unique. */
    public ApiResponse createCustomer(Object payload) {
        return createCustomer(payload, null, null);
    }

    public ApiResponse createCustomer(Object payload, RequestOptions options) {
        return createCustomer(payload, null, options);
    }

    public ApiResponse createCustomer(Object payload, String transactionNo) {
        return createCustomer(payload, transactionNo, null);
    }

    public ApiResponse createCustomer(Object payload, String transactionNo,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        RequestOptions given = options == null ? RequestOptions.none() : options;
        Map<String, String> headers = new LinkedHashMap<>();
        if (given.getHeaders() != null) {
            headers.putAll(given.getHeaders());
        }
        if (transactionNo != null) {
            headers.put(AdminCustomersUrls.TRANSACTION_NO_HEADER, transactionNo);
        }
        return client.sendPostRequest(AdminCustomersUrls.ADMIN_CUSTOMERS,
                RequestOptions.builder(options).jsonBody(payload).headers(headers)
                        .dtoFactory(body -> Dto.fromMap(Customer.class, body)).build());
    }

    public ApiResponse createCustomer(CustomerInput payload) {
        return createCustomer((Object) payload, null, null);
    }

    public ApiResponse createCustomer(CustomerInput payload, RequestOptions options) {
        return createCustomer((Object) payload, null, options);
    }

    public ApiResponse createCustomer(CustomerInput payload, String transactionNo) {
        return createCustomer((Object) payload, transactionNo, null);
    }

    public ApiResponse createCustomer(CustomerInput payload, String transactionNo,
            RequestOptions options) {
        return createCustomer((Object) payload, transactionNo, options);
    }

    /** Update a customer — {@code PUT /admin/customers/{customer_id}}. */
    public ApiResponse updateCustomer(Object customerId, Object payload) {
        return updateCustomer(customerId, payload, null);
    }

    public ApiResponse updateCustomer(Object customerId, Object payload,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPutRequest(AdminCustomersUrls.ADMIN_CUSTOMER,
                RequestOptions.builder(options).pathParams(pathParams(customerId)).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(Customer.class, body)).build());
    }

    public ApiResponse updateCustomer(Object customerId, CustomerInput payload) {
        return updateCustomer(customerId, (Object) payload, null);
    }

    public ApiResponse updateCustomer(Object customerId, CustomerInput payload,
            RequestOptions options) {
        return updateCustomer(customerId, (Object) payload, options);
    }

    /** Delete a customer — {@code DELETE /admin/customers/{customer_id}}; the delete is soft. */
    public ApiResponse deleteCustomer(Object customerId) {
        return deleteCustomer(customerId, null);
    }

    public ApiResponse deleteCustomer(Object customerId, RequestOptions options) {
        return client.sendDeleteRequest(AdminCustomersUrls.ADMIN_CUSTOMER,
                RequestOptions.builder(options).pathParams(pathParams(customerId))
                        .dtoFactory(null).build());
    }

    /** Add addresses — {@code POST /admin/customers/{customer_id}/addresses}. */
    public ApiResponse createCustomerAddresses(Object customerId, Object payload) {
        return createCustomerAddresses(customerId, payload, null);
    }

    public ApiResponse createCustomerAddresses(Object customerId, Object payload,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPostRequest(AdminCustomersUrls.ADMIN_CUSTOMER_ADDRESSES,
                RequestOptions.builder(options).pathParams(pathParams(customerId)).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(Customer.class, body)).build());
    }

    public ApiResponse createCustomerAddresses(Object customerId, CustomerInput payload) {
        return createCustomerAddresses(customerId, (Object) payload, null);
    }

    public ApiResponse createCustomerAddresses(Object customerId, CustomerInput payload,
            RequestOptions options) {
        return createCustomerAddresses(customerId, (Object) payload, options);
    }

    /** Set a password — {@code POST /admin/customers/{customer_id}/reset-password}. */
    public ApiResponse resetCustomerPassword(Object customerId, Object payload) {
        return resetCustomerPassword(customerId, payload, null);
    }

    public ApiResponse resetCustomerPassword(Object customerId, Object payload,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPostRequest(AdminCustomersUrls.ADMIN_CUSTOMER_RESET_PASSWORD,
                RequestOptions.builder(options).pathParams(pathParams(customerId)).jsonBody(payload)
                        .dtoFactory(null).build());
    }

    public ApiResponse resetCustomerPassword(Object customerId, CustomerInput payload) {
        return resetCustomerPassword(customerId, (Object) payload, null);
    }

    public ApiResponse resetCustomerPassword(Object customerId, CustomerInput payload,
            RequestOptions options) {
        return resetCustomerPassword(customerId, (Object) payload, options);
    }

    /** Change a password — {@code POST /admin/customers/{customer_id}/change-password}. */
    public ApiResponse changeCustomerPassword(Object customerId, Object payload) {
        return changeCustomerPassword(customerId, payload, null);
    }

    public ApiResponse changeCustomerPassword(Object customerId, Object payload,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPostRequest(AdminCustomersUrls.ADMIN_CUSTOMER_CHANGE_PASSWORD,
                RequestOptions.builder(options).pathParams(pathParams(customerId)).jsonBody(payload)
                        .dtoFactory(null).build());
    }

    public ApiResponse changeCustomerPassword(Object customerId, CustomerInput payload) {
        return changeCustomerPassword(customerId, (Object) payload, null);
    }

    public ApiResponse changeCustomerPassword(Object customerId, CustomerInput payload,
            RequestOptions options) {
        return changeCustomerPassword(customerId, (Object) payload, options);
    }

    /** Queue resets for many customers — {@code POST /admin/customers/bulk-reset-password}. */
    public ApiResponse bulkResetCustomerPasswords(Object payload) {
        return bulkResetCustomerPasswords(payload, null);
    }

    public ApiResponse bulkResetCustomerPasswords(Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPostRequest(AdminCustomersUrls.ADMIN_CUSTOMERS_BULK_RESET_PASSWORD,
                RequestOptions.builder(options).jsonBody(payload).dtoFactory(null).build());
    }

    /** Import customers — {@code POST /admin/customers/import}; the part name is {@code file}. */
    public ApiResponse importCustomers(Map<String, Object> files) {
        return importCustomers(files, null);
    }

    public ApiResponse importCustomers(Map<String, Object> files, RequestOptions options) {
        if (files == null || files.isEmpty()) {
            throw new SdkUsageError("files is required (part name 'file')");
        }
        return client.sendPostRequest(AdminCustomersUrls.ADMIN_CUSTOMERS_IMPORT,
                RequestOptions.builder(options).files(files).dtoFactory(null).build());
    }

    /** Store-credit history, newest first — {@code GET .../store-credit/adjustments}. */
    public ApiResponse getCustomerStoreCreditAdjustments(Object customerId) {
        return getCustomerStoreCreditAdjustments(customerId, null, null);
    }

    public ApiResponse getCustomerStoreCreditAdjustments(Object customerId,
            Map<String, Object> queryParams) {
        return getCustomerStoreCreditAdjustments(customerId, queryParams, null);
    }

    public ApiResponse getCustomerStoreCreditAdjustments(Object customerId,
            Map<String, Object> queryParams, RequestOptions options) {
        return client.sendGetRequest(AdminCustomersUrls.ADMIN_CUSTOMER_STORE_CREDIT_ADJUSTMENTS,
                RequestOptions.builder(options).pathParams(pathParams(customerId))
                        .queryParams(queryParams).dtoFactory(Page.of(StoreCreditAdjustment.class))
                        .build());
    }

    /** Move store credit — {@code POST /admin/customers/{customer_id}/store-credit/adjustments}. */
    public ApiResponse createCustomerStoreCreditAdjustment(Object customerId, Object payload) {
        return createCustomerStoreCreditAdjustment(customerId, payload, null);
    }

    public ApiResponse createCustomerStoreCreditAdjustment(Object customerId, Object payload,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPostRequest(AdminCustomersUrls.ADMIN_CUSTOMER_STORE_CREDIT_ADJUSTMENTS,
                RequestOptions.builder(options).pathParams(pathParams(customerId)).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(StoreCreditAdjustment.class, body))
                        .build());
    }

    public ApiResponse createCustomerStoreCreditAdjustment(Object customerId,
            StoreCreditAdjustmentInput payload) {
        return createCustomerStoreCreditAdjustment(customerId, (Object) payload, null);
    }

    public ApiResponse createCustomerStoreCreditAdjustment(Object customerId,
            StoreCreditAdjustmentInput payload, RequestOptions options) {
        return createCustomerStoreCreditAdjustment(customerId, (Object) payload, options);
    }

    /** Never {@code Map.of}: a null id must reach the URL builder's {@link SdkUsageError}. */
    private static Map<String, Object> pathParams(Object customerId) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("customer_id", customerId);
        return params;
    }
}
