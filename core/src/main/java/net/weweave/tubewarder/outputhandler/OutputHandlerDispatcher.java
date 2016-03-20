package net.weweave.tubewarder.outputhandler;

import net.weweave.tubewarder.outputhandler.api.Address;
import net.weweave.tubewarder.outputhandler.api.Attachment;
import net.weweave.tubewarder.outputhandler.api.Config;
import net.weweave.tubewarder.outputhandler.api.IOutputHandler;
import net.weweave.tubewarder.outputhandler.api.TemporaryProcessingException;
import net.weweave.tubewarder.outputhandler.api.PermanentProcessingException;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class OutputHandlerDispatcher {
    private static final Logger LOG = Logger.getLogger(OutputHandlerDispatcher.class.getName());

    @Asynchronous
    public void invoke(IOutputHandler handler, Config config, Address sender, Address recipient, String subject, String content, List<Attachment> attachments) {
        LOG.info("Invoking " + handler.getClass().getName());
        try {
            handler.process(config, sender, recipient, subject, content, attachments);
        } catch (TemporaryProcessingException e) {
            LOG.warning("TemporaryProcessingException while processing in " + handler.getClass().getName() + ": " + e.getMessage());
        } catch (PermanentProcessingException e) {
            LOG.warning("PermanentProcessingException while processing in " + handler.getClass().getName() + ": " + e.getMessage());
        }
    }
}
