package com.webcommander.resources.trash;

import com.webcommander.Dto;
import com.webcommander.client.ApiResponse;
import com.webcommander.client.BoundResource;
import com.webcommander.client.RequestOptions;
import com.webcommander.client.ResourceBinding;
import com.webcommander.client.WebCommanderClient;
import com.webcommander.resources.trash.trash_dto.TrashDto.TrashBulkDeleteResult;
import com.webcommander.resources.trash.trash_dto.TrashDto.TrashBulkInput;
import com.webcommander.resources.trash.trash_dto.TrashDto.TrashBulkRestoreResult;
import com.webcommander.resources.trash.trash_dto.TrashDto.TrashItem;
import com.webcommander.resources.trash.trash_dto.TrashDto.TrashPage;
import com.webcommander.resources.trash.trash_dto.TrashDto.TrashTypeList;

import java.util.LinkedHashMap;
import java.util.Map;

/** The store's trash — {@code /api/{api_version}/admin/trash}. */
public final class Trash implements BoundResource {

    private final WebCommanderClient client;

    public Trash(WebCommanderClient client) {
        this.client = client;
    }

    /**
     * List trashed rows, newest change first — {@code GET /admin/trash}. {@code type} (a
     * {@code types} value, any case, or a comma list), {@code search}, {@code sort}, {@code order},
     * {@code limit} (above 100 is {@code 400}), {@code offset}, {@code page},
     * {@code removed_from} and {@code removed_to}; any other name is {@code 400}.
     */
    public ApiResponse listTrash() {
        return listTrash(null, null);
    }

    public ApiResponse listTrash(Map<String, Object> queryParams) {
        return listTrash(queryParams, null);
    }

    public ApiResponse listTrash(Map<String, Object> queryParams, RequestOptions options) {
        return client.sendGetRequest(TrashUrls.TRASH,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(TrashPage.class, body)).build());
    }

    /**
     * Ask whether the listing is reachable — {@code HEAD /admin/trash}; no body. It runs the
     * listing's query, so pass a {@code type} as on the {@code GET}.
     */
    public ApiResponse headTrash() {
        return headTrash(null, null);
    }

    public ApiResponse headTrash(Map<String, Object> queryParams) {
        return headTrash(queryParams, null);
    }

    public ApiResponse headTrash(Map<String, Object> queryParams, RequestOptions options) {
        return client.sendApiRequest("HEAD", TrashUrls.TRASH,
                RequestOptions.builder(options).queryParams(queryParams).dtoFactory(null).build());
    }

    /** List the types the trash holds, snake_case — {@code GET /admin/trash/types}. */
    public ApiResponse listTrashTypes() {
        return listTrashTypes(null);
    }

    public ApiResponse listTrashTypes(RequestOptions options) {
        return client.sendGetRequest(TrashUrls.TRASH_TYPES,
                RequestOptions.builder(options)
                        .dtoFactory(body -> Dto.fromMap(TrashTypeList.class, body)).build());
    }

    /** Ask whether the type list is reachable — {@code HEAD /admin/trash/types}; no body. */
    public ApiResponse headTrashTypes() {
        return headTrashTypes(null);
    }

    public ApiResponse headTrashTypes(RequestOptions options) {
        return client.sendApiRequest("HEAD", TrashUrls.TRASH_TYPES,
                RequestOptions.builder(options).dtoFactory(null).build());
    }

    /**
     * Restore one trashed row — {@code POST .../{trash_type}/{trash_item_id}/restore}, answered
     * {@code 200} with the row under {@code item}; sends no body. A live row is {@code 409}, an
     * id the type does not hold {@code 404}.
     */
    public ApiResponse restoreTrashItem(String trashType, Object trashItemId) {
        return restoreTrashItem(trashType, trashItemId, null);
    }

    public ApiResponse restoreTrashItem(String trashType, Object trashItemId,
            RequestOptions options) {
        return client.sendPostRequest(TrashUrls.TRASH_ITEM_RESTORE,
                RequestOptions.builder(options).pathParams(pathParams(trashType, trashItemId))
                        .dtoFactory(body -> Dto.fromMap(TrashItem.class, body)).build());
    }

    /**
     * Delete one trashed row for good — {@code DELETE /admin/trash/{trash_type}/{trash_item_id}};
     * {@code 204}. A live row is {@code 409}; once purged, a second delete is {@code 404}.
     */
    public ApiResponse deleteTrashItem(String trashType, Object trashItemId) {
        return deleteTrashItem(trashType, trashItemId, null);
    }

    public ApiResponse deleteTrashItem(String trashType, Object trashItemId,
            RequestOptions options) {
        return client.sendDeleteRequest(TrashUrls.TRASH_ITEM,
                RequestOptions.builder(options).pathParams(pathParams(trashType, trashItemId))
                        .dtoFactory(null).build());
    }

    /**
     * Restore up to 100 rows — {@code POST /admin/trash/bulk-restore} with
     * {@code {"items": [{"type", "id"}]}}. {@code 200} when every item is restored,
     * {@code 207} with the same body when any fails.
     */
    public ApiResponse bulkRestoreTrash(Object payload) {
        return bulkRestoreTrash(payload, null);
    }

    public ApiResponse bulkRestoreTrash(Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPostRequest(TrashUrls.TRASH_BULK_RESTORE,
                RequestOptions.builder(options).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(TrashBulkRestoreResult.class, body))
                        .build());
    }

    public ApiResponse bulkRestoreTrash(TrashBulkInput payload) {
        return bulkRestoreTrash((Object) payload, null);
    }

    public ApiResponse bulkRestoreTrash(TrashBulkInput payload, RequestOptions options) {
        return bulkRestoreTrash((Object) payload, options);
    }

    /**
     * Delete up to 100 trashed rows for good — {@code POST /admin/trash/bulk-delete}, the bulk
     * restore's body. {@code 200} when every item is deleted, {@code 207} with the same body
     * when any fails.
     */
    public ApiResponse bulkDeleteTrash(Object payload) {
        return bulkDeleteTrash(payload, null);
    }

    public ApiResponse bulkDeleteTrash(Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPostRequest(TrashUrls.TRASH_BULK_DELETE,
                RequestOptions.builder(options).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(TrashBulkDeleteResult.class, body))
                        .build());
    }

    public ApiResponse bulkDeleteTrash(TrashBulkInput payload) {
        return bulkDeleteTrash((Object) payload, null);
    }

    public ApiResponse bulkDeleteTrash(TrashBulkInput payload, RequestOptions options) {
        return bulkDeleteTrash((Object) payload, options);
    }

    private static Map<String, Object> pathParams(String trashType, Object trashItemId) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("trash_type", trashType);
        params.put("trash_item_id", trashItemId);
        return params;
    }
}
