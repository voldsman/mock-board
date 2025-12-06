package app.voldpix.mockboard;

import app.voldpix.mockboard.config.AppConfig;
import app.voldpix.mockboard.web.Router;
import freemarker.template.Configuration;
import freemarker.template.Version;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
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
            });

            config.fileRenderer(null);//todo:
        });

        new Router(app).register();

        registerShutdownHook();
    }

    public void start() {
        log.info("Starting MockBoard on port {}", AppConfig.PORT);
        app.start(AppConfig.PORT);
    }

    public void stop() {
        log.info("Stopping server...");
        app.stop();
    }

    private void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.warn("Shutting down mockboard server...");
            this.stop();
            log.info("MockBoard server stopped");
        }));
    }
}
