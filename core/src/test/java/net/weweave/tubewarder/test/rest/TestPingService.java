package net.weweave.tubewarder.test.rest;

import com.jayway.restassured.http.ContentType;
import net.weweave.tubewarder.service.model.ErrorCode;
import org.apache.http.HttpStatus;
import org.jboss.arquillian.junit.Arquillian;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@RunWith(Arquillian.class)
public class TestPingService extends AbstractRestTest {
    @Test
    public void testInvalidToken() {
        JSONObject payload = new JSONObject();
        payload.put("token", UUID.randomUUID().toString());

        given()
                .body(payload.toString())
                .contentType(ContentType.JSON)
        .expect()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK)
                .body("error", equalTo(ErrorCode.INVALID_INPUT_PARAMETERS))
        .when()
                .post(getUri("ping"))
                .asString();
    }

    @Test
    public void testEmptyToken() {
        JSONObject payload = new JSONObject();
        payload.put("token", "");

        given()
                .body(payload.toString())
                .contentType(ContentType.JSON)
        .expect()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK)
                .body("error", equalTo(ErrorCode.INVALID_INPUT_PARAMETERS))
        .when()
                .post(getUri("ping"))
                .asString();
    }

    @Test
    public void testValidToken() {
        createAdminUser();
        String token = authAdminGetToken();

        JSONObject payload = new JSONObject();
        payload.put("token", token);

        given()
                .body(payload.toString())
                .contentType(ContentType.JSON)
        .expect()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK)
                .body("error", equalTo(ErrorCode.OK))
        .when()
                .post(getUri("ping"))
                .asString();
    }
}
