package net.weweave.tubewarder.test.rest;

import com.jayway.restassured.specification.ResponseSpecification;
import net.weweave.tubewarder.service.model.ErrorCode;
import org.jboss.arquillian.junit.Arquillian;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.*;

@RunWith(Arquillian.class)
public class TestAuthService extends AbstractRestTest {
    @Test
    public void testValidAuth() {
        createAdminUser();
        validateAuthResponse("admin", "admin",
                "error", equalTo(ErrorCode.OK),
                "token", not(isEmptyOrNullString()));
    }

    @Test
    public void testValidSecondAuth() {
        createUserWithNoRights("user1", "123456");
        createUserWithNoRights("user2", "098765");

        JSONObject r1 = validateAuthResponse("user1", "123456",
                "error", equalTo(ErrorCode.OK),
                "token", not(isEmptyOrNullString()),
                "user.username", equalTo("user1"));

        JSONObject r2 = validateAuthResponse("user2", "098765",
                "error", equalTo(ErrorCode.OK),
                "token", not(isEmptyOrNullString()),
                "user.username", equalTo("user2"));

        Assert.assertNotEquals(r1.getString("token"), r2.getString("token"));
    }

    @Test
    public void testEmptyPayload() {
        createAdminUser();
        validateAuthResponse(null, null,
                "error", equalTo(ErrorCode.INVALID_INPUT_PARAMETERS),
                "token", isEmptyOrNullString());
    }

    @Test
    public void testInvalidPassword() {
        createAdminUser();
        validateAuthResponse("admin", "12345",
                "error", equalTo(ErrorCode.INVALID_INPUT_PARAMETERS),
                "token", isEmptyOrNullString());
    }

    @Test
    public void testEmptyPassword() {
        createAdminUser();
        validateAuthResponse("admin", "",
                "error", equalTo(ErrorCode.INVALID_INPUT_PARAMETERS),
                "token", isEmptyOrNullString());
    }

    private JSONObject validateAuthResponse(String username, String password, Object... body) {
        JSONObject payload = getAuthRequestPayload(username, password);
        ResponseSpecification response = getResponseSpecificationPost(payload);
        setExpectedBodies(response, body);
        return getPostResponse(response, "auth");
    }

    private JSONObject getAuthRequestPayload(String username, String password) {
        JSONObject payload = new JSONObject();
        payload.put("username", username);
        payload.put("password", password);
        return payload;
    }
}
