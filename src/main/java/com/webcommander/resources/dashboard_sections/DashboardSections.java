package com.webcommander.resources.dashboard_sections;

import com.webcommander.Dto;
import com.webcommander.client.ApiResponse;
import com.webcommander.client.BoundResource;
import com.webcommander.client.RequestOptions;
import com.webcommander.client.WebCommanderClient;
import com.webcommander.resources.dashboard_sections.dashboard_sections_dto.DashboardSectionsDto.DashboardSectionReport;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Dashboard sections — {@code /api/{api_version}/admin/dashboard/sections}. The Dashboard shows
 * five tabs; a tab is a path value.
 */
public final class DashboardSections implements BoundResource {

    private final WebCommanderClient client;

    public DashboardSections(WebCommanderClient client) {
        this.client = client;
    }

    /**
     * Read one tab — {@code GET /admin/dashboard/sections/{section}}, {@code section} being one
     * of {@link DashboardSectionsUrls#DASHBOARD_SECTIONS} ({@code ecommerce}, {@code content},
     * {@code bookings}, {@code courses}, {@code blog}); the answer is bare, {@code section}
     * echoing it, with {@code period} and that tab's own fields. Any other value is {@code 404}
     * {@code not_found} on {@code key}. {@code duration} or {@code start_date} with
     * {@code end_date} choose the period, as in the reports.
     */
    public ApiResponse getDashboardSection(String section) {
        return getDashboardSection(section, null, null);
    }

    public ApiResponse getDashboardSection(String section, Map<String, Object> queryParams) {
        return getDashboardSection(section, queryParams, null);
    }

    public ApiResponse getDashboardSection(String section, Map<String, Object> queryParams,
            RequestOptions options) {
        Map<String, Object> pathParams = new LinkedHashMap<>();
        pathParams.put("section", section);
        return client.sendGetRequest(DashboardSectionsUrls.DASHBOARD_SECTION,
                RequestOptions.builder(options).pathParams(pathParams).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(DashboardSectionReport.class, body))
                        .build());
    }
}
