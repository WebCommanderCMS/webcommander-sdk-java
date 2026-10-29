package com.webcommander.resources.settings.search_settings;

import java.util.List;

/** Path templates for the search settings endpoints. */
public final class SearchSettingsUrls {

    private SearchSettingsUrls() {}

    public static final String SEARCH_SETTINGS =
            "/api/{api_version}/admin/settings/search_settings";

    public static final String SEARCH_SETTINGS_SECTION =
            "/api/{api_version}/admin/settings/search_settings/{section}";

    public static final String SEARCH_SETTINGS_REINDEX_STATUS =
            "/api/{api_version}/admin/settings/search_settings/reindex_status";

    /** The three values {@code {section}} takes, in the order the facade sends them. */
    public static final List<String> SEARCH_SETTINGS_SECTIONS = List.of("searchable_content",
            "search_fields", "synonyms");

    /** The one section to leave alone: writing it triggers a reindex, which cannot be undone. */
    public static final String SEARCH_SETTINGS_REINDEXING_SECTION = "synonyms";
}
