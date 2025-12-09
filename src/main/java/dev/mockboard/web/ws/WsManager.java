package dev.mockboard.web.ws;

import io.javalin.websocket.WsContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public final class WsManager {

    private WsManager() {}

    private static final Map<String, Set<WsContext>> boardSessions = new ConcurrentHashMap<>();

    public static void add(String boardId, WsContext ctx) {
        boardSessions.computeIfAbsent(boardId, key -> ConcurrentHashMap.newKeySet()).add(ctx);
    }

    public static void remove(String boardId, WsContext ctx) {
        if (boardSessions.containsKey(boardId)) {
            boardSessions.get(boardId).remove(ctx);

            // remove empty boards
            if (boardSessions.get(boardId).isEmpty()) {
                boardSessions.remove(boardId);
            }
        }
    }

    public static void broadcast(String boardId, String message) {
        var sessions = boardSessions.get(boardId);
        if (sessions != null) {
            sessions.stream()
                    .filter(ctx -> ctx.session.isOpen())
                    .forEach(session -> session.send(message));
        }
    }
}
