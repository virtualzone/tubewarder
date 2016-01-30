package net.weweave.tubewarder.outputhandler.api;

public class InvalidConfigException extends Exception {
    public InvalidConfigException() {
        super();
    }

    public InvalidConfigException(String message) {
        super(message);
    }
}
