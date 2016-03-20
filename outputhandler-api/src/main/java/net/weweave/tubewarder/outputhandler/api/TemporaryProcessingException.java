package net.weweave.tubewarder.outputhandler.api;

/**
 * A TemporaryProcessingException is thrown if an error occurred while executing
 * IOutputHandler.process() which is likely to be temporary. Thus, the dispatcher
 * should try again later.
 */
public class TemporaryProcessingException extends Exception {
    public TemporaryProcessingException() {
        super();
    }

    public TemporaryProcessingException(String message) {
        super(message);
    }
}
