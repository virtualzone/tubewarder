package tubewarder.util.output;

import tubewarder.domain.AbstractOutputHandlerConfiguration;
import tubewarder.service.model.AttachmentModel;
import tubewarder.util.Address;

import java.util.List;

public abstract class AbstractOutputHandler<T extends AbstractOutputHandlerConfiguration> {
    public abstract void process(T config, Address sender, Address recipient, String subject, String content, List<AttachmentModel> attachments);
}
