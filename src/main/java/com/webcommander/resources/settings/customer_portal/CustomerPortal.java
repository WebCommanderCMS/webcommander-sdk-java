package com.webcommander.resources.settings.customer_portal;

import com.webcommander.Dto;
import com.webcommander.client.ApiResponse;
import com.webcommander.client.BoundResource;
import com.webcommander.client.RequestOptions;
import com.webcommander.client.ResourceBinding;
import com.webcommander.client.WebCommanderClient;
import com.webcommander.resources.settings.customer_portal.customer_portal_dto
        .CustomerPortalDto;

import java.util.LinkedHashMap;
import java.util.Map;

/** Customer portal settings — {@code /api/{api_version}/admin/settings/customer_portal}. */
public final class CustomerPortal implements BoundResource {

    private final WebCommanderClient client;

    public CustomerPortal(WebCommanderClient client) {
        this.client = client;
    }

    /** Read the whole area — {@code GET /admin/settings/customer_portal}. */
    public ApiResponse getCustomerPortal() {
        return getCustomerPortal(null, null);
    }

    public ApiResponse getCustomerPortal(Map<String, Object> queryParams) {
        return getCustomerPortal(queryParams, null);
    }

    public ApiResponse getCustomerPortal(Map<String, Object> queryParams, RequestOptions options) {
        return client.sendGetRequest(CustomerPortalUrls.CUSTOMER_PORTAL,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(
                                CustomerPortalDto.CustomerPortal.class, body)).build());
    }

    /** Write fields — {@code PATCH /admin/settings/customer_portal}, in its facade wrapper. */
    public ApiResponse patchCustomerPortal(Object payload) {
        return patchCustomerPortal(payload, null);
    }

    public ApiResponse patchCustomerPortal(Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPatchRequest(CustomerPortalUrls.CUSTOMER_PORTAL,
                RequestOptions.builder(options).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(
                                CustomerPortalDto.CustomerPortal.class, body)).build());
    }

    /** Ask whether the area is reachable — {@code HEAD /admin/settings/customer_portal}. */
    public ApiResponse headCustomerPortal() {
        return headCustomerPortal(null);
    }

    public ApiResponse headCustomerPortal(RequestOptions options) {
        return client.sendApiRequest("HEAD", CustomerPortalUrls.CUSTOMER_PORTAL,
                RequestOptions.builder(options).dtoFactory(null).build());
    }

    /** Read one section — {@code GET /admin/settings/customer_portal/{section}}. */
    public ApiResponse getCustomerPortalSection(String section) {
        return getCustomerPortalSection(section, null, null);
    }

    public ApiResponse getCustomerPortalSection(String section, Map<String, Object> queryParams) {
        return getCustomerPortalSection(section, queryParams, null);
    }

    public ApiResponse getCustomerPortalSection(String section, Map<String, Object> queryParams,
            RequestOptions options) {
        return client.sendGetRequest(CustomerPortalUrls.CUSTOMER_PORTAL_SECTION,
                RequestOptions.builder(options).pathParams(pathParams("section", section))
                        .queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(
                                CustomerPortalDto.CustomerPortal.class, body)).build());
    }

    /** Write one section — {@code PATCH .../customer_portal/{section}}, keyed or bare. */
    public ApiResponse patchCustomerPortalSection(String section, Object payload) {
        return patchCustomerPortalSection(section, payload, null);
    }

    public ApiResponse patchCustomerPortalSection(String section, Object payload,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPatchRequest(CustomerPortalUrls.CUSTOMER_PORTAL_SECTION,
                RequestOptions.builder(options).pathParams(pathParams("section", section))
                        .jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(
                                CustomerPortalDto.CustomerPortal.class, body)).build());
    }

    private static Map<String, Object> pathParams(String name, Object value) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put(name, value);
        return params;
    }
}
