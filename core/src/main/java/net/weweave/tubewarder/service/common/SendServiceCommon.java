package net.weweave.tubewarder.service.common;

import net.weweave.tubewarder.dao.AppTokenDao;
import net.weweave.tubewarder.dao.ChannelTemplateDao;
import net.weweave.tubewarder.dao.LogDao;
import net.weweave.tubewarder.domain.AppToken;
import net.weweave.tubewarder.domain.Channel;
import net.weweave.tubewarder.domain.ChannelTemplate;
import net.weweave.tubewarder.domain.Log;
import net.weweave.tubewarder.exception.*;
import net.weweave.tubewarder.outputhandler.OutputHandlerConfig;
import net.weweave.tubewarder.service.model.AttachmentModel;
import net.weweave.tubewarder.service.model.ErrorCode;
import net.weweave.tubewarder.service.model.KeyValueModel;
import net.weweave.tubewarder.service.model.SendModel;
import net.weweave.tubewarder.service.response.SendServiceResponse;
import net.weweave.tubewarder.util.Address;
import net.weweave.tubewarder.util.TemplateRenderer;
import net.weweave.tubewarder.outputhandler.OutputHandler;
import net.weweave.tubewarder.outputhandler.OutputHandlerFactory;
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

    private void renderAndSend(SendModel sendModel, SendServiceResponse response) throws ObjectNotFoundException, TemplateCorruptException, TemplateModelException {
        ChannelTemplate channelTemplate = getChannelTemplateDao().getChannelTemplateByNames(sendModel.template, sendModel.channel);
        Channel channel = channelTemplate.getChannel();

        Map<String, Object> model = convertDataModelToMap(sendModel.model == null ? new ArrayList<>() : sendModel.model);
        String subject = getTemplateRenderer().render(channelTemplate.getSubject(), model);
        String content = getTemplateRenderer().render(channelTemplate.getContent(), model);
        if (sendModel.echo) {
            response.subject = subject;
            response.content = content;
        }

        Map<String, Object> config = OutputHandlerConfig.configJsonStringToMap(channel.getConfigJson());
        OutputHandler outputHandler = OutputHandlerFactory.getOutputHandler(config);
        Address sender = new Address(channelTemplate.getSenderName(), channelTemplate.getSenderAddress());
        Address recipient = new Address((GenericValidator.isBlankOrNull(sendModel.recipient.name) ? "" : sendModel.recipient.name), sendModel.recipient.address);
        List<AttachmentModel> attachments = (sendModel.attachments == null ? new ArrayList<>() : sendModel.attachments);
        outputHandler.process(sender, recipient, subject, content, attachments);
        log(sendModel, channelTemplate, recipient, subject, content);
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
}
