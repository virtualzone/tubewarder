package net.weweave.tubewarder.outputhandler.config;

import java.util.HashMap;
import java.util.Map;

public abstract class OutputHandlerConfigOption {
    private final String type;
    private String id;
    private String label;
    private boolean required;

    public OutputHandlerConfigOption(String type, String id, String label, boolean required) {
        this.type = type;
        this.id = id;
        this.label = label;
        this.required = required;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public Map<String, Object> getAdditionalParameters() {
        return new HashMap<>();
    }
}
