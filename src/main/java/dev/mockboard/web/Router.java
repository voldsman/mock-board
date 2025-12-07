package dev.mockboard.web;

import dev.mockboard.config.AppConfig;
import io.javalin.Javalin;
import io.javalin.http.BadRequestResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
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
        app.get("/", ctx -> ctx.render(FREEMARKER_TEMPLATE_RESOLVER.apply("landing"), Map.of(
                "version", AppConfig.APP_VERSION,
                "githubURL", "https://github.com/voldsman/mock-board"
        )));

        app.post("/start", ctx -> ctx.redirect("/board/123"));
        app.get("/board/{uuid}", ctx -> ctx.render(FREEMARKER_TEMPLATE_RESOLVER.apply("board"), Map.of(
                "version", AppConfig.APP_VERSION,
                "uuid", "c75f7c66-e858-47d6-bb82-7ea5547c800c"
        )));
    }

    private void registerApi() {
        app.get("/api", ctx -> ctx.json("api home"));
    }

    private void registerWebsocket() {
        app.ws("/ws/{uuid}", ws -> {
            ws.onConnect(ctx -> log.info("Client connected: {}", ctx.sessionId()));
            ws.onMessage(ctx -> log.info("Message received: {}", ctx.sessionId()));
            ws.onClose(ctx -> log.info("Client disconnected: {}", ctx.sessionId()));
        });
    }
}
