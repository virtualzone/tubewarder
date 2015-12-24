package eio.util.output;

import eio.domain.AbstractOutputHandlerConfiguration;
import eio.service.model.AttachmentModel;
import eio.util.Address;

import java.util.List;

public abstract class AbstractOutputHandler<T extends AbstractOutputHandlerConfiguration> {
    public abstract void process(T config, Address sender, Address recipient, String subject, String content, List<AttachmentModel> attachments);
}
