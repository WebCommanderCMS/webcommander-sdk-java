package com.webcommander.resources.dashboards;

/** Path templates for the dashboard layout endpoints. */
public final class DashboardsUrls {

    private DashboardsUrls() {}

    public static final String DASHBOARDS = "/api/{api_version}/admin/dashboards";

    public static final String DASHBOARD = "/api/{api_version}/admin/dashboards/{dashboard_id}";
}
