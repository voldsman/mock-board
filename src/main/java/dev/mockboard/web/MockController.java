package dev.mockboard.web;

import dev.mockboard.Constants;
import dev.mockboard.service.RequestCaptureService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.MOCK_PATH_PREFIX)
@RequiredArgsConstructor
public class MockController {

    private final RequestCaptureService requestCaptureService;

    @RequestMapping(value = {"/{sessionId}", "/{sessionId}/**"})
    public ResponseEntity<?> handleMockRequest(@PathVariable String sessionId, HttpServletRequest request) {
        requestCaptureService.captureRequest(sessionId, request);
        return ResponseEntity.ok("{\"status\": \"captured\"}");
    }
}
