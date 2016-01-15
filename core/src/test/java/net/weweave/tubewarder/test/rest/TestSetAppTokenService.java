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
public class TestSetAppTokenService extends AbstractRestTest {
    @Test
    public void testCreateAppTokenSuccess() {
        createAdminUser();
        String token = authAdminGetToken();

        JSONObject response = validateSetTokenResponse(token, null, "App Token 1",
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));

        String id = response.getString("id");

        validateGetTokenResponse(token, id, "App Token 1");
    }

    @Test
    public void testUpdateAppTokenSuccess() {
        createAdminUser();
        String token = authAdminGetToken();

        JSONObject response = validateSetTokenResponse(token, null, "App Token 1",
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));

        String id = response.getString("id");

        validateGetTokenResponse(token, id, "App Token 1");

        validateSetTokenResponse(token, id, "Renamed App Token",
                "error", equalTo(ErrorCode.OK),
                "id", equalTo(id));

        validateGetTokenResponse(token, id, "Renamed App Token");
    }

    @Test
    public void testCreateAppTokenInvalidToken() {
        createAdminUser();
        authAdminGetToken();

        validateSetTokenResponse(UUID.randomUUID().toString(), null, "App Token 1",
                "error", equalTo(ErrorCode.AUTH_REQUIRED),
                "id", isEmptyOrNullString());
    }

    @Test
    public void testCreateAppTokenEmptyName() {
        createAdminUser();
        String token = authAdminGetToken();

        validateSetTokenResponse(token, null, "",
                "error", equalTo(ErrorCode.INVALID_INPUT_PARAMETERS),
                "id", isEmptyOrNullString());
    }

    @Test
    public void testInsufficientRights() {
        createUserWithNoRights("dummy", "dummy");
        String token = authGetToken("dummy", "dummy");

        validateSetTokenResponse(token, null, "App Token 1",
                "error", equalTo(ErrorCode.PERMISSION_DENIED),
                "id", isEmptyOrNullString());
    }

    @Test
    public void testUpdateNonExistingObject() {
        createAdminUser();
        String token = authAdminGetToken();

        validateSetTokenResponse(token, UUID.randomUUID().toString(), "App Token 1",
                "error", equalTo(ErrorCode.OBJECT_LOOKUP_ERROR),
                "id", isEmptyOrNullString());
    }

    private JSONObject validateSetTokenResponse(String token, String id, String name, Object... body) {
        JSONObject payload = getSetRequestPayload(token, id, name);
        ResponseSpecification response = getResponseSpecificationPost(payload);
        setExpectedBodies(response, body);
        return getPostResponse(response, "apptoken/set");
    }

    private JSONObject getSetRequestPayload(String token, String id, String name) {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("name", name);

        JSONObject payload = new JSONObject();
        payload.put("token", token);
        payload.put("object", object);
        return payload;
    }

    private JSONObject validateGetTokenResponse(String token, String expectedId, String expectedName) {
        ResponseSpecification response = getResponseSpecificationGet("token", token, "id", expectedId);
        setExpectedBodies(response,
                "error", equalTo(ErrorCode.OK),
                "tokens.size()", is(1),
                "tokens[0].id", equalTo(expectedId),
                "tokens[0].name", equalTo(expectedName));
        return getGetResponse(response, "apptoken/get");
    }
}
