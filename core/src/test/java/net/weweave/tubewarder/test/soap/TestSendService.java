package net.weweave.tubewarder.test.soap;

import net.weweave.tubewarder.domain.AppToken;
import net.weweave.tubewarder.domain.Channel;
import net.weweave.tubewarder.domain.Template;
import net.weweave.tubewarder.service.model.ErrorCode;
import net.weweave.tubewarder.test.AbstractServiceTest;
import net.weweave.tubewarder.test.TestSendServiceCommon;
import net.weweave.tubewarder.test.soap.client.*;
import org.jboss.arquillian.junit.Arquillian;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.xml.namespace.QName;
import java.net.URL;
import java.util.*;

@RunWith(Arquillian.class)
public class TestSendService extends AbstractServiceTest {
    @Inject
    private TestSendServiceCommon common;

    private Ws_002fSend service;

    @Before
    public void setUp() throws Exception {
        service = new Ws_002fSend(
                new URL(deploymentUrl, "ws/send?wsdl"),
                new QName("http://soap.service.tubewarder.weweave.net/", "ws/send"));
    }

    @Test
    public void testSendSuccess() {
        AppToken token = getCommon().createAppToken();
        Channel channel = getCommon().createChannel();
        Template template = getCommon().createTemplate();
        getCommon().createChannelTemplate(template, channel,
                "Welcome to Tubewarder, {{firstname}}!",
                "Hi {{firstname}} {{lastname}}, here is your activation code: {{code}}");

        JSONObject model = new JSONObject(createMap(
                "firstname", "John",
                "lastname", "Doe",
                "code", "1234567890"));
        SoapSendModel payload = getSendRequestPayload(token.getExposableId(), template.getName(), channel.getName(), model, "John", "+490000");
        SendService port = getService().getWs_002fSend_002fPort();
        SendServiceResponse response = port.send(payload);

        Assert.assertEquals(ErrorCode.OK, (long)response.getError());
        Assert.assertEquals("Welcome to Tubewarder, John!", response.getSubject());
        Assert.assertEquals("Hi John Doe, here is your activation code: 1234567890", response.getContent());
    }

    @Test
    public void testConditional() {
        AppToken token = getCommon().createAppToken();
        Channel channel = getCommon().createChannel();
        Template template = getCommon().createTemplate();
        getCommon().createChannelTemplate(template, channel,
                "",
                "Hello {{#if male}}Mr. {{name}}{{else}}Mrs. {{name}}{{/if}}!");

        JSONObject model = new JSONObject(createMap(
                "name", "Smith",
                "male", true));
        SoapSendModel payload = getSendRequestPayload(token.getExposableId(), template.getName(), channel.getName(), model, "John", "+490000");
        SendService port = getService().getWs_002fSend_002fPort();
        SendServiceResponse response = port.send(payload);

        Assert.assertEquals(ErrorCode.OK, (long)response.getError());
        Assert.assertEquals("", response.getSubject());
        Assert.assertEquals("Hello Mr. Smith!", response.getContent());
    }

    @Test
    public void testConditionalElse() {
        AppToken token = getCommon().createAppToken();
        Channel channel = getCommon().createChannel();
        Template template = getCommon().createTemplate();
        getCommon().createChannelTemplate(template, channel,
                "",
                "Hello {{#if male}}Mr. {{name}}{{else}}Mrs. {{name}}{{/if}}!");

        JSONObject model = new JSONObject(createMap(
                "name", "Smith",
                "male", false));
        SoapSendModel payload = getSendRequestPayload(token.getExposableId(), template.getName(), channel.getName(), model, "John", "+490000");
        SendService port = getService().getWs_002fSend_002fPort();
        SendServiceResponse response = port.send(payload);

        Assert.assertEquals(ErrorCode.OK, (long)response.getError());
        Assert.assertEquals("", response.getSubject());
        Assert.assertEquals("Hello Mrs. Smith!", response.getContent());
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
        List<Map> people = new java.util.ArrayList<>();
        people.add(p1);
        people.add(p2);
        JSONObject model = new JSONObject(createMap(
                "people", people));
        SoapSendModel payload = getSendRequestPayload(token.getExposableId(), template.getName(), channel.getName(), model, "John", "+490000");
        SendService port = getService().getWs_002fSend_002fPort();
        SendServiceResponse response = port.send(payload);

        Assert.assertEquals(ErrorCode.OK, (long)response.getError());
        Assert.assertEquals("", response.getSubject());
        Assert.assertEquals("Hello -> John Doe -> Max Miller!", response.getContent());
    }

    @Test
    public void testMissingModelParameter() {
        AppToken token = getCommon().createAppToken();
        Channel channel = getCommon().createChannel();
        Template template = getCommon().createTemplate();
        getCommon().createChannelTemplate(template, channel,
                "{{firstname}}",
                "{{firstname}} {{lastname}}");

        JSONObject model = new JSONObject(createMap(
                "firstname", "John"));
        SoapSendModel payload = getSendRequestPayload(token.getExposableId(), template.getName(), channel.getName(), model, "John", "+490000");
        SendService port = getService().getWs_002fSend_002fPort();
        SendServiceResponse response = port.send(payload);

        Assert.assertEquals(ErrorCode.OK, (long)response.getError());
        Assert.assertEquals("John", response.getSubject());
        Assert.assertEquals("John ", response.getContent());
    }

