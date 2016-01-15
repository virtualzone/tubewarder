package net.weweave.tubewarder.test.rest;

import com.jayway.restassured.specification.ResponseSpecification;
import net.weweave.tubewarder.service.model.ErrorCode;
import org.jboss.arquillian.junit.Arquillian;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

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
        validateGetChannelResponse(token, id, "Channel 1");
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
        validateGetChannelResponse(token, id, "Channel 1");
        validateSetChannelResponse(token, id, "Renamed Channel 1", configId,
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
        validateGetChannelResponse(token, id, "Renamed Channel 1");
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
        validateGetChannelResponse(token, id, "Channel 1");

        validateSetChannelResponse(token, id, "Channel 2", configId1,
                "error", equalTo(ErrorCode.INVALID_INPUT_PARAMETERS));
        validateGetChannelResponse(token, id, "Channel 1");
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

    private JSONObject validateGetChannelResponse(String token, String expectedId, String expectedName) {
        ResponseSpecification response = getResponseSpecificationGet("token", token, "id", expectedId);
        setExpectedBodies(response,
                "error", equalTo(ErrorCode.OK),
                "channels.size()", is(1),
                "channels[0].id", equalTo(expectedId),
                "channels[0].name", equalTo(expectedName));
        return getGetResponse(response, "channel/get");
    }
}
