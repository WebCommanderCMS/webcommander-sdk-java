package com.webcommander.resources.customer_groups;

/** Path templates for the customer group endpoints. */
public final class CustomerGroupsUrls {

    private CustomerGroupsUrls() {}

    public static final String CUSTOMER_GROUPS = "/api/{api_version}/admin/customer_groups";

    public static final String CUSTOMER_GROUPS_COUNT =
            "/api/{api_version}/admin/customer_groups/count";

    public static final String CUSTOMER_GROUPS_ASSIGN =
            "/api/{api_version}/admin/customer_groups/assign";

    public static final String CUSTOMER_GROUP =
            "/api/{api_version}/admin/customer_groups/{customer_group_id}";

    public static final String CUSTOMER_GROUP_CUSTOMERS =
            "/api/{api_version}/admin/customer_groups/{customer_group_id}/customers";

    public static final String CUSTOMER_GROUP_CUSTOMER =
            "/api/{api_version}/admin/customer_groups/{customer_group_id}/customers/{customer_id}";
}
