package dev.mockboard.web;

import dev.mockboard.Constants;
import dev.mockboard.core.events.EventQueueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping(Constants.API_PATH_PREFIX + "/events")
@RequiredArgsConstructor
public class LongPoolingController {

    private final EventQueueService eventQueueService;

    @GetMapping("/{sessionId}")
    public CompletableFuture<ResponseEntity<?>> pollEvents(
            @PathVariable String sessionId,
            @RequestParam(defaultValue = "30000") long timeout) {
        log.debug("Long poll request for session: {} (timeout: {}ms)", sessionId, timeout);

        if (timeout < 1000 || timeout > 60000) {
            return CompletableFuture.completedFuture(
                    ResponseEntity.badRequest().build()
            );
        }

        var timeoutDuration = Duration.ofMillis(timeout);
        return eventQueueService.waitForEvent(sessionId, timeoutDuration)
                .thenApply(event -> {
                    if (event != null) {
                        log.debug("Returning event to client for session: {}", sessionId);
                        return ResponseEntity.ok(event);
                    } else {
                        log.trace("Timeout - no events for session: {}", sessionId);
                        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
                    }
                })
                .exceptionally(throwable -> {
                    log.error("Error during long poll for session: {}", sessionId, throwable);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                });
    }

    @DeleteMapping("/{sessionId}")
    public ResponseEntity<Void> disconnect(@PathVariable String sessionId) {
        log.info("Manual disconnect requested for session: {}", sessionId);
        eventQueueService.removeSession(sessionId);
        return ResponseEntity.noContent().build();
    }
}
