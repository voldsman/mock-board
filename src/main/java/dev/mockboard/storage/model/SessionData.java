package dev.mockboard.storage.model;

import lombok.Data;
import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Data
public class SessionData implements Serializable {

    private final Set<WebSocketSession> webSocketSessions = ConcurrentHashMap.newKeySet();
    private final BoardData boardData = new BoardData();

    @Getter
    private final AtomicLong lastAccessTime = new AtomicLong(System.currentTimeMillis());

    public void touchLastAccessTime() {
        lastAccessTime.set(System.currentTimeMillis());
    }

    public void addWebSocketSession(WebSocketSession webSocketSession) {
        webSocketSessions.add(webSocketSession);
    }

    public void removeWebSocketSession(WebSocketSession webSocketSession) {
        webSocketSessions.remove(webSocketSession);
    }
}
