package net.weweave.tubewarder.util.output;

import net.weweave.tubewarder.domain.SysoutOutputHandlerConfiguration;
import net.weweave.tubewarder.util.Address;
import net.weweave.tubewarder.service.model.AttachmentModel;

import java.util.List;

public class SysoutOutputHandler extends AbstractOutputHandler<SysoutOutputHandlerConfiguration> {
    @Override
    public void process(SysoutOutputHandlerConfiguration config, Address sender, Address recipient, String subject, String content, List<AttachmentModel> attachments) {
        System.out.println(config.getPrefix() + content + config.getSuffix());
    }
}
