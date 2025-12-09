package dev.mockboard;

import dev.mockboard.config.AppConfig;
import dev.mockboard.web.Router;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.json.JavalinJackson;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MockBoardServer {

    private final Javalin app;

    public MockBoardServer() {
        this.app = Javalin.create(config -> {
            config.showJavalinBanner = false;
            config.useVirtualThreads = true;

            if (AppConfig.DEV_MODE) {
                config.requestLogger.http((ctx, ms) ->
                        log.info("{} {} - {}ms", ctx.method(), ctx.path(), ms)
                );
            }

            config.staticFiles.add(staticFiles -> {
                staticFiles.hostedPath = "/";
                staticFiles.directory = "/web";
                staticFiles.location = Location.CLASSPATH;
                staticFiles.precompress = true;
            });
            config.jsonMapper(new JavalinJackson());
        });

        new Router(app).register();

        registerShutdownHook();
    }

    public void start() {
        log.info("Starting on port {}", AppConfig.PORT);
        app.start(AppConfig.PORT);
    }

    public void stop() {
        log.info("Stopping server...");
        app.stop();
    }

    private void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.warn("Shutting down server...");
            this.stop();
            log.info("Server stopped");
        }));
    }
}
