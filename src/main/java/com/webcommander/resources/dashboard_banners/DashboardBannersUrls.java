package com.webcommander.resources.dashboard_banners;

/** Path templates for the dashboard banner endpoints. */
public final class DashboardBannersUrls {

    private DashboardBannersUrls() {}

    public static final String DASHBOARD_BANNERS = "/api/{api_version}/admin/dashboard/banners";

    public static final String DASHBOARD_BANNER =
            "/api/{api_version}/admin/dashboard/banners/{banner_key}";

    public static final String DASHBOARD_CONTENT_INFO =
            "/api/{api_version}/admin/dashboard/content_info";
}
