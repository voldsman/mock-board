package dev.mockboard.web;

import dev.mockboard.config.AppConfig;
import dev.mockboard.storage.RuleStore;
import dev.mockboard.web.model.MockRule;
import dev.mockboard.web.model.RuleRequest;
import dev.mockboard.web.ws.WsManager;
import io.javalin.Javalin;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.ContentType;
import io.javalin.http.Context;
import io.javalin.http.Cookie;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.UUID;
import java.util.function.UnaryOperator;

@Slf4j
public record Router(Javalin app) {

    private static final UnaryOperator<String> FREEMARKER_TEMPLATE_RESOLVER = template -> template + AppConfig.FREEMARKER_EXTENSION;

    public void register() {
        app.before(ctx -> {
            if (ctx.req().getContentLength() > AppConfig.MAX_REQUEST_BODY_SIZE) {
                log.warn("Blocked large request from IP: {}", ctx.ip());
                throw new BadRequestResponse("Request too large (Max 1MB)");
            }

            ctx.header("Access-Control-Allow-Origin", "*");
            ctx.header("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE, OPTIONS");
            ctx.header("Access-Control-Allow-Headers", "*");
        });

        app.get("/", this::registerIndex);

        registerApi();
        registerWebsocket();

        app.error(404, ctx -> {
            var acceptHeader = ctx.header("Accept");
            if (acceptHeader != null && acceptHeader.contains(ContentType.HTML)) {
                registerIndex(ctx);
            }
        });
    }

    private void registerIndex(Context ctx) {
        ctx.contentType(ContentType.HTML);
        var index = getClass().getResourceAsStream("/web/index.html");
        if (index != null) {
            ctx.result(index);
        } else {
            ctx.status(500).result("Frontend not found. Run 'npm run build' in src/main/frontend.");
        }
    }

    private void registerApi() {
        app.get("/api", ctx -> ctx.json("api home"));

        app.post("/api/start", ctx -> {
            var sessionId = UUID.randomUUID().toString();
            var sessionCookie = new Cookie(AppConfig.SESSION_COOKIE_NAME, sessionId);
            sessionCookie.setHttpOnly(true);
            sessionCookie.setMaxAge(AppConfig.COOKIE_MAX_AGE);
            sessionCookie.setPath("/");
            ctx.cookie(sessionCookie);
            ctx.json(Map.of("sessionId", sessionId));
        });

        app.post("/api/reset", ctx -> {
            ctx.removeCookie(AppConfig.SESSION_COOKIE_NAME);
            ctx.json("ok");
        });

        app.get("/api/board/{boardId}/rules", ctx -> {
            var boardId = ctx.pathParam("boardId");
            ctx.json(RuleStore.getRules(boardId));
        });

        app.post("/api/board/{boardId}/rules", ctx -> {
            var boardId = ctx.pathParam("boardId");

            var request = ctx.bodyAsClass(RuleRequest.class);
            if (request.path() == null || request.path().isBlank()) {
                throw new BadRequestResponse("Path is required");
            }

            MockRule rule = MockRule.create(
                    request.method(),
                    request.path(),
                    request.status(),
                    request.contentType(),
                    request.body(),
                    request.delay()
            );
            RuleStore.addRule(boardId, rule);
            WsManager.broadcast(boardId, "SYSTEM: Rule added: " + rule.path());
            ctx.status(201).json(rule);
        });
    }

    private void registerWebsocket() {
        app.ws("/ws/{boardId}", ws -> {
            ws.onConnect(ctx -> {
                var boardId = ctx.pathParam("boardId");
                WsManager.add(boardId, ctx);

                log.info("Board rejoined: {}", boardId);
                // WsManager.broadcast(boardId, "System: Session " + boardId + " active.");
            });
            ws.onMessage(ctx -> {
                var msg = ctx.message();

                if (msg.equals("_ping")) {
                    ctx.send("_pong");
                    return;
                }

                var boardId = ctx.pathParam("boardId");
                WsManager.broadcast(boardId, "User: " + msg);
            });
            ws.onClose(ctx -> {
                var boardId = ctx.pathParam("boardId");
                WsManager.remove(boardId, ctx);
            });
            ws.onError(ctx -> {
                log.error("WS error: {}", ctx.error() != null ? ctx.error().getMessage() : "null");
                var boardId = ctx.pathParam("boardId");
                WsManager.remove(boardId, ctx);
            });
        });
    }
}
