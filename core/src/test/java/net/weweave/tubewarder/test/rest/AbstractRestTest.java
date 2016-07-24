package net.weweave.tubewarder.test.rest;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.specification.ResponseSpecification;
import net.weweave.tubewarder.dao.UserDao;
import net.weweave.tubewarder.dao.UserGroupDao;
import net.weweave.tubewarder.domain.User;
import net.weweave.tubewarder.domain.UserGroup;
import net.weweave.tubewarder.test.AbstractServiceTest;
import org.apache.http.HttpStatus;
import org.hamcrest.Matcher;
import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

import javax.inject.Inject;
import java.util.UUID;

import static com.jayway.restassured.RestAssured.given;

public abstract class AbstractRestTest extends AbstractServiceTest {
    @Inject
    private UserDao userDao;

    @Inject
    private UserGroupDao userGroupDao;

    protected ResponseSpecification getResponseSpecificationGet(String var1, Object var2, Object... var3) {
        return given()
                .parameters(var1, var2, var3)
                .contentType(ContentType.JSON)
        .expect()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK);
    }

    protected ResponseSpecification getResponseSpecificationPost(JSONObject payload) {
        return given()
                .body(payload.toString())
                .contentType(ContentType.JSON)
        .expect()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.SC_OK);
    }

    protected void setExpectedBodies(ResponseSpecification response, Object... body) {
        String oName = "";
        int i = 1;
        for (Object o : body) {
            if (i%2 == 1) {
                oName = (String)o;
            } else {
                response.body(oName, (Matcher)o);
            }
            i++;
        }
    }

    protected JSONObject getPostResponse(ResponseSpecification response, String service) {
        String responseString = response.when()
                .post(getUri(service))
                .asString();
        return new JSONObject(responseString);
    }

    protected JSONObject getGetResponse(ResponseSpecification response, String service) {
        String responseString = response.when()
                .get(getUri(service))
                .asString();
        return new JSONObject(responseString);
    }

    protected User createAdminUser() {
        User user = new User();
        user.setUsername("admin");
        user.setDisplayName("System Administrator");
        user.setHashedPassword(BCrypt.hashpw("admin", BCrypt.gensalt()));
        user.setEnabled(true);
        user.setAllowAppTokens(true);
        user.setAllowChannels(true);
        user.setAllowTemplates(true);
        user.setAllowSystemConfig(true);
        user.setAllowLogs(true);
        getUserDao().store(user);
        return user;
    }

    protected UserGroup createUserGroupAndAssignUser(User user) {
        UserGroup group = new UserGroup();
        group.setName("g1");
        group.getMembers().add(user);
        getUserGroupDao().store(group);
        return group;
    }

    protected User createUserWithNoRights(String username, String displayName, String password) {
        User user = new User();
        user.setUsername(username);
        user.setDisplayName(displayName);
        user.setHashedPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        user.setEnabled(true);
        getUserDao().store(user);
        return user;
    }

    protected User createUserWithNoRights(String username, String password) {
        return createUserWithNoRights(username, UUID.randomUUID().toString(), password);
    }

    protected String authGetToken(String username, String password) {
        JSONObject payload = new JSONObject();
        payload.put("username", username);
        payload.put("password", password);

        JSONObject result = new JSONObject(given()
                .body(payload.toString())
                .contentType(ContentType.JSON)
        .when()
                .post(getUri("auth"))
                .asString());
        return result.getString("token");
    }

    protected JSONObject getDeleteRequestPayload(String token, String id) {
        JSONObject payload = new JSONObject();
        payload.put("token", token);
        payload.put("id", id);
        return payload;
    }

    protected JSONObject getSetRequestPayload(String token, JSONObject object) {
        JSONObject payload = new JSONObject();
        payload.put("token", token);
        payload.put("object", object);
        return payload;
    }

    protected String authAdminGetToken() {
        return authGetToken("admin", "admin");
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public UserGroupDao getUserGroupDao() {
        return userGroupDao;
    }

    public void setUserGroupDao(UserGroupDao userGroupDao) {
        this.userGroupDao = userGroupDao;
    }
}
