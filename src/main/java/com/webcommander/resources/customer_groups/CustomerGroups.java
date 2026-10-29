package com.webcommander.resources.customer_groups;

import com.webcommander.Dto;
import com.webcommander.client.ApiResponse;
import com.webcommander.client.BoundResource;
import com.webcommander.client.RequestOptions;
import com.webcommander.client.ResourceBinding;
import com.webcommander.client.WebCommanderClient;
import com.webcommander.resources.customer_groups.customer_groups_dto
        .CustomerGroupsDto.CustomerGroup;
import com.webcommander.resources.customer_groups.customer_groups_dto
        .CustomerGroupsDto.CustomerGroupAssignInput;
import com.webcommander.resources.customer_groups.customer_groups_dto
        .CustomerGroupsDto.CustomerGroupAssignment;
import com.webcommander.resources.customer_groups.customer_groups_dto
        .CustomerGroupsDto.CustomerGroupCount;
import com.webcommander.resources.customer_groups.customer_groups_dto
        .CustomerGroupsDto.CustomerGroupCustomersInput;
import com.webcommander.resources.customer_groups.customer_groups_dto
        .CustomerGroupsDto.CustomerGroupInput;
import com.webcommander.resources.customer_groups.customer_groups_dto
        .CustomerGroupsDto.CustomerGroupMember;
import com.webcommander.resources.customer_groups.customer_groups_dto
        .CustomerGroupsDto.CustomerGroupPage;

import java.util.LinkedHashMap;
import java.util.Map;

/** Customer groups — {@code /api/{api_version}/admin/customer_groups} — and their members. */
public final class CustomerGroups implements BoundResource {

    private final WebCommanderClient client;

    public CustomerGroups(WebCommanderClient client) {
        this.client = client;
    }

    /** List the groups — {@code GET /admin/customer_groups}. */
    public ApiResponse listCustomerGroups() {
        return listCustomerGroups(null, null);
    }

    public ApiResponse listCustomerGroups(Map<String, Object> queryParams) {
        return listCustomerGroups(queryParams, null);
    }

