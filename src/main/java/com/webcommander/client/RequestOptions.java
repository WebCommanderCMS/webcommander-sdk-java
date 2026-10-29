package com.webcommander.client;

import com.webcommander.Redaction;

import java.util.Map;

/** The optional arguments of one API call, as a snapshot value with a builder. */
public final class RequestOptions {

    private static final RequestOptions NONE = builder().build();

    private final Map<String, Object> pathParams;
    private final Map<String, Object> queryParams;
    private final Object jsonBody;
    private final Map<String, Object> formData;
    private final Map<String, Object> files;
    private final Map<String, String> headers;
    private final boolean authenticated;
    private final Boolean raiseOnError;
    private final String responseFormat;
    private final DtoFactory dtoFactory;

    private RequestOptions(Builder builder) {
        this.pathParams = detach(builder.pathParams);
        this.queryParams = detach(builder.queryParams);
        this.jsonBody = Redaction.detachValue(builder.jsonBody);
        this.formData = detach(builder.formData);
        this.files = detach(builder.files);
        this.headers = detach(builder.headers);
        this.authenticated = builder.authenticated;
        this.raiseOnError = builder.raiseOnError;
        this.responseFormat = builder.responseFormat;
        this.dtoFactory = builder.dtoFactory;
    }

    /** Keeps {@code null} distinct from empty: no body and an empty body are different requests. */
    private static <V> Map<String, V> detach(Map<String, V> values) {
        return values == null ? null : Redaction.detachMapping(values);
    }

    /** No options at all: the bare call. */
    public static RequestOptions none() {
        return NONE;
    }

    /** A fresh builder; everything defaults to unset except {@code authenticated}. */
    public static Builder builder() {
        return new Builder();
    }

    /** A builder holding a copy of {@code base}; {@code null} starts from {@link #none()}. */
    public static Builder builder(RequestOptions base) {
        RequestOptions given = base == null ? NONE : base;
        return builder()
                .pathParams(given.getPathParams())
                .queryParams(given.getQueryParams())
                .jsonBody(given.getJsonBody())
                .formData(given.getFormData())
                .files(given.getFiles())
                .headers(given.getHeaders())
                .authenticated(given.isAuthenticated())
                .raiseOnError(given.getRaiseOnError())
                .responseFormat(given.getResponseFormat())
                .dtoFactory(given.getDtoFactory());
    }

    /** Values for the {@code {name}} placeholders in the path, or {@code null} when none. */
    public Map<String, Object> getPathParams() {
        return pathParams;
    }

    /** Query-string parameters, or {@code null} when none were given. */
    public Map<String, Object> getQueryParams() {
        return queryParams;
    }

    /** The JSON request body, or {@code null} when unset. */
    public Object getJsonBody() {
        return jsonBody;
    }

    /** The form-encoded request body, or {@code null} when none was given. */
    public Map<String, Object> getFormData() {
        return formData;
    }

    /** The multipart uploads by field name, a fresh copy with its bytes cloned on every call. */
    public Map<String, Object> getFiles() {
        return detach(files);
    }

    /** Extra request headers, or {@code null} when none were given. */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /** Whether a bearer token is attached; true unless turned off. */
    public boolean isAuthenticated() {
        return authenticated;
    }

    /** Whether a failed status raises for this one call; {@code null} defers to the config. */
    public Boolean getRaiseOnError() {
        return raiseOnError;
    }

    /** The per-call {@code getData()} format; {@code null} defers to the config. */
    public String getResponseFormat() {
        return responseFormat;
    }

    /** How this call's body becomes {@code getDto()}; {@code null} produces none. */
    public DtoFactory getDtoFactory() {
        return dtoFactory;
    }

    /** Collects the options for one call. */
    public static final class Builder {

        /** An empty builder; use {@link RequestOptions#builder()}. */
        public Builder() {
        }

        private Map<String, Object> pathParams;
        private Map<String, Object> queryParams;
        private Object jsonBody;
        private Map<String, Object> formData;
        private Map<String, Object> files;
        private Map<String, String> headers;
        private boolean authenticated = true;
        private Boolean raiseOnError;
        private String responseFormat;
        private DtoFactory dtoFactory;

        /** Fill the {@code {name}} placeholders in the path. */
        public Builder pathParams(Map<String, Object> pathParams) {
            this.pathParams = pathParams;
            return this;
        }

        /** Query-string parameters, captured at {@link #build()}. */
        public Builder queryParams(Map<String, Object> queryParams) {
            this.queryParams = queryParams;
            return this;
        }

        /** Send a JSON body: a mapping, a list, JSON text, or a payload object. */
        public Builder jsonBody(Object jsonBody) {
            this.jsonBody = jsonBody;
            return this;
        }

        /** Send a form-encoded body, or the non-file fields of a multipart one. */
        public Builder formData(Map<String, Object> formData) {
            this.formData = formData;
            return this;
        }

        /** Send a multipart body, uploads keyed by field name. */
        public Builder files(Map<String, Object> files) {
            this.files = files;
            return this;
        }

        /** Extra headers; {@code Authorization} cannot be overridden, whatever the casing. */
        public Builder headers(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        /** Attach the bearer token, the default; {@code false} for an endpoint that takes none. */
        public Builder authenticated(boolean authenticated) {
            this.authenticated = authenticated;
            return this;
        }

        /** Raise on a failed status; {@code null} defers to the config. */
        public Builder raiseOnError(Boolean raiseOnError) {
            this.raiseOnError = raiseOnError;
            return this;
        }

        /** The per-call {@code getData()} format; an unknown value is refused at call time. */
        public Builder responseFormat(String responseFormat) {
            this.responseFormat = responseFormat;
            return this;
        }

        /** The DTO shape this call produces, e.g. {@code Page.of(Customer.class)}. */
        public Builder dtoFactory(DtoFactory dtoFactory) {
            this.dtoFactory = dtoFactory;
            return this;
        }

        /** Capture the options as one value. */
        public RequestOptions build() {
            return new RequestOptions(this);
        }
    }
}
