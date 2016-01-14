package net.weweave.tubewarder.test.rest;

import com.jayway.restassured.http.ContentType;
import net.weweave.tubewarder.service.model.ErrorCode;
import org.apache.http.HttpStatus;
import org.jboss.arquillian.junit.Arquillian;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@RunWith(Arquillian.class)
public class TestAuthService extends AbstractRestTest {
    @Override
    public String getServiceName() {
        return "auth";
    }

    @Test
    public void testValidAuth() {
        createAdminUser();

        JSONObject payload = new JSONObject();
        payload.put("username", "admin");
        payload.put("password", "admin");

        given()
                .body(payload.toString())
                .contentType(ContentType.JSON)
        .expect()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK)
                .body("error", equalTo(ErrorCode.OK))
                .body("token", not(isEmptyOrNullString()))
        .when()
                .post(getUri())
                .asString();
    }

    @Test
    public void testEmptyPayload() {
        createAdminUser();

        JSONObject payload = new JSONObject();

        given()
                .body(payload.toString())
                .contentType(ContentType.JSON)
        .expect()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK)
                .body("error", equalTo(ErrorCode.INVALID_INPUT_PARAMETERS))
                .body("token", isEmptyOrNullString())
        .when()
                .post(getUri())
                .asString();
    }

    @Test
    public void testInvalidPassword() {
        createAdminUser();

        JSONObject payload = new JSONObject();
        payload.put("username", "admin");
        payload.put("password", "12345");

        given()
                .body(payload.toString())
                .contentType(ContentType.JSON)
        .expect()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK)
                .body("error", equalTo(ErrorCode.INVALID_INPUT_PARAMETERS))
                .body("token", isEmptyOrNullString())
        .when()
                .post(getUri())
                .asString();
    }

    @Test
    public void testEmptyPassword() {
        createAdminUser();

        JSONObject payload = new JSONObject();
        payload.put("username", "admin");
        payload.put("password", "");

        given()
                .body(payload.toString())
                .contentType(ContentType.JSON)
        .expect()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK)
                .body("error", equalTo(ErrorCode.INVALID_INPUT_PARAMETERS))
                .body("token", isEmptyOrNullString())
        .when()
                .post(getUri())
                .asString();
    }
}
