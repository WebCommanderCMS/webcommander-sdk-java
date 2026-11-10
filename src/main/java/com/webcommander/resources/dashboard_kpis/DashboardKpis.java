package com.webcommander.resources.dashboard_kpis;

import com.webcommander.Dto;
import com.webcommander.client.ApiResponse;
import com.webcommander.client.BoundResource;
import com.webcommander.client.RequestOptions;
import com.webcommander.client.ResourceBinding;
import com.webcommander.client.WebCommanderClient;
import com.webcommander.resources.dashboard_kpis.dashboard_kpis_dto.DashboardKpisDto.KpiOptions;
import com.webcommander.resources.dashboard_kpis.dashboard_kpis_dto.DashboardKpisDto.KpiSelection;
import com.webcommander.resources.dashboard_kpis.dashboard_kpis_dto.DashboardKpisDto.KpiSelectionInput;

import java.util.Map;

/** Dashboard KPI tiles — {@code /api/{api_version}/admin/dashboard/kpi_selection}. */
public final class DashboardKpis implements BoundResource {

    private final WebCommanderClient client;

    public DashboardKpis(WebCommanderClient client) {
        this.client = client;
    }

    /**
     * List the metrics a tile can show — {@code GET /admin/dashboard/kpi_options}, grouped, with
     * {@code min} 2 and {@code max} 5 tiles and the keys now in use under {@code selected}. It
     * reads the same tiles as {@code kpi_selection}.
     */
    public ApiResponse getDashboardKpiOptions() {
        return getDashboardKpiOptions(null);
    }

    public ApiResponse getDashboardKpiOptions(RequestOptions options) {
        return client.sendGetRequest(DashboardKpisUrls.DASHBOARD_KPI_OPTIONS,
                RequestOptions.builder(options)
                        .dtoFactory(body -> Dto.fromMap(KpiOptions.class, body)).build());
    }

    /**
     * Read the tiles in use with their figures — {@code GET /admin/dashboard/kpi_selection}, the
     * tiles of the default layout under {@code selected} in position order. {@code duration}
     * ({@code last_30_days} when left out) or {@code start_date} with {@code end_date}
     * ({@code YYYY-MM-DD}, sent together) choose the period; a bad value is {@code 400}.
     */
    public ApiResponse getDashboardKpiSelection() {
        return getDashboardKpiSelection(null, null);
    }

    public ApiResponse getDashboardKpiSelection(Map<String, Object> queryParams) {
        return getDashboardKpiSelection(queryParams, null);
    }

    public ApiResponse getDashboardKpiSelection(Map<String, Object> queryParams,
            RequestOptions options) {
        return client.sendGetRequest(DashboardKpisUrls.DASHBOARD_KPI_SELECTION,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(KpiSelection.class, body)).build());
    }

    /**
     * Save the tiles — {@code PUT /admin/dashboard/kpi_selection} with
     * {@code {"metrics": [...]}}, answered {@code 200} like the read. The list replaces the
     * default layout's tiles, which is store-wide state, and is read back at once by that layout
     * and by {@code kpi_options}. Fewer than 2 or more than 5, a repeat, an unknown key, a
     * missing {@code metrics} and a non-list are {@code 400}. {@code duration} or the dates in
     * the query set the period of the figures in the answer.
     */
    public ApiResponse updateDashboardKpiSelection(Object payload) {
        return updateDashboardKpiSelection(payload, null, null);
    }

    public ApiResponse updateDashboardKpiSelection(Object payload, RequestOptions options) {
        return updateDashboardKpiSelection(payload, null, options);
    }

    public ApiResponse updateDashboardKpiSelection(Object payload,
            Map<String, Object> queryParams) {
        return updateDashboardKpiSelection(payload, queryParams, null);
    }

    public ApiResponse updateDashboardKpiSelection(Object payload, Map<String, Object> queryParams,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPutRequest(DashboardKpisUrls.DASHBOARD_KPI_SELECTION,
                RequestOptions.builder(options).jsonBody(payload).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(KpiSelection.class, body)).build());
    }

    public ApiResponse updateDashboardKpiSelection(KpiSelectionInput payload) {
        return updateDashboardKpiSelection((Object) payload, null, null);
    }

    public ApiResponse updateDashboardKpiSelection(KpiSelectionInput payload,
            RequestOptions options) {
        return updateDashboardKpiSelection((Object) payload, null, options);
    }

    public ApiResponse updateDashboardKpiSelection(KpiSelectionInput payload,
            Map<String, Object> queryParams) {
        return updateDashboardKpiSelection((Object) payload, queryParams, null);
    }

    public ApiResponse updateDashboardKpiSelection(KpiSelectionInput payload,
            Map<String, Object> queryParams, RequestOptions options) {
        return updateDashboardKpiSelection((Object) payload, queryParams, options);
    }
}
