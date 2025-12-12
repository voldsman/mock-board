package dev.mockboard.web;

import dev.mockboard.Constants;
import dev.mockboard.service.BoardSessionService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {

    private final BoardSessionService boardSessionService;

    @GetMapping
    public ResponseEntity<?> welcome() {
        return ResponseEntity.ok(Map.of("message", "Welcome Home!"));
    }

    @PostMapping("/start")
    public ResponseEntity<Map<String, String>> startSession(
            @CookieValue(name = Constants.SESSION_COOKIE_NAME, required = false) String existingSessionId,
            HttpServletResponse response) {

        var sessionId = boardSessionService.createSession(existingSessionId);
        var cookie = ResponseCookie.from(Constants.SESSION_COOKIE_NAME, sessionId)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(Duration.ofSeconds(Constants.COOKIE_MAX_AGE))
                .sameSite("strict")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
        return ResponseEntity.ok(Map.of("sessionId", sessionId));
    }

    @PostMapping("/reset")
    public ResponseEntity<Void> reset(@CookieValue(name = Constants.SESSION_COOKIE_NAME, required = false) String sessionId,
                                      HttpServletResponse response) {
        boardSessionService.removeSession(sessionId);
        var cookie = ResponseCookie.from(Constants.SESSION_COOKIE_NAME, "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
        return ResponseEntity.ok().build();
    }
}
