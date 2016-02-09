package net.weweave.tubewarder.outputhandler.api;

/**
 * An InvalidAddessException is thrown if the provided address is invalid for the concrete output handler.
 */
public class InvalidAddessException extends Exception {
    public InvalidAddessException() {
        super();
    }

    public InvalidAddessException(String message) {
        super(message);
    }
}
