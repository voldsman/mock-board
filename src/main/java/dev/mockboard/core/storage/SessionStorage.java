package dev.mockboard.core.storage;

import dev.mockboard.core.storage.model.SessionData;
import dev.mockboard.utils.SessionIdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class SessionStorage {

    private final Map<String, SessionData> activeSessions = new ConcurrentHashMap<>();

    public String createSession() {
        var sessionId = SessionIdGenerator.generate();
        activeSessions.computeIfAbsent(sessionId, k -> new SessionData());
        log.info("Created new session with id {}", sessionId);
        return sessionId;
    }

    public void restoreSession(String sessionId) {
        log.info("Restoring session with id {}", sessionId);
        activeSessions.computeIfAbsent(sessionId, k -> new SessionData());
    }

    public boolean isValidSession(String sessionId) {
        return !CollectionUtils.isEmpty(activeSessions) && activeSessions.containsKey(sessionId);
    }

    public void addEmitter(String sessionId, SseEmitter emitter) {
        if (!isValidSession(sessionId)) return;

        var sessionData = activeSessions.get(sessionId);
        sessionData.addEmitter(emitter);
        sessionData.touchLastAccessTime();
    }

    public void removeEmitter(String sessionId, SseEmitter emitter) {
        if (!isValidSession(sessionId)) return;

        activeSessions.get(sessionId).removeEmitter(emitter);
    }

    public List<SseEmitter> getEmitters(String sessionId) {
        if (!isValidSession(sessionId)) return List.of();
        return activeSessions.get(sessionId).getEmitters();
    }

    public void removeSession(String sessionId) {
        if (!isValidSession(sessionId)) return;

        var sessionData = activeSessions.get(sessionId);
        for (var emitter : sessionData.getEmitters()) {
            try {
                emitter.complete();
            } catch (Exception e) {
                // ignore
            }
        }
        activeSessions.remove(sessionId);
        log.info("Removed session with id {}", sessionId);
    }
}
