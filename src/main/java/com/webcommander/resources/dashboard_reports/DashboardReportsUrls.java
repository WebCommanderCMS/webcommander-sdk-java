package com.webcommander.resources.dashboard_reports;

/** Path templates for the dashboard report endpoints. */
public final class DashboardReportsUrls {

    private DashboardReportsUrls() {}

    public static final String DASHBOARD_REPORT_QUICK =
            "/api/{api_version}/admin/dashboard/reports/quick";

    public static final String DASHBOARD_REPORT_SALES =
            "/api/{api_version}/admin/dashboard/reports/sales";

    public static final String DASHBOARD_REPORT_SUMMARY =
            "/api/{api_version}/admin/dashboard/reports/summary";
}
