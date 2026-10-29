package com.webcommander.resources.email;

/** Path templates for the email endpoints. */
public final class EmailUrls {

    private EmailUrls() {}

    public static final String EMAIL_SEND = "/api/{api_version}/admin/email/send";

    /** Names the plugin that owns a template {@code identifier}; a body field is ignored. */
    public static final String PLUGIN_UUID_HEADER = "plugin_uuid";
}
