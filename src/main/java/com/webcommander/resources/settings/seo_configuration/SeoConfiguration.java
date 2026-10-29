package com.webcommander.resources.settings.seo_configuration;

import com.webcommander.Dto;
import com.webcommander.client.ApiResponse;
import com.webcommander.client.BoundResource;
import com.webcommander.client.RequestOptions;
import com.webcommander.client.ResourceBinding;
import com.webcommander.client.WebCommanderClient;
import com.webcommander.errors.SdkUsageError;
import com.webcommander.resources.customers.admin_customers_dto.AdminCustomersDto.Page;
import com.webcommander.resources.settings.seo_configuration.seo_configuration_dto.SeoConfigurationDto;

import java.util.LinkedHashMap;
import java.util.Map;

/** SEO configuration settings — {@code /api/{api_version}/admin/settings/seo_configuration}. */
public final class SeoConfiguration implements BoundResource {

    private final WebCommanderClient client;

    public SeoConfiguration(WebCommanderClient client) {
        this.client = client;
    }

    /** Read the whole area — {@code GET /admin/settings/seo_configuration}. */
    public ApiResponse getSeoConfiguration() {
        return getSeoConfiguration(null, null);
    }

    public ApiResponse getSeoConfiguration(Map<String, Object> queryParams) {
        return getSeoConfiguration(queryParams, null);
    }

