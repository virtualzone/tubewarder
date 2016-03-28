package net.weweave.tubewarder.service.common;

import net.weweave.tubewarder.dao.*;
import net.weweave.tubewarder.domain.*;
import net.weweave.tubewarder.exception.*;
import net.weweave.tubewarder.outputhandler.OutputHandlerConfigUtil;
import net.weweave.tubewarder.outputhandler.OutputHandlerFactory;
import net.weweave.tubewarder.outputhandler.SendQueueScheduler;
import net.weweave.tubewarder.outputhandler.api.*;
import net.weweave.tubewarder.outputhandler.api.Attachment;
import net.weweave.tubewarder.outputhandler.api.configoption.OutputHandlerConfigOption;
import net.weweave.tubewarder.outputhandler.api.configoption.StringConfigOption;
import net.weweave.tubewarder.service.model.ErrorCode;
import net.weweave.tubewarder.service.model.KeyValueModel;
import net.weweave.tubewarder.service.model.SendModel;
import net.weweave.tubewarder.service.response.SendServiceResponse;
import net.weweave.tubewarder.util.TemplateRenderer;
import org.apache.commons.validator.GenericValidator;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.logging.Logger;

@Stateless
public class SendServiceCommon {
    private static final Logger LOG = Logger.getLogger(SendServiceCommon.class.getName());

    @Inject
    private ChannelTemplateDao channelTemplateDao;

    @Inject
    private TemplateRenderer templateRenderer;

    @Inject
    private AppTokenDao appTokenDao;

    @Inject
    private LogDao logDao;

    @Inject
    private OutputHandlerFactory outputHandlerFactory;

    @Inject
    private SendQueueItemDao sendQueueItemDao;

    @Inject
    private AttachmentDao attachmentDao;

    @Inject
    private SendQueueScheduler sendQueueScheduler;

    public SendServiceResponse process(SendModel sendModel) {
        SendServiceResponse response = new SendServiceResponse();
        try {
            LOG.info("Send API invoked with app token = " + sendModel.token);
            validateInputParameters(sendModel);
            checkPermission(sendModel);
            renderAndSend(sendModel, response);
        } catch (InvalidInputParametersException e) {
            response.error = ErrorCode.INVALID_INPUT_PARAMETERS;
        } catch (ObjectNotFoundException e) {
            response.error = ErrorCode.OBJECT_LOOKUP_ERROR;
        } catch (PermissionException e) {
            LOG.info("Permission denied for app token = " + sendModel.token);
            response.error = ErrorCode.PERMISSION_DENIED;
        } catch (TemplateCorruptException e) {
            response.error = ErrorCode.TEMPLATE_CORRUPT;
        } catch (TemplateModelException e) {
            response.error = ErrorCode.MISSING_MODEL_PARAMETER;
        }
        return response;
    }

    private void validateInputParameters(SendModel sendModel) throws InvalidInputParametersException {
        if (GenericValidator.isBlankOrNull(sendModel.template) ||
                GenericValidator.isBlankOrNull(sendModel.channel) ||
                sendModel.recipient == null ||
                GenericValidator.isBlankOrNull(sendModel.recipient.address) ||
                GenericValidator.isBlankOrNull(sendModel.token)) {
            throw new InvalidInputParametersException();
        }
    }

    private void checkPermission(SendModel sendModel) throws PermissionException {
        try {
            getAppTokenDao().get(sendModel.token);
        } catch (ObjectNotFoundException e) {
            throw new PermissionException();
        }
    }

