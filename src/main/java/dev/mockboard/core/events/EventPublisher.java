package dev.mockboard.core.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventPublisher {

    private final EventQueueService eventQueueService;

    public void publish(String sessionId, EventType eventType, Object data) {
        log.debug("Publishing event: {} to session: {}", eventType, sessionId);

        boolean success = eventQueueService.publishEvent(sessionId, eventType, data);

        if (!success) {
            log.warn("Failed to publish event to session: {}", sessionId);
        }
    }

    public void publishRequestCaptured(String sessionId, Object requestData) {
        publish(sessionId, EventType.REQUEST_CAPTURED, requestData);
    }
}
