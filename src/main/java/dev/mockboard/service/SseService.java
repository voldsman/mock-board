package dev.mockboard.service;

import dev.mockboard.core.enums.SseEventType;
import dev.mockboard.core.storage.SessionStorage;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class SseService {

    private final SessionStorage sessionStorage;
    private final ScheduledExecutorService heartbeatScheduler =
            Executors.newScheduledThreadPool(1, Thread.ofVirtual().factory());

    public SseEmitter createEmitter(String sessionId) {
        log.debug("Creating SSE emitter for session: {}", sessionId);
        var emitter = new SseEmitter();
        emitter.onCompletion(() -> handleCompletion(sessionId, emitter));
        emitter.onTimeout(() -> handleTimeout(sessionId, emitter));
        emitter.onError(e -> handleError(sessionId, emitter, e));

        sessionStorage.addEmitter(sessionId, emitter);
        if (!sendConnectionMessage(sessionId, emitter)) {
            return emitter;
        }
        scheduleHeartbeat(sessionId, emitter);
        return emitter;
    }

    private boolean sendConnectionMessage(String sessionId, SseEmitter emitter) {
        try {
            emitter.send(SseEmitter.event()
                    .name(SseEventType.CONNECTED.name())
                    .data(Map.of(
                            "message", "connected",
                            "sessionId", sessionId,
                            "timestamp", System.currentTimeMillis()
                    )));
            log.debug("Connection message sent for session: {}", sessionId);
            return true;
        } catch (IOException e) {
            log.error("Failed to send connection message for session: {}", sessionId, e);
            sessionStorage.removeEmitter(sessionId, emitter);
            emitter.completeWithError(e);
            return false;
        }
    }

    private void scheduleHeartbeat(String sessionId, SseEmitter emitter) {
        heartbeatScheduler.scheduleAtFixedRate(
                () -> sendHeartbeat(sessionId, emitter),
                20,
                20,
                TimeUnit.SECONDS
        );
    }

    private void sendHeartbeat(String sessionId, SseEmitter emitter) {
        try {
            emitter.send(SseEmitter.event()
                    .name(SseEventType.HEARTBEAT.name())
                    .data(Map.of("timestamp", System.currentTimeMillis())));
            log.trace("Heartbeat sent for session: {}", sessionId);
        } catch (Exception e) {
            log.warn("Heartbeat failed for session: {}, removing emitter", sessionId);
            sessionStorage.removeEmitter(sessionId, emitter);
            emitter.completeWithError(e);
        }
    }

    private void handleCompletion(String sessionId, SseEmitter emitter) {
        log.info("SSE completed for session: {}", sessionId);
        sessionStorage.removeEmitter(sessionId, emitter);
    }

    private void handleTimeout(String sessionId, SseEmitter emitter) {
        log.warn("SSE timeout for session: {}", sessionId);
        sessionStorage.removeEmitter(sessionId, emitter);
    }

    private void handleError(String sessionId, SseEmitter emitter, Throwable e) {
        log.error("SSE error for session: {}", sessionId, e);
        sessionStorage.removeEmitter(sessionId, emitter);
    }

    public void broadcast(String sessionId, SseEventType event, Object message) {
        var emitters = sessionStorage.getEmitters(sessionId);
        if (emitters.isEmpty()) {
            log.debug("No emitters found for session: {}", sessionId);
            return;
        }

        var deadEmitters = new ArrayList<SseEmitter>();
        for (var emitter : emitters) {
            try {
                emitter.send(SseEmitter.event()
                        .name(event.name())
                        .data(message));
                log.debug("Event {} sent to session {}", event.name(), sessionId);
            } catch (Exception e) {
                log.error("Failed to send event {} to session {}", event.name(), sessionId, e);
                deadEmitters.add(emitter);
            }
        }

        deadEmitters.forEach(e -> sessionStorage.removeEmitter(sessionId, e));
    }

    @PreDestroy
    public void shutdown() {
        log.info("Shutting down SSE service");
        heartbeatScheduler.shutdown();
        try {
            if (!heartbeatScheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                heartbeatScheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            heartbeatScheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
