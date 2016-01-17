package net.weweave.tubewarder.test.rest;

import com.jayway.restassured.specification.ResponseSpecification;
import net.weweave.tubewarder.domain.*;
import net.weweave.tubewarder.service.model.ErrorCode;
import net.weweave.tubewarder.test.TestSendServiceCommon;
import org.jboss.arquillian.junit.Arquillian;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;

@RunWith(Arquillian.class)
public class TestSendService extends AbstractRestTest {
    @Inject
    private TestSendServiceCommon common;

    @Test
    public void testSendSuccess() {
        AppToken token = getCommon().createAppToken();
        Channel channel = getCommon().createChannel();
        Template template = getCommon().createTemplate();
        getCommon().createChannelTemplate(template, channel,
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
        AppToken token = getCommon().createAppToken();
        Channel channel = getCommon().createChannel();
        Template template = getCommon().createTemplate();
        getCommon().createChannelTemplate(template, channel,
                "${firstname}",
                "${firstname} ${lastname}");

        Map<String, Object> model = createMap(
                "firstname", "John");
        validateSendResponse(token.getExposableId(), template.getName(), channel.getName(), model, "John", "+490000",
                "error", equalTo(ErrorCode.MISSING_MODEL_PARAMETER));
    }

    @Test
    public void testCorruptTemplate() {
        AppToken token = getCommon().createAppToken();
        Channel channel = getCommon().createChannel();
        Template template = getCommon().createTemplate();
        getCommon().createChannelTemplate(template, channel,
                "${firstname)",
                "Nothing");

        Map<String, Object> model = createMap(
                "firstname", "John");
        validateSendResponse(token.getExposableId(), template.getName(), channel.getName(), model, "John", "+490000",
                "error", equalTo(ErrorCode.TEMPLATE_CORRUPT));
    }

    @Test
    public void testInvalidTemplate() {
        AppToken token = getCommon().createAppToken();
        Channel channel = getCommon().createChannel();
        Template template = getCommon().createTemplate();
        getCommon().createChannelTemplate(template, channel, "", "");

        validateSendResponse(token.getExposableId(), "Unknown", channel.getName(), createMap(), "John", "+490000",
                "error", equalTo(ErrorCode.OBJECT_LOOKUP_ERROR));
    }

    @Test
    public void testInvalidChannel() {
        AppToken token = getCommon().createAppToken();
        Channel channel = getCommon().createChannel();
        Template template = getCommon().createTemplate();
        getCommon().createChannelTemplate(template, channel, "", "");

        validateSendResponse(token.getExposableId(), template.getName(), "Unknown", createMap(), "John", "+490000",
                "error", equalTo(ErrorCode.OBJECT_LOOKUP_ERROR));
    }

    @Test
    public void testUnassignedChannel() {
        AppToken token = getCommon().createAppToken();
        Channel channel = getCommon().createChannel();
        Channel channel2 = getCommon().createChannel("email");
        Template template = getCommon().createTemplate();
        getCommon().createChannelTemplate(template, channel, "", "");

        validateSendResponse(token.getExposableId(), template.getName(), channel2.getName(), createMap(), "John", "+490000",
                "error", equalTo(ErrorCode.OBJECT_LOOKUP_ERROR));
    }

    @Test
    public void testInvalidToken() {
        getCommon().createAppToken();
        Channel channel = getCommon().createChannel();
        Template template = getCommon().createTemplate();
        getCommon().createChannelTemplate(template, channel, "", "");

        validateSendResponse(UUID.randomUUID().toString(), template.getName(), channel.getName(), createMap(), "John", "+490000",
                "error", equalTo(ErrorCode.PERMISSION_DENIED));
    }

    @Test
    public void testMissingRecipientAddress() {
        AppToken token = getCommon().createAppToken();
        Channel channel = getCommon().createChannel();
        Template template = getCommon().createTemplate();
        getCommon().createChannelTemplate(template, channel, "", "");

        validateSendResponse(token.getExposableId(), template.getName(), channel.getName(), createMap(), "John", "",
                "error", equalTo(ErrorCode.INVALID_INPUT_PARAMETERS));
    }

    @Test
    public void testEmptyToken() {
        getCommon().createAppToken();
        Channel channel = getCommon().createChannel();
        Template template = getCommon().createTemplate();
        getCommon().createChannelTemplate(template, channel, "", "");

        validateSendResponse("", template.getName(), channel.getName(), createMap(), "John", "+490000",
                "error", equalTo(ErrorCode.INVALID_INPUT_PARAMETERS));
    }

    @Test
    public void testEmptyRecipientName() {
        AppToken token = getCommon().createAppToken();
        Channel channel = getCommon().createChannel();
        Template template = getCommon().createTemplate();
        getCommon().createChannelTemplate(template, channel, "", "");

        validateSendResponse(token.getExposableId(), template.getName(), channel.getName(), createMap(), "", "+490000",
                "error", equalTo(ErrorCode.OK));
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

        JSONArray modelArray = new JSONArray();
        for (String key : model.keySet()) {
            JSONObject entry = new JSONObject();
            entry.put("key", key);
            entry.put("value", model.get(key));
            modelArray.put(entry);
        }

        JSONObject payload = new JSONObject();
        payload.put("token", token);
        payload.put("echo", true);
        payload.put("template", templateName);
        payload.put("channel", channelName);
        payload.put("recipient", recipient);
        payload.put("model", modelArray);
        return payload;
    }

    public TestSendServiceCommon getCommon() {
        return common;
    }

    public void setCommon(TestSendServiceCommon common) {
        this.common = common;
    }
}
