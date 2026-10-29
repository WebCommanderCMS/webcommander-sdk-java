package com.webcommander.resources.trash;

/** Path templates for the trash endpoints. */
public final class TrashUrls {

    private TrashUrls() {}

    public static final String TRASH = "/api/{api_version}/admin/trash";

    public static final String TRASH_TYPES = "/api/{api_version}/admin/trash/types";

    public static final String TRASH_ITEM =
            "/api/{api_version}/admin/trash/{trash_type}/{trash_item_id}";

    public static final String TRASH_ITEM_RESTORE =
            "/api/{api_version}/admin/trash/{trash_type}/{trash_item_id}/restore";

    public static final String TRASH_BULK_RESTORE = "/api/{api_version}/admin/trash/bulk-restore";

    public static final String TRASH_BULK_DELETE = "/api/{api_version}/admin/trash/bulk-delete";
}
