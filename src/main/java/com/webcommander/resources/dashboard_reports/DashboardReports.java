package com.webcommander.resources.dashboard_reports;

import com.webcommander.Dto;
import com.webcommander.client.ApiResponse;
import com.webcommander.client.BoundResource;
import com.webcommander.client.RequestOptions;
import com.webcommander.client.WebCommanderClient;
import com.webcommander.resources.dashboard_reports.dashboard_reports_dto.DashboardReportsDto.QuickReport;
import com.webcommander.resources.dashboard_reports.dashboard_reports_dto.DashboardReportsDto.SalesReport;
import com.webcommander.resources.dashboard_reports.dashboard_reports_dto.DashboardReportsDto.SummaryReport;

import java.util.Map;

/**
 * Dashboard reports — {@code /api/{api_version}/admin/dashboard/reports}. Every report takes the
 * same period query: {@code duration} ({@code today}, {@code yesterday}, {@code last_24_hours},
 * {@code last_7_days}, {@code last_30_days}, {@code this_month}, {@code last_month},
 * {@code last_3_months}, {@code last_6_months}, {@code last_12_months}, {@code last_24_months},
 * {@code last_36_months}, {@code last_48_months}, {@code last_60_months} or {@code custom};
 * {@code last_30_days} when left out) or {@code start_date} with {@code end_date}
 * ({@code YYYY-MM-DD}, sent together, not reversed and not in the future). A bad value, a
 * repeated {@code duration} and a lone date are {@code 400} with every bad field listed; a name
 * the report does not know is ignored.
 */
public final class DashboardReports implements BoundResource {

    private final WebCommanderClient client;

    public DashboardReports(WebCommanderClient client) {
        this.client = client;
    }

    /**
     * Read the quick report — {@code GET /admin/dashboard/reports/quick}: {@code period},
     * {@code kpis}, grouped totals, the most viewed pages under {@code view_report} and
     * {@code trends}. {@code kpis} is a comma list of metric keys (an unknown one is {@code 400})
     * that narrows {@code kpis} to those, in that order; {@code entity_name} is {@code page} (the
     * default) or {@code product} and picks what {@code view_report} ranks.
     */
    public ApiResponse getDashboardReportQuick() {
        return getDashboardReportQuick(null, null);
    }

    public ApiResponse getDashboardReportQuick(Map<String, Object> queryParams) {
        return getDashboardReportQuick(queryParams, null);
    }

    public ApiResponse getDashboardReportQuick(Map<String, Object> queryParams,
            RequestOptions options) {
        return client.sendGetRequest(DashboardReportsUrls.DASHBOARD_REPORT_QUICK,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(QuickReport.class, body)).build());
    }

    /**
     * Ask whether the quick report is reachable — {@code HEAD /admin/dashboard/reports/quick};
     * the status of the read, with no body. It runs the read's query, so a bad value is
     * {@code 400}.
     */
    public ApiResponse headDashboardReportQuick() {
        return headDashboardReportQuick(null, null);
    }

    public ApiResponse headDashboardReportQuick(Map<String, Object> queryParams) {
        return headDashboardReportQuick(queryParams, null);
    }

    public ApiResponse headDashboardReportQuick(Map<String, Object> queryParams,
            RequestOptions options) {
        return client.sendApiRequest("HEAD", DashboardReportsUrls.DASHBOARD_REPORT_QUICK,
                RequestOptions.builder(options).queryParams(queryParams).dtoFactory(null)
                        .build());
    }

    /**
     * Read the sales report — {@code GET /admin/dashboard/reports/sales}: {@code rows} by day or
     * month and {@code totals}. {@code breakdown} ({@code true} or {@code false}) adds orders,
     * shipping, tax, discount, refunds and the top product to each row and total; {@code sort}
     * is {@code date} (the default) or {@code sales} and {@code order} is {@code asc} (the
     * default) or {@code desc}; any other value is {@code 400}.
     */
    public ApiResponse getDashboardReportSales() {
        return getDashboardReportSales(null, null);
    }

    public ApiResponse getDashboardReportSales(Map<String, Object> queryParams) {
        return getDashboardReportSales(queryParams, null);
    }

    public ApiResponse getDashboardReportSales(Map<String, Object> queryParams,
            RequestOptions options) {
        return client.sendGetRequest(DashboardReportsUrls.DASHBOARD_REPORT_SALES,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(SalesReport.class, body)).build());
    }

    /**
     * Ask whether the sales report is reachable — {@code HEAD /admin/dashboard/reports/sales};
     * the status of the read, with no body. It runs the read's query, so a bad value is
     * {@code 400}.
     */
    public ApiResponse headDashboardReportSales() {
        return headDashboardReportSales(null, null);
    }

    public ApiResponse headDashboardReportSales(Map<String, Object> queryParams) {
        return headDashboardReportSales(queryParams, null);
    }

    public ApiResponse headDashboardReportSales(Map<String, Object> queryParams,
            RequestOptions options) {
        return client.sendApiRequest("HEAD", DashboardReportsUrls.DASHBOARD_REPORT_SALES,
                RequestOptions.builder(options).queryParams(queryParams).dtoFactory(null)
                        .build());
    }

    /**
     * Read the summary — {@code GET /admin/dashboard/reports/summary}: the quick report without
     * {@code kpis}, orders by month, sales rows, the most visited and best selling products and
     * the order updates, in one answer. {@code compare_previous} ({@code true} or {@code false})
     * is checked as a boolean; any other value is {@code 400}.
     */
    public ApiResponse getDashboardReportSummary() {
        return getDashboardReportSummary(null, null);
    }

    public ApiResponse getDashboardReportSummary(Map<String, Object> queryParams) {
        return getDashboardReportSummary(queryParams, null);
    }

    public ApiResponse getDashboardReportSummary(Map<String, Object> queryParams,
            RequestOptions options) {
        return client.sendGetRequest(DashboardReportsUrls.DASHBOARD_REPORT_SUMMARY,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(SummaryReport.class, body)).build());
    }

    /**
     * Ask whether the summary is reachable — {@code HEAD /admin/dashboard/reports/summary}; the
     * status of the read, with no body. It runs the read's query, so a bad value is {@code 400}.
     */
    public ApiResponse headDashboardReportSummary() {
        return headDashboardReportSummary(null, null);
    }

    public ApiResponse headDashboardReportSummary(Map<String, Object> queryParams) {
        return headDashboardReportSummary(queryParams, null);
    }

    public ApiResponse headDashboardReportSummary(Map<String, Object> queryParams,
            RequestOptions options) {
        return client.sendApiRequest("HEAD", DashboardReportsUrls.DASHBOARD_REPORT_SUMMARY,
                RequestOptions.builder(options).queryParams(queryParams).dtoFactory(null)
                        .build());
    }
}
