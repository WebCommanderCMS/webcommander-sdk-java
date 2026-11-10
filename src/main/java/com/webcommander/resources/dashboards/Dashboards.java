package com.webcommander.resources.dashboards;

import com.webcommander.Dto;
import com.webcommander.client.ApiResponse;
import com.webcommander.client.BoundResource;
import com.webcommander.client.RequestOptions;
import com.webcommander.client.ResourceBinding;
import com.webcommander.client.WebCommanderClient;
import com.webcommander.resources.dashboards.dashboards_dto.DashboardsDto.Dashboard;
import com.webcommander.resources.dashboards.dashboards_dto.DashboardsDto.DashboardInput;
import com.webcommander.resources.dashboards.dashboards_dto.DashboardsDto.DashboardListing;

import java.util.LinkedHashMap;
import java.util.Map;

/** Dashboard layouts — {@code /api/{api_version}/admin/dashboards}. */
public final class Dashboards implements BoundResource {

    private final WebCommanderClient client;

    public Dashboards(WebCommanderClient client) {
        this.client = client;
    }

    /**
     * List the admin's layouts — {@code GET /admin/dashboards}, {@code {"dashboards": [...]}}
     * with no paging. Each row is {@code id}, {@code name}, {@code default} and the two
     * timestamps, and exactly one row is the default.
     */
    public ApiResponse listDashboards() {
        return listDashboards(null);
    }

    public ApiResponse listDashboards(RequestOptions options) {
        return client.sendGetRequest(DashboardsUrls.DASHBOARDS,
                RequestOptions.builder(options)
                        .dtoFactory(body -> Dto.fromMap(DashboardListing.class, body)).build());
    }

    /**
     * Create a layout — {@code POST /admin/dashboards}, answered {@code 201} with the whole
     * layout, bare and with no {@code Location}. The body is bare and needs {@code name}, unique
     * among the admin's layouts ({@code 409} {@code name_taken}); an empty body is {@code 400}.
     * It starts as a copy of the layout named in {@code copy_from} or of the default one, then
     * the sections and {@code kpis} sent are applied. Unknown fields are ignored.
     */
    public ApiResponse createDashboard(Object payload) {
        return createDashboard(payload, null);
    }

    public ApiResponse createDashboard(Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPostRequest(DashboardsUrls.DASHBOARDS,
                RequestOptions.builder(options).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(Dashboard.class, body)).build());
    }

    public ApiResponse createDashboard(DashboardInput payload) {
        return createDashboard((Object) payload, null);
    }

    public ApiResponse createDashboard(DashboardInput payload, RequestOptions options) {
        return createDashboard((Object) payload, options);
    }

    /**
     * Read one layout by its {@code id} — {@code GET /admin/dashboards/{dashboard_id}}, the
     * layout bare. An unknown id is {@code 404} {@code not_found} on {@code dashboard_id}.
     */
    public ApiResponse getDashboard(String dashboardId) {
        return getDashboard(dashboardId, null);
    }

    public ApiResponse getDashboard(String dashboardId, RequestOptions options) {
        return client.sendGetRequest(DashboardsUrls.DASHBOARD,
                RequestOptions.builder(options).pathParams(pathParams(dashboardId))
                        .dtoFactory(body -> Dto.fromMap(Dashboard.class, body)).build());
    }

    /**
     * Change only the fields sent — {@code PATCH /admin/dashboards/{dashboard_id}}, answered
     * {@code 200} with the whole layout. An empty body is {@code 400}; unknown fields are
     * ignored. A blank {@code name} or one over 60 characters is {@code 400}, a section key the
     * layout does not have is {@code 400}, a standard section with a {@code position} is
     * {@code 400} and hiding {@code contact} is {@code 400}. {@code default} true moves the
     * store's default layout to this one.
     */
    public ApiResponse patchDashboard(String dashboardId, Object payload) {
        return patchDashboard(dashboardId, payload, null);
    }

    public ApiResponse patchDashboard(String dashboardId, Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPatchRequest(DashboardsUrls.DASHBOARD,
                RequestOptions.builder(options).pathParams(pathParams(dashboardId))
                        .jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(Dashboard.class, body)).build());
    }

    public ApiResponse patchDashboard(String dashboardId, DashboardInput payload) {
        return patchDashboard(dashboardId, (Object) payload, null);
    }

    public ApiResponse patchDashboard(String dashboardId, DashboardInput payload,
            RequestOptions options) {
        return patchDashboard(dashboardId, (Object) payload, options);
    }

    /**
     * Delete one layout — {@code DELETE /admin/dashboards/{dashboard_id}}, {@code 204} with no
     * body; the layout then reads {@code 404}.
     */
    public ApiResponse deleteDashboard(String dashboardId) {
        return deleteDashboard(dashboardId, null);
    }

    public ApiResponse deleteDashboard(String dashboardId, RequestOptions options) {
        return client.sendDeleteRequest(DashboardsUrls.DASHBOARD,
                RequestOptions.builder(options).pathParams(pathParams(dashboardId))
                        .dtoFactory(null).build());
    }

    private static Map<String, Object> pathParams(String dashboardId) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("dashboard_id", dashboardId);
        return params;
    }
}
