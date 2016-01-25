package net.weweave.tubewarder.outputhandler.config;

import org.json.JSONObject;

public abstract class OutputHandlerConfigOption {
    private String id;
    private String label;
    private boolean required;

    public OutputHandlerConfigOption(String id, String label, boolean required) {
        this.id = id;
        this.label = label;
        this.required = required;
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

    public JSONObject getJson() {
        JSONObject result = new JSONObject();
        result.put("id", getId());
        result.put("label", getLabel());
        result.put("required", isRequired());
        return result;
    }
}
