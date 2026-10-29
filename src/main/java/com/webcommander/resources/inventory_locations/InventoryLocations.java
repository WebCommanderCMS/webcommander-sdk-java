package com.webcommander.resources.inventory_locations;

import com.webcommander.Dto;
import com.webcommander.client.ApiResponse;
import com.webcommander.client.BoundResource;
import com.webcommander.client.RequestOptions;
import com.webcommander.client.ResourceBinding;
import com.webcommander.client.WebCommanderClient;
import com.webcommander.resources.inventory_locations.inventory_locations_dto
        .InventoryLocationsDto.InventoryLocation;
import com.webcommander.resources.inventory_locations.inventory_locations_dto
        .InventoryLocationsDto.InventoryLocationInput;
import com.webcommander.resources.inventory_locations.inventory_locations_dto
        .InventoryLocationsDto.InventoryLocationPage;
import com.webcommander.resources.inventory_locations.inventory_locations_dto
        .InventoryLocationsDto.InventoryLocationStatusInput;
import com.webcommander.resources.inventory_locations.inventory_locations_dto
        .InventoryLocationsDto.InventoryLocationStock;

import java.util.LinkedHashMap;
import java.util.Map;

/** Inventory locations — {@code /api/{api_version}/admin/inventory_locations}. */
public final class InventoryLocations implements BoundResource {

    private final WebCommanderClient client;

    public InventoryLocations(WebCommanderClient client) {
        this.client = client;
    }

    /** List the locations — {@code GET /admin/inventory_locations}. */
    public ApiResponse listInventoryLocations() {
        return listInventoryLocations(null, null);
    }

    public ApiResponse listInventoryLocations(Map<String, Object> queryParams) {
        return listInventoryLocations(queryParams, null);
    }

