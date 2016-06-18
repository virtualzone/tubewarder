package net.weweave.tubewarder.outputhandler.api;

/**
 * An InvalidConfigException is thrown if the config is invalid for the concrete output handler.
 */
public class InvalidConfigException extends Exception {
    private String field;

    public InvalidConfigException() {
        super();
    }

    public InvalidConfigException(String message) {
        super(message);
    }

    public InvalidConfigException(String message, String field) {
        super(message);
        setField(field);
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
