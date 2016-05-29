package net.weweave.tubewarder.test.rest;

import com.jayway.restassured.specification.ResponseSpecification;
import net.weweave.tubewarder.dao.UserGroupDao;
import net.weweave.tubewarder.domain.User;
import net.weweave.tubewarder.domain.UserGroup;
import net.weweave.tubewarder.service.model.ErrorCode;
import org.jboss.arquillian.junit.Arquillian;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.UUID;

import static org.hamcrest.Matchers.*;

@RunWith(Arquillian.class)
public class TestChannelService extends AbstractRestTest {
    @Inject
    private UserGroupDao userGroupDao;

    @Test
    public void testCreateChannelSuccess() {
        User user = createAdminUser();
        UserGroup group = createUserGroupAndAssignUser(user);
        String token = authAdminGetToken();
        JSONObject response = validateSetChannelResponse(token, null, "Channel 1", group.getExposableId(),
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
        String id = response.getString("id");
        validateGetSingleChannelResponse(token, id, "Channel 1");
    }

    @Test
    public void testCreateChannelDuplicateName() {
        User user = createAdminUser();
        UserGroup group = createUserGroupAndAssignUser(user);
        String token = authAdminGetToken();
        validateSetChannelResponse(token, null, "Channel 1", group.getExposableId(),
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
        validateSetChannelResponse(token, null, "Channel 1", group.getExposableId(),
                "error", equalTo(ErrorCode.INVALID_INPUT_PARAMETERS),
                "id", isEmptyOrNullString());
    }

    @Test
    public void testCreateChannelDifferentName() {
        User user = createAdminUser();
        UserGroup group = createUserGroupAndAssignUser(user);
        String token = authAdminGetToken();
        validateSetChannelResponse(token, null, "Channel 1", group.getExposableId(),
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
        validateSetChannelResponse(token, null, "Channel 2", group.getExposableId(),
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
    }

    @Test
    public void testUpdateChannelSuccess() {
        User user = createAdminUser();
        UserGroup group = createUserGroupAndAssignUser(user);
        String token = authAdminGetToken();
        JSONObject response = validateSetChannelResponse(token, null, "Channel 1", group.getExposableId(),
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
        String id = response.getString("id");
        validateGetSingleChannelResponse(token, id, "Channel 1");
        validateSetChannelResponse(token, id, "Renamed Channel 1", group.getExposableId(),
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
        validateGetSingleChannelResponse(token, id, "Renamed Channel 1");
    }

    @Test
    public void testUpdateNonExistingObject() {
        User user = createAdminUser();
        UserGroup group = createUserGroupAndAssignUser(user);
        String token = authAdminGetToken();
        validateSetChannelResponse(token, UUID.randomUUID().toString(), "Channel 1", group.getExposableId(),
                "error", equalTo(ErrorCode.OBJECT_LOOKUP_ERROR),
                "id", isEmptyOrNullString());
    }

    @Test
    public void testUpdateRenameDuplicateName() {
        User user = createAdminUser();
        UserGroup group = createUserGroupAndAssignUser(user);
        String token = authAdminGetToken();

        JSONObject response = validateSetChannelResponse(token, null, "Channel 1", group.getExposableId(),
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
        String id = response.getString("id");

        validateSetChannelResponse(token, null, "Channel 2", group.getExposableId(),
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
        validateGetSingleChannelResponse(token, id, "Channel 1");

        validateSetChannelResponse(token, id, "Channel 2", group.getExposableId(),
                "error", equalTo(ErrorCode.INVALID_INPUT_PARAMETERS));
        validateGetSingleChannelResponse(token, id, "Channel 1");
    }

    @Test
    public void testCreateInsufficientRights() {
        createUserWithNoRights("dummy", "dummy");
        String token = authGetToken("dummy", "dummy");
        validateSetChannelResponse(token, null, "Channel 1", "xyz",
                "error", equalTo(ErrorCode.PERMISSION_DENIED),
                "id", isEmptyOrNullString());
    }

    @Test
    public void testCreateEmptyToken() {
        createAdminUser();
        validateSetChannelResponse("", null, "Channel 1", "xyz",
                "error", equalTo(ErrorCode.AUTH_REQUIRED),
                "id", isEmptyOrNullString());
    }

    @Test
    public void testCreateInvalidToken() {
        createAdminUser();
        validateSetChannelResponse(UUID.randomUUID().toString(), null, "Channel 1", "xyz",
                "error", equalTo(ErrorCode.AUTH_REQUIRED),
                "id", isEmptyOrNullString());
    }

    @Test
    public void testDeleteSuccess() {
        User user = createAdminUser();
        UserGroup group = createUserGroupAndAssignUser(user);

        String token = authAdminGetToken();
        JSONObject response = validateSetChannelResponse(token, null, "Channel 1", group.getExposableId(),
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
        String id = response.getString("id");
        validateDeleteChannelResponse(token, id,
                "error", equalTo(ErrorCode.OK));
        validateGetChannelResponse(token, id,
                "error", equalTo(ErrorCode.OBJECT_LOOKUP_ERROR),
                "channels.size()", is(0));
    }

    @Test
    public void testDeleteInsufficientRights() {
        User user = createAdminUser();
        UserGroup group = createUserGroupAndAssignUser(user);
        String token = authAdminGetToken();
        JSONObject response = validateSetChannelResponse(token, null, "Channel 1", group.getExposableId(),
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
        String id = response.getString("id");

        createUserWithNoRights("dummy", "dummy");
        token = authGetToken("dummy", "dummy");
        validateDeleteChannelResponse(token, id,
                "error", equalTo(ErrorCode.PERMISSION_DENIED));
    }

    @Test
    public void testDeleteInvalidToken() {
        User user = createAdminUser();
        UserGroup group = createUserGroupAndAssignUser(user);
        String token = authAdminGetToken();
        JSONObject response = validateSetChannelResponse(token, null, "Channel 1", group.getExposableId(),
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
        String id = response.getString("id");

        validateDeleteChannelResponse(UUID.randomUUID().toString(), id,
                "error", equalTo(ErrorCode.AUTH_REQUIRED));
    }

    @Test
    public void testDeleteInvalidId() {
        User user = createAdminUser();
        UserGroup group = createUserGroupAndAssignUser(user);
        String token = authAdminGetToken();
        validateSetChannelResponse(token, null, "Channel 1", group.getExposableId(),
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));

        validateDeleteChannelResponse(token, UUID.randomUUID().toString(),
                "error", equalTo(ErrorCode.OBJECT_LOOKUP_ERROR));
    }

    @Test
    public void testDeleteEmptyId() {
        User user = createAdminUser();
        UserGroup group = createUserGroupAndAssignUser(user);
        String token = authAdminGetToken();
        validateSetChannelResponse(token, null, "Channel 1", group.getExposableId(),
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));

        validateDeleteChannelResponse(token, "",
                "error", equalTo(ErrorCode.INVALID_INPUT_PARAMETERS));
    }

    @Test
    public void testGetEmptyList() {
        createAdminUser();
        String token = authAdminGetToken();
        validateGetChannelResponse(token, null,
                "error", equalTo(ErrorCode.OK),
                "channels.size()", is(0));
    }

    @Test
    public void testGetInvalidId() {
        createAdminUser();
        String token = authAdminGetToken();
        validateGetChannelResponse(token, UUID.randomUUID().toString(),
                "error", equalTo(ErrorCode.OBJECT_LOOKUP_ERROR),
                "channels.size()", is(0));
    }

    @Test
    public void testGetInsufficientRights() {
        createUserWithNoRights("dummy", "dummy");
        String token = authGetToken("dummy", "dummy");
        validateGetChannelResponse(token, null,
                "error", equalTo(ErrorCode.PERMISSION_DENIED),
                "channels.size()", is(0));
    }

    @Test
    public void testGetTwoItems() {
        User user = createAdminUser();
        UserGroup group = createUserGroupAndAssignUser(user);
        String token = authAdminGetToken();

        JSONObject response = validateSetChannelResponse(token, null, "Channel 1", group.getExposableId(),
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
        String id1 = response.getString("id");

        response = validateSetChannelResponse(token, null, "Channel 2",  group.getExposableId(),
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
        String id2 = response.getString("id");

        validateGetChannelResponse(token, null,
                "error", equalTo(ErrorCode.OK),
                "channels.size()", is(2),
                "channels[0].id", equalTo(id1),
                "channels[1].id", equalTo(id2),
                "channels[0].name", equalTo("Channel 1"),
                "channels[1].name", equalTo("Channel 2"));
    }

    private JSONObject getSysoutConfig() {
        JSONObject object = new JSONObject();
        object.put("id", "SYSOUT");
        object.put("prefix", "[");
        object.put("suffix", "]");
        return object;
    }

    private UserGroup createUserGroupAndAssignUser(User user) {
        UserGroup group = new UserGroup();
        group.setName("g1");
        group.getMembers().add(user);
        getUserGroupDao().store(group);
        return group;
    }

    private JSONObject validateSetChannelResponse(String token, String id, String name, String groupId, Object... body) {
        JSONObject payload = getSetRequestPayload(token, id, name, groupId);
        ResponseSpecification response = getResponseSpecificationPost(payload);
        setExpectedBodies(response, body);
        return getPostResponse(response, "channel/set");
    }

    private JSONObject validateDeleteChannelResponse(String token, String id, Object... body) {
        JSONObject payload = getDeleteRequestPayload(token, id);
        ResponseSpecification response = getResponseSpecificationPost(payload);
        setExpectedBodies(response, body);
        return getPostResponse(response, "channel/delete");
    }

    private JSONObject validateGetChannelResponse(String token, String id, Object... body) {
        ResponseSpecification response = getResponseSpecificationGet("token", token, "id", id);
        setExpectedBodies(response, body);
        return getGetResponse(response, "channel/get");
    }

    private JSONObject getSetRequestPayload(String token, String id, String name, String groupId) {
        JSONObject groupJson = new JSONObject();
        groupJson.put("id", groupId);

        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("name", name);
        object.put("group", groupJson);
        object.put("config", getSysoutConfig());
        return super.getSetRequestPayload(token, object);
    }

    private JSONObject validateGetSingleChannelResponse(String token, String expectedId, String expectedName) {
        return validateGetChannelResponse(token, expectedId,
                "error", equalTo(ErrorCode.OK),
                "channels.size()", is(1),
                "channels[0].id", equalTo(expectedId),
                "channels[0].name", equalTo(expectedName),
                "channels[0].config.id", equalTo("SYSOUT"));
    }

    public UserGroupDao getUserGroupDao() {
        return userGroupDao;
    }

    public void setUserGroupDao(UserGroupDao userGroupDao) {
        this.userGroupDao = userGroupDao;
    }
}
