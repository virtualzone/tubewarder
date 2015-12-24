package eio.util.output;

import eio.domain.SysoutOutputHandlerConfiguration;
import eio.service.model.AttachmentModel;
import eio.util.Address;

import java.util.List;

public class SysoutOutputHandler extends AbstractOutputHandler<SysoutOutputHandlerConfiguration> {
    @Override
    public void process(SysoutOutputHandlerConfiguration config, Address sender, Address recipient, String subject, String content, List<AttachmentModel> attachments) {
        System.out.println(config.getPrefix() + content + config.getSuffix());
    }
}
