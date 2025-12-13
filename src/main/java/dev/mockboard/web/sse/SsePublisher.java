package dev.mockboard.web.sse;

import dev.mockboard.core.enums.SseEventType;
import dev.mockboard.service.SseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SsePublisher {

    private final SseService sseService;

    public void publish(final String sessionId, SseEventType event, Object message) {
        log.debug("Publishing SSE event, sessionId: {}, {}", sessionId, event);
        sseService.broadcast(sessionId, event, message);
    }
}
