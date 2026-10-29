package com.webcommander.client;

/** How one endpoint's response body becomes the typed view behind {@link ApiResponse#getDto()}. */
@FunctionalInterface
public interface DtoFactory {

    /** Build the typed view of one decoded body, which is always a {@code Map}. */
    Object buildDto(Object decodedBody);
}
