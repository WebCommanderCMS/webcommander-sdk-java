package com.webcommander.resources.dashboard_banners;

import com.webcommander.Dto;
import com.webcommander.client.ApiResponse;
import com.webcommander.client.BoundResource;
import com.webcommander.client.RequestOptions;
import com.webcommander.client.ResourceBinding;
import com.webcommander.client.WebCommanderClient;
import com.webcommander.resources.dashboard_banners.dashboard_banners_dto.DashboardBannersDto.DashboardBanner;
import com.webcommander.resources.dashboard_banners.dashboard_banners_dto.DashboardBannersDto.DashboardBannerInput;
import com.webcommander.resources.dashboard_banners.dashboard_banners_dto.DashboardBannersDto.DashboardBannerListing;
import com.webcommander.resources.dashboard_banners.dashboard_banners_dto.DashboardBannersDto.DashboardContentInfo;

import java.util.LinkedHashMap;
import java.util.Map;

/** Dashboard banners — {@code /api/{api_version}/admin/dashboard/banners}. */
public final class DashboardBanners implements BoundResource {

    private final WebCommanderClient client;

    public DashboardBanners(WebCommanderClient client) {
        this.client = client;
    }

    /**
     * List every banner, active or not — {@code GET /admin/dashboard/banners},
     * {@code {"banners": [...]}} in position order, positions counting from 1 without a gap.
     */
    public ApiResponse listDashboardBanners() {
        return listDashboardBanners(null);
    }

    public ApiResponse listDashboardBanners(RequestOptions options) {
        return client.sendGetRequest(DashboardBannersUrls.DASHBOARD_BANNERS,
                RequestOptions.builder(options)
                        .dtoFactory(body -> Dto.fromMap(DashboardBannerListing.class, body))
                        .build());
    }

    /**
     * Create a banner — {@code POST /admin/dashboard/banners}, answered {@code 201} with the
     * banner bare and no {@code Location}. The body is bare; every failing field is listed in
     * one {@code 400}, a {@code key} already used is {@code 400} {@code duplicate}, an unknown
     * field is {@code 400}. It joins the Dashboard feed at once while {@code active}.
     */
    public ApiResponse createDashboardBanner(Object payload) {
        return createDashboardBanner(payload, null);
    }

    public ApiResponse createDashboardBanner(Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPostRequest(DashboardBannersUrls.DASHBOARD_BANNERS,
                RequestOptions.builder(options).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(DashboardBanner.class, body)).build());
    }

    public ApiResponse createDashboardBanner(DashboardBannerInput payload) {
        return createDashboardBanner((Object) payload, null);
    }

    public ApiResponse createDashboardBanner(DashboardBannerInput payload, RequestOptions options) {
        return createDashboardBanner((Object) payload, options);
    }

    /**
     * Read one banner by its {@code key} — {@code GET /admin/dashboard/banners/{banner_key}},
     * the banner bare, hidden ones included. An unknown key is {@code 404}.
     */
    public ApiResponse getDashboardBanner(String bannerKey) {
        return getDashboardBanner(bannerKey, null);
    }

    public ApiResponse getDashboardBanner(String bannerKey, RequestOptions options) {
        return client.sendGetRequest(DashboardBannersUrls.DASHBOARD_BANNER,
                RequestOptions.builder(options).pathParams(pathParams(bannerKey))
                        .dtoFactory(body -> Dto.fromMap(DashboardBanner.class, body)).build());
    }

    /**
     * Change only the fields sent — {@code PATCH /admin/dashboard/banners/{banner_key}},
     * answered {@code 200} with the banner. An empty body, a blank {@code title}, a {@code key}
     * and an unknown field are {@code 400}; an unknown banner is {@code 404}. {@code learn_more}
     * and {@code try_it} are replaced whole, and {@code try_it} {@code null} (which takes a
     * {@code Map}) removes the second button. {@code updated_at} is not moved.
     */
    public ApiResponse patchDashboardBanner(String bannerKey, Object payload) {
        return patchDashboardBanner(bannerKey, payload, null);
    }

    public ApiResponse patchDashboardBanner(String bannerKey, Object payload,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPatchRequest(DashboardBannersUrls.DASHBOARD_BANNER,
                RequestOptions.builder(options).pathParams(pathParams(bannerKey))
                        .jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(DashboardBanner.class, body)).build());
    }

    public ApiResponse patchDashboardBanner(String bannerKey, DashboardBannerInput payload) {
        return patchDashboardBanner(bannerKey, (Object) payload, null);
    }

    public ApiResponse patchDashboardBanner(String bannerKey, DashboardBannerInput payload,
            RequestOptions options) {
        return patchDashboardBanner(bannerKey, (Object) payload, options);
    }

    /**
     * Delete one banner — {@code DELETE /admin/dashboard/banners/{banner_key}}, {@code 204} with
     * no body. The banners after it move up, so the positions stay without a gap; an unknown key
     * is {@code 404}.
     */
    public ApiResponse deleteDashboardBanner(String bannerKey) {
        return deleteDashboardBanner(bannerKey, null);
    }

    public ApiResponse deleteDashboardBanner(String bannerKey, RequestOptions options) {
        return client.sendDeleteRequest(DashboardBannersUrls.DASHBOARD_BANNER,
                RequestOptions.builder(options).pathParams(pathParams(bannerKey))
                        .dtoFactory(null).build());
    }

    /**
     * Read what the Dashboard shows besides the figures — {@code GET
     * /admin/dashboard/content_info}: the active banners under {@code banners}, the support card
     * under {@code contact}, {@code online_store} and {@code member_label}. A hidden banner is
     * not listed.
     */
    public ApiResponse getDashboardContentInfo() {
        return getDashboardContentInfo(null);
    }

    public ApiResponse getDashboardContentInfo(RequestOptions options) {
        return client.sendGetRequest(DashboardBannersUrls.DASHBOARD_CONTENT_INFO,
                RequestOptions.builder(options)
                        .dtoFactory(body -> Dto.fromMap(DashboardContentInfo.class, body))
                        .build());
    }

    private static Map<String, Object> pathParams(String bannerKey) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("banner_key", bannerKey);
        return params;
    }
}
