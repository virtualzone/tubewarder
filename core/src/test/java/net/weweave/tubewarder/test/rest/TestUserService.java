package net.weweave.tubewarder.test.rest;

import com.jayway.restassured.specification.ResponseSpecification;
import net.weweave.tubewarder.service.model.ErrorCode;
import org.jboss.arquillian.junit.Arquillian;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;
import java.util.UUID;

import static org.hamcrest.Matchers.*;

@RunWith(Arquillian.class)
public class TestUserService extends AbstractRestTest {
    @Test
    public void testCreateSuccess() {
        createAdminUser();
        String token = authAdminGetToken();
        JSONObject response = validateSetUserResponse(token, null, getParamsForDefaultUser(),
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
        String id = response.getString("id");
        validateGetSingleUserResponse(token, id, "user1", "User 1");
    }

    @Test
    public void testUpdateSuccess() {
        createAdminUser();
        String token = authAdminGetToken();

        Map<String, Object> params;
        JSONObject response;
        String id;

        params = getParamsForDefaultUser();
        response = validateSetUserResponse(token, null, params,
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
        id = response.getString("id");
        validateGetSingleUserResponse(token, id, "user1", "User 1");

        params = createMap(
                "username", "user1.1",
                "displayName", "User 1.1",
                "password", "123ABCabc",
                "enabled", true,
                "allowAppTokens", false,
                "allowChannels", false,
                "allowTemplates", false,
                "allowSystemConfig", false);
        validateSetUserResponse(token, id, params,
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()),
                "id", equalTo(id));
        validateGetSingleUserResponse(token, id, "user1.1", "User 1.1");
    }

    @Test
    public void testCreateDuplicateUsername() {
        createAdminUser();
        String token = authAdminGetToken();

        Map<String, Object> params;

        params = getParamsForDefaultUser();
        validateSetUserResponse(token, null, params,
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));

        params.put("displayName", "User 2");
        validateSetUserResponse(token, null, params,
                "error", equalTo(ErrorCode.INVALID_INPUT_PARAMETERS),
                "id", isEmptyOrNullString());
    }

    @Test
    public void testUpdateDuplicateUsername() {
        createAdminUser();
        String token = authAdminGetToken();

        Map<String, Object> params;
        JSONObject response;

        // Create user1
        params = getParamsForDefaultUser();
        response = validateSetUserResponse(token, null, params,
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
        String id = response.getString("id");

        // Create user2
        params.put("username", "user2");
        params.put("displayName", "User 2");
        validateSetUserResponse(token, null, params,
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));

        // Try to rename user1 to user2
        params.put("username", "user2");
        params.put("displayName", "User 1");
        validateSetUserResponse(token, id, params,
                "error", equalTo(ErrorCode.INVALID_INPUT_PARAMETERS),
                "id", isEmptyOrNullString());
    }

    @Test
    public void testCreateInvalidToken() {
        createAdminUser();
        authAdminGetToken();
        validateSetUserResponse(UUID.randomUUID().toString(), null, getParamsForDefaultUser(),
                "error", equalTo(ErrorCode.AUTH_REQUIRED),
                "id", isEmptyOrNullString());
    }

    @Test
    public void testCreateInsufficientRights() {
        createUserWithNoRights("dummy", "dummy");
        String token = authGetToken("dummy", "dummy");
        validateSetUserResponse(token, null, getParamsForDefaultUser(),
                "error", equalTo(ErrorCode.PERMISSION_DENIED),
                "id", isEmptyOrNullString());
    }

    @Test
    public void testUpdateNonExistingObject() {
        createAdminUser();
        String token = authAdminGetToken();
        validateSetUserResponse(token, UUID.randomUUID().toString(), getParamsForDefaultUser(),
                "error", equalTo(ErrorCode.OBJECT_LOOKUP_ERROR),
                "id", isEmptyOrNullString());
    }

