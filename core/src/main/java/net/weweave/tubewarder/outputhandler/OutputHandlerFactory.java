package net.weweave.tubewarder.outputhandler;

import org.apache.commons.validator.GenericValidator;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class OutputHandlerFactory {
    private static final Map<String, Class<? extends OutputHandler>> HANDLERS;

    static {
        HANDLERS = new HashMap<>();
        HANDLERS.put(SysoutOutputHandler.ID, SysoutOutputHandler.class);
        HANDLERS.put(EmailOutputHandler.ID, EmailOutputHandler.class);
    }

    public static OutputHandler getOutputHandler(Map<String, Object> config) {
        String id = (String)config.getOrDefault("id", "");
        if (isValidId(config)) {
            Class<? extends OutputHandler> clazz = HANDLERS.get(id);
            try {
                return clazz.getDeclaredConstructor(Map.class).newInstance(config);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static boolean isValidId(Map<String, Object> config) {
        if (config.containsKey("id") && (config.get("id") instanceof String)) {
            String id = (String)config.getOrDefault("id", "");
            if (!GenericValidator.isBlankOrNull(id)) {
                return HANDLERS.containsKey(id);
            }
        }
        return false;
    }

    public static Set<String> getOutputHandlerIds() {
        return HANDLERS.keySet();
    }
}
