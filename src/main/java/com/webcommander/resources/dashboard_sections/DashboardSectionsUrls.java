package com.webcommander.resources.dashboard_sections;

import java.util.List;

/** Path templates for the dashboard section endpoints. */
public final class DashboardSectionsUrls {

    private DashboardSectionsUrls() {}

    public static final String DASHBOARD_SECTION =
            "/api/{api_version}/admin/dashboard/sections/{section}";

    /** The five {@code {section}} path values, the tabs of the Dashboard's {@code section_tabs}. */
    public static final List<String> DASHBOARD_SECTIONS =
            List.of("ecommerce", "content", "bookings", "courses", "blog");
}
