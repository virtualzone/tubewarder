package net.weweave.tubewarder.outputhandler.api;

/**
 * A FieldInvalidException is thrown if a field doesn't match the required formatting.
 */
public class FieldInvalidException extends InvalidConfigException {
    public FieldInvalidException(String field) {
        super("Field " + field + " is invalid", field);
    }
}
