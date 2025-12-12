package dev.mockboard.storage.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class HttpRequestData {
    private long timestamp;

    private String method;
    private String path;
    private String query;
    private String protocol;

    private Map<String, List<String>> headers;

    private String body;
    private String contentType;
    private int contentLength;
    private int status;
}
