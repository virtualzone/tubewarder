package eio.util.output;

import eio.domain.AbstractOutputHandlerConfiguration;
import eio.util.Address;

public abstract class AbstractOutputHandler<T extends AbstractOutputHandlerConfiguration> {
    public abstract void process(T config, Address sender, Address recipient, String subject, String content);
}
