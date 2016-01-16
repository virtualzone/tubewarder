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
public class TestChannelService extends AbstractRestTest {
    @Test
    public void testCreateChannelSuccess() {
        createAdminUser();
        String token = authAdminGetToken();
        String configId = createSysoutConfig(token);
        JSONObject response = validateSetChannelResponse(token, null, "Channel 1", configId,
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
        String id = response.getString("id");
        validateGetSingleChannelResponse(token, id, "Channel 1");
    }

    @Test
    public void testCreateChannelDuplicateName() {
        createAdminUser();
        String token = authAdminGetToken();
        String configId = createSysoutConfig(token);
        validateSetChannelResponse(token, null, "Channel 1", configId,
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
        validateSetChannelResponse(token, null, "Channel 1", configId,
                "error", equalTo(ErrorCode.INVALID_INPUT_PARAMETERS),
                "id", isEmptyOrNullString());
    }

    @Test
    public void testCreateChannelDifferentName() {
        createAdminUser();
        String token = authAdminGetToken();
        String configId1 = createSysoutConfig(token);
        validateSetChannelResponse(token, null, "Channel 1", configId1,
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
        String configId2 = createSysoutConfig(token);
        validateSetChannelResponse(token, null, "Channel 2", configId2,
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
    }

    @Test
    public void testUpdateChannelSuccess() {
        createAdminUser();
        String token = authAdminGetToken();
        String configId = createSysoutConfig(token);
        JSONObject response = validateSetChannelResponse(token, null, "Channel 1", configId,
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
        String id = response.getString("id");
        validateGetSingleChannelResponse(token, id, "Channel 1");
        validateSetChannelResponse(token, id, "Renamed Channel 1", configId,
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
        validateGetSingleChannelResponse(token, id, "Renamed Channel 1");
    }

    @Test
    public void testUpdateNonExistingObject() {
        createAdminUser();
        String token = authAdminGetToken();
        String configId = createSysoutConfig(token);
        validateSetChannelResponse(token, UUID.randomUUID().toString(), "Channel 1", configId,
                "error", equalTo(ErrorCode.OBJECT_LOOKUP_ERROR),
                "id", isEmptyOrNullString());
    }

    @Test
    public void testUpdateRenameDuplicateName() {
        createAdminUser();
        String token = authAdminGetToken();

        String configId1 = createSysoutConfig(token);
        JSONObject response = validateSetChannelResponse(token, null, "Channel 1", configId1,
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
        String id = response.getString("id");

        String configId2 = createSysoutConfig(token);
        validateSetChannelResponse(token, null, "Channel 2", configId2,
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
        validateGetSingleChannelResponse(token, id, "Channel 1");

        validateSetChannelResponse(token, id, "Channel 2", configId1,
                "error", equalTo(ErrorCode.INVALID_INPUT_PARAMETERS));
        validateGetSingleChannelResponse(token, id, "Channel 1");
    }

    @Test
    public void testCreateInsufficientRights() {
        createAdminUser();
        String token = authAdminGetToken();
        String configId = createSysoutConfig(token);

        createUserWithNoRights("dummy", "dummy");
        token = authGetToken("dummy", "dummy");
        validateSetChannelResponse(token, null, "Channel 1", configId,
                "error", equalTo(ErrorCode.PERMISSION_DENIED),
                "id", isEmptyOrNullString());
    }

    @Test
    public void testCreateEmptyToken() {
        createAdminUser();
        String token = authAdminGetToken();
        String configId = createSysoutConfig(token);
        validateSetChannelResponse("", null, "Channel 1", configId,
                "error", equalTo(ErrorCode.AUTH_REQUIRED),
                "id", isEmptyOrNullString());
    }

    @Test
    public void testCreateInvalidToken() {
        createAdminUser();
        String token = authAdminGetToken();
        String configId = createSysoutConfig(token);
        validateSetChannelResponse(UUID.randomUUID().toString(), null, "Channel 1", configId,
                "error", equalTo(ErrorCode.AUTH_REQUIRED),
                "id", isEmptyOrNullString());
    }

    @Test
    public void testDeleteSuccess() {
        createAdminUser();
        String token = authAdminGetToken();
        String configId = createSysoutConfig(token);
        JSONObject response = validateSetChannelResponse(token, null, "Channel 1", configId,
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
        String id = response.getString("id");
        validateDeleteChannelResponse(token, id,
                "error", equalTo(ErrorCode.OK));
        validateGetChannelResponse(token, id,
                "error", equalTo(ErrorCode.OBJECT_LOOKUP_ERROR),
                "channels.size()", is(0));
    }

    @Test
    public void testDeleteInsufficientRights() {
        createAdminUser();
        String token = authAdminGetToken();
        String configId = createSysoutConfig(token);
        JSONObject response = validateSetChannelResponse(token, null, "Channel 1", configId,
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
        String id = response.getString("id");

        createUserWithNoRights("dummy", "dummy");
        token = authGetToken("dummy", "dummy");
        validateDeleteChannelResponse(token, id,
                "error", equalTo(ErrorCode.PERMISSION_DENIED));
    }

    @Test
    public void testDeleteInvalidToken() {
        createAdminUser();
        String token = authAdminGetToken();
        String configId = createSysoutConfig(token);
        JSONObject response = validateSetChannelResponse(token, null, "Channel 1", configId,
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
        String id = response.getString("id");

        validateDeleteChannelResponse(UUID.randomUUID().toString(), id,
                "error", equalTo(ErrorCode.AUTH_REQUIRED));
    }

    @Test
    public void testDeleteInvalidId() {
        createAdminUser();
        String token = authAdminGetToken();
        String configId = createSysoutConfig(token);
        JSONObject response = validateSetChannelResponse(token, null, "Channel 1", configId,
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
        String id = response.getString("id");

        validateDeleteChannelResponse(token, UUID.randomUUID().toString(),
                "error", equalTo(ErrorCode.OBJECT_LOOKUP_ERROR));
    }

    @Test
    public void testDeleteEmptyId() {
        createAdminUser();
        String token = authAdminGetToken();
        String configId = createSysoutConfig(token);
        JSONObject response = validateSetChannelResponse(token, null, "Channel 1", configId,
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
        String id = response.getString("id");

        validateDeleteChannelResponse(token, "",
                "error", equalTo(ErrorCode.INVALID_INPUT_PARAMETERS));
    }

    @Test
    public void testGetEmptyList() {
        createAdminUser();
        String token = authAdminGetToken();
        validateGetChannelResponse(token, null,
                "error", equalTo(ErrorCode.OK),
                "channels.size()", is(0));
    }

    @Test
    public void testGetInvalidId() {
        createAdminUser();
        String token = authAdminGetToken();
        validateGetChannelResponse(token, UUID.randomUUID().toString(),
                "error", equalTo(ErrorCode.OBJECT_LOOKUP_ERROR),
                "channels.size()", is(0));
    }

    @Test
    public void testGetInsufficientRights() {
        createUserWithNoRights("dummy", "dummy");
        String token = authGetToken("dummy", "dummy");
        validateGetChannelResponse(token, null,
                "error", equalTo(ErrorCode.PERMISSION_DENIED),
                "channels.size()", is(0));
    }

    @Test
    public void testGetTwoItems() {
        createAdminUser();
        String token = authAdminGetToken();

        String configId1 = createSysoutConfig(token);
        JSONObject response = validateSetChannelResponse(token, null, "Channel 1", configId1,
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
        String id1 = response.getString("id");

        String configId2 = createSysoutConfig(token);
        response = validateSetChannelResponse(token, null, "Channel 2", configId2,
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
        String id2 = response.getString("id");

        validateGetChannelResponse(token, null,
                "error", equalTo(ErrorCode.OK),
                "channels.size()", is(2),
                "channels[0].id", equalTo(id1),
                "channels[1].id", equalTo(id2),
                "channels[0].name", equalTo("Channel 1"),
                "channels[1].name", equalTo("Channel 2"));
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

    private JSONObject validateSetChannelResponse(String token, String id, String name, String configId, Object... body) {
        JSONObject payload = getSetRequestPayload(token, id, name, configId);
        ResponseSpecification response = getResponseSpecificationPost(payload);
        setExpectedBodies(response, body);
        return getPostResponse(response, "channel/set");
    }

    private JSONObject validateDeleteChannelResponse(String token, String id, Object... body) {
        JSONObject payload = getDeleteRequestPayload(token, id);
        ResponseSpecification response = getResponseSpecificationPost(payload);
        setExpectedBodies(response, body);
        return getPostResponse(response, "channel/delete");
    }

    private JSONObject validateGetChannelResponse(String token, String id, Object... body) {
        ResponseSpecification response = getResponseSpecificationGet("token", token, "id", id);
        setExpectedBodies(response, body);
        return getGetResponse(response, "channel/get");
    }

    private JSONObject getSetRequestPayload(String token, String id, String name, String configId) {
        JSONObject config = new JSONObject();
        config.put("id", configId);
        config.put("type", "SYSOUT");
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("name", name);
        object.put("outputHandler", "SYSOUT");
        object.put("config", config);
        return super.getSetRequestPayload(token, object);
    }

    private JSONObject validateGetSingleChannelResponse(String token, String expectedId, String expectedName) {
        return validateGetChannelResponse(token, expectedId,
                "error", equalTo(ErrorCode.OK),
                "channels.size()", is(1),
                "channels[0].id", equalTo(expectedId),
                "channels[0].name", equalTo(expectedName));
    }
}
