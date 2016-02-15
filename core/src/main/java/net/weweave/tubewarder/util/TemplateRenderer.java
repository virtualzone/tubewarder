package net.weweave.tubewarder.util;

import freemarker.core.InvalidReferenceException;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import net.weweave.tubewarder.exception.TemplateCorruptException;
import net.weweave.tubewarder.exception.TemplateModelException;
import org.apache.commons.validator.GenericValidator;

import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.*;

@ApplicationScoped
public class TemplateRenderer {
    private static Configuration CONFIG = null;

    public String render(String content, Map<String, Object> dataModel) throws TemplateCorruptException, TemplateModelException {
        if (GenericValidator.isBlankOrNull(content)) {
            return "";
        }
        try {
            Template freemarkerTemplate = new Template(UUID.randomUUID().toString(), new StringReader(content), getConfiguration());
            Writer out = new StringWriter();
            freemarkerTemplate.process(dataModel, out);
            return out.toString();
        } catch (InvalidReferenceException e) {
            throw new TemplateModelException();
        } catch (ParseException e) {
            throw new TemplateCorruptException();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        } catch (TemplateException e) {
            throw new TemplateModelException();
        }
    }

    private Configuration getConfiguration() {
        if (CONFIG == null) {
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            cfg.setLogTemplateExceptions(false);
            CONFIG = cfg;
        }
        return CONFIG;
    }
}
