package net.weweave.tubewarder.test.rest;

import com.jayway.restassured.specification.ResponseSpecification;
import net.weweave.tubewarder.dao.*;
import net.weweave.tubewarder.domain.*;
import net.weweave.tubewarder.service.model.ErrorCode;
import org.jboss.arquillian.junit.Arquillian;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;

@RunWith(Arquillian.class)
public class TestSendService extends AbstractRestTest {
    @Inject
    private ChannelDao channelDao;

    @Inject
    private ChannelTemplateDao channelTemplateDao;

    @Inject
    private TemplateDao templateDao;

    @Inject
    private AppTokenDao appTokenDao;

    @Inject
    private SysoutOutputHandlerConfigurationDao configurationDao;

    @Test
    public void testSendSuccess() {
        AppToken token = createAppToken();
        Channel channel = createChannel();
        Template template = createTemplate();
        ChannelTemplate ct = createChannelTemplate(template, channel,
                "Welcome to Tubewarder, ${firstname}!",
                "Hi ${firstname} ${lastname}, here is your activation code: ${code}");

        Map<String, Object> model = createMap(
                "firstname", "John",
                "lastname", "Doe",
                "code", "1234567890");
        validateSendResponse(token.getExposableId(), template.getName(), channel.getName(), model, "John", "+490000",
                "error", equalTo(ErrorCode.OK),
                "subject", equalTo("Welcome to Tubewarder, John!"),
                "content", equalTo("Hi John Doe, here is your activation code: 1234567890"));
    }

    @Test
    public void testMissingModelParameter() {
        AppToken token = createAppToken();
        Channel channel = createChannel();
        Template template = createTemplate();
        ChannelTemplate ct = createChannelTemplate(template, channel,
                "${firstname}",
                "${firstname} ${lastname}");

        Map<String, Object> model = createMap(
                "firstname", "John");
        validateSendResponse(token.getExposableId(), template.getName(), channel.getName(), model, "John", "+490000",
                "error", equalTo(ErrorCode.MISSING_MODEL_PARAMETER));
    }

    @Test
    public void testCorruptTemplate() {
        AppToken token = createAppToken();
        Channel channel = createChannel();
        Template template = createTemplate();
        ChannelTemplate ct = createChannelTemplate(template, channel,
                "${firstname)",
                "Nothing");

        Map<String, Object> model = createMap(
                "firstname", "John");
        validateSendResponse(token.getExposableId(), template.getName(), channel.getName(), model, "John", "+490000",
                "error", equalTo(ErrorCode.TEMPLATE_CORRUPT));
    }

    private JSONObject validateSendResponse(String token, String templateName, String channelName, Map<String, Object> model, String recipientName, String recipientAddress, Object... body) {
        JSONObject payload = getSendRequestPayload(token, templateName, channelName, model, recipientName, recipientAddress);
        ResponseSpecification response = getResponseSpecificationPost(payload);
        setExpectedBodies(response, body);
        return getPostResponse(response, "send");
    }

    private JSONObject getSendRequestPayload(String token, String templateName, String channelName, Map<String, Object> model, String recipientName, String recipientAddress) {
        JSONObject recipient = new JSONObject();
        recipient.put("name", recipientName);
        recipient.put("address", recipientAddress);

        JSONObject modelJson = new JSONObject();
        for (String key : model.keySet()) {
            modelJson.put(key, (String)model.get(key));
        }

        JSONObject payload = new JSONObject();
        payload.put("token", token);
        payload.put("echo", true);
        payload.put("template", templateName);
        payload.put("channel", channelName);
        payload.put("recipient", recipient);
        payload.put("model", modelJson);
        return payload;
    }

    private AppToken createAppToken() {
        AppToken token = new AppToken();
        token.setName("Default");
        getAppTokenDao().store(token);
        return token;
    }

    private Channel createChannel() {
        SysoutOutputHandlerConfiguration config = new SysoutOutputHandlerConfiguration();
        config.setPrefix("Debug: [");
        config.setSuffix("]");
        getConfigurationDao().store(config);

        Channel channel = new Channel();
        channel.setName("sms");
        channel.setOutputHandler(OutputHandler.SYSOUT);
        channel.setConfig(config);
        getChannelDao().store(channel);
        return channel;
    }

    private Template createTemplate() {
        Template template = new Template();
        template.setName("DOI");
        getTemplateDao().store(template);
        return template;
    }

    private ChannelTemplate createChannelTemplate(Template template, Channel channel, String subject, String content) {
        ChannelTemplate ct = new ChannelTemplate();
        ct.setTemplate(template);
        ct.setChannel(channel);
        ct.setSubject(subject);
        ct.setContent(content);
        ct.setSenderName("weweave");
        ct.setSenderAddress("noreply@weweave.net");
        getChannelTemplateDao().store(ct);
        return ct;
    }

    public ChannelDao getChannelDao() {
        return channelDao;
    }

    public void setChannelDao(ChannelDao channelDao) {
        this.channelDao = channelDao;
    }

    public ChannelTemplateDao getChannelTemplateDao() {
        return channelTemplateDao;
    }

    public void setChannelTemplateDao(ChannelTemplateDao channelTemplateDao) {
        this.channelTemplateDao = channelTemplateDao;
    }

    public TemplateDao getTemplateDao() {
        return templateDao;
    }

    public void setTemplateDao(TemplateDao templateDao) {
        this.templateDao = templateDao;
    }

    public AppTokenDao getAppTokenDao() {
        return appTokenDao;
    }

    public void setAppTokenDao(AppTokenDao appTokenDao) {
        this.appTokenDao = appTokenDao;
    }

    public SysoutOutputHandlerConfigurationDao getConfigurationDao() {
        return configurationDao;
    }

    public void setConfigurationDao(SysoutOutputHandlerConfigurationDao configurationDao) {
        this.configurationDao = configurationDao;
    }
}
