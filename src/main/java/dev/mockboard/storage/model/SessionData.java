package dev.mockboard.storage.model;

import dev.mockboard.Constants;
import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

public class SessionData implements Serializable {
    private final ConcurrentLinkedDeque<HttpRequestData> recentRequests = new ConcurrentLinkedDeque<>();
    private final CopyOnWriteArrayList<String> mockRules = new CopyOnWriteArrayList<>();

    @Getter
    private final AtomicLong lastAccessTime = new AtomicLong(System.currentTimeMillis());

    public void addRequest(HttpRequestData request) {
        recentRequests.addFirst(request);

        while (recentRequests.size() > Constants.MAX_RECENT_REQUESTS) {
            recentRequests.removeLast();
        }
    }

    public List<HttpRequestData> getRecentRequests() {
        return new ArrayList<>(recentRequests);
    }

    public void touchLastAccessTime() {
        lastAccessTime.set(System.currentTimeMillis());
    }
}
