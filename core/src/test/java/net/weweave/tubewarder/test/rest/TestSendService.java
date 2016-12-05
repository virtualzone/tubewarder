package net.weweave.tubewarder.test.rest;

import com.jayway.restassured.specification.ResponseSpecification;
import net.weweave.tubewarder.domain.*;
import net.weweave.tubewarder.service.model.ErrorCode;
import net.weweave.tubewarder.test.TestSendServiceCommon;
import org.jboss.arquillian.junit.Arquillian;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.lang.System;
import java.util.ArrayList;
import java.util.List;
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
                "Welcome to Tubewarder, {{firstname}}!",
                "Hi {{firstname}} {{lastname}}, here is your activation code: {{code}}");

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
    public void testConditional() {
        AppToken token = getCommon().createAppToken();
        Channel channel = getCommon().createChannel();
        Template template = getCommon().createTemplate();
        getCommon().createChannelTemplate(template, channel,
                "",
                "Hello {{#if male}}Mr. {{name}}{{else}}Mrs. {{name}}{{/if}}!");

        Map<String, Object> model = createMap(
                "name", "Smith",
                "male", true);
        validateSendResponse(token.getExposableId(), template.getName(), channel.getName(), model, "John", "+490000",
                "error", equalTo(ErrorCode.OK),
                "subject", equalTo(""),
                "content", equalTo("Hello Mr. Smith!"));
    }

    @Test
    public void testConditionalElse() {
        AppToken token = getCommon().createAppToken();
        Channel channel = getCommon().createChannel();
        Template template = getCommon().createTemplate();
        getCommon().createChannelTemplate(template, channel,
                "",
                "Hello {{#if male}}Mr. {{name}}{{else}}Mrs. {{name}}{{/if}}!");

        Map<String, Object> model = createMap(
                "name", "Smith",
                "male", false);
        validateSendResponse(token.getExposableId(), template.getName(), channel.getName(), model, "John", "+490000",
                "error", equalTo(ErrorCode.OK),
                "subject", equalTo(""),
                "content", equalTo("Hello Mrs. Smith!"));
    }

    @Test
    public void testList() {
        AppToken token = getCommon().createAppToken();
        Channel channel = getCommon().createChannel();
        Template template = getCommon().createTemplate();
        getCommon().createChannelTemplate(template, channel,
                "",
                "Hello{{#each people}} -> {{firstname}} {{lastname}}{{/each}}!");

        Map<String, Object> p1 = createMap(
                "firstname", "John",
                "lastname", "Doe");
        Map<String, Object> p2 = createMap(
                "firstname", "Max",
                "lastname", "Miller");
        List<Map> people = new ArrayList<>();
        people.add(p1);
        people.add(p2);
        Map<String, Object> model = createMap(
                "people", people);
        validateSendResponse(token.getExposableId(), template.getName(), channel.getName(), model, "John", "+490000",
                "error", equalTo(ErrorCode.OK),
                "subject", equalTo(""),
                "content", equalTo("Hello -> John Doe -> Max Miller!"));
    }

    @Test
    public void testMissingModelParameter() {
        AppToken token = getCommon().createAppToken();
        Channel channel = getCommon().createChannel();
        Template template = getCommon().createTemplate();
        getCommon().createChannelTemplate(template, channel,
                "{{firstname}}",
                "{{firstname}} {{lastname}}");

        Map<String, Object> model = createMap(
                "firstname", "John");
        validateSendResponse(token.getExposableId(), template.getName(), channel.getName(), model, "John", "+490000",
                "error", equalTo(ErrorCode.OK),
                "subject", equalTo("John"),
                "content", equalTo("John "));
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

    @Test
    public void testRewriteSubject() {
        AppToken token = getCommon().createAppToken();

        JSONObject config = getCommon().getSysoutChannelConfigJson();
        Channel channel = new Channel();
        channel.setName("sms");
        channel.setRewriteRecipientName("{{recipientName}}");
        channel.setRewriteRecipientAddress("{{recipientAddress}}");
        channel.setRewriteSubject("This is a message to {{recipientName}}");
        channel.setRewriteContent("{{content}}");
        channel.setConfigJson(config.toString());
        getCommon().getChannelDao().store(channel);

        Template template = getCommon().createTemplate();
        getCommon().createChannelTemplate(template, channel,
                "Welcome to Tubewarder, {{firstname}}!",
                "Hi {{firstname}} {{lastname}}, here is your activation code: {{code}}");

        Map<String, Object> model = createMap(
                "firstname", "John",
                "lastname", "Doe",
                "code", "1234567890");
        validateSendResponse(token.getExposableId(), template.getName(), channel.getName(), model, "John", "+490000",
                "error", equalTo(ErrorCode.OK),
                "subject", equalTo("This is a message to John"),
                "content", equalTo("Hi John Doe, here is your activation code: 1234567890"));
    }

    @Test
    public void testRewriteMixup() {
        AppToken token = getCommon().createAppToken();

        JSONObject config = getCommon().getSysoutChannelConfigJson();
        Channel channel = new Channel();
        channel.setName("sms");
        channel.setRewriteRecipientName("{{subject}}");
        channel.setRewriteRecipientAddress("smsgateway@some.where");
        channel.setRewriteSubject("This is a message with content [{{content}}]");
        channel.setRewriteContent("Should go to {{recipientName}}");
        channel.setConfigJson(config.toString());
        getCommon().getChannelDao().store(channel);

        Template template = getCommon().createTemplate();
        getCommon().createChannelTemplate(template, channel,
                "Welcome to Tubewarder, {{firstname}}!",
                "Hi {{firstname}} {{lastname}}, here is your activation code: {{code}}");

        Map<String, Object> model = createMap(
                "firstname", "John",
                "lastname", "Doe",
                "code", "1234567890");
        validateSendResponse(token.getExposableId(), template.getName(), channel.getName(), model, "John", "+490000",
                "error", equalTo(ErrorCode.OK),
                "recipient.name", equalTo("Welcome to Tubewarder, John!"),
                "recipient.address", equalTo("smsgateway@some.where"),
                "subject", equalTo("This is a message with content [Hi John Doe, here is your activation code: 1234567890]"),
                "content", equalTo("Should go to John"));
    }

    private JSONObject validateSendResponse(String token,
                                            String templateName,
                                            String channelName,
                                            Map<String, Object> model,
                                            String recipientName,
                                            String recipientAddress,
                                            Object... body) {
        JSONObject payload = getCommon().getSendRequestJsonPayload(
                token,
                templateName,
                channelName,
                model,
                recipientName,
                recipientAddress,
                "",
                "");
        ResponseSpecification response = getResponseSpecificationPost(payload);
        setExpectedBodies(response, body);
        return getPostResponse(response, "send");
    }

    public TestSendServiceCommon getCommon() {
        return common;
    }

    public void setCommon(TestSendServiceCommon common) {
        this.common = common;
    }
}
