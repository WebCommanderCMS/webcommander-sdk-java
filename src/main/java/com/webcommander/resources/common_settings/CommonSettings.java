package com.webcommander.resources.common_settings;

import com.webcommander.Dto;
import com.webcommander.client.ApiResponse;
import com.webcommander.client.BoundResource;
import com.webcommander.client.RequestOptions;
import com.webcommander.client.ResourceBinding;
import com.webcommander.client.WebCommanderClient;
import com.webcommander.resources.common_settings.common_settings_dto.CommonSettingsDto;

import java.util.Map;

/** Common settings — {@code /api/{api_version}/settings} and {@code /create-customer-fields}. */
public final class CommonSettings implements BoundResource {

    private final WebCommanderClient client;

    public CommonSettings(WebCommanderClient client) {
        this.client = client;
    }

    /**
     * Read the store's settings — {@code GET /settings}, {@code {"settings": {group:
     * [{"config_key", "value"}]}}}. {@code type} names one group, an unknown one {@code 404};
     * {@code config_key} one key, with or without {@code type}. A comma list of keys matches
     * nothing and answers {@code {"settings": {}}}. Secrets read {@code ***}. Other names, paging
     * included, are {@code 400}. Without {@code type} the answer is every group, about 2 MB, and
     * can take 30 seconds or more.
     */
    public ApiResponse getSettings() {
        return getSettings(null, null);
    }

    public ApiResponse getSettings(Map<String, Object> queryParams) {
        return getSettings(queryParams, null);
    }

    public ApiResponse getSettings(Map<String, Object> queryParams, RequestOptions options) {
        return client.sendGetRequest(CommonSettingsUrls.SETTINGS,
                RequestOptions.builder(options).queryParams(queryParams)
                        .dtoFactory(body -> Dto.fromMap(CommonSettingsDto.CommonSettings.class,
                                body)).build());
    }

    /**
     * Ask whether the settings are reachable — {@code HEAD /settings}; no body. It runs the
     * read's query, so pass a {@code type}: an unknown one is {@code 404}.
     */
    public ApiResponse headSettings() {
        return headSettings(null, null);
    }

    public ApiResponse headSettings(Map<String, Object> queryParams) {
        return headSettings(queryParams, null);
    }

    public ApiResponse headSettings(Map<String, Object> queryParams, RequestOptions options) {
        return client.sendApiRequest("HEAD", CommonSettingsUrls.SETTINGS,
                RequestOptions.builder(options).queryParams(queryParams).dtoFactory(null).build());
    }

    /**
     * Save settings — {@code PUT /settings} with {@code {"settings": {group: ...}}}; {@code 200}
     * and every group of the store. Only the keys sent change, and one bad entry refuses the
     * whole request with {@code 400}. A write was measured at 5 to 53 seconds, past the default
     * 30 of {@code timeoutSeconds} at the slow end: build the client with a longer one.
     */
    public ApiResponse updateSettings(Object payload) {
        return updateSettings(payload, null);
    }

    public ApiResponse updateSettings(Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPutRequest(CommonSettingsUrls.SETTINGS,
                RequestOptions.builder(options).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(CommonSettingsDto.CommonSettings.class,
                                body)).build());
    }

    public ApiResponse updateSettings(CommonSettingsDto.CommonSettingsInput payload) {
        return updateSettings((Object) payload, null);
    }

    public ApiResponse updateSettings(CommonSettingsDto.CommonSettingsInput payload,
            RequestOptions options) {
        return updateSettings((Object) payload, options);
    }

    /**
     * Save settings — {@code POST /settings}, the {@code PUT}'s body, behaviour and timing,
     * answered {@code 201}. A {@code POST} is not resent after a timeout, so read the keys back
     * before retrying.
     */
    public ApiResponse saveSettings(Object payload) {
        return saveSettings(payload, null);
    }

    public ApiResponse saveSettings(Object payload, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        return client.sendPostRequest(CommonSettingsUrls.SETTINGS,
                RequestOptions.builder(options).jsonBody(payload)
                        .dtoFactory(body -> Dto.fromMap(CommonSettingsDto.CommonSettings.class,
                                body)).build());
    }

    public ApiResponse saveSettings(CommonSettingsDto.CommonSettingsInput payload) {
        return saveSettings((Object) payload, null);
    }

    public ApiResponse saveSettings(CommonSettingsDto.CommonSettingsInput payload,
            RequestOptions options) {
        return saveSettings((Object) payload, options);
    }

    /**
     * Read the customer registration form — {@code GET /create-customer-fields}, the active
     * {@code fields} in order and every field's {@code fields_configs}. Any query name is
     * {@code 400}.
     */
    public ApiResponse getCreateCustomerFields() {
        return getCreateCustomerFields(null);
    }

    public ApiResponse getCreateCustomerFields(RequestOptions options) {
        return client.sendGetRequest(CommonSettingsUrls.CREATE_CUSTOMER_FIELDS,
                RequestOptions.builder(options)
                        .dtoFactory(body -> Dto.fromMap(CommonSettingsDto.CustomerFields.class,
                                body)).build());
    }

    /** Ask whether the registration form is there — {@code HEAD /create-customer-fields}. */
    public ApiResponse headCreateCustomerFields() {
        return headCreateCustomerFields(null);
    }

    public ApiResponse headCreateCustomerFields(RequestOptions options) {
        return client.sendApiRequest("HEAD", CommonSettingsUrls.CREATE_CUSTOMER_FIELDS,
                RequestOptions.builder(options).dtoFactory(null).build());
    }
}
