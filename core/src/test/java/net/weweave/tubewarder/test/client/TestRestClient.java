package net.weweave.tubewarder.test.client;

import net.weweave.tubewarder.client.*;
import net.weweave.tubewarder.domain.AppToken;
import net.weweave.tubewarder.domain.Channel;
import net.weweave.tubewarder.domain.Template;
import net.weweave.tubewarder.service.model.ErrorCode;
import net.weweave.tubewarder.test.AbstractServiceTest;
import net.weweave.tubewarder.test.TestSendServiceCommon;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.net.ConnectException;

@RunWith(Arquillian.class)
public class TestRestClient extends AbstractServiceTest {
    @Inject
    private TestSendServiceCommon common;

    @Test
    public void testSimple() throws Exception {
        AppToken token = getCommon().createAppToken();
        Channel channel = getCommon().createChannel();
        Template template = getCommon().createTemplate();
        getCommon().createChannelTemplate(template, channel,
                "Welcome to Tubewarder!",
                "Hi, here is your activation code: {{code}}");

        TubewarderClient client = new TubewarderRestClient(deploymentUrl.toString());
        SendRequest request = new SendRequest(token.getExposableId());
        request.setChannel(channel.getName());
        request.setTemplate(template.getName());
        request.addModelParam("code", "12345");
        request.setRecipient(new Address("no-reply@weweave.net", "weweave"));
        request.setEcho(true);
        SendResponse response = client.send(request);

        Assert.assertNotNull(response);
        Assert.assertEquals(new Integer(ErrorCode.OK), response.getError());
        Assert.assertNotEquals("", response.getQueueId());
        Assert.assertEquals("Welcome to Tubewarder!", response.getSubject());
        Assert.assertEquals("Hi, here is your activation code: 12345", response.getContent());
    }

    @Test
    public void testInvalidToken() throws Exception {
        TubewarderClient client = new TubewarderRestClient(deploymentUrl.toString());
        SendRequest request = new SendRequest("000000");
        request.setChannel("test");
        request.setTemplate("test");
        request.addModelParam("code", "12345");
        request.setRecipient(new Address("no-reply@weweave.net", "weweave"));
        request.setEcho(true);
        SendResponse response = client.send(request);

        Assert.assertNotNull(response);
        Assert.assertEquals(new Integer(ErrorCode.PERMISSION_DENIED), response.getError());
        Assert.assertEquals("", response.getQueueId());
    }

    @Test(expected = ConnectException.class)
    public void testInvalidServer() throws Exception {
        TubewarderClient client = new TubewarderRestClient("http://localhost:" + (deploymentUrl.getPort()+1));
        SendRequest request = new SendRequest("000000");
        request.setChannel("test");
        request.setTemplate("test");
        request.addModelParam("code", "12345");
        request.setRecipient(new Address("no-reply@weweave.net", "weweave"));
        request.setEcho(true);
        client.send(request);
    }

    @Test(expected = ConnectException.class)
    public void testInvalidPath() throws Exception {
        String url = deploymentUrl.toString();
        if (!url.endsWith("/")) {
            url += "/";
        }
        url += "something/";

        TubewarderClient client = new TubewarderRestClient(url);
        SendRequest request = new SendRequest("000000");
        request.setChannel("test");
        request.setTemplate("test");
        request.addModelParam("code", "12345");
        request.setRecipient(new Address("no-reply@weweave.net", "weweave"));
        request.setEcho(true);
        client.send(request);
    }

    public TestSendServiceCommon getCommon() {
        return common;
    }

    public void setCommon(TestSendServiceCommon common) {
        this.common = common;
    }
}
