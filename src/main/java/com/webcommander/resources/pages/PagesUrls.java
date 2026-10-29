package com.webcommander.resources.pages;

/** Path templates for the page endpoints. */
public final class PagesUrls {

    private PagesUrls() {}

    public static final String PAGES = "/api/{api_version}/admin/pages";

    public static final String PAGES_COUNT = "/api/{api_version}/admin/pages/count";

    public static final String PAGE = "/api/{api_version}/admin/pages/{page_id}";

    public static final String PAGE_URL = "/api/{api_version}/admin/pages/{page_id}/url";

    public static final String PAGE_LANDING = "/api/{api_version}/admin/pages/{page_id}/landing";

    public static final String PAGE_DELETE_BLOCKERS =
            "/api/{api_version}/admin/pages/{page_id}/delete-blockers";

    public static final String PAGE_DUPLICATE =
            "/api/{api_version}/admin/pages/{page_id}/duplicate";

    /** Carries the {@code ETag} a read answered; a stale one makes the write {@code 412}. */
    public static final String IF_MATCH_HEADER = "If-Match";
}
