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
import net.weweave.tubewarder.outputhandler.OutputHandlerDispatcher;
import net.weweave.tubewarder.outputhandler.OutputHandlerFactory;
import net.weweave.tubewarder.outputhandler.api.*;
import net.weweave.tubewarder.outputhandler.api.configoption.OutputHandlerConfigOption;
import net.weweave.tubewarder.outputhandler.api.configoption.StringConfigOption;
import net.weweave.tubewarder.service.model.ErrorCode;
import net.weweave.tubewarder.service.model.KeyValueModel;
import net.weweave.tubewarder.service.model.SendModel;
import net.weweave.tubewarder.service.response.SendServiceResponse;
import net.weweave.tubewarder.util.TemplateRenderer;
import org.apache.commons.validator.GenericValidator;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
    private OutputHandlerDispatcher outputHandlerDispatcher;

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

        // Send
        List<Attachment> attachments = sendModel.attachmentModelToList();
        getOutputHandlerDispatcher().invoke(outputHandler, config, sender, recipient, rewrites.subject, rewrites.content, attachments);
        log(sendModel, channelTemplate, sender, recipient, rewrites.subject, rewrites.content);
    }

    private void rewriteConfig(IOutputHandler outputHandler, Config config, Map<String, Object> model, Rewrites rewrites) throws TemplateCorruptException, TemplateModelException {
        Map<String, Object> newModel = new HashMap<>(model);
        newModel.put("recipientName", rewrites.recipientName);
        newModel.put("recipientAddress", rewrites.recipientAddress);
        newModel.put("subject", rewrites.subject);
        newModel.put("content", rewrites.content);

        Map<String, Object> uriEncodedModel = getUriEncodedModel(newModel);

        List<OutputHandlerConfigOption> configOptions = outputHandler.getConfigOptions();

        for (String key : config.keySet()) {
            Object value = config.get(key);
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
        for (String key : model.keySet()) {
            Object value = model.get(key);
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

    private void log(SendModel model, ChannelTemplate channelTemplate, Address sender, Address recipient, String subject, String content) {
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

    public OutputHandlerDispatcher getOutputHandlerDispatcher() {
        return outputHandlerDispatcher;
    }

    public void setOutputHandlerDispatcher(OutputHandlerDispatcher outputHandlerDispatcher) {
        this.outputHandlerDispatcher = outputHandlerDispatcher;
    }

    private class Rewrites {
        public String recipientName;
        public String recipientAddress;
        public String subject;
        public String content;
    }
}
