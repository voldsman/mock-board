package dev.mockboard.service;

import dev.mockboard.storage.BoardSessionStorage;
import dev.mockboard.storage.WsSessionStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardSessionService {

    private final BoardSessionStorage boardSessionStorage;
    private final WsSessionStorage wsSessionStorage;

    public String createSession(final String existingSessionId) {
        String sessionId;
        if (existingSessionId != null && boardSessionStorage.isValidSession(existingSessionId)) {
            sessionId = existingSessionId;
            log.info("Reusing exising session: {}", sessionId);
        } else if (existingSessionId != null) {
            sessionId = existingSessionId;
            boardSessionStorage.addSession(sessionId);
            log.info("Restored session after restart: {}", sessionId);
        } else {
            sessionId = boardSessionStorage.createSession();
            log.info("Created new session: {}", sessionId);
        }

        return sessionId;
    }

    public void removeSession(final String sessionId) {
        log.info("Removing session: {}", sessionId);
        boardSessionStorage.removeSession(sessionId);
        wsSessionStorage.removeSession(sessionId);
    }
}
