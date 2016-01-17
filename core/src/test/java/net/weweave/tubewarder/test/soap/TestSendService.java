package net.weweave.tubewarder.test.soap;

import net.weweave.tubewarder.domain.AppToken;
import net.weweave.tubewarder.domain.Channel;
import net.weweave.tubewarder.domain.Template;
import net.weweave.tubewarder.service.model.ErrorCode;
import net.weweave.tubewarder.test.AbstractServiceTest;
import net.weweave.tubewarder.test.TestSendServiceCommon;
import net.weweave.tubewarder.test.soap.client.*;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.xml.namespace.QName;
import java.net.URL;
import java.util.Map;
import java.util.UUID;

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
                "Welcome to Tubewarder, ${firstname}!",
                "Hi ${firstname} ${lastname}, here is your activation code: ${code}");

        Map<String, Object> model = createMap(
                "firstname", "John",
                "lastname", "Doe",
                "code", "1234567890");
        SendModel payload = getSendRequestPayload(token.getExposableId(), template.getName(), channel.getName(), model, "John", "+490000");
        SendService port = getService().getWs_002fSend_002fPort();
        SendServiceResponse response = port.send(payload);

        Assert.assertEquals(ErrorCode.OK, (long)response.getError());
        Assert.assertEquals("Welcome to Tubewarder, John!", response.getSubject());
        Assert.assertEquals("Hi John Doe, here is your activation code: 1234567890", response.getContent());
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
        SendModel payload = getSendRequestPayload(token.getExposableId(), template.getName(), channel.getName(), model, "John", "+490000");
        SendService port = getService().getWs_002fSend_002fPort();
        SendServiceResponse response = port.send(payload);

        Assert.assertEquals(ErrorCode.MISSING_MODEL_PARAMETER, (long)response.getError());
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
        SendModel payload = getSendRequestPayload(token.getExposableId(), template.getName(), channel.getName(), model, "John", "+490000");
        SendService port = getService().getWs_002fSend_002fPort();
        SendServiceResponse response = port.send(payload);

        Assert.assertEquals(ErrorCode.TEMPLATE_CORRUPT, (long)response.getError());
    }

    @Test
    public void testInvalidTemplate() {
        AppToken token = getCommon().createAppToken();
        Channel channel = getCommon().createChannel();
        Template template = getCommon().createTemplate();
        getCommon().createChannelTemplate(template, channel, "", "");

        SendModel payload = getSendRequestPayload(token.getExposableId(), "Unknown", channel.getName(), createMap(), "John", "+490000");
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

        SendModel payload = getSendRequestPayload(token.getExposableId(), template.getName(), "Unknown", createMap(), "John", "+490000");
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

        SendModel payload = getSendRequestPayload(token.getExposableId(), template.getName(), channel2.getName(), createMap(), "John", "+490000");
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

        SendModel payload = getSendRequestPayload(UUID.randomUUID().toString(), template.getName(), channel.getName(), createMap(), "John", "+490000");
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

        SendModel payload = getSendRequestPayload(token.getExposableId(), template.getName(), channel.getName(), createMap(), "John", "");
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

        SendModel payload = getSendRequestPayload("", template.getName(), channel.getName(), createMap(), "John", "+490000");
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

        SendModel payload = getSendRequestPayload(token.getExposableId(), template.getName(), channel.getName(), createMap(), "", "+490000");
        SendService port = getService().getWs_002fSend_002fPort();
        SendServiceResponse response = port.send(payload);

        Assert.assertEquals(ErrorCode.OK, (long)response.getError());
    }

    private SendModel getSendRequestPayload(String token, String templateName, String channelName, Map<String, Object> model, String recipientName, String recipientAddress) {
        AddressModel recipient = new AddressModel();
        recipient.setName(recipientName);
        recipient.setAddress(recipientAddress);

        SendModel payload = new SendModel();
        payload.setToken(token);
        payload.setEcho(true);
        payload.setTemplate(templateName);
        payload.setChannel(channelName);
        payload.setRecipient(recipient);

        for (String key : model.keySet()) {
            KeyValueModel entry = new KeyValueModel();
            entry.setKey(key);
            entry.setValue(model.get(key));
            payload.getModel().add(entry);
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
