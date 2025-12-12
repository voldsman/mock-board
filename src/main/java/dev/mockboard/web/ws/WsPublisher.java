package dev.mockboard.web.ws;

import dev.mockboard.core.enums.WsEventType;
import dev.mockboard.core.storage.SessionStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import tools.jackson.databind.ObjectMapper;

import java.util.HashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class WsPublisher {

    private final SessionStorage sessionStorage;
    private final ObjectMapper objectMapper;

    public void broadcast(String sessionId, WsEventType event, Object message) {
        var wsSessions = sessionStorage.getWsSessions(sessionId);
        if (!CollectionUtils.isEmpty(wsSessions)) {
            var result = new HashMap<String, Object>();
            result.put("event", event);
            result.put("data", message);

            wsSessions.stream()
                    .filter(WebSocketSession::isOpen)
                    .forEach(session -> {
                        try {
                            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(result)));
                        } catch (Exception e) {
                            log.warn(e.getMessage(), e);
                        }
                    });
        }
    }
}
