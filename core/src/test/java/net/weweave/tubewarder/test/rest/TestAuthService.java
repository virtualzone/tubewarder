package net.weweave.tubewarder.test.rest;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.specification.ResponseSpecification;
import net.weweave.tubewarder.service.model.ErrorCode;
import net.weweave.tubewarder.util.ConfigManager;
import org.apache.http.HttpStatus;
import org.jboss.arquillian.junit.Arquillian;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

import static com.jayway.restassured.RestAssured.given;
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

    @Test
    public void testLogoutSuccess() {
        createAdminUser();
        JSONObject response = validateAuthResponse("admin", "admin",
                "error", equalTo(ErrorCode.OK),
                "token", not(isEmptyOrNullString()));
        String token = response.getString("token");
        validateLogoutResponse(token,
                "error", equalTo(ErrorCode.OK));
        validatePingResponse(token,
                "error", equalTo(ErrorCode.INVALID_INPUT_PARAMETERS));
    }

    @Test
    public void testLogoutInvalidToken() {
        createAdminUser();
        JSONObject response = validateAuthResponse("admin", "admin",
                "error", equalTo(ErrorCode.OK),
                "token", not(isEmptyOrNullString()));
        String token = response.getString("token");
        validateLogoutResponse(UUID.randomUUID().toString(),
                "error", equalTo(ErrorCode.INVALID_INPUT_PARAMETERS));
        validatePingResponse(token,
                "error", equalTo(ErrorCode.OK));
    }

    @Test
    public void testPingInvalidToken() {
        validatePingResponse(UUID.randomUUID().toString(),
                "error", equalTo(ErrorCode.INVALID_INPUT_PARAMETERS));
    }

    @Test
    public void testPingEmptyToken() {
        validatePingResponse("",
                "error", equalTo(ErrorCode.INVALID_INPUT_PARAMETERS));
    }

    @Test
    public void testWithoutTermsAccepted() {
        getConfigItemDao().setValue(ConfigManager.CONFIG_TERMS_ACCEPTED, false);
        createAdminUser();
        JSONObject payload = getAuthRequestPayload("admin", "admin");
        ResponseSpecification response = given()
                .body(payload.toString())
                .contentType(ContentType.JSON)
            .expect()
                .statusCode(HttpStatus.SC_FORBIDDEN);
        response.when().post(getUri("auth"));
    }

    @Test
    public void testPingValidToken() {
        createAdminUser();
        String token = authAdminGetToken();
        validatePingResponse(token,
                "error", equalTo(ErrorCode.OK));
    }

    private JSONObject validateAuthResponse(String username, String password, Object... body) {
        JSONObject payload = getAuthRequestPayload(username, password);
        ResponseSpecification response = getResponseSpecificationPost(payload);
        setExpectedBodies(response, body);
        return getPostResponse(response, "auth");
    }

    private JSONObject validateLogoutResponse(String token, Object... body) {
        JSONObject payload = getLogoutRequestPayload(token);
        ResponseSpecification response = getResponseSpecificationPost(payload);
        setExpectedBodies(response, body);
        return getPostResponse(response, "logout");
    }

    private JSONObject getAuthRequestPayload(String username, String password) {
        JSONObject payload = new JSONObject();
        payload.put("username", username);
        payload.put("password", password);
        return payload;
    }

    private JSONObject getLogoutRequestPayload(String token) {
        JSONObject payload = new JSONObject();
        payload.put("token", token);
        return payload;
    }

    private JSONObject validatePingResponse(String token, Object... body) {
        JSONObject payload = getPingRequestPayload(token);
        ResponseSpecification response = getResponseSpecificationPost(payload);
        setExpectedBodies(response, body);
        return getPostResponse(response, "ping");
    }

    private JSONObject getPingRequestPayload(String token) {
        JSONObject payload = new JSONObject();
        payload.put("token", token);
        return payload;
    }
}