    @Test
    public void testInvalidTemplate() {
        AppToken token = getCommon().createAppToken();
        Channel channel = getCommon().createChannel();
        Template template = getCommon().createTemplate();
        getCommon().createChannelTemplate(template, channel, "", "");

        SoapSendModel payload = getSendRequestPayload(token.getExposableId(), "Unknown", channel.getName(), new JSONObject(createMap()), "John", "+490000");
        SendService port = getService().getWs_002fSend_002fPort();
        SendServiceResponse response = port.send(payload);

        Assert.assertEquals(ErrorCode.OBJECT_LOOKUP_ERROR, (long)response.getError());
    }

    @Test
    public void testInvalidChannel() {
        AppToken token = getCommon().createAppToken();
        Channel channel = getCommon().createChannel();
        Template template = getCommon().createTemplate();
        getCommon().createChannelTemplate(template, channel, "", "");

        SoapSendModel payload = getSendRequestPayload(token.getExposableId(), template.getName(), "Unknown", new JSONObject(createMap()), "John", "+490000");
        SendService port = getService().getWs_002fSend_002fPort();
        SendServiceResponse response = port.send(payload);

        Assert.assertEquals(ErrorCode.OBJECT_LOOKUP_ERROR, (long)response.getError());
    }

    @Test
    public void testUnassignedChannel() {
        AppToken token = getCommon().createAppToken();
        Channel channel = getCommon().createChannel();
        Channel channel2 = getCommon().createChannel("email");
        Template template = getCommon().createTemplate();
        getCommon().createChannelTemplate(template, channel, "", "");

        SoapSendModel payload = getSendRequestPayload(token.getExposableId(), template.getName(), channel2.getName(), new JSONObject(createMap()), "John", "+490000");
        SendService port = getService().getWs_002fSend_002fPort();
        SendServiceResponse response = port.send(payload);

        Assert.assertEquals(ErrorCode.OBJECT_LOOKUP_ERROR, (long)response.getError());
    }

    @Test
    public void testInvalidToken() {
        getCommon().createAppToken();
        Channel channel = getCommon().createChannel();
        Template template = getCommon().createTemplate();
        getCommon().createChannelTemplate(template, channel, "", "");

        SoapSendModel payload = getSendRequestPayload(UUID.randomUUID().toString(), template.getName(), channel.getName(), new JSONObject(createMap()), "John", "+490000");
        SendService port = getService().getWs_002fSend_002fPort();
        SendServiceResponse response = port.send(payload);

        Assert.assertEquals(ErrorCode.PERMISSION_DENIED, (long)response.getError());
    }

    @Test
    public void testMissingRecipientAddress() {
        AppToken token = getCommon().createAppToken();
        Channel channel = getCommon().createChannel();
        Template template = getCommon().createTemplate();
        getCommon().createChannelTemplate(template, channel, "", "");

        SoapSendModel payload = getSendRequestPayload(token.getExposableId(), template.getName(), channel.getName(), new JSONObject(createMap()), "John", "");
        SendService port = getService().getWs_002fSend_002fPort();
        SendServiceResponse response = port.send(payload);

        Assert.assertEquals(ErrorCode.INVALID_INPUT_PARAMETERS, (long)response.getError());
    }

    @Test
    public void testEmptyToken() {
        getCommon().createAppToken();
        Channel channel = getCommon().createChannel();
        Template template = getCommon().createTemplate();
        getCommon().createChannelTemplate(template, channel, "", "");

        SoapSendModel payload = getSendRequestPayload("", template.getName(), channel.getName(), new JSONObject(createMap()), "John", "+490000");
        SendService port = getService().getWs_002fSend_002fPort();
        SendServiceResponse response = port.send(payload);

        Assert.assertEquals(ErrorCode.INVALID_INPUT_PARAMETERS, (long)response.getError());
    }

    @Test
    public void testEmptyRecipientName() {
        AppToken token = getCommon().createAppToken();
        Channel channel = getCommon().createChannel();
        Template template = getCommon().createTemplate();
        getCommon().createChannelTemplate(template, channel, "", "");

        SoapSendModel payload = getSendRequestPayload(token.getExposableId(), template.getName(), channel.getName(), new JSONObject(createMap()), "", "+490000");
        SendService port = getService().getWs_002fSend_002fPort();
        SendServiceResponse response = port.send(payload);

        Assert.assertEquals(ErrorCode.OK, (long)response.getError());
    }

    private SoapSendModel getSendRequestPayload(String token, String templateName, String channelName, JSONObject modelJson, String recipientName, String recipientAddress) {
        AddressModel recipient = new AddressModel();
        recipient.setName(recipientName);
        recipient.setAddress(recipientAddress);

        SoapSendModel payload = new SoapSendModel();
        payload.setToken(token);
        payload.setEcho(true);
        payload.setTemplate(templateName);
        payload.setChannel(channelName);
        payload.setRecipient(recipient);

        if (modelJson != null) {
            payload.setModelJson(modelJson.toString());
        }

        return payload;
    }

    public Ws_002fSend getService() {
        return service;
    }

    public void setService(Ws_002fSend service) {
        this.service = service;
    }

    public TestSendServiceCommon getCommon() {
        return common;
    }

    public void setCommon(TestSendServiceCommon common) {
        this.common = common;
    }
}
