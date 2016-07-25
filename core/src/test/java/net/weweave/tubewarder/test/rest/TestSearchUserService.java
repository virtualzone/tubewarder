package net.weweave.tubewarder.test.rest;

import com.jayway.restassured.specification.ResponseSpecification;
import net.weweave.tubewarder.domain.User;
import net.weweave.tubewarder.service.model.ErrorCode;
import org.jboss.arquillian.junit.Arquillian;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;

@RunWith(Arquillian.class)
public class TestSearchUserService extends AbstractRestTest {
    @Test
    public void testSearchMatchAtBeginningOfUsername() {
        createAdminUser();
        String token = authAdminGetToken();

        User u1 = createUserWithNoRights("john.doe", "John Doe", "dummy");
        User u2 = createUserWithNoRights("john.miller", "John Miller", "dummy");
        createUserWithNoRights("jack.something", "NoGoodUsername", "dummy");
        createUserWithNoRights("weweave", "This is weweave", "dummy");

        JSONObject users = validateSearchUserResponse(token, "john",
                "error", equalTo(ErrorCode.OK)).getJSONObject("users");
        Assert.assertNotNull(users);
        Assert.assertEquals(2, users.length());
        Assert.assertTrue(users.has(u1.getExposableId()));
        Assert.assertTrue(users.has(u2.getExposableId()));
        Assert.assertEquals("john.doe (John Doe)", users.get(u1.getExposableId()));
        Assert.assertEquals("john.miller (John Miller)", users.get(u2.getExposableId()));
    }

    @Test
    public void testSearchMatchAtMiddleOfUsername() {
        createAdminUser();
        String token = authAdminGetToken();

        User u1 = createUserWithNoRights("john.doe", "John Doe", "dummy");
        User u2 = createUserWithNoRights("john.miller", "John Miller", "dummy");
        createUserWithNoRights("jack.something", "NoGoodUsername", "dummy");
        createUserWithNoRights("weweave", "This is weweave", "dummy");

        JSONObject users = validateSearchUserResponse(token, "oh",
                "error", equalTo(ErrorCode.OK)).getJSONObject("users");
        Assert.assertNotNull(users);
        Assert.assertEquals(2, users.length());
        Assert.assertTrue(users.has(u1.getExposableId()));
        Assert.assertTrue(users.has(u2.getExposableId()));
        Assert.assertEquals("john.doe (John Doe)", users.get(u1.getExposableId()));
        Assert.assertEquals("john.miller (John Miller)", users.get(u2.getExposableId()));
    }

    @Test
    public void testSearchMatchInDisplayName() {
        createAdminUser();
        String token = authAdminGetToken();

        createUserWithNoRights("john.doe", "John Doe", "dummy");
        createUserWithNoRights("john.miller", "John Miller", "dummy");
        User u3 = createUserWithNoRights("jack.something", "NoGoodUsername", "dummy");
        createUserWithNoRights("weweave", "This is weweave", "dummy");

        JSONObject users = validateSearchUserResponse(token, "good",
                "error", equalTo(ErrorCode.OK)).getJSONObject("users");
        Assert.assertNotNull(users);
        Assert.assertEquals(1, users.length());
        Assert.assertTrue(users.has(u3.getExposableId()));
        Assert.assertEquals("jack.something (NoGoodUsername)", users.get(u3.getExposableId()));
    }

    @Test
    public void testSearchFirstLetterOnly() {
        createAdminUser();
        String token = authAdminGetToken();

        User u1 = createUserWithNoRights("john.doe", "John Doe", "dummy");
        User u2 = createUserWithNoRights("john.miller", "John Miller", "dummy");
        User u3 = createUserWithNoRights("jack.something", "NoGoodUsername", "dummy");
        createUserWithNoRights("weweave", "This is weweave", "dummy");

        JSONObject users = validateSearchUserResponse(token, "j",
                "error", equalTo(ErrorCode.OK)).getJSONObject("users");
        Assert.assertNotNull(users);
        Assert.assertEquals(3, users.length());
        Assert.assertTrue(users.has(u1.getExposableId()));
        Assert.assertTrue(users.has(u2.getExposableId()));
        Assert.assertTrue(users.has(u3.getExposableId()));
        Assert.assertEquals("john.doe (John Doe)", users.get(u1.getExposableId()));
        Assert.assertEquals("john.miller (John Miller)", users.get(u2.getExposableId()));
        Assert.assertEquals("jack.something (NoGoodUsername)", users.get(u3.getExposableId()));
    }

    @Test
    public void testSearchEmptyResult() {
        createAdminUser();
        String token = authAdminGetToken();

        createUserWithNoRights("john.doe", "John Doe", "dummy");
        createUserWithNoRights("john.miller", "John Miller", "dummy");
        createUserWithNoRights("jack.something", "NoGoodUsername", "dummy");
        createUserWithNoRights("weweave", "This is weweave", "dummy");

        JSONObject users = validateSearchUserResponse(token, "blah",
                "error", equalTo(ErrorCode.OK)).getJSONObject("users");
        Assert.assertNotNull(users);
        Assert.assertEquals(0, users.length());
    }

    @Test
    public void testSearchInsufficientRights() {
        createUserWithNoRights("dummy", "dummy");
        String token = authGetToken("dummy", "dummy");

        validateSearchUserResponse(token, "weweave",
                "error", equalTo(ErrorCode.PERMISSION_DENIED));
    }

    @Test
    public void testSearchInvalidToken() {
        validateSearchUserResponse(UUID.randomUUID().toString(), "weweave",
                "error", equalTo(ErrorCode.AUTH_REQUIRED));
    }

    private JSONObject validateSearchUserResponse(String token, String query, Object... body) {
        ResponseSpecification response = getResponseSpecificationGet("token", token);
        setExpectedBodies(response, body);
        return getGetResponse(response, "user/search/"+query);
    }
}
