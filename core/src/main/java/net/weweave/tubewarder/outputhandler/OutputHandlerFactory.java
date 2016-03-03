package net.weweave.tubewarder.outputhandler;

import net.weweave.tubewarder.outputhandler.api.IOutputHandler;
import net.weweave.tubewarder.outputhandler.api.OutputHandler;
import org.apache.commons.validator.GenericValidator;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

@ApplicationScoped
public class OutputHandlerFactory {
    private static final Logger LOG = Logger.getLogger(OutputHandlerFactory.class.getName());
    private static Map<String, Class<? extends IOutputHandler>> HANDLERS = null;

    @Inject
    private ServletContext context;

    public IOutputHandler getOutputHandler(Map<String, Object> config) {
        String id = (String)config.getOrDefault("id", "");
        if (isValidId(config)) {
            Class<? extends IOutputHandler> clazz = getHandlers().get(id);
            try {
                return clazz.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public boolean isValidId(Map<String, Object> config) {
        if (config.containsKey("id") && (config.get("id") instanceof String)) {
            String id = (String)config.getOrDefault("id", "");
            if (!GenericValidator.isBlankOrNull(id)) {
                return getHandlers().containsKey(id);
            }
        }
        return false;
    }

    public Set<String> getOutputHandlerIds() {
        return getHandlers().keySet();
    }

    public Map<String, Class<? extends IOutputHandler>> getHandlers() {
        if (HANDLERS == null) {
            init();
        }
        return HANDLERS;
    }

    private synchronized void init() {
        LOG.info("Initializing output handlers...");
        HANDLERS = new HashMap<>();
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .addUrls(ClasspathHelper.forJavaClassPath())
                .addUrls(ClasspathHelper.forWebInfClasses(getContext()))
                .addUrls(ClasspathHelper.forWebInfLib(getContext()))
                .addUrls(ClasspathHelper.forClassLoader())
                .setScanners(new TypeAnnotationsScanner(), new SubTypesScanner(false)));
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(OutputHandler.class);
        for (Class<?> clazz : classes) {
            LOG.info("Found annotated output handler class: " + clazz.getName());
            if (IOutputHandler.class.isAssignableFrom(clazz)) {
                String id = clazz.getAnnotation(OutputHandler.class).id();
                HANDLERS.put(id, (Class<? extends IOutputHandler>)clazz);
                LOG.info("Added output handler " + id + " in class: " + clazz.getName());
            }
        }
        LOG.info("Initialization of output handlers completed.");
    }

    public ServletContext getContext() {
        return context;
    }

    public void setContext(ServletContext context) {
        this.context = context;
    }
}
