package eio.util.output;

import eio.domain.OutputHandler;

public class OutputHandlerFactory {
    public static AbstractOutputHandler getOutputHandler(OutputHandler outputHandler) {
        AbstractOutputHandler result = null;
        if (OutputHandler.SYSOUT.equals(outputHandler)) {
            result = new SysoutOutputHandler();
        } else if (OutputHandler.EMAIL.equals(outputHandler)) {
            result = new EmailOutputHandler();
        }
        return result;
    }
}
