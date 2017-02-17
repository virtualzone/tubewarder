package net.weweave.tubewarder.test.rest;

import com.jayway.restassured.specification.ResponseSpecification;
import net.weweave.tubewarder.service.model.ErrorCode;
import org.jboss.arquillian.junit.Arquillian;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.*;

@RunWith(Arquillian.class)
public class TestConfigService extends AbstractRestTest {
    @Test
    public void testSetGetConfig() {
        createAdminUser();
        String token = authAdminGetToken();

        validateGetConfigResponse(token,
                "error", equalTo(ErrorCode.OK),
                "items.size()", is(0));

        validateSetConfigResponse(token, "key1", "value 1",
                "error", equalTo(ErrorCode.OK));

        validateGetConfigResponse(token,
                "error", equalTo(ErrorCode.OK),
                "items.size()", is(1),
                "items[0].key", equalTo("key1"),
                "items[0].label", equalTo("key1"),
                "items[0].value", equalTo("value 1"));

        validateSetConfigResponse(token, "key1", "value 1.1",
                "error", equalTo(ErrorCode.OK));

        validateSetConfigResponse(token, "key2", "value 2",
                "error", equalTo(ErrorCode.OK));

        validateGetConfigResponse(token,
                "error", equalTo(ErrorCode.OK),
                "items.size()", is(2),
                "items[0].key", equalTo("key1"),
                "items[0].label", equalTo("key1"),
                "items[0].value", equalTo("value 1.1"),
                "items[1].key", equalTo("key2"),
                "items[1].label", equalTo("key2"),
                "items[1].value", equalTo("value 2"));
    }

    private JSONObject validateSetConfigResponse(String token, String key, String value, Object... body) {
        JSONObject payload = getSetConfigRequestPayload(token, key, value);
        ResponseSpecification response = getResponseSpecificationPost(payload);
        setExpectedBodies(response, body);
        return getPostResponse(response, "config/set");
    }

    private JSONObject validateGetConfigResponse(String token, Object... body) {
        getGetConfigRequestPayload(token);
        ResponseSpecification response = getResponseSpecificationGet("token", token);
        setExpectedBodies(response, body);
        return getGetResponse(response, "config/get");
    }

    private JSONObject getSetConfigRequestPayload(String token, String key, String value) {
        JSONObject payload = new JSONObject();
        payload.put("token", token);

        JSONArray items = new JSONArray();
        JSONObject item = new JSONObject();
        item.put("key", key);
        item.put("value", value);
        items.put(item);

        payload.put("items", items);
        return payload;
    }

    private JSONObject getGetConfigRequestPayload(String token) {
        JSONObject payload = new JSONObject();
        payload.put("token", token);
        return payload;
    }
}
