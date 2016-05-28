package net.weweave.tubewarder.test.rest;

import com.jayway.restassured.specification.ResponseSpecification;
import net.weweave.tubewarder.domain.User;
import net.weweave.tubewarder.service.model.ErrorCode;
import org.jboss.arquillian.junit.Arquillian;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

import static org.hamcrest.Matchers.*;

@RunWith(Arquillian.class)
public class TestUserGroupService extends AbstractRestTest {
    @Test
    public void testCreateSuccess() {
        createAdminUser();
        String token = authAdminGetToken();
        JSONObject response = validateSetUserGroupResponse(token, null, "g1",
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
        String id = response.getString("id");
        validateGetSingleUserGroupResponse(token, id, "g1");
    }

    @Test
    public void testUpdateSuccess() {
        createAdminUser();
        String token = authAdminGetToken();

        JSONObject response;
        String id;

        response = validateSetUserGroupResponse(token, null, "g1.0",
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
        id = response.getString("id");
        validateGetSingleUserGroupResponse(token, id, "g1.0");

        validateSetUserGroupResponse(token, id, "g1.1",
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()),
                "id", equalTo(id));
        validateGetSingleUserGroupResponse(token, id, "g1.1");
    }

    @Test
    public void testCreateDuplicateName() {
        createAdminUser();
        String token = authAdminGetToken();
        validateSetUserGroupResponse(token, null, "g1",
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
        validateSetUserGroupResponse(token, null, "g1",
                "error", equalTo(ErrorCode.INVALID_INPUT_PARAMETERS),
                "id", isEmptyOrNullString());
    }

    @Test
    public void testUpdateDuplicateName() {
        createAdminUser();
        String token = authAdminGetToken();

        JSONObject response = validateSetUserGroupResponse(token, null, "g1",
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
        String id = response.getString("id");

        validateSetUserGroupResponse(token, null, "g2",
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));

        validateSetUserGroupResponse(token, id, "g2",
                "error", equalTo(ErrorCode.INVALID_INPUT_PARAMETERS),
                "id", isEmptyOrNullString());
    }

    @Test
    public void testCreateInvalidToken() {
        createAdminUser();
        authAdminGetToken();
        validateSetUserGroupResponse(UUID.randomUUID().toString(), null, "g1",
                "error", equalTo(ErrorCode.AUTH_REQUIRED),
                "id", isEmptyOrNullString());
    }

    @Test
    public void testCreateInsufficientRights() {
        createUserWithNoRights("dummy", "dummy");
        String token = authGetToken("dummy", "dummy");
        validateSetUserGroupResponse(token, null, "g1",
                "error", equalTo(ErrorCode.PERMISSION_DENIED),
                "id", isEmptyOrNullString());
    }

    @Test
    public void testUpdateNonExistingObject() {
        createAdminUser();
        String token = authAdminGetToken();
        validateSetUserGroupResponse(token, UUID.randomUUID().toString(), "g1",
                "error", equalTo(ErrorCode.OBJECT_LOOKUP_ERROR),
                "id", isEmptyOrNullString());
    }

    @Test
    public void testDeleteSuccess() {
        createAdminUser();
        String token = authAdminGetToken();

        JSONObject response = validateSetUserGroupResponse(token, null, "g1",
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
        String id = response.getString("id");

        validateDeleteUserGroupResponse(token, id,
                "error", equalTo(ErrorCode.OK));
        validateGetUserGroupResponse(token, id, null,
                "error", equalTo(ErrorCode.OBJECT_LOOKUP_ERROR),
                "groups.size()", is(0));
    }

    @Test
    public void testDeleteNonExistingObject() {
        createAdminUser();
        String token = authAdminGetToken();
        validateDeleteUserGroupResponse(token, UUID.randomUUID().toString(),
                "error", equalTo(ErrorCode.OBJECT_LOOKUP_ERROR));
    }

    @Test
    public void testDeleteInvalidToken() {
        createAdminUser();
        String token = authAdminGetToken();
        JSONObject response = validateSetUserGroupResponse(token, null, "g1",
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
        String id = response.getString("id");
        validateDeleteUserGroupResponse(UUID.randomUUID().toString(), id,
                "error", equalTo(ErrorCode.AUTH_REQUIRED));
    }

    @Test
    public void testDeleteInsufficientRights() {
        createAdminUser();
        String token = authAdminGetToken();
        JSONObject response = validateSetUserGroupResponse(token, null, "g1",
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
        String id = response.getString("id");

        createUserWithNoRights("dummy", "dummy");
        token = authGetToken("dummy", "dummy");
        validateDeleteUserGroupResponse(token, id,
                "error", equalTo(ErrorCode.PERMISSION_DENIED));
    }

    @Test
    public void testGetSingleResultList() {
        createAdminUser();
        String token = authAdminGetToken();

        JSONObject response = validateSetUserGroupResponse(token, null, "g1",
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));

        validateGetUserGroupResponse(token, null, null,
                "error", equalTo(ErrorCode.OK),
                "groups.size()", is(1),
                "groups[0].id", equalTo(response.get("id")),
                "groups[0].name", equalTo("g1"));
    }

    @Test
    public void testGetInvalidId() {
        createAdminUser();
        String token = authAdminGetToken();
        validateGetUserGroupResponse(token, UUID.randomUUID().toString(), null,
                "error", equalTo(ErrorCode.OBJECT_LOOKUP_ERROR),
                "groups.size()", is(0));
    }

    @Test
    public void testGetInsufficientRights() {
        createUserWithNoRights("dummy", "dummy");
        String token = authGetToken("dummy", "dummy");
        validateGetUserGroupResponse(token, null, null,
                "error", equalTo(ErrorCode.PERMISSION_DENIED),
                "groups.size()", is(0));
    }

    @Test
    public void testGetThreeItems() {
        createAdminUser();
        String token = authAdminGetToken();

        JSONObject response1 = validateSetUserGroupResponse(token, null, "g1",
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));

        JSONObject response2 = validateSetUserGroupResponse(token, null, "g2",
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));

        JSONObject response3 = validateSetUserGroupResponse(token, null, "g3",
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));

        validateGetUserGroupResponse(token, null, null,
                "error", equalTo(ErrorCode.OK),
                "groups.size()", is(3),
                "groups[0].id", equalTo(response1.get("id")),
                "groups[0].name", equalTo("g1"),
                "groups[1].id", equalTo(response2.get("id")),
                "groups[1].name", equalTo("g2"),
                "groups[2].id", equalTo(response3.get("id")),
                "groups[2].name", equalTo("g3"));
    }

    @Test
    public void testAddUserToGroup() {
        createAdminUser();
        String token = authAdminGetToken();

        JSONObject response;
        User u1, u2, u3;
        String g1id, g2id;

        // Create 3 users
        u1 = createUserWithNoRights("u1", "");
        u2 = createUserWithNoRights("u2", "");
        u3 = createUserWithNoRights("u3", "");

        // Create 2 groups
        response = validateSetUserGroupResponse(token, null, "g1",
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
        g1id = response.getString("id");
        response = validateSetUserGroupResponse(token, null, "g2",
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
        g2id = response.getString("id");

        // Add users to groups
        validateAddUserToGroupResponse(token, u1.getExposableId(), g1id, "error", equalTo(ErrorCode.OK));
        validateAddUserToGroupResponse(token, u2.getExposableId(), g1id, "error", equalTo(ErrorCode.OK));
        validateAddUserToGroupResponse(token, u2.getExposableId(), g2id, "error", equalTo(ErrorCode.OK));
        validateAddUserToGroupResponse(token, u3.getExposableId(), g2id, "error", equalTo(ErrorCode.OK));

        // Check via group
        validateGetUserGroupResponse(token, g1id, null,
                "groups.size()", is(1),
                "groups[0].id", equalTo(g1id),
                "groups[0].name", equalTo("g1"),
                "groups[0].members.size()", is(2),
                "groups[0].members[0].id", equalTo(u1.getExposableId()),
                "groups[0].members[1].id", equalTo(u2.getExposableId()));
        validateGetUserGroupResponse(token, g2id, null,
                "groups.size()", is(1),
                "groups[0].id", equalTo(g2id),
                "groups[0].name", equalTo("g2"),
                "groups[0].members.size()", is(2),
                "groups[0].members[0].id", equalTo(u2.getExposableId()),
                "groups[0].members[1].id", equalTo(u3.getExposableId()));

        // Check via user
        validateGetUserGroupResponse(token, null, u1.getExposableId(),
                "groups.size()", is(1),
                "groups[0].id", equalTo(g1id));
        validateGetUserGroupResponse(token, null, u2.getExposableId(),
                "groups.size()", is(2),
                "groups[0].id", equalTo(g1id),
                "groups[1].id", equalTo(g2id));
        validateGetUserGroupResponse(token, null, u3.getExposableId(),
                "groups.size()", is(1),
                "groups[0].id", equalTo(g2id));
    }

    @Test
    public void testRemoveUserFromGroup() {
        createAdminUser();
        String token = authAdminGetToken();

        JSONObject response;
        User u1, u2, u3;
        String groupId;

        // Create 3 users
        u1 = createUserWithNoRights("u1", "");
        u2 = createUserWithNoRights("u2", "");
        u3 = createUserWithNoRights("u3", "");

        // Create one group
        response = validateSetUserGroupResponse(token, null, "g1",
                "error", equalTo(ErrorCode.OK),
                "id", not(isEmptyOrNullString()));
        groupId = response.getString("id");

        // Add all users to the group
        validateAddUserToGroupResponse(token, u1.getExposableId(), groupId, "error", equalTo(ErrorCode.OK));
        validateAddUserToGroupResponse(token, u2.getExposableId(), groupId, "error", equalTo(ErrorCode.OK));
        validateAddUserToGroupResponse(token, u3.getExposableId(), groupId, "error", equalTo(ErrorCode.OK));

        // Check
        validateGetUserGroupResponse(token, groupId, null,
                "groups.size()", is(1),
                "groups[0].id", equalTo(groupId),
                "groups[0].name", equalTo("g1"),
                "groups[0].members.size()", is(3),
                "groups[0].members[0].id", equalTo(u1.getExposableId()),
                "groups[0].members[1].id", equalTo(u2.getExposableId()),
                "groups[0].members[2].id", equalTo(u3.getExposableId()));

        // Remove u2 and check
        validateRemoveUserFromGroupResponse(token, u2.getExposableId(), groupId, "error", equalTo(ErrorCode.OK));
        validateGetUserGroupResponse(token, groupId, null,
                "groups.size()", is(1),
                "groups[0].id", equalTo(groupId),
                "groups[0].name", equalTo("g1"),
                "groups[0].members.size()", is(2),
                "groups[0].members[0].id", equalTo(u1.getExposableId()),
                "groups[0].members[1].id", equalTo(u3.getExposableId()));
    }

    private JSONObject validateSetUserGroupResponse(String token, String id, String name, Object... body) {
        JSONObject payload = getSetRequestPayload(token, id, name);
        ResponseSpecification response = getResponseSpecificationPost(payload);
        setExpectedBodies(response, body);
        return getPostResponse(response, "group/set");
    }

    private JSONObject validateDeleteUserGroupResponse(String token, String id, Object... body) {
        JSONObject payload = getDeleteRequestPayload(token, id);
        ResponseSpecification response = getResponseSpecificationPost(payload);
        setExpectedBodies(response, body);
        return getPostResponse(response, "group/delete");
    }

    private JSONObject validateRemoveUserFromGroupResponse(String token, String userId, String groupId, Object... body) {
        JSONObject payload = new JSONObject();
        payload.put("token", token);
        payload.put("userId", userId);
        payload.put("groupId", groupId);
        ResponseSpecification response = getResponseSpecificationPost(payload);
        setExpectedBodies(response, body);
        return getPostResponse(response, "group/removeuser");
    }

    private JSONObject validateAddUserToGroupResponse(String token, String userId, String groupId, Object... body) {
        JSONObject payload = new JSONObject();
        payload.put("token", token);
        payload.put("userId", userId);
        payload.put("groupId", groupId);
        ResponseSpecification response = getResponseSpecificationPost(payload);
        setExpectedBodies(response, body);
        return getPostResponse(response, "group/adduser");
    }

    private JSONObject validateGetUserGroupResponse(String token, String id, String userId, Object... body) {
        ResponseSpecification response = getResponseSpecificationGet("token", token, "id", id, "userId", userId);
        setExpectedBodies(response, body);
        return getGetResponse(response, "group/get");
    }

    private JSONObject getSetRequestPayload(String token, String id, String name) {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("name", name);
        return super.getSetRequestPayload(token, object);
    }

    private JSONObject validateGetSingleUserGroupResponse(String token, String expectedId, String expectedName) {
        return validateGetUserGroupResponse(token, expectedId, null,
                "error", equalTo(ErrorCode.OK),
                "groups.size()", is(1),
                "groups[0].id", equalTo(expectedId),
                "groups[0].name", equalTo(expectedName));
    }
}
