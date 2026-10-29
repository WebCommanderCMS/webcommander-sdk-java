package com.webcommander.resources.aliases;

import com.webcommander.Dto;
import com.webcommander.client.ApiResponse;
import com.webcommander.client.BoundResource;
import com.webcommander.client.RequestOptions;
import com.webcommander.client.ResourceBinding;
import com.webcommander.client.WebCommanderClient;
import com.webcommander.resources.aliases.aliases_dto.AliasesDto.Alias;
import com.webcommander.resources.aliases.aliases_dto.AliasesDto.AliasInput;
import com.webcommander.resources.aliases.aliases_dto.AliasesDto.AliasPage;

import java.util.LinkedHashMap;
import java.util.Map;

/** Domain aliases — {@code /api/{api_version}/admin/aliases}. */
public final class Aliases implements BoundResource {

    private final WebCommanderClient client;

    public Aliases(WebCommanderClient client) {
        this.client = client;
    }

    /** List the aliases — {@code GET /admin/aliases}; a created row is listed last. */
    public ApiResponse listAliases() {
        return listAliases(null, null);
    }

    public ApiResponse listAliases(Map<String, Object> queryParams) {
        return listAliases(queryParams, null);
    }

    public ApiResponse listAliases(Map<String, Object> queryParams, RequestOptions options) {
        return client.sendGetRequest(AliasesUrls.ALIASES,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(AliasPage.class, body)).build());
    }

    /** Ask whether the collection answers — {@code HEAD /admin/aliases}. */
    public ApiResponse headAliases() {
        return headAliases(null);
    }

    public ApiResponse headAliases(RequestOptions options) {
        return client.sendApiRequest("HEAD", AliasesUrls.ALIASES,
                RequestOptions.builder(options).dtoFactory(null).build());
    }

    /** Create an alias — {@code POST /admin/aliases}, body {@code {"alias": "<hostname>"}}. */
    public ApiResponse createAlias(Object payload) {
        return createAlias(payload, null);
    }

    public ApiResponse createAlias(Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPostRequest(AliasesUrls.ALIASES,
                RequestOptions.builder(options).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(Alias.class, body)).build());
    }

    public ApiResponse createAlias(AliasInput payload) {
        return createAlias((Object) payload, null);
    }

    public ApiResponse createAlias(AliasInput payload, RequestOptions options) {
        return createAlias((Object) payload, options);
    }

    /** Delete one alias — {@code DELETE /admin/aliases/{alias_id}}. */
    public ApiResponse deleteAlias(Object aliasId) {
        return deleteAlias(aliasId, null);
    }

    public ApiResponse deleteAlias(Object aliasId, RequestOptions options) {
        return client.sendDeleteRequest(AliasesUrls.ALIAS,
                RequestOptions.builder(options).pathParams(pathParams(aliasId)).dtoFactory(null)
                        .build());
    }

    private static Map<String, Object> pathParams(Object aliasId) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("alias_id", aliasId);
        return params;
    }
}
