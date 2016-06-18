package net.weweave.tubewarder.outputhandler.api;

/**
 * A FieldRequiredException is thrown if a field is required but is actually empty.
 */
public class FieldRequiredException extends InvalidConfigException {
    public FieldRequiredException(String field) {
        super("Field " + field + " is required", field);
    }
}
