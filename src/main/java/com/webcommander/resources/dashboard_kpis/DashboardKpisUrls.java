package com.webcommander.resources.dashboard_kpis;

/** Path templates for the dashboard KPI endpoints. */
public final class DashboardKpisUrls {

    private DashboardKpisUrls() {}

    public static final String DASHBOARD_KPI_OPTIONS =
            "/api/{api_version}/admin/dashboard/kpi_options";

    public static final String DASHBOARD_KPI_SELECTION =
            "/api/{api_version}/admin/dashboard/kpi_selection";
}
