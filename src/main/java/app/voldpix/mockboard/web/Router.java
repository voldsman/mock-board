package app.voldpix.mockboard.web;

import app.voldpix.mockboard.config.AppConfig;
import io.javalin.Javalin;
import io.javalin.http.BadRequestResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Router {

    private final Javalin app;

    public Router(Javalin app) {
        this.app = app;
    }

    public void register() {
        app.before(ctx -> {
            if (ctx.req().getContentLength() > AppConfig.MAX_REQUEST_BODY_SIZE) {
                log.warn("Blocked large request from IP: {}", ctx.ip());
                throw new BadRequestResponse("Request too large (Max 1MB)");
            }
        });

        registerStatic();
        registerApi();
        registerWebsocket();
    }

    private void registerStatic() {
        app.get("/", ctx -> ctx.json("home page"));
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
