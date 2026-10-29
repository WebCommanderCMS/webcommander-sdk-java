package com.webcommander.resources.tax_zones;

import com.webcommander.Dto;
import com.webcommander.client.ApiResponse;
import com.webcommander.client.BoundResource;
import com.webcommander.client.RequestOptions;
import com.webcommander.client.ResourceBinding;
import com.webcommander.client.WebCommanderClient;
import com.webcommander.resources.tax_zones.tax_zones_dto.TaxZonesDto.TaxZone;
import com.webcommander.resources.tax_zones.tax_zones_dto.TaxZonesDto.TaxZoneInput;
import com.webcommander.resources.tax_zones.tax_zones_dto.TaxZonesDto.TaxZonePage;

import java.util.LinkedHashMap;
import java.util.Map;

/** Tax zones — {@code /api/{api_version}/admin/tax_zones}. */
public final class TaxZones implements BoundResource {

    private final WebCommanderClient client;

    public TaxZones(WebCommanderClient client) {
        this.client = client;
    }

    /** List the tax zones — {@code GET /admin/tax_zones}. */
    public ApiResponse listTaxZones() {
        return listTaxZones(null, null);
    }

    public ApiResponse listTaxZones(Map<String, Object> queryParams) {
        return listTaxZones(queryParams, null);
    }

    public ApiResponse listTaxZones(Map<String, Object> queryParams, RequestOptions options) {
        return client.sendGetRequest(TaxZonesUrls.TAX_ZONES,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(TaxZonePage.class, body)).build());
    }

    /** Ask whether the collection is reachable — {@code HEAD /admin/tax_zones}. */
    public ApiResponse headTaxZones() {
        return headTaxZones(null);
    }

    public ApiResponse headTaxZones(RequestOptions options) {
        return client.sendApiRequest("HEAD", TaxZonesUrls.TAX_ZONES,
                RequestOptions.builder(options).dtoFactory(null).build());
    }

    /** Create a tax zone — {@code POST /admin/tax_zones}, wrapped in {@code tax_zone}. */
    public ApiResponse createTaxZone(Object payload) {
        return createTaxZone(payload, null);
    }

    public ApiResponse createTaxZone(Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPostRequest(TaxZonesUrls.TAX_ZONES,
                RequestOptions.builder(options).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(TaxZone.class, body)).build());
    }

    public ApiResponse createTaxZone(TaxZoneInput payload) {
        return createTaxZone((Object) payload, null);
    }

    public ApiResponse createTaxZone(TaxZoneInput payload, RequestOptions options) {
        return createTaxZone((Object) payload, options);
    }

    /** Retrieve one tax zone — {@code GET /admin/tax_zones/{tax_zone_id}}. */
    public ApiResponse getTaxZone(Object taxZoneId) {
        return getTaxZone(taxZoneId, null);
    }

    public ApiResponse getTaxZone(Object taxZoneId, RequestOptions options) {
        return client.sendGetRequest(TaxZonesUrls.TAX_ZONE,
                RequestOptions.builder(options).pathParams(pathParams(taxZoneId))
                        .dtoFactory(body -> Dto.fromMap(TaxZone.class, body)).build());
    }

    /** Probe one tax zone — {@code HEAD /admin/tax_zones/{tax_zone_id}}. */
    public ApiResponse headTaxZone(Object taxZoneId) {
        return headTaxZone(taxZoneId, null);
    }

    public ApiResponse headTaxZone(Object taxZoneId, RequestOptions options) {
        return client.sendApiRequest("HEAD", TaxZonesUrls.TAX_ZONE,
                RequestOptions.builder(options).pathParams(pathParams(taxZoneId)).dtoFactory(null)
                        .build());
    }

    /** Update one tax zone — {@code PUT /admin/tax_zones/{tax_zone_id}}; a replace. */
    public ApiResponse updateTaxZone(Object taxZoneId, Object payload) {
        return updateTaxZone(taxZoneId, payload, null);
    }

    public ApiResponse updateTaxZone(Object taxZoneId, Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPutRequest(TaxZonesUrls.TAX_ZONE,
                RequestOptions.builder(options).pathParams(pathParams(taxZoneId)).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(TaxZone.class, body)).build());
    }

    public ApiResponse updateTaxZone(Object taxZoneId, TaxZoneInput payload) {
        return updateTaxZone(taxZoneId, (Object) payload, null);
    }

    public ApiResponse updateTaxZone(Object taxZoneId, TaxZoneInput payload,
            RequestOptions options) {
        return updateTaxZone(taxZoneId, (Object) payload, options);
    }

    /** Delete one tax zone — {@code DELETE /admin/tax_zones/{tax_zone_id}}. */
    public ApiResponse deleteTaxZone(Object taxZoneId) {
        return deleteTaxZone(taxZoneId, null);
    }

    public ApiResponse deleteTaxZone(Object taxZoneId, RequestOptions options) {
        return client.sendDeleteRequest(TaxZonesUrls.TAX_ZONE,
                RequestOptions.builder(options).pathParams(pathParams(taxZoneId)).dtoFactory(null)
                        .build());
    }

    private static Map<String, Object> pathParams(Object taxZoneId) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("tax_zone_id", taxZoneId);
        return params;
    }
}
