package net.weweave.tubewarder.test.rest;

import com.jayway.restassured.http.ContentType;
import net.weweave.tubewarder.service.model.ErrorCode;
import org.apache.http.HttpStatus;
import org.jboss.arquillian.junit.Arquillian;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@RunWith(Arquillian.class)
public class TestSetAppTokenService extends AbstractRestTest {
    @Test
    public void testCreateAppTokenSuccess() {
        createAdminUser();
        String token = authAdminGetToken();

        JSONObject object = new JSONObject();
        object.put("name", "App Token 1");
        JSONObject payload = new JSONObject();
        payload.put("token", token);
        payload.put("object", object);

        String response = given()
                .body(payload.toString())
                .contentType(ContentType.JSON)
        .expect()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK)
                .body("error", equalTo(ErrorCode.OK))
                .body("id", not(isEmptyOrNullString()))
        .when()
                .post(getUri("apptoken/set"))
                .asString();

        JSONObject jsonResponse = new JSONObject(response);
        String id = jsonResponse.getString("id");

        given()
                .parameters("token", token, "id", id)
                .contentType(ContentType.JSON)
        .expect()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK)
                .body("error", equalTo(ErrorCode.OK))
                .body("tokens.size()", is(1))
                .body("tokens[0].id", equalTo(id))
                .body("tokens[0].name", equalTo("App Token 1"))
        .when()
                .get(getUri("apptoken/get"));
    }

    @Test
    public void testUpdateAppTokenSuccess() {
        createAdminUser();
        String token = authAdminGetToken();

        JSONObject object = new JSONObject();
        object.put("name", "App Token 1");
        JSONObject payload = new JSONObject();
        payload.put("token", token);
        payload.put("object", object);

        String response = given()
                .body(payload.toString())
                .contentType(ContentType.JSON)
        .expect()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK)
                .body("error", equalTo(ErrorCode.OK))
                .body("id", not(isEmptyOrNullString()))
        .when()
                .post(getUri("apptoken/set"))
                .asString();

        JSONObject jsonResponse = new JSONObject(response);
        String id = jsonResponse.getString("id");

        given()
                .parameters("token", token, "id", id)
                .contentType(ContentType.JSON)
        .expect()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK)
                .body("error", equalTo(ErrorCode.OK))
                .body("tokens.size()", is(1))
                .body("tokens[0].id", equalTo(id))
                .body("tokens[0].name", equalTo("App Token 1"))
        .when()
                .get(getUri("apptoken/get"));

        object.put("id", id);
        object.put("name", "Renamed App Token");

        given()
                .body(payload.toString())
                .contentType(ContentType.JSON)
        .expect()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK)
                .body("error", equalTo(ErrorCode.OK))
                .body("id", equalTo(id))
        .when()
                .post(getUri("apptoken/set"))
                .asString();

        given()
                .parameters("token", token, "id", id)
                .contentType(ContentType.JSON)
        .expect()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK)
                .body("error", equalTo(ErrorCode.OK))
                .body("tokens.size()", is(1))
                .body("tokens[0].id", equalTo(id))
                .body("tokens[0].name", equalTo("Renamed App Token"))
        .when()
                .get(getUri("apptoken/get"));
    }

    @Test
    public void testCreateAppTokenInvalidToken() {
        createAdminUser();
        authAdminGetToken();

        JSONObject object = new JSONObject();
        object.put("name", "App Token 1");
        JSONObject payload = new JSONObject();
        payload.put("token", UUID.randomUUID().toString());
        payload.put("object", object);

        given()
                .body(payload.toString())
                .contentType(ContentType.JSON)
        .expect()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK)
                .body("error", equalTo(ErrorCode.AUTH_REQUIRED))
                .body("id", isEmptyOrNullString())
        .when()
                .post(getUri("apptoken/set"))
                .asString();
    }

    @Test
    public void testCreateAppTokenEmptyName() {
        createAdminUser();
        String token = authAdminGetToken();

        JSONObject object = new JSONObject();
        object.put("name", "");
        JSONObject payload = new JSONObject();
        payload.put("token", token);
        payload.put("object", object);

        given()
                .body(payload.toString())
                .contentType(ContentType.JSON)
        .expect()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK)
                .body("error", equalTo(ErrorCode.INVALID_INPUT_PARAMETERS))
                .body("id", isEmptyOrNullString())
        .when()
                .post(getUri("apptoken/set"))
                .asString();
    }
}
