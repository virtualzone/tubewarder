package net.weweave.tubewarder.test.rest;

import com.jayway.restassured.specification.ResponseSpecification;
import net.weweave.tubewarder.domain.*;
import net.weweave.tubewarder.service.model.ErrorCode;
import net.weweave.tubewarder.test.TestSendServiceCommon;
import net.weweave.tubewarder.util.DateTimeFormat;
import org.jboss.arquillian.junit.Arquillian;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.Matchers.*;

@RunWith(Arquillian.class)
public class TestLogService extends AbstractRestTest {
    @Inject
    private TestSendServiceCommon common;

    @Test
    public void testGetInvalidLogId() {
        createAdminUser();
        String token = authAdminGetToken();

        validateGetLogResponse(token, UUID.randomUUID().toString(),
                "error", equalTo(ErrorCode.OBJECT_LOOKUP_ERROR),
                "logs.size()", is(0));
    }

    @Test
    public void testEmptyList() {
        createAdminUser();
        String token = authAdminGetToken();

        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.DAY_OF_MONTH, -1);
        Calendar endDate = Calendar.getInstance();

        validateGetLogResponse(token, startDate.getTime(), endDate.getTime(), "", "",
                "error", equalTo(ErrorCode.OK),
                "logs.size()", is(0));
    }

    @Test
    public void testThreeEntries() {
        User user = createAdminUser();
        UserGroup group = createUserGroupAndAssignUser(user);
        String token = authAdminGetToken();

        SendEssentials sendEssentials = new SendEssentials(group);
        send(sendEssentials, "DOI", "some more details 1");
        send(sendEssentials, "DOI", "some more details 2");
        send(sendEssentials, "DOI 2.0", "some more details 3");

        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.DAY_OF_MONTH, -1);
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DAY_OF_MONTH, +1);

        validateGetLogResponse(token, startDate.getTime(), endDate.getTime(), "", "",
                "error", equalTo(ErrorCode.OK),
                "logs.size()", is(3),

                "logs[0].id", not(isEmptyOrNullString()),
                "logs[0].keyword", equalTo("DOI 2.0"),
                "logs[0].details", isEmptyOrNullString(),
                "logs[0].appToken", equalTo(sendEssentials.token.getExposableId()),
                "logs[0].appTokenName", equalTo(sendEssentials.token.getName()),
                "logs[0].templateId", equalTo(sendEssentials.template.getExposableId()),
                "logs[0].templateName", equalTo(sendEssentials.template.getName()),
                "logs[0].channelId", equalTo(sendEssentials.channel.getExposableId()),
                "logs[0].channelName", equalTo(sendEssentials.channel.getName()),
                "logs[0].senderName", equalTo("weweave"),
                "logs[0].senderAddress", equalTo("noreply@weweave.net"),
                "logs[0].recipientName", equalTo("John"),
                "logs[0].recipientAddress", equalTo("+490000"),
                "logs[0].subject", isEmptyOrNullString(),
                "logs[0].content", isEmptyOrNullString(),

                "logs[1].id", not(isEmptyOrNullString()),
                "logs[1].keyword", equalTo("DOI"),
                "logs[1].details", isEmptyOrNullString(),
                "logs[1].appToken", equalTo(sendEssentials.token.getExposableId()),
                "logs[1].appTokenName", equalTo(sendEssentials.token.getName()),
                "logs[1].templateId", equalTo(sendEssentials.template.getExposableId()),
                "logs[1].templateName", equalTo(sendEssentials.template.getName()),
                "logs[1].channelId", equalTo(sendEssentials.channel.getExposableId()),
                "logs[1].channelName", equalTo(sendEssentials.channel.getName()),
                "logs[1].senderName", equalTo("weweave"),
                "logs[1].senderAddress", equalTo("noreply@weweave.net"),
                "logs[1].recipientName", equalTo("John"),
                "logs[1].recipientAddress", equalTo("+490000"),
                "logs[1].subject", isEmptyOrNullString(),
                "logs[1].content", isEmptyOrNullString(),

                "logs[2].id", not(isEmptyOrNullString()),
                "logs[2].keyword", equalTo("DOI"),
                "logs[2].details", isEmptyOrNullString(),
                "logs[2].appToken", equalTo(sendEssentials.token.getExposableId()),
                "logs[2].appTokenName", equalTo(sendEssentials.token.getName()),
                "logs[2].templateId", equalTo(sendEssentials.template.getExposableId()),
                "logs[2].templateName", equalTo(sendEssentials.template.getName()),
                "logs[2].channelId", equalTo(sendEssentials.channel.getExposableId()),
                "logs[2].channelName", equalTo(sendEssentials.channel.getName()),
                "logs[2].senderName", equalTo("weweave"),
                "logs[2].senderAddress", equalTo("noreply@weweave.net"),
                "logs[2].recipientName", equalTo("John"),
                "logs[2].recipientAddress", equalTo("+490000"),
                "logs[2].subject", isEmptyOrNullString(),
                "logs[2].content", isEmptyOrNullString());
    }

    @Test
    public void testGetLogDetails() {
        User user = createAdminUser();
        UserGroup group = createUserGroupAndAssignUser(user);
        String token = authAdminGetToken();

        SendEssentials sendEssentials = new SendEssentials(group);
        send(sendEssentials, "DOI", "some more details 1");

        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.DAY_OF_MONTH, -1);
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DAY_OF_MONTH, +1);

        JSONObject result = validateGetLogResponse(token, startDate.getTime(), endDate.getTime(), "", "",
                "error", equalTo(ErrorCode.OK),
                "logs.size()", is(1),
                "logs[0].id", not(isEmptyOrNullString()));
        String id = result.getJSONArray("logs").getJSONObject(0).getString("id");

        validateGetLogResponse(token, id,
                "error", equalTo(ErrorCode.OK),
                "logs.size()", is(1),
                "logs[0].id", equalTo(id),
                "logs[0].details", equalTo("some more details 1"),
                "logs[0].subject", equalTo("Welcome to Tubewarder, John!"),
                "logs[0].content", equalTo("Hi John Doe, here is your activation code: 1234567890"));
    }

    @Test
    public void testKeywordFilter() {
        User user = createAdminUser();
        UserGroup group = createUserGroupAndAssignUser(user);
        String token = authAdminGetToken();

        SendEssentials sendEssentials = new SendEssentials(group);
        send(sendEssentials, "DOI", "some more details 1");
        send(sendEssentials, "DOI", "some more details 2");
        send(sendEssentials, "DOI 2.0", "some more details 3");

        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.DAY_OF_MONTH, -1);
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DAY_OF_MONTH, +1);

        validateGetLogResponse(token, startDate.getTime(), endDate.getTime(), "DOI", "",
                "error", equalTo(ErrorCode.OK),
                "logs.size()", is(2),
                "logs[0].keyword", equalTo("DOI"),
                "logs[1].keyword", equalTo("DOI"));
        validateGetLogResponse(token, startDate.getTime(), endDate.getTime(), "DOI 2.0", "",
                "error", equalTo(ErrorCode.OK),
                "logs.size()", is(1),
                "logs[0].keyword", equalTo("DOI 2.0"));
    }

    @Test
    public void testDetailsFilter() {
        User user = createAdminUser();
        UserGroup group = createUserGroupAndAssignUser(user);
        String token = authAdminGetToken();

        SendEssentials sendEssentials = new SendEssentials(group);
        send(sendEssentials, "DOI", "some more details 1");
        send(sendEssentials, "DOI", "some more details 2");
        send(sendEssentials, "DOI 2.0", "some more details 3");

        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.DAY_OF_MONTH, -1);
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DAY_OF_MONTH, +1);

        validateGetLogResponse(token, startDate.getTime(), endDate.getTime(), "", "some more details 2",
                "error", equalTo(ErrorCode.OK),
                "logs.size()", is(1));

        validateGetLogResponse(token, startDate.getTime(), endDate.getTime(), "", "some more details",
                "error", equalTo(ErrorCode.OK),
                "logs.size()", is(3));

        validateGetLogResponse(token, startDate.getTime(), endDate.getTime(), "", "more",
                "error", equalTo(ErrorCode.OK),
                "logs.size()", is(3));

        validateGetLogResponse(token, startDate.getTime(), endDate.getTime(), "", "some more stuff",
                "error", equalTo(ErrorCode.OK),
                "logs.size()", is(0));
    }

    @Test
    public void testDateRange() throws InterruptedException{
        User user = createAdminUser();
        UserGroup group = createUserGroupAndAssignUser(user);
        String token = authAdminGetToken();


        SendEssentials sendEssentials = new SendEssentials(group);
        send(sendEssentials, "DOI1", "some more details 1");

        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.SECOND, -1);

        Thread.sleep(2000);
        send(sendEssentials, "DOI2", "some more details 2");
        Thread.sleep(2000);
        send(sendEssentials, "DOI3", "some more details 3");

        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.SECOND, +1);

        validateGetLogResponse(token, startDate.getTime(), endDate.getTime(), "", "",
                "error", equalTo(ErrorCode.OK),
                "logs.size()", is(3),
                "logs[0].keyword", equalTo("DOI3"),
                "logs[1].keyword", equalTo("DOI2"),
                "logs[2].keyword", equalTo("DOI1"));

        endDate.add(Calendar.SECOND, -2);
        validateGetLogResponse(token, startDate.getTime(), endDate.getTime(), "", "",
                "error", equalTo(ErrorCode.OK),
                "logs.size()", is(2),
                "logs[0].keyword", equalTo("DOI2"),
                "logs[1].keyword", equalTo("DOI1"));

        startDate.add(Calendar.SECOND, +2);
        validateGetLogResponse(token, startDate.getTime(), endDate.getTime(), "", "",
                "error", equalTo(ErrorCode.OK),
                "logs.size()", is(1),
                "logs[0].keyword", equalTo("DOI2"));
    }

    private JSONObject validateGetLogResponse(String token, String id, Object... body) {
        ResponseSpecification response = getResponseSpecificationGet("token", token, "id", id);
        setExpectedBodies(response, body);
        return getGetResponse(response, "log/get");
    }

    private JSONObject validateGetLogResponse(String token,
                                              Date startDate,
                                              Date endDate,
                                              String keyword,
                                              String searchString,
                                              Object... body) {
        ResponseSpecification response = getResponseSpecificationGet(
                "token", token,
                "startDate", DateTimeFormat.format(startDate),
                "endDate", DateTimeFormat.format(endDate),
                "keyword", keyword,
                "searchString", searchString);
        setExpectedBodies(response, body);
        return getGetResponse(response, "log/get");
    }

    private JSONObject send(SendEssentials sendEssentials, String keyword, String details) {
        Map<String, Object> model = createMap(
                "firstname", "John",
                "lastname", "Doe",
                "code", "1234567890");
        return validateSendResponse(sendEssentials.token.getExposableId(), sendEssentials.template.getName(), sendEssentials.channel.getName(), model, "John", "+490000", keyword, details,
                "error", equalTo(ErrorCode.OK),
                "subject", equalTo("Welcome to Tubewarder, John!"),
                "content", equalTo("Hi John Doe, here is your activation code: 1234567890"));
    }

    private JSONObject validateSendResponse(String token,
                                            String templateName,
                                            String channelName,
                                            Map<String, Object> model,
                                            String recipientName,
                                            String recipientAddress,
                                            String keyword,
                                            String details,
                                            Object... body) {
        JSONObject payload = getCommon().getSendRequestJsonPayload(
                token,
                templateName,
                channelName,
                model,
                recipientName,
                recipientAddress,
                keyword,
                details);
        ResponseSpecification response = getResponseSpecificationPost(payload);
        setExpectedBodies(response, body);
        return getPostResponse(response, "send");
    }

    public TestSendServiceCommon getCommon() {
        return common;
    }

    public void setCommon(TestSendServiceCommon common) {
        this.common = common;
    }

    private class SendEssentials {
        public final AppToken token;
        public final Channel channel;
        public final Template template;

        public SendEssentials() {
            this(null);
        }

        public SendEssentials(UserGroup group) {
            token = getCommon().createAppToken();
            channel = getCommon().createChannel(group);
            template = getCommon().createTemplate(group);
            getCommon().createChannelTemplate(template, channel,
                    "Welcome to Tubewarder, ${firstname}!",
                    "Hi ${firstname} ${lastname}, here is your activation code: ${code}");
        }
    }
}
