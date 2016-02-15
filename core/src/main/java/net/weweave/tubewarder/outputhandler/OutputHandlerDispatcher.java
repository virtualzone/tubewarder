package net.weweave.tubewarder.outputhandler;

import net.weweave.tubewarder.outputhandler.api.Address;
import net.weweave.tubewarder.outputhandler.api.Attachment;
import net.weweave.tubewarder.outputhandler.api.Config;
import net.weweave.tubewarder.outputhandler.api.IOutputHandler;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import java.util.List;

@Stateless
public class OutputHandlerDispatcher {
    @Asynchronous
    public void invoke(IOutputHandler handler, Config config, Address sender, Address recipient, String subject, String content, List<Attachment> attachments) {
        handler.process(config, sender, recipient, subject, content, attachments);
    }
}
