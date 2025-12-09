package dev.mockboard.web;

import dev.mockboard.config.AppConfig;
import dev.mockboard.storage.RuleStore;
import dev.mockboard.web.model.MockRule;
import dev.mockboard.web.model.RuleRequest;
import dev.mockboard.web.ws.WsManager;
import io.javalin.Javalin;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;

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

        registerStatic();
        registerApi();
        registerWebsocket();
    }

    private void registerStatic() {
        app.get("/", ctx -> {
            var existingSession = ctx.cookie(AppConfig.SESSION_COOKIE_NAME);
            if (existingSession != null && !existingSession.isBlank()) {
                ctx.redirect("/board/" + existingSession);
                return;
            }

            ctx.render(FREEMARKER_TEMPLATE_RESOLVER.apply("landing"), Map.of(
                    "version", AppConfig.APP_VERSION,
                    "githubURL", "https://github.com/voldsman/mock-board"
            ));
        });

        app.post("/start", ctx -> {
            var newSessionId = UUID.randomUUID().toString();

            var sessionCookie = new Cookie(AppConfig.SESSION_COOKIE_NAME, newSessionId);
            sessionCookie.setHttpOnly(true);
            sessionCookie.setMaxAge(AppConfig.COOKIE_MAX_AGE);
            sessionCookie.setPath("/");

            ctx.cookie(sessionCookie);
            ctx.redirect("/board/" + newSessionId);
        });

        app.get("/board/{sessionId}", ctx -> {
            var urlSessionId = ctx.pathParam("sessionId");
            var cookieSessionId = ctx.cookie(AppConfig.SESSION_COOKIE_NAME);

            if (cookieSessionId == null || !cookieSessionId.equalsIgnoreCase(urlSessionId)) {
                ctx.cookie("");//tmp
                ctx.redirect("/");
                return;
            }

            ctx.render(FREEMARKER_TEMPLATE_RESOLVER.apply("board"), Map.of(
                    "version", AppConfig.APP_VERSION,
                    "sessionId", urlSessionId
            ));
        });
    }

    private void registerApi() {
        app.get("/api", ctx -> ctx.json("api home"));

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
