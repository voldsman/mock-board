package dev.mockboard.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

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
        var wsSession = boardSessions.get(sessionId);
        if (wsSession != null) {
            wsSession.remove(session);
            if (wsSession.isEmpty()) {
                boardSessions.remove(sessionId);
            }
        }
    }

    public void removeSession(String sessionId) {
        var wsSession = boardSessions.get(sessionId);
        if (wsSession != null) {
            wsSession.clear();
            boardSessions.remove(sessionId);
        }
    }
}
