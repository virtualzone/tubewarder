package net.weweave.tubewarder.util.output;

import net.weweave.tubewarder.util.Address;
import net.weweave.tubewarder.domain.AbstractOutputHandlerConfiguration;
import net.weweave.tubewarder.service.model.AttachmentModel;

import java.util.List;

public abstract class AbstractOutputHandler<T extends AbstractOutputHandlerConfiguration> {
    public abstract void process(T config, Address sender, Address recipient, String subject, String content, List<AttachmentModel> attachments);
}
