package app.voldpix.mockboard.web;

import app.voldpix.mockboard.config.AppConfig;
import io.javalin.Javalin;
import io.javalin.http.BadRequestResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public record Router(Javalin app) {

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
        app.get("/", ctx -> ctx.render("index.ftlh", Map.of(
                "title", "Hello FreeMarker + Javalin!",
                "username", "Voldpix"
        )));
        app.get("/{uuid}", ctx -> ctx.json("board page"));
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
