package dev.mockboard.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class WsSessionStorage {

    private final Map<String, Set<WebSocketSession>> boardSessions = new ConcurrentHashMap<>();

    public void addSession(String sessionId, WebSocketSession session) {
        boardSessions.computeIfAbsent(sessionId, k -> ConcurrentHashMap.newKeySet()).add(session);
    }

    public void removeSession(String sessionId, WebSocketSession session) {
        var wsSessions = getSessions(sessionId);
        if (!CollectionUtils.isEmpty(wsSessions)) {
            wsSessions.remove(session);
            if (wsSessions.isEmpty()) {
                boardSessions.remove(sessionId);
            }
        }
    }

    public void removeSession(String sessionId) {
        var wsSessions = getSessions(sessionId);
        if (!CollectionUtils.isEmpty(wsSessions)) {
            wsSessions.forEach(session -> {
                try {
                    if (session.isOpen()) {
                        session.close(CloseStatus.NORMAL);
                    }
                } catch (Exception e) {
                    log.warn("Error closing session", e);
                }
            });
            boardSessions.remove(sessionId);
        }
    }

    public Set<WebSocketSession> getSessions(String sessionId) {
        return boardSessions.getOrDefault(sessionId, Collections.emptySet());
    }

    public boolean hasActiveSessions(String sessionId) {
        return !getSessions(sessionId).isEmpty();
    }
}
