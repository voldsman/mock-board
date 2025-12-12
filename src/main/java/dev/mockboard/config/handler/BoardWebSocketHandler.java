package dev.mockboard.config.handler;

import dev.mockboard.storage.SessionStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.net.URI;

@Slf4j
@Component
@RequiredArgsConstructor
public class BoardWebSocketHandler extends TextWebSocketHandler {

    private final SessionStorage sessionStorage;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        var sessionId = extractSessionId(session);
        if (!sessionStorage.isValidSession(sessionId)) {
            session.close(CloseStatus.BAD_DATA);
        }

        if (sessionId != null) {
            sessionStorage.addWsSession(sessionId, session);
        } else {
            session.close(CloseStatus.BAD_DATA);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        var payload = message.getPayload();
        if ("_ping".equalsIgnoreCase(payload)) {
            session.sendMessage(new TextMessage("_pong"));
        }

        var sessionId = extractSessionId(session);
        if (!sessionStorage.isValidSession(sessionId)) {
            session.close(CloseStatus.NOT_ACCEPTABLE);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        var sessionId = extractSessionId(session);
        if (sessionId != null) {
            sessionStorage.removeWsSession(sessionId, session);
            log.info("Client disconnected from board: {}", sessionId);
        }
    }

    private String extractSessionId(WebSocketSession session) {
        try {
            URI uri = session.getUri();
            if (uri == null) return null;

            var path = uri.getPath();
            String[] parts = path.split("/");
            if (parts.length > 0) {
                return parts[parts.length - 1];
            }
        } catch (Exception e) {
            log.error("Failed to extract board ID from URL", e);
        }

        return null;
    }
}
