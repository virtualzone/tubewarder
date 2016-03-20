package net.weweave.tubewarder.outputhandler.api;

/**
 * A PermanentProcessingException is thrown if an error occurred while executing
 * IOutputHandler.process() which will occur again if the dispatcher tries a second
 * time. Thus, the dispatcher should give up and not try again.
 */
public class PermanentProcessingException extends Exception {
    public PermanentProcessingException() {
        super();
    }

    public PermanentProcessingException(String message) {
        super(message);
    }
}