    public ApiResponse listInventoryLocations(Map<String, Object> queryParams,
            RequestOptions options) {
        return client.sendGetRequest(InventoryLocationsUrls.INVENTORY_LOCATIONS,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(InventoryLocationPage.class, body))
                        .build());
    }

    /** Ask whether the collection is reachable — {@code HEAD /admin/inventory_locations}. */
    public ApiResponse headInventoryLocations() {
        return headInventoryLocations(null);
    }

    public ApiResponse headInventoryLocations(RequestOptions options) {
        return client.sendApiRequest("HEAD", InventoryLocationsUrls.INVENTORY_LOCATIONS,
                RequestOptions.builder(options).dtoFactory(null).build());
    }

    /** Create a location — {@code POST /admin/inventory_locations}; the body takes no envelope. */
    public ApiResponse createInventoryLocation(Object payload) {
        return createInventoryLocation(payload, null);
    }

    public ApiResponse createInventoryLocation(Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPostRequest(InventoryLocationsUrls.INVENTORY_LOCATIONS,
                RequestOptions.builder(options).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(InventoryLocation.class, body)).build());
    }

    public ApiResponse createInventoryLocation(InventoryLocationInput payload) {
        return createInventoryLocation((Object) payload, null);
    }

    public ApiResponse createInventoryLocation(InventoryLocationInput payload,
            RequestOptions options) {
        return createInventoryLocation((Object) payload, options);
    }

    /** Retrieve one location — {@code GET /admin/inventory_locations/{inventory_location_id}}. */
    public ApiResponse getInventoryLocation(Object inventoryLocationId) {
        return getInventoryLocation(inventoryLocationId, null);
    }

    public ApiResponse getInventoryLocation(Object inventoryLocationId, RequestOptions options) {
        return client.sendGetRequest(InventoryLocationsUrls.INVENTORY_LOCATION,
                RequestOptions.builder(options).pathParams(pathParams(inventoryLocationId))
                        .dtoFactory(body -> Dto.fromMap(InventoryLocation.class, body)).build());
    }

    /** Probe one location — {@code HEAD /admin/inventory_locations/{inventory_location_id}}. */
    public ApiResponse headInventoryLocation(Object inventoryLocationId) {
        return headInventoryLocation(inventoryLocationId, null);
    }

    public ApiResponse headInventoryLocation(Object inventoryLocationId, RequestOptions options) {
        return client.sendApiRequest("HEAD", InventoryLocationsUrls.INVENTORY_LOCATION,
                RequestOptions.builder(options).pathParams(pathParams(inventoryLocationId))
                        .dtoFactory(null).build());
    }

    /** Replace one location — {@code PUT /admin/inventory_locations/{inventory_location_id}}. */
    public ApiResponse updateInventoryLocation(Object inventoryLocationId, Object payload) {
        return updateInventoryLocation(inventoryLocationId, payload, null);
    }

    public ApiResponse updateInventoryLocation(Object inventoryLocationId, Object payload,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPutRequest(InventoryLocationsUrls.INVENTORY_LOCATION,
                RequestOptions.builder(options).pathParams(pathParams(inventoryLocationId))
                        .jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(InventoryLocation.class, body)).build());
    }

    public ApiResponse updateInventoryLocation(Object inventoryLocationId,
            InventoryLocationInput payload) {
        return updateInventoryLocation(inventoryLocationId, (Object) payload, null);
    }

    public ApiResponse updateInventoryLocation(Object inventoryLocationId,
            InventoryLocationInput payload, RequestOptions options) {
        return updateInventoryLocation(inventoryLocationId, (Object) payload, options);
    }

    /** Merge into one location — {@code PATCH /admin/inventory_locations/{inventory_location_id}}. */
    public ApiResponse patchInventoryLocation(Object inventoryLocationId, Object payload) {
        return patchInventoryLocation(inventoryLocationId, payload, null);
    }

    public ApiResponse patchInventoryLocation(Object inventoryLocationId, Object payload,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPatchRequest(InventoryLocationsUrls.INVENTORY_LOCATION,
                RequestOptions.builder(options).pathParams(pathParams(inventoryLocationId))
                        .jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(InventoryLocation.class, body)).build());
    }

    public ApiResponse patchInventoryLocation(Object inventoryLocationId,
            InventoryLocationInput payload) {
        return patchInventoryLocation(inventoryLocationId, (Object) payload, null);
    }

    public ApiResponse patchInventoryLocation(Object inventoryLocationId,
            InventoryLocationInput payload, RequestOptions options) {
        return patchInventoryLocation(inventoryLocationId, (Object) payload, options);
    }

    /** Delete a location — {@code DELETE /admin/inventory_locations/{inventory_location_id}}. */
    public ApiResponse deleteInventoryLocation(Object inventoryLocationId) {
        return deleteInventoryLocation(inventoryLocationId, null);
    }

    public ApiResponse deleteInventoryLocation(Object inventoryLocationId, RequestOptions options) {
        return client.sendDeleteRequest(InventoryLocationsUrls.INVENTORY_LOCATION,
                RequestOptions.builder(options).pathParams(pathParams(inventoryLocationId))
                        .dtoFactory(null).build());
    }

    /** One location's stock — {@code GET .../{inventory_location_id}/stock}. */
    public ApiResponse getInventoryLocationStock(Object inventoryLocationId) {
        return getInventoryLocationStock(inventoryLocationId, null, null);
    }

    public ApiResponse getInventoryLocationStock(Object inventoryLocationId,
            Map<String, Object> queryParams) {
        return getInventoryLocationStock(inventoryLocationId, queryParams, null);
    }

    public ApiResponse getInventoryLocationStock(Object inventoryLocationId,
            Map<String, Object> queryParams, RequestOptions options) {
        return client.sendGetRequest(InventoryLocationsUrls.INVENTORY_LOCATION_STOCK,
                RequestOptions.builder(options).pathParams(pathParams(inventoryLocationId))
                        .queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(InventoryLocationStock.class, body))
                        .build());
    }

    /** Make one location the default — {@code PUT .../{inventory_location_id}/default}. */
    public ApiResponse setInventoryLocationDefault(Object inventoryLocationId) {
        return setInventoryLocationDefault(inventoryLocationId, null);
    }

    public ApiResponse setInventoryLocationDefault(Object inventoryLocationId,
            RequestOptions options) {
        return client.sendPutRequest(InventoryLocationsUrls.INVENTORY_LOCATION_DEFAULT,
                RequestOptions.builder(options).pathParams(pathParams(inventoryLocationId))
                        .dtoFactory(body -> Dto.fromMap(InventoryLocation.class, body)).build());
    }

    /** Activate or deactivate one location — {@code PUT .../{inventory_location_id}/status}. */
    public ApiResponse updateInventoryLocationStatus(Object inventoryLocationId, Object payload) {
        return updateInventoryLocationStatus(inventoryLocationId, payload, null);
    }

    public ApiResponse updateInventoryLocationStatus(Object inventoryLocationId, Object payload,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPutRequest(InventoryLocationsUrls.INVENTORY_LOCATION_STATUS,
                RequestOptions.builder(options).pathParams(pathParams(inventoryLocationId))
                        .jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(InventoryLocation.class, body)).build());
    }

    public ApiResponse updateInventoryLocationStatus(Object inventoryLocationId,
            InventoryLocationStatusInput payload) {
        return updateInventoryLocationStatus(inventoryLocationId, (Object) payload, null);
    }

    public ApiResponse updateInventoryLocationStatus(Object inventoryLocationId,
            InventoryLocationStatusInput payload, RequestOptions options) {
        return updateInventoryLocationStatus(inventoryLocationId, (Object) payload, options);
    }

    private static Map<String, Object> pathParams(Object inventoryLocationId) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("inventory_location_id", inventoryLocationId);
        return params;
    }
}