    @Test
    public void testDeleteSuccess() {
        createAdminUser();
        String token = authAdminGetToken();

        JSONObject response = validateSetUserResponse(token, null, getParamsForDefaultUser(),
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
        String id = response.getString("id");

        validateDeleteUserResponse(token, id,
                "error", equalTo(ErrorCode.OK));
        validateGetUserResponse(token, id,
                "error", equalTo(ErrorCode.OBJECT_LOOKUP_ERROR),
                "users.size()", is(0));
    }

    @Test
    public void testDeleteNonExistingObject() {
        createAdminUser();
        String token = authAdminGetToken();
        validateDeleteUserResponse(token, UUID.randomUUID().toString(),
                "error", equalTo(ErrorCode.OBJECT_LOOKUP_ERROR));
    }

    @Test
    public void testDeleteInvalidToken() {
        createAdminUser();
        String token = authAdminGetToken();
        JSONObject response = validateSetUserResponse(token, null, getParamsForDefaultUser(),
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
        String id = response.getString("id");
        validateDeleteUserResponse(UUID.randomUUID().toString(), id,
                "error", equalTo(ErrorCode.AUTH_REQUIRED));
    }

    @Test
    public void testDeleteInsufficientRights() {
        createAdminUser();
        String token = authAdminGetToken();
        JSONObject response = validateSetUserResponse(token, null, getParamsForDefaultUser(),
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
        String id = response.getString("id");

        createUserWithNoRights("dummy", "dummy");
        token = authGetToken("dummy", "dummy");
        validateDeleteUserResponse(token, id,
                "error", equalTo(ErrorCode.PERMISSION_DENIED));
    }

    @Test
    public void testGetSingleResultList() {
        createAdminUser();
        String token = authAdminGetToken();
        validateGetUserResponse(token, null,
                "error", equalTo(ErrorCode.OK),
                "users.size()", is(1),
                "users[0].username", equalTo("admin"));
    }

    @Test
    public void testGetInvalidId() {
        createAdminUser();
        String token = authAdminGetToken();
        validateGetUserResponse(token, UUID.randomUUID().toString(),
                "error", equalTo(ErrorCode.OBJECT_LOOKUP_ERROR),
                "users.size()", is(0));
    }

    @Test
    public void testGetInsufficientRights() {
        createUserWithNoRights("dummy", "dummy");
        String token = authGetToken("dummy", "dummy");
        validateGetUserResponse(token, null,
                "error", equalTo(ErrorCode.PERMISSION_DENIED),
                "users.size()", is(0));
    }

    @Test
    public void testGetThreeItems() {
        createAdminUser();
        String token = authAdminGetToken();

        Map<String, Object> params = getParamsForDefaultUser();
        JSONObject response = validateSetUserResponse(token, null, params,
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
        String id1 = response.getString("id");

        params.put("username", "user2");
        params.put("displayName", "User 2");
        response = validateSetUserResponse(token, null, params,
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
        String id2 = response.getString("id");

        // Number of users is 3, because we've create an admin user first
        validateGetUserResponse(token, null,
                "error", equalTo(ErrorCode.OK),
                "users.size()", is(3),
                "users[1].id", equalTo(id1),
                "users[2].id", equalTo(id2),
                "users[0].username", equalTo("admin"),
                "users[1].username", equalTo("user1"),
                "users[2].username", equalTo("user2"),
                "users[1].displayName", equalTo("User 1"),
                "users[2].displayName", equalTo("User 2"));
    }

    private Map<String, Object> getParamsForDefaultUser() {
        return createMap(
                "username", "user1",
                "displayName", "User 1",
                "password", "abcABC12",
                "enabled", true,
                "allowAppTokens", true,
                "allowChannels", true,
                "allowTemplates", true,
                "allowSystemConfig", true);
    }

    private JSONObject validateSetUserResponse(String token, String id, Map<String, Object> params, Object... body) {
        JSONObject payload = getSetRequestPayload(token, id, params);
        ResponseSpecification response = getResponseSpecificationPost(payload);
        setExpectedBodies(response, body);
        return getPostResponse(response, "user/set");
    }

    private JSONObject validateDeleteUserResponse(String token, String id, Object... body) {
        JSONObject payload = getDeleteRequestPayload(token, id);
        ResponseSpecification response = getResponseSpecificationPost(payload);
        setExpectedBodies(response, body);
        return getPostResponse(response, "user/delete");
    }

    private JSONObject validateGetUserResponse(String token, String id, Object... body) {
        ResponseSpecification response = getResponseSpecificationGet("token", token, "id", id);
        setExpectedBodies(response, body);
        return getGetResponse(response, "user/get");
    }

    private JSONObject getSetRequestPayload(String token, String id, Map<String, Object> params) {
        JSONObject object = new JSONObject();
        object.put("id", id);
        for (String key : params.keySet()) {
            object.put(key, params.get(key));
        }
        return super.getSetRequestPayload(token, object);
    }

    private JSONObject validateGetSingleUserResponse(String token, String expectedId, String expectedUsername, String expectedDisplayName) {
        return validateGetUserResponse(token, expectedId,
                "error", equalTo(ErrorCode.OK),
                "users.size()", is(1),
                "users[0].id", equalTo(expectedId),
                "users[0].username", equalTo(expectedUsername),
                "users[0].displayName", equalTo(expectedDisplayName));
    }
}