    public ApiResponse getSeoConfiguration(Map<String, Object> queryParams,
            RequestOptions options) {
        return client.sendGetRequest(SeoConfigurationUrls.SEO_CONFIGURATION,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(
                                SeoConfigurationDto.SeoConfiguration.class, body)).build());
    }

    /** Write fields — {@code PATCH /admin/settings/seo_configuration}. */
    public ApiResponse patchSeoConfiguration(Object payload) {
        return patchSeoConfiguration(payload, null);
    }

    public ApiResponse patchSeoConfiguration(Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPatchRequest(SeoConfigurationUrls.SEO_CONFIGURATION,
                RequestOptions.builder(options).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(
                                SeoConfigurationDto.SeoConfiguration.class, body)).build());
    }

    /** Replace the sections a body names — {@code PUT /admin/settings/seo_configuration}. */
    public ApiResponse putSeoConfiguration(Object payload) {
        return putSeoConfiguration(payload, null);
    }

    public ApiResponse putSeoConfiguration(Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPutRequest(SeoConfigurationUrls.SEO_CONFIGURATION,
                RequestOptions.builder(options).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(
                                SeoConfigurationDto.SeoConfiguration.class, body)).build());
    }

    /** Ask whether the area is reachable — {@code HEAD /admin/settings/seo_configuration}. */
    public ApiResponse headSeoConfiguration() {
        return headSeoConfiguration(null);
    }

    public ApiResponse headSeoConfiguration(RequestOptions options) {
        return client.sendApiRequest("HEAD", SeoConfigurationUrls.SEO_CONFIGURATION,
                RequestOptions.builder(options).dtoFactory(null).build());
    }

    /** Read one section — {@code GET /admin/settings/seo_configuration/{section}}. */
    public ApiResponse getSeoConfigurationSection(String section) {
        return getSeoConfigurationSection(section, null, null);
    }

    public ApiResponse getSeoConfigurationSection(String section,
            Map<String, Object> queryParams) {
        return getSeoConfigurationSection(section, queryParams, null);
    }

    public ApiResponse getSeoConfigurationSection(String section, Map<String, Object> queryParams,
            RequestOptions options) {
        return client.sendGetRequest(SeoConfigurationUrls.SEO_CONFIGURATION_SECTION,
                RequestOptions.builder(options).pathParams(pathParams("section", section))
                        .queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(
                                SeoConfigurationDto.SeoConfiguration.class, body)).build());
    }

    /** Write one section — {@code PATCH .../seo_configuration/{section}}, wrapped in its key. */
    public ApiResponse patchSeoConfigurationSection(String section, Object payload) {
        return patchSeoConfigurationSection(section, payload, null);
    }

    public ApiResponse patchSeoConfigurationSection(String section, Object payload,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPatchRequest(SeoConfigurationUrls.SEO_CONFIGURATION_SECTION,
                RequestOptions.builder(options).pathParams(pathParams("section", section))
                        .jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(
                                SeoConfigurationDto.SeoConfiguration.class, body)).build());
    }

    /** Replace one section — {@code PUT .../seo_configuration/{section}}, wrapped in its key. */
    public ApiResponse putSeoConfigurationSection(String section, Object payload) {
        return putSeoConfigurationSection(section, payload, null);
    }

    public ApiResponse putSeoConfigurationSection(String section, Object payload,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPutRequest(SeoConfigurationUrls.SEO_CONFIGURATION_SECTION,
                RequestOptions.builder(options).pathParams(pathParams("section", section))
                        .jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(
                                SeoConfigurationDto.SeoConfiguration.class, body)).build());
    }

    /** List the files served from the document root — {@code GET .../document_root}. */
    public ApiResponse getSeoConfigurationDocumentRoot() {
        return getSeoConfigurationDocumentRoot(null, null);
    }

    public ApiResponse getSeoConfigurationDocumentRoot(Map<String, Object> queryParams) {
        return getSeoConfigurationDocumentRoot(queryParams, null);
    }

    public ApiResponse getSeoConfigurationDocumentRoot(Map<String, Object> queryParams,
            RequestOptions options) {
        return client.sendGetRequest(SeoConfigurationUrls.SEO_CONFIGURATION_DOCUMENT_ROOT,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(Page.of(SeoConfigurationDto.DocumentRootFile.class)).build());
    }

    /** Ask whether the document root is reachable — {@code HEAD .../document_root}; no body. */
    public ApiResponse headSeoConfigurationDocumentRoot() {
        return headSeoConfigurationDocumentRoot(null);
    }

    public ApiResponse headSeoConfigurationDocumentRoot(RequestOptions options) {
        return client.sendApiRequest("HEAD", SeoConfigurationUrls.SEO_CONFIGURATION_DOCUMENT_ROOT,
                RequestOptions.builder(options).dtoFactory(null).build());
    }

    /** Add a file to the document root — {@code POST .../seo_configuration/document_root}. */
    public ApiResponse uploadSeoConfigurationDocumentRoot(Map<String, Object> files) {
        return uploadSeoConfigurationDocumentRoot(files, null);
    }

    public ApiResponse uploadSeoConfigurationDocumentRoot(Map<String, Object> files,
            RequestOptions options) {
        if (files == null || files.isEmpty()) {
            throw new SdkUsageError("files is required (part name 'file')");
        }
        return client.sendPostRequest(SeoConfigurationUrls.SEO_CONFIGURATION_DOCUMENT_ROOT,
                RequestOptions.builder(options).files(files)
                        .dtoFactory(body -> Dto.fromMap(
                                SeoConfigurationDto.DocumentRootFile.class, body)).build());
    }

    /** Stop serving one file — {@code DELETE .../document_root/{config_key}}. */
    public ApiResponse deleteSeoConfigurationDocumentRootFile(String configKey) {
        return deleteSeoConfigurationDocumentRootFile(configKey, null);
    }

    public ApiResponse deleteSeoConfigurationDocumentRootFile(String configKey,
            RequestOptions options) {
        return client.sendDeleteRequest(SeoConfigurationUrls.SEO_CONFIGURATION_DOCUMENT_ROOT_FILE,
                RequestOptions.builder(options).pathParams(pathParams("config_key", configKey))
                        .dtoFactory(null).build());
    }

    /** List the redirects — {@code GET /admin/settings/seo_configuration/redirects}. */
    public ApiResponse getSeoConfigurationRedirects() {
        return getSeoConfigurationRedirects(null, null);
    }

    public ApiResponse getSeoConfigurationRedirects(Map<String, Object> queryParams) {
        return getSeoConfigurationRedirects(queryParams, null);
    }

    public ApiResponse getSeoConfigurationRedirects(Map<String, Object> queryParams,
            RequestOptions options) {
        return client.sendGetRequest(SeoConfigurationUrls.SEO_CONFIGURATION_REDIRECTS,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(Page.of(SeoConfigurationDto.Redirect.class)).build());
    }

    /** Ask whether the redirects are reachable — {@code HEAD .../redirects}; no body. */
    public ApiResponse headSeoConfigurationRedirects() {
        return headSeoConfigurationRedirects(null);
    }

    public ApiResponse headSeoConfigurationRedirects(RequestOptions options) {
        return client.sendApiRequest("HEAD", SeoConfigurationUrls.SEO_CONFIGURATION_REDIRECTS,
                RequestOptions.builder(options).dtoFactory(null).build());
    }

    /** Add a redirect — {@code POST /admin/settings/seo_configuration/redirects}. */
    public ApiResponse createSeoConfigurationRedirect(Object payload) {
        return createSeoConfigurationRedirect(payload, null);
    }

    public ApiResponse createSeoConfigurationRedirect(Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPostRequest(SeoConfigurationUrls.SEO_CONFIGURATION_REDIRECTS,
                RequestOptions.builder(options).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(
                                SeoConfigurationDto.Redirect.class, body)).build());
    }

    /** Read one redirect — {@code GET .../redirects/{redirect_id}}. */
    public ApiResponse getSeoConfigurationRedirect(Object redirectId) {
        return getSeoConfigurationRedirect(redirectId, null);
    }

    public ApiResponse getSeoConfigurationRedirect(Object redirectId, RequestOptions options) {
        return client.sendGetRequest(SeoConfigurationUrls.SEO_CONFIGURATION_REDIRECT,
                RequestOptions.builder(options).pathParams(pathParams("redirect_id", redirectId))
                        .dtoFactory(body -> Dto.fromMap(
                                SeoConfigurationDto.Redirect.class, body)).build());
    }

    /**
     * Ask whether one redirect is there — {@code HEAD .../redirects/{redirect_id}}; no body, and
     * {@code 404} for an unknown id.
     */
    public ApiResponse headSeoConfigurationRedirect(Object redirectId) {
        return headSeoConfigurationRedirect(redirectId, null);
    }

    public ApiResponse headSeoConfigurationRedirect(Object redirectId, RequestOptions options) {
        return client.sendApiRequest("HEAD", SeoConfigurationUrls.SEO_CONFIGURATION_REDIRECT,
                RequestOptions.builder(options).pathParams(pathParams("redirect_id", redirectId))
                        .dtoFactory(null).build());
    }

    /** Change one redirect — {@code PATCH .../redirects/{redirect_id}}. */
    public ApiResponse patchSeoConfigurationRedirect(Object redirectId, Object payload) {
        return patchSeoConfigurationRedirect(redirectId, payload, null);
    }

    public ApiResponse patchSeoConfigurationRedirect(Object redirectId, Object payload,
            RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPatchRequest(SeoConfigurationUrls.SEO_CONFIGURATION_REDIRECT,
                RequestOptions.builder(options).pathParams(pathParams("redirect_id", redirectId))
                        .jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(
                                SeoConfigurationDto.Redirect.class, body)).build());
    }

    /** Remove one redirect — {@code DELETE .../redirects/{redirect_id}}. */
    public ApiResponse deleteSeoConfigurationRedirect(Object redirectId) {
        return deleteSeoConfigurationRedirect(redirectId, null);
    }

    public ApiResponse deleteSeoConfigurationRedirect(Object redirectId, RequestOptions options) {
        return client.sendDeleteRequest(SeoConfigurationUrls.SEO_CONFIGURATION_REDIRECT,
                RequestOptions.builder(options).pathParams(pathParams("redirect_id", redirectId))
                        .dtoFactory(null).build());
    }

    private static Map<String, Object> pathParams(String name, Object value) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put(name, value);
        return params;
    }
}
