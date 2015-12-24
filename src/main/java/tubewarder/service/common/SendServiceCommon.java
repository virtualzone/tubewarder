package tubewarder.service.common;

import tubewarder.dao.ChannelTemplateDao;
import tubewarder.domain.Channel;
import tubewarder.domain.ChannelTemplate;
import tubewarder.exception.InvalidInputParametersException;
import tubewarder.exception.ObjectNotFoundException;
import tubewarder.service.model.AbstractResponse;
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
import java.util.List;

@RequestScoped
public class SendServiceCommon {
    @Inject
    private ChannelTemplateDao channelTemplateDao;

    @Inject
    private TemplateRenderer templateRenderer;

    public AbstractResponse process(SendModel sendModel) {
        AbstractResponse response = new AbstractResponse();
        try {
            validateInputParameters(sendModel);
            renderAndSend(sendModel);
        } catch (InvalidInputParametersException e) {
            response.error = ErrorCode.INVALID_INPUT_PARAMETERS;
        } catch (ObjectNotFoundException e) {
            response.error = ErrorCode.OBJECT_LOOKUP_ERROR;
        }
        return response;
    }

    private void validateInputParameters(SendModel sendModel) throws InvalidInputParametersException {
        if (GenericValidator.isBlankOrNull(sendModel.template) ||
                GenericValidator.isBlankOrNull(sendModel.channel) ||
                sendModel.recipient == null ||
                GenericValidator.isBlankOrNull(sendModel.recipient.address)) {
            throw new InvalidInputParametersException();
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
}
