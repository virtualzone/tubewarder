package net.weweave.tubewarder.outputhandler.api;

/**
 * An InvalidConfigException is thrown if the config is invalid for the concrete output handler.
 */
public class InvalidConfigException extends Exception {
    public InvalidConfigException() {
        super();
    }

    public InvalidConfigException(String message) {
        super(message);
    }
}
