package dev.mockboard.web.ws;

import dev.mockboard.storage.WsSessionStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
@Component
@RequiredArgsConstructor
public class WsPublisher {

    public final WsSessionStorage wsSessionStorage;

    public void broadcast(String sessionId, String message) {
        var wsSessions = wsSessionStorage.getSessions(sessionId);
        if (!CollectionUtils.isEmpty(wsSessions)) {
            wsSessions.stream()
                    .filter(WebSocketSession::isOpen)
                    .forEach(session -> {
                        try {
                            session.sendMessage(new TextMessage(message));
                        } catch (Exception e) {
                            log.warn(e.getMessage(), e);
                        }
                    });
        }
    }
}
