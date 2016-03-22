package net.weweave.tubewarder.outputhandler;

import net.weweave.tubewarder.domain.ChannelTemplate;
import net.weweave.tubewarder.domain.SendQueueItem;
import net.weweave.tubewarder.outputhandler.api.Address;
import net.weweave.tubewarder.outputhandler.api.Attachment;
import net.weweave.tubewarder.outputhandler.api.Config;
import net.weweave.tubewarder.outputhandler.api.IOutputHandler;
import net.weweave.tubewarder.outputhandler.api.TemporaryProcessingException;
import net.weweave.tubewarder.outputhandler.api.PermanentProcessingException;

import javax.ejb.*;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Singleton
public class OutputHandlerDispatcher {
    private static final Logger LOG = Logger.getLogger(OutputHandlerDispatcher.class.getName());

    @Inject
    private OutputHandlerFactory outputHandlerFactory;

    @Asynchronous
    public void processSendQueueItem(SendQueueItem item, SendQueueCallback callback) {
        log(item, "Start processing");

        Config config = OutputHandlerConfigUtil.configJsonStringToMap(item.getConfigJson());
        IOutputHandler handler = getOutputHandlerFactory().getOutputHandler(config);
        ChannelTemplate channelTemplate = item.getChannelTemplate();
        Address sender = new Address(channelTemplate.getSenderName(), channelTemplate.getSenderAddress());
        Address recipient = new Address(item.getRecipientName(), item.getRecipientAddress());
        String subject = item.getSubject();
        String content = item.getContent();
        List<Attachment> attachments = createAttachmentList(item);

        invoke(item, callback, handler, config, sender, recipient, subject, content, attachments);

        log(item, "Finished processing");
    }

    private List<Attachment> createAttachmentList(SendQueueItem item) {
        List<Attachment> result = new ArrayList<>();
        for (net.weweave.tubewarder.domain.Attachment atm : item.getAttachments()) {
            Attachment attachment = new Attachment();
            attachment.setContentType(atm.getContentType());
            attachment.setFilename(atm.getFilename());
            attachment.setPayload(atm.getPayload());
            result.add(attachment);
        }
        return result;
    }

    public void invoke(SendQueueItem item, SendQueueCallback callback, IOutputHandler handler, Config config, Address sender, Address recipient, String subject, String content, List<Attachment> attachments) {
        log(item, "Invoking " + handler.getClass().getName());
        try {
            handler.process(config, sender, recipient, subject, content, attachments);
            callback.success(item);
        } catch (TemporaryProcessingException e) {
            log(item, "TemporaryProcessingException while processing in " + handler.getClass().getName() + ": " + e.getMessage());
            callback.temporaryError(item);
        } catch (PermanentProcessingException e) {
            log(item, "PermanentProcessingException while processing in " + handler.getClass().getName() + ": " + e.getMessage());
            callback.permanentError(item);
        }
    }

    private void log(SendQueueItem item, String message) {
        LOG.info("Queue item ID = " + item.getExposableId() + ": " + message);
    }

    public OutputHandlerFactory getOutputHandlerFactory() {
        return outputHandlerFactory;
    }

    public void setOutputHandlerFactory(OutputHandlerFactory outputHandlerFactory) {
        this.outputHandlerFactory = outputHandlerFactory;
    }
}
