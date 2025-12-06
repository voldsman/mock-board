package app.voldpix.mockboard.web;

import app.voldpix.mockboard.config.AppConfig;
import io.javalin.Javalin;
import io.javalin.http.BadRequestResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class Router {

    public static void register(Javalin app) {
        app.before(ctx -> {
            if (ctx.req().getContentLength() > AppConfig.MAX_REQUEST_BODY_SIZE) {
                log.warn("Blocked large request from IP: {}", ctx.ip());
                throw new BadRequestResponse("Request too large (Max 1MB)");
            }
        });

        app.get("/", ctx -> ctx.redirect("/index.html"));

        app.ws("/ws/{uuid}", ws -> {
            ws.onConnect(ctx -> log.info("Client connected: {}", ctx.sessionId()));
            ws.onMessage(ctx -> log.info("Message received: {}", ctx.sessionId()));
            ws.onClose(ctx -> log.info("Client disconnected: {}", ctx.sessionId()));
        });
    }
}