    private void renderAndSend(SendModel sendModel, SendServiceResponse response) throws ObjectNotFoundException, TemplateCorruptException, TemplateModelException, InvalidInputParametersException {
        ChannelTemplate channelTemplate = getChannelTemplateDao().getChannelTemplateByNames(sendModel.template, sendModel.channel);
        Channel channel = channelTemplate.getChannel();

        // Render subject and content
        Map<String, Object> model = convertDataModelToMap(sendModel.model == null ? new ArrayList<>() : sendModel.model);
        String subject = getTemplateRenderer().render(channelTemplate.getSubject(), model);
        String content = getTemplateRenderer().render(channelTemplate.getContent(), model);

        // Do rewrites
        Rewrites rewrites = new Rewrites();
        rewrites.recipientName = (GenericValidator.isBlankOrNull(sendModel.recipient.name) ? "" : sendModel.recipient.name);
        rewrites.recipientAddress = sendModel.recipient.address;
        rewrites.subject = subject;
        rewrites.content = content;
        rewrite(rewrites, channel);

        // Echo?
        if (sendModel.echo) {
            response.recipient.name = rewrites.recipientName;
            response.recipient.address = rewrites.recipientAddress;
            response.subject = rewrites.subject;
            response.content = rewrites.content;
        }

        // Load output handler
        Config config = OutputHandlerConfigUtil.configJsonStringToMap(channel.getConfigJson());
        IOutputHandler outputHandler = getOutputHandlerFactory().getOutputHandler(config);

        // Apply rewriting to output handler config
        rewriteConfig(outputHandler, config, model, rewrites);

        // Build addresses and check recipient
        Address sender = new Address(channelTemplate.getSenderName(), channelTemplate.getSenderAddress());
        Address recipient = new Address(rewrites.recipientName, rewrites.recipientAddress);
        try {
            outputHandler.checkRecipientAddress(recipient);
        } catch (InvalidAddessException e) {
            throw new InvalidInputParametersException(e.getMessage());
        }

        // Log
        Log log = log(sendModel, channelTemplate, sender, recipient, rewrites.subject, rewrites.content);

        // Create queue item
        List<Attachment> attachments = sendModel.attachmentModelToList();
        SendQueueItem sendQueueItem = createSendQueueItem(channelTemplate, recipient, rewrites, config, attachments, sendModel, log);
        updateLogWithQueueId(log, sendQueueItem);

        // Enqueue
        response.queueId = sendQueueItem.getExposableId();
        getSendQueueScheduler().addSendQueueItem(sendQueueItem.getId());
    }

    private void updateLogWithQueueId(Log log, SendQueueItem sendQueueItem) {
        log.setQueueId(sendQueueItem.getExposableId());
        getLogDao().update(log);
    }

    private SendQueueItem createSendQueueItem(ChannelTemplate channelTemplate, Address recipient, Rewrites rewrites, Config config, List<Attachment> attachments, SendModel sendModel, Log log) {
        SendQueueItem item = new SendQueueItem();

        // Set payload properties
        item.setChannelTemplate(channelTemplate);
        item.setRecipientAddress(recipient.getAddress());
        item.setRecipientName(recipient.getName());
        item.setSubject(rewrites.subject);
        item.setContent(rewrites.content);
        item.setKeyword(sendModel.keyword);
        item.setDetails(sendModel.details);
        item.setConfigJson(OutputHandlerConfigUtil.configMapToJsonString(config));
        item.setLog(log);

        // Set controlling properties
        item.setInProcessing(true);
        item.setCreateDate(new Date());
        item.setLastTryDate(null);
        item.setTryCount(0);

        // Store with active processing flag, so we can add attachments without interruption
        getSendQueueItemDao().store(item);

        // Add attachments
        for (Attachment attachment : attachments) {
            net.weweave.tubewarder.domain.Attachment atm = new net.weweave.tubewarder.domain.Attachment();
            atm.setContentType(attachment.getContentType());
            atm.setFilename(attachment.getFilename());
            atm.setPayload(attachment.getPayload());
            atm.setSendQueueItem(item);
            getAttachmentDao().store(atm);
        }

        // Remove processing flag
        item.setInProcessing(false);
        getSendQueueItemDao().update(item);

        return item;
    }

