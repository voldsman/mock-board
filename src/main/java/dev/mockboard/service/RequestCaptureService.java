package dev.mockboard.service;

import dev.mockboard.Constants;
import dev.mockboard.core.enums.WsEventType;
import dev.mockboard.core.storage.model.RequestData;
import dev.mockboard.web.ws.WsPublisher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RequestCaptureService {

    private final WsPublisher wsPublisher;
    private final ObjectMapper objectMapper;

    public void captureRequest(String sessionId, HttpServletRequest request) {
        try {
            RequestData requestData = RequestData.builder()
                    .timestamp(System.currentTimeMillis())
                    .method(request.getMethod())
                    .path(extractPath(sessionId, request.getRequestURI()))
                    .query(request.getQueryString())
                    .protocol(request.getProtocol())
                    .headers(extractHeaders(request))
                    .body(extractBody(request))
                    .contentType(request.getContentType())
                    .contentLength(request.getContentLength())
                    .status(200)
                    .build();
            System.out.println(requestData);

            wsPublisher.broadcast(sessionId, WsEventType.REQUEST_CAPTURED, requestData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String extractPath(String sessionId, String fullPath) {
        var prefix = Constants.MOCK_PATH_PREFIX + "/" + sessionId;
        if (fullPath.startsWith(prefix)) {
            var path = fullPath.substring(prefix.length());
            return path.isEmpty() ? "/" : path;
        }
        return fullPath;
    }

    private Map<String, List<String>> extractHeaders(HttpServletRequest request) {
        Map<String, List<String>> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();

        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            List<String> headerValues = Collections.list(request.getHeaders(headerName));
            headers.put(headerName, headerValues);
        }

        return headers;
    }

    private String extractBody(HttpServletRequest request) throws IOException {
        if (request.getContentLength() <= 0) {
            return null;
        }

        StringBuilder body = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                body.append(line);
            }
        }

        return body.toString();
    }
}
