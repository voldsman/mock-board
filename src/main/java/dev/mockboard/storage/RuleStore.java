package dev.mockboard.storage;

import dev.mockboard.web.model.MockRule;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class RuleStore {

    private static final Map<String, List<MockRule>> store = new ConcurrentHashMap<>();

    public static void addRule(String boardId, MockRule rule) {
        store.computeIfAbsent(
                boardId,
                k -> Collections.synchronizedList(new ArrayList<>())).add(rule);
    }

    public static List<MockRule> getRules(String boardId) {
        return store.getOrDefault(boardId, Collections.emptyList());
    }

    public static void deleteRule(String boardId, String ruleId) {
        if (store.containsKey(boardId)) {
            store.get(boardId).removeIf(r -> r.id().equals(ruleId));
        }
    }
}
