package dev.mockboard.config.handler;

import dev.mockboard.storage.BoardSessionStorage;
import dev.mockboard.storage.WsSessionStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.net.URI;

@Slf4j
@Component
@RequiredArgsConstructor
public class BoardWebSocketHandler extends TextWebSocketHandler {

    private final BoardSessionStorage boardSessionStorage;
    private final WsSessionStorage wsSessionStorage;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        var boardId = extractBoardId(session);
        if (!boardSessionStorage.isValidSession(boardId)) {
            session.close(CloseStatus.BAD_DATA);
        }

        if (boardId != null) {
            wsSessionStorage.addSession(boardId, session);
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

        var boardId = extractBoardId(session);
        if (!boardSessionStorage.isValidSession(boardId)) {
            session.close(CloseStatus.NOT_ACCEPTABLE);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        var boardId = extractBoardId(session);
        if (boardId != null) {
            wsSessionStorage.removeSession(boardId, session);
            log.info("Client disconnected from board: {}", boardId);
        }
    }

    private String extractBoardId(WebSocketSession session) {
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
