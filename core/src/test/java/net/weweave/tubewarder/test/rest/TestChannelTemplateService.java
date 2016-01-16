package net.weweave.tubewarder.test.rest;

import com.jayway.restassured.specification.ResponseSpecification;
import net.weweave.tubewarder.service.model.ErrorCode;
import org.jboss.arquillian.junit.Arquillian;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.*;

@RunWith(Arquillian.class)
public class TestChannelTemplateService extends AbstractRestTest {
    @Test
    public void testCreateSuccess() {
        createAdminUser();
        String token = authAdminGetToken();
        String templateId = createTemplateGetId(token, "t1");
        String channelId = createChannelGetId(token, "c1");
        JSONObject ctObject = getJsonObjectForChannelTemplate(null, templateId, channelId);
        JSONObject response = validateSetChannelTemplateResponse(token, ctObject,
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
        String id = response.getString("id");
        validateGetSingleChannelTemplateResponse(token, id, templateId, channelId);
    }

    private String createChannelGetId(String token, String name) {
        String configId = createSysoutConfig(token);

        JSONObject config = new JSONObject();
        config.put("id", configId);
        config.put("type", "SYSOUT");

        JSONObject object = new JSONObject();
        object.put("name", name);
        object.put("outputHandler", "SYSOUT");
        object.put("config", config);
        JSONObject payload = super.getSetRequestPayload(token, object);
        System.out.println(payload.toString());
        ResponseSpecification response = getResponseSpecificationPost(payload);
        JSONObject json = getPostResponse(response, "channel/set");
        System.out.println(json.toString());
        return json.getString("id");
    }

    private String createTemplateGetId(String token, String name) {
        JSONObject object = new JSONObject();
        object.put("name", name);
        JSONObject payload = super.getSetRequestPayload(token, object);
        ResponseSpecification response = getResponseSpecificationPost(payload);
        JSONObject json = getPostResponse(response, "template/set");
        return json.getString("id");
    }

    private String createSysoutConfig(String token) {
        JSONObject object = new JSONObject();
        object.put("type", "SYSOUT");
        object.put("prefix", "[");
        object.put("suffix", "]");
        JSONObject payload = super.getSetRequestPayload(token, object);
        ResponseSpecification response = getResponseSpecificationPost(payload);
        setExpectedBodies(response,
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
        JSONObject result = getPostResponse(response, "sysoutoutputhandlerconfiguration/set");
        return result.getString("id");
    }

    private JSONObject getJsonObjectForChannelTemplate(String id, String templateId, String channelId) {
        JSONObject channel = new JSONObject();
        channel.put("id", channelId);

        JSONObject template = new JSONObject();
        template.put("id", templateId);

        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("channel", channel);
        object.put("template", template);
        object.put("subject", "This is the subject");
        object.put("content", "This is the content");
        object.put("senderAddress", "noreply@weweave.net");
        object.put("senderName", "weweave");
        return object;
    }

    private JSONObject validateSetChannelTemplateResponse(String token, JSONObject object, Object... body) {
        JSONObject payload = getSetRequestPayload(token, object);
        ResponseSpecification response = getResponseSpecificationPost(payload);
        setExpectedBodies(response, body);
        return getPostResponse(response, "channeltemplate/set");
    }

    private JSONObject validateDeleteChannelTemplateResponse(String token, String id, Object... body) {
        JSONObject payload = getDeleteRequestPayload(token, id);
        ResponseSpecification response = getResponseSpecificationPost(payload);
        setExpectedBodies(response, body);
        return getPostResponse(response, "channeltemplate/delete");
    }

    private JSONObject validateGetChannelTemplateResponse(String token, String id, String templateId, Object... body) {
        ResponseSpecification response = getResponseSpecificationGet("token", token, "id", id, "templateId", templateId);
        setExpectedBodies(response, body);
        return getGetResponse(response, "channeltemplate/get");
    }

    private JSONObject validateGetSingleChannelTemplateResponse(String token, String expectedId, String expectedTemplateId, String expectedChannelId) {
        return validateGetChannelTemplateResponse(token, expectedId, null,
                "error", equalTo(ErrorCode.OK),
                "channelTemplates.size()", is(1),
                "channelTemplates[0].id", equalTo(expectedId),
                "channelTemplates[0].channel.id", equalTo(expectedChannelId),
                "channelTemplates[0].template.id", equalTo(expectedTemplateId));
    }
}
