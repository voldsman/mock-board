package dev.mockboard.core.storage.model;

import dev.mockboard.Constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CopyOnWriteArrayList;

public class BoardData implements Serializable {
    private final ConcurrentLinkedDeque<RequestData> requests = new ConcurrentLinkedDeque<>();
    private final CopyOnWriteArrayList<MockRuleData> mockRules = new CopyOnWriteArrayList<>();

    public void addRequest(RequestData request) {
        requests.addFirst(request);

        while (requests.size() > Constants.MAX_RECENT_REQUESTS) {
            requests.removeLast();
        }
    }

    public List<RequestData> getRequests() {
        return new ArrayList<>(requests);
    }

    public void clearRequests() {
        requests.clear();
    }

    public void addMockRule(MockRuleData mockRule) {
        if (mockRules.size() < Constants.MAX_MOCK_RULES) {
            mockRules.add(mockRule);
        }
    }

    public void removeMockRule(String id) {
        mockRules.removeIf(rule -> rule.getId().equals(id));
    }

    public List<MockRuleData> getMockRules() {
        return new ArrayList<>(mockRules);
    }
}
