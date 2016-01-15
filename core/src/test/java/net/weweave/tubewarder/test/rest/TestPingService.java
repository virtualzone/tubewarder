package net.weweave.tubewarder.test.rest;

import com.jayway.restassured.specification.ResponseSpecification;
import net.weweave.tubewarder.service.model.ErrorCode;
import org.jboss.arquillian.junit.Arquillian;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;

@RunWith(Arquillian.class)
public class TestPingService extends AbstractRestTest {
    @Test
    public void testInvalidToken() {
        validatePingResponse(UUID.randomUUID().toString(),
                "error", equalTo(ErrorCode.INVALID_INPUT_PARAMETERS));
    }

    @Test
    public void testEmptyToken() {
        validatePingResponse("",
                "error", equalTo(ErrorCode.INVALID_INPUT_PARAMETERS));
    }

    @Test
    public void testValidToken() {
        createAdminUser();
        String token = authAdminGetToken();
        validatePingResponse(token,
                "error", equalTo(ErrorCode.OK));
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
