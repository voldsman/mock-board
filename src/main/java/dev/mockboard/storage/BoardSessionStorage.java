package dev.mockboard.storage;

import dev.mockboard.utils.SessionIdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class BoardSessionStorage {

    private final Set<String> activeSessions = ConcurrentHashMap.newKeySet();

    public String createSession() {
        var sessionId = SessionIdGenerator.generate();
        activeSessions.add(sessionId);
        return sessionId;
    }

    public void addSession(String sessionId) {
        activeSessions.add(sessionId);
    }

    public boolean isValidSession(String sessionId) {
        return sessionId != null && activeSessions.contains(sessionId);
    }

    public void removeSession(String sessionId) {
        if (sessionId != null) {
            activeSessions.remove(sessionId);
        }
    }
}