    private void rewriteConfig(IOutputHandler outputHandler, Config config, Map<String, Object> model, Rewrites rewrites) throws TemplateCorruptException, TemplateModelException {
        Map<String, Object> newModel = new HashMap<>(model);
        newModel.put("recipientName", rewrites.recipientName);
        newModel.put("recipientAddress", rewrites.recipientAddress);
        newModel.put("subject", rewrites.subject);
        newModel.put("content", rewrites.content);

        Map<String, Object> uriEncodedModel = getUriEncodedModel(newModel);

        List<OutputHandlerConfigOption> configOptions = outputHandler.getConfigOptions();

        for (Map.Entry<String, Object> entry: config.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof String) {
                String stringValue = (String)value;
                if (!GenericValidator.isBlankOrNull(stringValue)) {
                    stringValue = getTemplateRenderer().render(stringValue, requiresUriEncoding(configOptions, key) ? uriEncodedModel : newModel);
                    config.put(key, stringValue);
                }
            }
        }
    }

    private Map<String, Object> getUriEncodedModel(Map<String, Object> model) {
        Map<String, Object> result = new HashMap<>();
        for (Map.Entry<String, Object> entry : model.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof String) {
                try {
                    result.put(key, URLEncoder.encode((String) value, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    result.put(key, "");
                }
            } else {
                result.put(key, value);
            }
        }
        return result;
    }

    private boolean requiresUriEncoding(List<OutputHandlerConfigOption> configOptions, String id) {
        for (OutputHandlerConfigOption option : configOptions) {
            if (id.equals(option.getId())) {
                if (option instanceof StringConfigOption) {
                    return ((StringConfigOption)option).isRequiresUriEncoding();
                }
                return false;
            }
        }
        return false;
    }

    private void rewrite(Rewrites rewrites, Channel channel) throws TemplateCorruptException, TemplateModelException {
        Map<String, Object> model = new HashMap<>();
        model.put("recipientName", rewrites.recipientName);
        model.put("recipientAddress", rewrites.recipientAddress);
        model.put("subject", rewrites.subject);
        model.put("content", rewrites.content);

        rewrites.recipientName = getTemplateRenderer().render(channel.getRewriteRecipientName(), model);
        rewrites.recipientAddress = getTemplateRenderer().render(channel.getRewriteRecipientAddress(), model);
        rewrites.subject = getTemplateRenderer().render(channel.getRewriteSubject(), model);
        rewrites.content = getTemplateRenderer().render(channel.getRewriteContent(), model);
    }

    private Map<String, Object> convertDataModelToMap(List<KeyValueModel> dataModel) {
        Map<String, Object> result = new HashMap<>();
        for (KeyValueModel entry : dataModel) {
            result.put(entry.key, entry.value);
        }
        return result;
    }

    private Log log(SendModel model, ChannelTemplate channelTemplate, Address sender, Address recipient, String subject, String content) {
        Log log = new Log();
        log.setDate(new Date());
        log.setAppToken(model.token);
        try {
            AppToken appToken = getAppTokenDao().get(model.token);
            log.setAppTokenName(appToken.getName());
        } catch (ObjectNotFoundException e) {
            log.setAppTokenName("");
        }
        log.setKeyword(model.keyword);
        log.setDetails(model.details);
        log.setTemplateName(channelTemplate.getTemplate().getName());
        log.setTemplateId(channelTemplate.getTemplate().getExposableId());
        log.setChannelName(channelTemplate.getChannel().getName());
        log.setChannelId(channelTemplate.getChannel().getExposableId());
        log.setSenderName(sender.getName());
        log.setSenderAddress(sender.getAddress());
        log.setRecipientName(recipient.getName());
        log.setRecipientAddress(recipient.getAddress());
        log.setSubject(subject);
        log.setContent(content);
        log.setStatus(QueueItemStatus.WAITING);
        getLogDao().store(log);
        return log;
    }

    public ChannelTemplateDao getChannelTemplateDao() {
        return channelTemplateDao;
    }

    public void setChannelTemplateDao(ChannelTemplateDao channelTemplateDao) {
        this.channelTemplateDao = channelTemplateDao;
    }

    public TemplateRenderer getTemplateRenderer() {
        return templateRenderer;
    }

    public void setTemplateRenderer(TemplateRenderer templateRenderer) {
        this.templateRenderer = templateRenderer;
    }

    public AppTokenDao getAppTokenDao() {
        return appTokenDao;
    }

    public void setAppTokenDao(AppTokenDao appTokenDao) {
        this.appTokenDao = appTokenDao;
    }

    public LogDao getLogDao() {
        return logDao;
    }

    public void setLogDao(LogDao logDao) {
        this.logDao = logDao;
    }

    public OutputHandlerFactory getOutputHandlerFactory() {
        return outputHandlerFactory;
    }

    public void setOutputHandlerFactory(OutputHandlerFactory outputHandlerFactory) {
        this.outputHandlerFactory = outputHandlerFactory;
    }

    public SendQueueItemDao getSendQueueItemDao() {
        return sendQueueItemDao;
    }

    public void setSendQueueItemDao(SendQueueItemDao sendQueueItemDao) {
        this.sendQueueItemDao = sendQueueItemDao;
    }

    public AttachmentDao getAttachmentDao() {
        return attachmentDao;
    }

    public void setAttachmentDao(AttachmentDao attachmentDao) {
        this.attachmentDao = attachmentDao;
    }

    public SendQueueScheduler getSendQueueScheduler() {
        return sendQueueScheduler;
    }

    public void setSendQueueScheduler(SendQueueScheduler sendQueueScheduler) {
        this.sendQueueScheduler = sendQueueScheduler;
    }

    private static class Rewrites {
        public String recipientName;
        public String recipientAddress;
        public String subject;
        public String content;
    }
}
