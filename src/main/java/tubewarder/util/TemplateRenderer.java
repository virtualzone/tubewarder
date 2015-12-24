package tubewarder.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;
import java.util.UUID;

@ApplicationScoped
public class TemplateRenderer {
    private static Configuration CONFIG = null;

    public String render(String content, Map<String, Object> dataModel) {
        try {
            Template freemarkerTemplate = new Template(UUID.randomUUID().toString(), new StringReader(content), getConfiguration());
            Writer out = new StringWriter();
            freemarkerTemplate.process(dataModel, out);
            return out.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        } catch (TemplateException e) {
            e.printStackTrace();
            return "";
        }
    }

    private Configuration getConfiguration() {
        if (CONFIG == null) {
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            CONFIG = cfg;
        }
        return CONFIG;
    }
}