    public ApiResponse listCustomerGroups(Map<String, Object> queryParams,
            RequestOptions options) {
        return client.sendGetRequest(CustomerGroupsUrls.CUSTOMER_GROUPS,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(CustomerGroupPage.class, body)).build());
    }

    /** How many groups match — {@code GET /admin/customer_groups/count}. */
    public ApiResponse countCustomerGroups() {
        return countCustomerGroups(null, null);
    }

    public ApiResponse countCustomerGroups(Map<String, Object> queryParams) {
        return countCustomerGroups(queryParams, null);
    }

    public ApiResponse countCustomerGroups(Map<String, Object> queryParams,
            RequestOptions options) {
        return client.sendGetRequest(CustomerGroupsUrls.CUSTOMER_GROUPS_COUNT,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(CustomerGroupCount.class, body)).build());
    }

    /** Create a group — {@code POST /admin/customer_groups}. */
    public ApiResponse createCustomerGroup(Object payload) {
        return createCustomerGroup(payload, null);
    }

    public ApiResponse createCustomerGroup(Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPostRequest(CustomerGroupsUrls.CUSTOMER_GROUPS,
                RequestOptions.builder(options).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(CustomerGroup.class, body)).build());
    }

    public ApiResponse createCustomerGroup(CustomerGroupInput payload) {
        return createCustomerGroup((Object) payload, null);
    }

    public ApiResponse createCustomerGroup(CustomerGroupInput payload, RequestOptions options) {
        return createCustomerGroup((Object) payload, options);
    }

    /** Retrieve one group — {@code GET /admin/customer_groups/{customer_group_id}}. */
    public ApiResponse getCustomerGroup(Object customerGroupId) {
        return getCustomerGroup(customerGroupId, null, null);
    }

    public ApiResponse getCustomerGroup(Object customerGroupId, Map<String, Object> queryParams) {
        return getCustomerGroup(customerGroupId, queryParams, null);
    }

    public ApiResponse getCustomerGroup(Object customerGroupId, Map<String, Object> queryParams,
            RequestOptions options) {
        return client.sendGetRequest(CustomerGroupsUrls.CUSTOMER_GROUP,
                RequestOptions.builder(options).pathParams(pathParams(customerGroupId))
                        .queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(CustomerGroup.class, body)).build());
    }

    /** Change only the fields sent — {@code PATCH /admin/customer_groups/{customer_group_id}}. */
    public ApiResponse patchCustomerGroup(Object customerGroupId, Object payload) {
        return patchCustomerGroup(customerGroupId, payload, null);
    }

    public ApiResponse patchCustomerGroup(Object customerGroupId, Object payload,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPatchRequest(CustomerGroupsUrls.CUSTOMER_GROUP,
                RequestOptions.builder(options).pathParams(pathParams(customerGroupId))
                        .jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(CustomerGroup.class, body)).build());
    }

    public ApiResponse patchCustomerGroup(Object customerGroupId, CustomerGroupInput payload) {
        return patchCustomerGroup(customerGroupId, (Object) payload, null);
    }

    public ApiResponse patchCustomerGroup(Object customerGroupId, CustomerGroupInput payload,
            RequestOptions options) {
        return patchCustomerGroup(customerGroupId, (Object) payload, options);
    }

    /** Delete one group and its memberships — {@code DELETE .../{customer_group_id}}. */
    public ApiResponse deleteCustomerGroup(Object customerGroupId) {
        return deleteCustomerGroup(customerGroupId, null);
    }

    public ApiResponse deleteCustomerGroup(Object customerGroupId, RequestOptions options) {
        return client.sendDeleteRequest(CustomerGroupsUrls.CUSTOMER_GROUP,
                RequestOptions.builder(options).pathParams(pathParams(customerGroupId))
                        .dtoFactory(null).build());
    }

    /** Replace the member list — {@code PATCH .../{customer_group_id}/customers}. */
    public ApiResponse updateCustomerGroupCustomers(Object customerGroupId, Object payload) {
        return updateCustomerGroupCustomers(customerGroupId, payload, null);
    }

    public ApiResponse updateCustomerGroupCustomers(Object customerGroupId, Object payload,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPatchRequest(CustomerGroupsUrls.CUSTOMER_GROUP_CUSTOMERS,
                RequestOptions.builder(options).pathParams(pathParams(customerGroupId))
                        .jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(CustomerGroup.class, body)).build());
    }

    public ApiResponse updateCustomerGroupCustomers(Object customerGroupId,
            CustomerGroupCustomersInput payload) {
        return updateCustomerGroupCustomers(customerGroupId, (Object) payload, null);
    }

    public ApiResponse updateCustomerGroupCustomers(Object customerGroupId,
            CustomerGroupCustomersInput payload, RequestOptions options) {
        return updateCustomerGroupCustomers(customerGroupId, (Object) payload, options);
    }

    /** Read one member — {@code GET .../{customer_group_id}/customers/{customer_id}}. */
    public ApiResponse getCustomerGroupCustomer(Object customerGroupId, Object customerId) {
        return getCustomerGroupCustomer(customerGroupId, customerId, null);
    }

    public ApiResponse getCustomerGroupCustomer(Object customerGroupId, Object customerId,
            RequestOptions options) {
        Map<String, Object> params = pathParams(customerGroupId);
        params.put("customer_id", customerId);
        return client.sendGetRequest(CustomerGroupsUrls.CUSTOMER_GROUP_CUSTOMER,
                RequestOptions.builder(options).pathParams(params)
                        .dtoFactory(body -> Dto.fromMap(CustomerGroupMember.class, body)).build());
    }

    /** Add one customer to one group — {@code POST /admin/customer_groups/assign}. */
    public ApiResponse assignCustomerGroup(Object payload) {
        return assignCustomerGroup(payload, null);
    }

    public ApiResponse assignCustomerGroup(Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPostRequest(CustomerGroupsUrls.CUSTOMER_GROUPS_ASSIGN,
                RequestOptions.builder(options).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(CustomerGroupAssignment.class, body))
                        .build());
    }

    public ApiResponse assignCustomerGroup(CustomerGroupAssignInput payload) {
        return assignCustomerGroup((Object) payload, null);
    }

    public ApiResponse assignCustomerGroup(CustomerGroupAssignInput payload,
            RequestOptions options) {
        return assignCustomerGroup((Object) payload, options);
    }

    private static Map<String, Object> pathParams(Object customerGroupId) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("customer_group_id", customerGroupId);
        return params;
    }
}
