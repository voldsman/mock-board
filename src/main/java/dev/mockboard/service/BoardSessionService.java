package dev.mockboard.service;

import dev.mockboard.core.storage.SessionStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardSessionService {

    private final SessionStorage sessionStorage;

    public String createSession(final String existingSessionId) {
        String sessionId;
        if (existingSessionId != null && sessionStorage.isValidSession(existingSessionId)) {
            sessionId = existingSessionId;
            log.info("Reusing exising session: {}", sessionId);
        } else if (existingSessionId != null) {
            sessionId = existingSessionId;
            sessionStorage.restoreSession(sessionId);
            log.info("Restored session after restart: {}", sessionId);
        } else {
            sessionId = sessionStorage.createSession();
            log.info("Created new session: {}", sessionId);
        }

        return sessionId;
    }

    public void removeSession(final String sessionId) {
        log.info("Removing session: {}", sessionId);
        sessionStorage.removeSession(sessionId);
    }
}
