package dev.mockboard.web;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/m")
public class MockController {

    @RequestMapping(value = {"/{sessionId}", "/{sessionId}/**"})
    public ResponseEntity<?> handleMockRequest(@PathVariable String sessionId, HttpServletRequest request) {
        return ResponseEntity.ok("{\"status\": \"captured\"}");
    }
}
