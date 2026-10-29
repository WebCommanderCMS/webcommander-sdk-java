package com.webcommander.resources.email;

import com.webcommander.Dto;
import com.webcommander.client.ApiResponse;
import com.webcommander.client.BoundResource;
import com.webcommander.client.RequestOptions;
import com.webcommander.client.ResourceBinding;
import com.webcommander.client.WebCommanderClient;
import com.webcommander.resources.email.email_dto.EmailDto.EmailInput;
import com.webcommander.resources.email.email_dto.EmailDto.EmailSendResult;

import java.util.LinkedHashMap;
import java.util.Map;

/** Email — {@code /api/{api_version}/admin/email}. */
public final class Email implements BoundResource {

    private final WebCommanderClient client;

    public Email(WebCommanderClient client) {
        this.client = client;
    }

    /**
     * Send an email — {@code POST /admin/email/send}, answered {@code 200} once every message is
     * sent. A bare body: {@code recipients} and {@code subject}, with {@code htmlContent} or a
     * template {@code identifier} and its {@code data}. {@code pluginUuid} is sent as the header a
     * template send needs and switches the call to plugin auth, so an admin token is {@code 401}.
     * Past 500 addresses a day per sender the store answers {@code 429} until 00:00 UTC; a body
     * over 1048576 bytes is {@code 413}. A send the mail server fails is {@code 502}, {@code status}
     * {@code failed}, each address in {@code rejected}.
     */
    public ApiResponse sendEmail(Object payload) {
        return sendEmail(payload, null, null);
    }

    public ApiResponse sendEmail(Object payload, RequestOptions options) {
        return sendEmail(payload, null, options);
    }

    public ApiResponse sendEmail(Object payload, String pluginUuid) {
        return sendEmail(payload, pluginUuid, null);
    }

    public ApiResponse sendEmail(Object payload, String pluginUuid, RequestOptions options) {
        ResourceBinding.requirePayload(payload);
        RequestOptions given = options == null ? RequestOptions.none() : options;
        Map<String, String> headers = new LinkedHashMap<>();
        if (given.getHeaders() != null) {
            headers.putAll(given.getHeaders());
        }
        if (pluginUuid != null) {
            headers.put(EmailUrls.PLUGIN_UUID_HEADER, pluginUuid);
        }
        return client.sendPostRequest(EmailUrls.EMAIL_SEND,
                RequestOptions.builder(options).jsonBody(payload).headers(headers)
                        .dtoFactory(body -> Dto.fromMap(EmailSendResult.class, body)).build());
    }

    public ApiResponse sendEmail(EmailInput payload) {
        return sendEmail((Object) payload, null, null);
    }

    public ApiResponse sendEmail(EmailInput payload, RequestOptions options) {
        return sendEmail((Object) payload, null, options);
    }

    public ApiResponse sendEmail(EmailInput payload, String pluginUuid) {
        return sendEmail((Object) payload, pluginUuid, null);
    }

    public ApiResponse sendEmail(EmailInput payload, String pluginUuid, RequestOptions options) {
        return sendEmail((Object) payload, pluginUuid, options);
    }
}
