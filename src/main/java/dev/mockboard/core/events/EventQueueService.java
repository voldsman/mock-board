package dev.mockboard.core.events;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.*;

@Slf4j
@Service
public class EventQueueService {

    private final Map<String, BlockingQueue<Event>> queues = new ConcurrentHashMap<>();
    private final Map<String, Long> lastActivity = new ConcurrentHashMap<>();
    private final ScheduledExecutorService cleaner;

    public EventQueueService() {
        cleaner = Executors.newSingleThreadScheduledExecutor(Thread.ofVirtual().factory());
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
        lastActivity.clear();
    }
}
