package com.webcommander.resources.email.email_dto;

import com.webcommander.Dto;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Typed views over the email send bodies. */
public final class EmailDto {

    private EmailDto() {
    }

    /**
     * The send body, bare and camelCase as the store takes it. {@code recipients}, {@code cc} and
     * {@code bcc} hold at most 100 addresses together. Send {@code htmlContent} or a plugin
     * template's {@code identifier}, not both.
     */
    public static final class EmailInput extends Dto {

        public List<String> recipients;

        public List<String> cc;

        public List<String> bcc;

        public String subject;

        public String htmlContent;

        /** {@code html}, {@code text} or {@code text/html}. */
        public String contentType;

        public String identifier;

        /** The template's variables, an object of any shape. */
        public Object data;

        @Override
        public Map<String, Object> toMap() {
            // The derived keys would be html_content and content_type, which the store ignores.
            Map<String, Object> body = new LinkedHashMap<>();
            for (Map.Entry<String, Object> entry : super.toMap().entrySet()) {
                String key = switch (entry.getKey()) {
                    case "html_content" -> "htmlContent";
                    case "content_type" -> "contentType";
                    default -> entry.getKey();
                };
                body.put(key, entry.getValue());
            }
            return body;
        }
    }

    /** One address the store could not send to, and why, such as {@code send_failed}. */
    public static final class EmailRejection extends Dto {

        public String email;

        public String reason;
    }

    /** The send's verdict: each list holds the addresses trimmed, a repeat in any case once. */
    public static final class EmailSendResult extends Dto {

        public String status;

        public List<String> accepted;

        public List<EmailRejection> rejected;

        public List<String> cc;

        public List<String> bcc;
    }
}
