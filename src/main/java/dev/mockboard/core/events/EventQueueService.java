package dev.mockboard.core.events;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class EventQueueService {

    private final Map<String, BlockingQueue<Event>> queues = new ConcurrentHashMap<>();
    private final Map<String, Long> lastActivities = new ConcurrentHashMap<>();
    private final ScheduledExecutorService cleaner;

    public EventQueueService() {
        cleaner = Executors.newSingleThreadScheduledExecutor(Thread.ofVirtual().factory());
        this.startCleanupTask();
    }

    public CompletableFuture<Event> waitForEvent(String sessionId, Duration timeout) {
        var queue = getOrCreateQueue(sessionId);
        updateLastActivity(sessionId);

        return CompletableFuture.supplyAsync(() -> {
            try {
                log.debug("Waiting for event on session: {} (timeout: {}ms)", sessionId, timeout.toMillis());
                Event event = queue.poll(timeout.toMillis(), TimeUnit.MILLISECONDS);

                if (event != null) {
                    log.debug("Event received for session: {} - type: {}", sessionId, event.getType());
                } else {
                    log.trace("Timeout reached for session: {}", sessionId);
                }

                return event;
            } catch (InterruptedException e) {
                log.warn("Wait interrupted for session: {}", sessionId);
                Thread.currentThread().interrupt();
                throw new CompletionException(e);
            }
        }, Executors.newVirtualThreadPerTaskExecutor());
    }

    public boolean publishEvent(String sessionId, Event event) {
        var queue = queues.get(sessionId);

        if (queue == null) {
            log.warn("No active queue for session: {}, creating one", sessionId);
            queue = getOrCreateQueue(sessionId);
        }

        boolean offered = queue.offer(event);
        if (offered) {
            log.info("Event published to session: {} - type: {}", sessionId, event.getType());
            updateLastActivity(sessionId);
        } else {
            log.error("Failed to publish event to session: {} - queue full", sessionId);
        }

        return offered;
    }

    public boolean publishEvent(String sessionId, EventType eventType, Object data) {
        return publishEvent(sessionId, Event.of(eventType, data));
    }

    public void removeSession(String sessionId) {
        queues.remove(sessionId);
        lastActivities.remove(sessionId);
        log.info("Session removed: {}", sessionId);
    }

    private BlockingQueue<Event> getOrCreateQueue(String sessionId) {
        return queues.computeIfAbsent(sessionId, k -> {
            log.info("Creating new queue for session: {}", sessionId);
            return new LinkedBlockingQueue<>(100);
        });
    }

    private void updateLastActivity(String sessionId) {
        lastActivities.put(sessionId, System.currentTimeMillis());
    }

    private void startCleanupTask() {
        cleaner.scheduleAtFixedRate(() -> {
            try {
                cleanupInactiveSessions();
            } catch (Exception e) {
                log.error("Error during cleanup", e);
            }
        }, 10, 10, TimeUnit.MINUTES);
    }

    private void cleanupInactiveSessions() {
        long now = System.currentTimeMillis();
        long inactiveThreshold = Duration.ofMinutes(5).toMillis();

        var toRemove = lastActivities.entrySet().stream()
                .filter(entry -> (now - entry.getValue()) > inactiveThreshold)
                .map(Map.Entry::getKey)
                .toList();

        if (!toRemove.isEmpty()) {
            log.info("Cleaning up {} inactive sessions", toRemove.size());
            toRemove.forEach(this::removeSession);
        }
    }

    @PreDestroy
    public void shutdown() {
        log.info("Shutting down EventQueueService");
        cleaner.shutdown();

        try {
            if (!cleaner.awaitTermination(5, TimeUnit.SECONDS)) {
                cleaner.shutdownNow();
            }
        } catch (InterruptedException e) {
            cleaner.shutdownNow();
            Thread.currentThread().interrupt();
        }

        queues.clear();
        lastActivities.clear();
    }
}
