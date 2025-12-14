package dev.mockboard.core.storage;

import dev.mockboard.core.storage.model.SessionData;
import dev.mockboard.core.utils.SessionIdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class SessionStorage {

    private final Map<String, SessionData> activeSessions = new ConcurrentHashMap<>();

    public String createSession() {
        var sessionId = SessionIdGenerator.generate();
        activeSessions.computeIfAbsent(sessionId, k -> new SessionData());
        log.info("Created new session with id {}", sessionId);
        return sessionId;
    }

    public void restoreSession(String sessionId) {
        log.info("Restoring session with id {}", sessionId);
        activeSessions.computeIfAbsent(sessionId, k -> new SessionData());
    }

    public boolean isValidSession(String sessionId) {
        return !CollectionUtils.isEmpty(activeSessions) && activeSessions.containsKey(sessionId);
    }

    public void addWsSession(String sessionId, WebSocketSession session) {
        if (!isValidSession(sessionId)) {
            log.warn("Invalid session id {}", sessionId);
            return;
        }

        var sessionData = activeSessions.getOrDefault(sessionId, new SessionData());
        sessionData.addWebSocketSession(session);
        sessionData.touchLastAccessTime();
    }

    public void removeWsSession(String sessionId, WebSocketSession session) {
        if (!isValidSession(sessionId)) {
            log.warn("Invalid session id {}", sessionId);
            return;
        }
        activeSessions.get(sessionId).removeWebSocketSession(session);
        log.info("Removing WS session with id {}", sessionId);
    }

    public Set<WebSocketSession> getWsSessions(String sessionId) {
        if (!isValidSession(sessionId)) {
            throw new IllegalArgumentException("Invalid session id " + sessionId);
        }

        return activeSessions.get(sessionId).getWebSocketSessions();
    }

    public void removeSession(String sessionId) {
        if (!isValidSession(sessionId)) {
            throw new IllegalArgumentException("Invalid session id " + sessionId);
        }

        var sessionData = activeSessions.get(sessionId);
        for (var webSocketSession : sessionData.getWebSocketSessions()) {
            try {
                webSocketSession.close(CloseStatus.NORMAL);
            } catch (IOException e) {
                log.error("Failed to close web socket session", e);
            }
        }
        activeSessions.remove(sessionId);
        log.info("Removed session with id {}", sessionId);
    }
}
