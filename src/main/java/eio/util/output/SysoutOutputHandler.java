package eio.util.output;

import eio.domain.SysoutOutputHandlerConfiguration;
import eio.util.Address;

public class SysoutOutputHandler extends AbstractOutputHandler<SysoutOutputHandlerConfiguration> {
    @Override
    public void process(SysoutOutputHandlerConfiguration config, Address sender, Address recipient, String subject, String content) {
        System.out.println(config.getPrefix() + content + config.getSuffix());
    }
}
