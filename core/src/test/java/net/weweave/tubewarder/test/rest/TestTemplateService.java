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
public class TestTemplateService extends AbstractRestTest {
    @Test
    public void testCreateSuccess() {
        createAdminUser();
        String token = authAdminGetToken();
        JSONObject response = validateSetTemplateResponse(token, null, "Template 1",
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
        String id = response.getString("id");
        validateGetSingleTemplateResponse(token, id, "Template 1");
    }

    @Test
    public void testCreateDuplicateName() {
        createAdminUser();
        String token = authAdminGetToken();
        validateSetTemplateResponse(token, null, "Template 1",
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
        validateSetTemplateResponse(token, null, "Template 1",
                "error", equalTo(ErrorCode.INVALID_INPUT_PARAMETERS),
                "id", isEmptyOrNullString());
    }

    @Test
    public void testCreateInvalidToken() {
        createAdminUser();
        authAdminGetToken();
        validateSetTemplateResponse(UUID.randomUUID().toString(), null, "Template 1",
                "error", equalTo(ErrorCode.AUTH_REQUIRED),
                "id", isEmptyOrNullString());
    }

    @Test
    public void testCreateInsufficientRights() {
        createUserWithNoRights("dummy", "dummy");
        String token = authGetToken("dummy", "dummy");
        validateSetTemplateResponse(token, null, "Template 1",
                "error", equalTo(ErrorCode.PERMISSION_DENIED),
                "id", isEmptyOrNullString());
    }

    @Test
    public void testUpdateSuccess() {
        createAdminUser();
        String token = authAdminGetToken();

        JSONObject response = validateSetTemplateResponse(token, null, "Template 1",
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
        String id = response.getString("id");

        response = validateSetTemplateResponse(token, id, "Template 1.1",
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()),
                "id", equalTo(id));
        validateGetSingleTemplateResponse(token, id, "Template 1.1");
    }

    @Test
    public void testUpdateDuplicateName() {
        createAdminUser();
        String token = authAdminGetToken();

        JSONObject response = validateSetTemplateResponse(token, null, "Template 1",
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
        String id = response.getString("id");

        validateSetTemplateResponse(token, null, "Template 2",
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));

        validateSetTemplateResponse(token, id, "Template 2",
                "error", equalTo(ErrorCode.INVALID_INPUT_PARAMETERS),
                "id", isEmptyOrNullString());
    }

    @Test
    public void testUpdateNonExistingObject() {
        createAdminUser();
        String token = authAdminGetToken();
        JSONObject response = validateSetTemplateResponse(token, UUID.randomUUID().toString(), "Template 1",
                "error", equalTo(ErrorCode.OBJECT_LOOKUP_ERROR),
                "id", isEmptyOrNullString());
    }

    @Test
    public void testDeleteSuccess() {
        createAdminUser();
        String token = authAdminGetToken();

        JSONObject response = validateSetTemplateResponse(token, null, "Template 1",
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
        String id = response.getString("id");

        validateDeleteTemplateResponse(token, id,
                "error", equalTo(ErrorCode.OK));
        validateGetTemplateResponse(token, id,
                "error", equalTo(ErrorCode.OBJECT_LOOKUP_ERROR),
                "templates.size()", is(0));
    }

    @Test
    public void testDeleteNonExistingObject() {
        createAdminUser();
        String token = authAdminGetToken();
        validateDeleteTemplateResponse(token, UUID.randomUUID().toString(),
                "error", equalTo(ErrorCode.OBJECT_LOOKUP_ERROR));
    }

    @Test
    public void testDeleteInvalidToken() {
        createAdminUser();
        String token = authAdminGetToken();

        JSONObject response = validateSetTemplateResponse(token, null, "Template 1",
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
        String id = response.getString("id");

        validateDeleteTemplateResponse(UUID.randomUUID().toString(), id,
                "error", equalTo(ErrorCode.AUTH_REQUIRED));
    }

    @Test
    public void testDeleteInsufficientRights() {
        createAdminUser();
        String token = authAdminGetToken();

        JSONObject response = validateSetTemplateResponse(token, null, "Template 1",
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
        String id = response.getString("id");

        createUserWithNoRights("dummy", "dummy");
        token = authGetToken("dummy", "dummy");
        validateDeleteTemplateResponse(token, id,
                "error", equalTo(ErrorCode.PERMISSION_DENIED));
    }

    @Test
    public void testGetEmptyList() {
        createAdminUser();
        String token = authAdminGetToken();
        validateGetTemplateResponse(token, null,
                "error", equalTo(ErrorCode.OK),
                "templates.size()", is(0));
    }

    @Test
    public void testGetInvalidId() {
        createAdminUser();
        String token = authAdminGetToken();
        validateGetTemplateResponse(token, UUID.randomUUID().toString(),
                "error", equalTo(ErrorCode.OBJECT_LOOKUP_ERROR),
                "templates.size()", is(0));
    }

    @Test
    public void testGetInsufficientRights() {
        createUserWithNoRights("dummy", "dummy");
        String token = authGetToken("dummy", "dummy");
        validateGetTemplateResponse(token, null,
                "error", equalTo(ErrorCode.PERMISSION_DENIED),
                "templates.size()", is(0));
    }

    @Test
    public void testGetInvalidToken() {
        createAdminUser();
        String token = authAdminGetToken();
        validateGetTemplateResponse(UUID.randomUUID().toString(), null,
                "error", equalTo(ErrorCode.AUTH_REQUIRED),
                "templates.size()", is(0));
    }

    @Test
    public void testGetTwoItems() {
        createAdminUser();
        String token = authAdminGetToken();

        JSONObject response = validateSetTemplateResponse(token, null, "Template 1",
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
        String id1 = response.getString("id");

        response = validateSetTemplateResponse(token, null, "Template 2",
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
        String id2 = response.getString("id");

        validateGetTemplateResponse(token, null,
                "error", equalTo(ErrorCode.OK),
                "templates.size()", is(2),
                "templates[0].id", equalTo(id1),
                "templates[1].id", equalTo(id2),
                "templates[0].name", equalTo("Template 1"),
                "templates[1].name", equalTo("Template 2"));
    }

    private JSONObject validateSetTemplateResponse(String token, String id, String name, Object... body) {
        JSONObject payload = getSetRequestPayload(token, id, name);
        ResponseSpecification response = getResponseSpecificationPost(payload);
        setExpectedBodies(response, body);
        return getPostResponse(response, "template/set");
    }

    private JSONObject validateDeleteTemplateResponse(String token, String id, Object... body) {
        JSONObject payload = getDeleteRequestPayload(token, id);
        ResponseSpecification response = getResponseSpecificationPost(payload);
        setExpectedBodies(response, body);
        return getPostResponse(response, "template/delete");
    }

    private JSONObject validateGetTemplateResponse(String token, String id, Object... body) {
        ResponseSpecification response = getResponseSpecificationGet("token", token, "id", id);
        setExpectedBodies(response, body);
        return getGetResponse(response, "template/get");
    }

    private JSONObject getSetRequestPayload(String token, String id, String name) {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("name", name);
        return super.getSetRequestPayload(token, object);
    }

    private JSONObject validateGetSingleTemplateResponse(String token, String expectedId, String expectedName) {
        return validateGetTemplateResponse(token, expectedId,
                "error", equalTo(ErrorCode.OK),
                "templates.size()", is(1),
                "templates[0].id", equalTo(expectedId),
                "templates[0].name", equalTo(expectedName));
    }
}
