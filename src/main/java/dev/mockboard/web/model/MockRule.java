package dev.mockboard.web.model;

import java.util.UUID;

public record MockRule(
        String id,
        String method,
        String path,
        int statusCode,
        String contentType,
        String body,
        int delayMs
) {

    public static MockRule create(String method, String path, int status, String contentType, String body, int delayMs) {
        return new MockRule(UUID.randomUUID().toString(), method, path, status, contentType, body, delayMs);
    }
}
