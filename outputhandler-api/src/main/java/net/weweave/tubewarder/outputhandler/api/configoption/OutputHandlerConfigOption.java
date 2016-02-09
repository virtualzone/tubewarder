package net.weweave.tubewarder.outputhandler.api.configoption;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract base class for all output handler configuration options.
 */
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

    /**
     * @return The type of the option (should be set by the child class)
     */
    public String getType() {
        return type;
    }

    /**
     * @return The unique id of the option
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return The readable label of the option
     */
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * @return True if option must have a value
     */
    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    /**
     * @return Additional parameters
     */
    public Map<String, Object> getAdditionalParameters() {
        return new HashMap<>();
    }
}
