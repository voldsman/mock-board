package app.voldpix.mockboard.config;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import io.javalin.http.Context;
import io.javalin.rendering.FileRenderer;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.io.StringWriter;
import java.util.Locale;
import java.util.Map;

public final class FreemarkerConfig implements FileRenderer {

    private final Configuration configuration;

    public FreemarkerConfig() {
        this.configuration = new Configuration(Configuration.VERSION_2_3_34);

        configuration.setTemplateLoader(new ClassTemplateLoader(
                getClass().getClassLoader(),
                "web/templates"
        ));

        configuration.setLocale(Locale.US);
        configuration.setDefaultEncoding("UTF-8");
        configuration.setRecognizeStandardFileExtensions(true);
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        if (AppConfig.DEV_MODE) {
            configuration.setTemplateUpdateDelayMilliseconds(0L);
        } else {
            configuration.setTemplateUpdateDelayMilliseconds(Integer.MAX_VALUE);
        }
    }

    @NotNull
    @Override
    @SneakyThrows(Exception.class)
    public String render(@NotNull String filePath, @NotNull Map<String, ?> model, @NotNull Context context) {
        try {
            var template = configuration.getTemplate(filePath);
            var writer = new StringWriter();
            template.process(model, writer);
            return writer.toString();

        } catch (Exception e) {
            throw new RuntimeException("Failed to render FreeMarker template: " + filePath, e);
        }
    }
}
