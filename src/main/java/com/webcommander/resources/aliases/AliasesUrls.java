package com.webcommander.resources.aliases;

/** Path templates for the domain alias endpoints. */
public final class AliasesUrls {

    private AliasesUrls() {}

    public static final String ALIASES = "/api/{api_version}/admin/aliases";

    public static final String ALIAS = "/api/{api_version}/admin/aliases/{alias_id}";
}
