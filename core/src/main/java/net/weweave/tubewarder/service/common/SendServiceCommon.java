package net.weweave.tubewarder.service.common;

import net.weweave.tubewarder.dao.AppTokenDao;
import net.weweave.tubewarder.dao.ChannelTemplateDao;
import net.weweave.tubewarder.dao.LogDao;
import net.weweave.tubewarder.domain.AppToken;
import net.weweave.tubewarder.domain.Channel;
import net.weweave.tubewarder.domain.ChannelTemplate;
import net.weweave.tubewarder.domain.Log;
import net.weweave.tubewarder.exception.*;
import net.weweave.tubewarder.outputhandler.OutputHandlerConfigUtil;
import net.weweave.tubewarder.outputhandler.OutputHandlerFactory;
import net.weweave.tubewarder.outputhandler.api.*;
import net.weweave.tubewarder.service.model.ErrorCode;
import net.weweave.tubewarder.service.model.KeyValueModel;
import net.weweave.tubewarder.service.model.SendModel;
import net.weweave.tubewarder.service.response.SendServiceResponse;
import net.weweave.tubewarder.util.TemplateRenderer;
import org.apache.commons.validator.GenericValidator;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.*;

@RequestScoped
public class SendServiceCommon {
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

    public SendServiceResponse process(SendModel sendModel) {
        SendServiceResponse response = new SendServiceResponse();
        try {
            validateInputParameters(sendModel);
            checkPermission(sendModel);
            renderAndSend(sendModel, response);
        } catch (InvalidInputParametersException e) {
            response.error = ErrorCode.INVALID_INPUT_PARAMETERS;
        } catch (ObjectNotFoundException e) {
            response.error = ErrorCode.OBJECT_LOOKUP_ERROR;
        } catch (PermissionException e) {
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
            AppToken appToken = getAppTokenDao().get(sendModel.token);
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

        // Build addresses and check recipient
        Address sender = new Address(channelTemplate.getSenderName(), channelTemplate.getSenderAddress());
        Address recipient = new Address(rewrites.recipientName, rewrites.recipientAddress);
        try {
            outputHandler.checkRecipientAddress(recipient);
        } catch (InvalidAddessException e) {
            throw new InvalidInputParametersException(e.getMessage());
        }

        // Send
        List<Attachment> attachments = sendModel.attachmentModelToList();
        outputHandler.process(config, sender, recipient, rewrites.subject, rewrites.content, attachments);
        log(sendModel, channelTemplate, recipient, rewrites.subject, rewrites.content);
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

    private void log(SendModel model, ChannelTemplate channelTemplate, Address recipient, String subject, String content) {
        Log log = new Log();
        log.setDate(new Date());
        log.setKeyword(model.keyword);
        log.setDetails(model.details);
        log.setTemplateName(channelTemplate.getTemplate().getName());
        log.setTemplateId(channelTemplate.getTemplate().getExposableId());
        log.setChannelName(channelTemplate.getChannel().getName());
        log.setChannelId(channelTemplate.getChannel().getExposableId());
        log.setRecipientName(recipient.getName());
        log.setRecipientAddress(recipient.getAddress());
        log.setSubject(subject);
        log.setContent(content);
        getLogDao().store(log);
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

    private class Rewrites {
        public String recipientName;
        public String recipientAddress;
        public String subject;
        public String content;
    }
}
