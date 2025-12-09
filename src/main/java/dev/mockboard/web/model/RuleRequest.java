package dev.mockboard.web.model;

public record RuleRequest(
        String method,
        String path,
        int status,
        String contentType,
        String body,
        int delay) {

}