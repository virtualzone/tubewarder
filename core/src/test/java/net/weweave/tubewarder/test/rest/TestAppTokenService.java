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
public class TestAppTokenService extends AbstractRestTest {
    @Test
    public void testCreateAppTokenSuccess() {
        createAdminUser();
        String token = authAdminGetToken();

        JSONObject response = validateSetTokenResponse(token, null, "App Token 1",
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));

        String id = response.getString("id");

        validateGetSingleTokenResponse(token, id, "App Token 1");
    }

    @Test
    public void testUpdateAppTokenSuccess() {
        createAdminUser();
        String token = authAdminGetToken();

        JSONObject response = validateSetTokenResponse(token, null, "App Token 1",
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));

        String id = response.getString("id");

        validateGetSingleTokenResponse(token, id, "App Token 1");

        validateSetTokenResponse(token, id, "Renamed App Token",
                "error", equalTo(ErrorCode.OK),
                "id", equalTo(id));

        validateGetSingleTokenResponse(token, id, "Renamed App Token");
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

    @Test
    public void testDeleteSuccess() {
        createAdminUser();
        String token = authAdminGetToken();

        JSONObject response = validateSetTokenResponse(token, null, "App Token 1",
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));

        String id = response.getString("id");

        validateDeleteTokenResponse(token, id,
                "error", equalTo(ErrorCode.OK));
    }

    @Test
    public void testDeleteNonExistingObject() {
        createAdminUser();
        String token = authAdminGetToken();

        validateDeleteTokenResponse(token, UUID.randomUUID().toString(),
                "error", equalTo(ErrorCode.OBJECT_LOOKUP_ERROR));
    }

    @Test
    public void testDeleteInvalidToken() {
        createAdminUser();
        String token = authAdminGetToken();

        JSONObject response = validateSetTokenResponse(token, null, "App Token 1",
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));

        String id = response.getString("id");

        validateDeleteTokenResponse(UUID.randomUUID().toString(), id,
                "error", equalTo(ErrorCode.AUTH_REQUIRED));
    }

    @Test
    public void testDeleteInsufficientRights() {
        createAdminUser();
        String token = authAdminGetToken();

        JSONObject response = validateSetTokenResponse(token, null, "App Token 1",
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));

        String id = response.getString("id");

        createUserWithNoRights("dummy", "dummy");
        token = authGetToken("dummy", "dummy");

        validateDeleteTokenResponse(token, id,
                "error", equalTo(ErrorCode.PERMISSION_DENIED));
    }

    @Test
    public void testGetEmptyList() {
        createAdminUser();
        String token = authAdminGetToken();
        validateGetTokenResponse(token, null,
                "error", equalTo(ErrorCode.OK),
                "tokens.size()", is(0));
    }

    @Test
    public void testGetInvalidId() {
        createAdminUser();
        String token = authAdminGetToken();
        validateGetTokenResponse(token, UUID.randomUUID().toString(),
                "error", equalTo(ErrorCode.OBJECT_LOOKUP_ERROR),
                "tokens.size()", is(0));
    }

    @Test
    public void testGetInsufficientRights() {
        createUserWithNoRights("dummy", "dummy");
        String token = authGetToken("dummy", "dummy");
        validateGetTokenResponse(token, null,
                "error", equalTo(ErrorCode.PERMISSION_DENIED),
                "tokens.size()", is(0));
    }

    @Test
    public void testGetTwoItems() {
        createAdminUser();
        String token = authAdminGetToken();

        JSONObject response = validateSetTokenResponse(token, null, "App Token 1");
        String id1 = response.getString("id");
        response = validateSetTokenResponse(token, null, "App Token 2");
        String id2 = response.getString("id");

        validateGetTokenResponse(token, null,
                "error", equalTo(ErrorCode.OK),
                "tokens.size()", is(2),
                "tokens[0].id", equalTo(id1),
                "tokens[1].id", equalTo(id2),
                "tokens[0].name", equalTo("App Token 1"),
                "tokens[1].name", equalTo("App Token 2"));
    }

    private JSONObject validateSetTokenResponse(String token, String id, String name, Object... body) {
        JSONObject payload = getSetRequestPayload(token, id, name);
        ResponseSpecification response = getResponseSpecificationPost(payload);
        setExpectedBodies(response, body);
        return getPostResponse(response, "apptoken/set");
    }

    private JSONObject validateDeleteTokenResponse(String token, String id, Object... body) {
        JSONObject payload = getDeleteRequestPayload(token, id);
        ResponseSpecification response = getResponseSpecificationPost(payload);
        setExpectedBodies(response, body);
        return getPostResponse(response, "apptoken/delete");
    }

    private JSONObject validateGetTokenResponse(String token, String id, Object... body) {
        ResponseSpecification response = getResponseSpecificationGet("token", token, "id", id);
        setExpectedBodies(response, body);
        return getGetResponse(response, "apptoken/get");
    }

    private JSONObject getSetRequestPayload(String token, String id, String name) {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("name", name);
        return super.getSetRequestPayload(token, object);
    }

    private JSONObject validateGetSingleTokenResponse(String token, String expectedId, String expectedName) {
        return validateGetTokenResponse(token, expectedId,
                "error", equalTo(ErrorCode.OK),
                "tokens.size()", is(1),
                "tokens[0].id", equalTo(expectedId),
                "tokens[0].name", equalTo(expectedName));
    }
}
