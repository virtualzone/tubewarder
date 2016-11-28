package net.weweave.tubewarder.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jknack.handlebars.*;
import com.github.jknack.handlebars.cache.ConcurrentMapTemplateCache;
import com.github.jknack.handlebars.context.MapValueResolver;
import net.weweave.tubewarder.exception.TemplateCorruptException;
import net.weweave.tubewarder.exception.TemplateModelException;
import org.apache.commons.validator.GenericValidator;

import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.util.Map;

@ApplicationScoped
public class TemplateRenderer {
    public String render(String content, Map<String, Object> dataModel) throws TemplateCorruptException, TemplateModelException {
        Context context = getMapContext(dataModel);
        return render(content, context);
    }

    public String render(String content, JsonNode json) throws TemplateCorruptException, TemplateModelException {
        try {
            Context context = getJsonContext(json);
            return render(content, context);
        } catch (IOException e) {
            throw new TemplateModelException();
        }
    }

    public String render(String content, String json) throws TemplateCorruptException, TemplateModelException {
        try {
            Context context = getJsonContext(json);
            return render(content, context);
        } catch (IOException e) {
            throw new TemplateModelException();
        }
    }

    private String render(String content, Context context) {
        if (GenericValidator.isBlankOrNull(content)) {
            return "";
        }

        Handlebars hbs = getHandlebarsInstance();
        try {
            Template template = hbs.compileInline(content);
            String result = template.apply(context);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private Handlebars getHandlebarsInstance() {
        Handlebars hbs = new Handlebars()
                .with(EscapingStrategy.NOOP)
                .with(new ConcurrentMapTemplateCache());
        return hbs;
    }

    private Context getMapContext(Map<String, Object> dataModel) {
        Context context = Context
                .newBuilder(dataModel)
                .resolver(MapValueResolver.INSTANCE)
                .build();
        return context;
    }

    private Context getJsonContext(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode model = mapper.readValue(json, JsonNode.class);
        return getJsonContext(model);
    }

    private Context getJsonContext(JsonNode model) throws IOException {
        Context context = Context
                .newBuilder(model)
                .resolver(JsonNodeValueResolver.INSTANCE)
                .build();
        return context;
    }
}
