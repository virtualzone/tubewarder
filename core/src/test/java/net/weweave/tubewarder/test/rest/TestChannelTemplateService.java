package net.weweave.tubewarder.test.rest;

import com.jayway.restassured.specification.ResponseSpecification;
import net.weweave.tubewarder.service.model.ErrorCode;
import org.jboss.arquillian.junit.Arquillian;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

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

    @Test
    public void testCreateDuplicateChannel() {
        createAdminUser();
        String token = authAdminGetToken();

        String templateId = createTemplateGetId(token, "t1");
        String channelId = createChannelGetId(token, "c1");

        JSONObject ctObject = getJsonObjectForChannelTemplate(null, templateId, channelId);
        validateSetChannelTemplateResponse(token, ctObject,
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));

        ctObject = getJsonObjectForChannelTemplate(null, templateId, channelId);
        validateSetChannelTemplateResponse(token, ctObject,
                "error", equalTo(ErrorCode.INVALID_INPUT_PARAMETERS),
                "id", isEmptyOrNullString());
    }

    @Test
    public void testCreateTwoTemplatesSameChannel() {
        createAdminUser();
        String token = authAdminGetToken();

        String templateId1 = createTemplateGetId(token, "t1");
        String templateId2 = createTemplateGetId(token, "t2");
        String channelId = createChannelGetId(token, "c1");

        JSONObject ctObject = getJsonObjectForChannelTemplate(null, templateId1, channelId);
        JSONObject response = validateSetChannelTemplateResponse(token, ctObject,
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
        String id = response.getString("id");
        validateGetSingleChannelTemplateResponse(token, id, templateId1, channelId);

        ctObject = getJsonObjectForChannelTemplate(null, templateId2, channelId);
        response = validateSetChannelTemplateResponse(token, ctObject,
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
        id = response.getString("id");
        validateGetSingleChannelTemplateResponse(token, id, templateId2, channelId);
    }

    @Test
    public void testCreateInsufficientRights() {
        createAdminUser();
        String token = authAdminGetToken();

        String templateId = createTemplateGetId(token, "t1");
        String channelId = createChannelGetId(token, "c1");

        createUserWithNoRights("dummy", "dummy");
        token = authGetToken("dummy", "dummy");

        JSONObject ctObject = getJsonObjectForChannelTemplate(null, templateId, channelId);
        JSONObject response = validateSetChannelTemplateResponse(token, ctObject,
                "error", equalTo(ErrorCode.PERMISSION_DENIED),
                "id", isEmptyOrNullString());
    }

    @Test
    public void testCreateInvalidToken() {
        createAdminUser();
        String token = authAdminGetToken();

        String templateId = createTemplateGetId(token, "t1");
        String channelId = createChannelGetId(token, "c1");

        JSONObject ctObject = getJsonObjectForChannelTemplate(null, templateId, channelId);
        JSONObject response = validateSetChannelTemplateResponse(UUID.randomUUID().toString(), ctObject,
                "error", equalTo(ErrorCode.AUTH_REQUIRED),
                "id", isEmptyOrNullString());
    }

    @Test
    public void testUpdateSuccess() {
        createAdminUser();
        String token = authAdminGetToken();

        String templateId1 = createTemplateGetId(token, "t1");
        String channelId1 = createChannelGetId(token, "c1");
        JSONObject ctObject = getJsonObjectForChannelTemplate(null, templateId1, channelId1);
        JSONObject response = validateSetChannelTemplateResponse(token, ctObject,
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
        String id = response.getString("id");


        String templateId2 = createTemplateGetId(token, "t2");
        String channelId2 = createChannelGetId(token, "c2");
        ctObject = getJsonObjectForChannelTemplate(id, templateId2, channelId2);
        response = validateSetChannelTemplateResponse(token, ctObject,
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));

        validateGetSingleChannelTemplateResponse(token, id, templateId2, channelId2);
    }

    @Test
    public void testUpdateNonExistingObject() {
        createAdminUser();
        String token = authAdminGetToken();

        String templateId = createTemplateGetId(token, "t1");
        String channelId1 = createChannelGetId(token, "c1");
        JSONObject ctObject = getJsonObjectForChannelTemplate(null, templateId, channelId1);
        validateSetChannelTemplateResponse(token, ctObject,
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));

        String channelId2 = createChannelGetId(token, "c2");
        ctObject = getJsonObjectForChannelTemplate(UUID.randomUUID().toString(), templateId, channelId2);
        validateSetChannelTemplateResponse(token, ctObject,
                "error", equalTo(ErrorCode.OBJECT_LOOKUP_ERROR),
                "id", isEmptyOrNullString());
    }

    @Test
    public void testUpdateDuplicateChannel() {
        createAdminUser();
        String token = authAdminGetToken();

        String templateId = createTemplateGetId(token, "t1");
        String channelId1 = createChannelGetId(token, "c1");
        String channelId2 = createChannelGetId(token, "c2");

        // Create channel template for channel 1
        JSONObject ctObject = getJsonObjectForChannelTemplate(null, templateId, channelId1);
        JSONObject response = validateSetChannelTemplateResponse(token, ctObject,
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
        String id = response.getString("id");

        // Create channel template for channel 2
        ctObject = getJsonObjectForChannelTemplate(null, templateId, channelId2);
        validateSetChannelTemplateResponse(token, ctObject,
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));

        // Try to update channel template from channel 1 to channel 2
        ctObject = getJsonObjectForChannelTemplate(id, templateId, channelId2);
        validateSetChannelTemplateResponse(token, ctObject,
                "error", equalTo(ErrorCode.INVALID_INPUT_PARAMETERS),
                "id", isEmptyOrNullString());
    }

    @Test
    public void testDeleteSuccess() {
        createAdminUser();
        String token = authAdminGetToken();

        String templateId = createTemplateGetId(token, "t1");
        String channelId = createChannelGetId(token, "c1");
        JSONObject ctObject = getJsonObjectForChannelTemplate(null, templateId, channelId);
        JSONObject response = validateSetChannelTemplateResponse(token, ctObject,
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
        String id = response.getString("id");

        validateDeleteChannelTemplateResponse(token, id,
                "error", equalTo(ErrorCode.OK));
        validateGetChannelTemplateResponse(token, id, null,
                "error", equalTo(ErrorCode.OBJECT_LOOKUP_ERROR),
                "channelTemplates.size()", is(0));
    }

    @Test
    public void testDeleteNonExistingObject() {
        createAdminUser();
        String token = authAdminGetToken();
        validateDeleteChannelTemplateResponse(token, UUID.randomUUID().toString(),
                "error", equalTo(ErrorCode.OBJECT_LOOKUP_ERROR));
    }

    @Test
    public void testDeleteInvalidToken() {
        createAdminUser();
        String token = authAdminGetToken();

        String templateId = createTemplateGetId(token, "t1");
        String channelId = createChannelGetId(token, "c1");
        JSONObject ctObject = getJsonObjectForChannelTemplate(null, templateId, channelId);
        JSONObject response = validateSetChannelTemplateResponse(token, ctObject,
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
        String id = response.getString("id");

        validateDeleteChannelTemplateResponse(UUID.randomUUID().toString(), id,
                "error", equalTo(ErrorCode.AUTH_REQUIRED));
    }

    @Test
    public void testDeleteInsufficientRights() {
        createAdminUser();
        String token = authAdminGetToken();

        String templateId = createTemplateGetId(token, "t1");
        String channelId = createChannelGetId(token, "c1");
        JSONObject ctObject = getJsonObjectForChannelTemplate(null, templateId, channelId);
        JSONObject response = validateSetChannelTemplateResponse(token, ctObject,
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
        String id = response.getString("id");

        createUserWithNoRights("dummy", "dummy");
        token = authGetToken("dummy", "dummy");
        validateDeleteChannelTemplateResponse(token, id,
                "error", equalTo(ErrorCode.PERMISSION_DENIED));
    }

    @Test
    public void testGetWithoutIdOrTemplateId() {
        createAdminUser();
        String token = authAdminGetToken();
        validateGetChannelTemplateResponse(token, null, null,
                "error", equalTo(ErrorCode.INVALID_INPUT_PARAMETERS),
                "channelTemplates.size()", is(0));
    }

    @Test
    public void testGetEmptyList() {
        createAdminUser();
        String token = authAdminGetToken();

        String templateId = createTemplateGetId(token, "t1");

        validateGetChannelTemplateResponse(token, null, templateId,
                "error", equalTo(ErrorCode.OK),
                "channelTemplates.size()", is(0));
    }

    @Test
    public void testGetNonExistingTemplate() {
        createAdminUser();
        String token = authAdminGetToken();
        validateGetChannelTemplateResponse(token, null, UUID.randomUUID().toString(),
                "error", equalTo(ErrorCode.OBJECT_LOOKUP_ERROR),
                "channelTemplates.size()", is(0));
    }

    @Test
    public void testGetNonExistingObject() {
        createAdminUser();
        String token = authAdminGetToken();
        validateGetChannelTemplateResponse(token, UUID.randomUUID().toString(), null,
                "error", equalTo(ErrorCode.OBJECT_LOOKUP_ERROR),
                "channelTemplates.size()", is(0));
    }

    @Test
    public void testGetTwoItems() {
        createAdminUser();
        String token = authAdminGetToken();

        String templateId = createTemplateGetId(token, "t1");
        String channelId1 = createChannelGetId(token, "c1");
        String channelId2 = createChannelGetId(token, "c2");

        // Create channel template for channel 1
        JSONObject ctObject = getJsonObjectForChannelTemplate(null, templateId, channelId1);
        JSONObject response = validateSetChannelTemplateResponse(token, ctObject,
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
        String id1 = response.getString("id");

        // Create channel template for channel 2
        ctObject = getJsonObjectForChannelTemplate(null, templateId, channelId2);
        response = validateSetChannelTemplateResponse(token, ctObject,
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
        String id2 = response.getString("id");

        validateGetChannelTemplateResponse(token, null, templateId,
                "error", equalTo(ErrorCode.OK),
                "channelTemplates.size()", is(2),
                "channelTemplates[0].id", equalTo(id1),
                "channelTemplates[1].id", equalTo(id2),
                "channelTemplates[0].template.id", equalTo(templateId),
                "channelTemplates[1].template.id", equalTo(templateId),
                "channelTemplates[0].channel.id", equalTo(channelId1),
                "channelTemplates[1].channel.id", equalTo(channelId2));
    }

    private String createChannelGetId(String token, String name) {
        JSONObject object = new JSONObject();
        object.put("name", name);
        object.put("config", getSysoutConfig());
        JSONObject payload = super.getSetRequestPayload(token, object);
        ResponseSpecification response = getResponseSpecificationPost(payload);
        JSONObject json = getPostResponse(response, "channel/set");
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

    private JSONObject getSysoutConfig() {
        JSONObject object = new JSONObject();
        object.put("id", "SYSOUT");
        object.put("prefix", "[");
        object.put("suffix", "]");
        return object;
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
