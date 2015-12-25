package tubewarder.service.common;

import tubewarder.dao.AppTokenDao;
import tubewarder.dao.ChannelTemplateDao;
import tubewarder.dao.LogDao;
import tubewarder.domain.AppToken;
import tubewarder.domain.Channel;
import tubewarder.domain.ChannelTemplate;
import tubewarder.domain.Log;
import tubewarder.exception.InvalidInputParametersException;
import tubewarder.exception.ObjectNotFoundException;
import tubewarder.exception.PermissionException;
import tubewarder.service.response.AbstractResponse;
import tubewarder.service.model.AttachmentModel;
import tubewarder.service.model.ErrorCode;
import tubewarder.service.model.SendModel;
import tubewarder.util.Address;
import tubewarder.util.TemplateRenderer;
import tubewarder.util.output.AbstractOutputHandler;
import tubewarder.util.output.OutputHandlerFactory;
import org.apache.commons.validator.GenericValidator;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    public AbstractResponse process(SendModel sendModel) {
        AbstractResponse response = new AbstractResponse();
        try {
            validateInputParameters(sendModel);
            checkPermission(sendModel);
            renderAndSend(sendModel);
        } catch (InvalidInputParametersException e) {
            response.error = ErrorCode.INVALID_INPUT_PARAMETERS;
        } catch (ObjectNotFoundException e) {
            response.error = ErrorCode.OBJECT_LOOKUP_ERROR;
        } catch (PermissionException e) {
            response.error = ErrorCode.PERMISSION_DENIED;
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

    private void renderAndSend(SendModel sendModel) throws ObjectNotFoundException {
        ChannelTemplate channelTemplate = getChannelTemplateDao().getChannelTemplate(sendModel.template, sendModel.channel);
        Channel channel = channelTemplate.getChannel();
        String subject = getTemplateRenderer().render(channelTemplate.getSubject(), sendModel.model);
        String content = getTemplateRenderer().render(channelTemplate.getContent(), sendModel.model);
        AbstractOutputHandler outputHandler = OutputHandlerFactory.getOutputHandler(channel.getOutputHandler());
        Address sender = new Address(channelTemplate.getSenderName(), channelTemplate.getSenderAddress());
        Address recipient = new Address((GenericValidator.isBlankOrNull(sendModel.recipient.name) ? "" : sendModel.recipient.name), sendModel.recipient.address);
        List<AttachmentModel> attachments = (sendModel.attachments == null ? new ArrayList<>() : sendModel.attachments);
        outputHandler.process(channel.getConfig(), sender, recipient, subject, content, attachments);
        log(sendModel, channelTemplate, recipient, subject, content);
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
}
