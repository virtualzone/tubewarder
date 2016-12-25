package net.weweave.tubewarder.outputhandler;

import net.weweave.tubewarder.outputhandler.api.*;
import net.weweave.tubewarder.outputhandler.api.configoption.OutputHandlerConfigOption;
import net.weweave.tubewarder.outputhandler.api.configoption.StringConfigOption;
import org.apache.commons.validator.GenericValidator;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@OutputHandler(id="TWITTER", name="Twitter")
public class TwitterOutputHandler implements IOutputHandler {
    private static final String BASE_URL = "https://api.twitter.com/1.1/direct_messages/new.json";

    @Override
    public void process(Config config, SendItem sendItem) throws TemporaryProcessingException, PermanentProcessingException {
        Map<String, String> params = getQueryParams(sendItem);
        String oauthHeader = getOauthHeader(config, "POST", BASE_URL, params);
        Response response = buildRequest(params, oauthHeader).post(Entity.json(null));
        handleResponse(response);
    }

    private Map<String, String> getQueryParams(SendItem item) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", item.getRecipient().getAddress());
        params.put("text", item.getContent());
        return params;
    }

    private Invocation.Builder buildRequest(Map<String, String> params, String oauthHeader) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(BASE_URL);
        for (String key : params.keySet()) {
            target = target.queryParam(key, params.get(key));
        }
        return target
                .request()
                .header("Authorization", oauthHeader);
    }

    private String getOauthHeader(Config config, String method, String url, Map<String, String> params) {
        TwitterOauth.Credentials credentials = new TwitterOauth.Credentials();
        credentials.setConsumerKey(config.getString("consumerKey"));
        credentials.setConsumerSecret(config.getString("consumerSecret"));
        credentials.setAccessToken(config.getString("accessToken"));
        credentials.setTokenSecret(config.getString("accessTokenSecret"));
        return TwitterOauth.getOautHeaderString(credentials, method, url, params);
    }

    private void handleResponse(Response response) throws TemporaryProcessingException, PermanentProcessingException {
        if (response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode() ||
                response.getStatus() == Response.Status.FORBIDDEN.getStatusCode()) {
            throwPermanentErrorWithMessages(response);
        }
        if (response.getStatus() != Response.Status.OK.getStatusCode()) {
            throwTemporaryErrorWithMessages(response);
        }
    }

    private void throwTemporaryErrorWithMessages(Response response) throws TemporaryProcessingException {
        String errors = createTwitterErrorString(response);
        throw new TemporaryProcessingException("Received errors: " + errors + "(HTTP status code = " + response.getStatus());
    }

    private void throwPermanentErrorWithMessages(Response response) throws PermanentProcessingException {
        String errors = createTwitterErrorString(response);
        throw new PermanentProcessingException("Received errors: " + errors + "(HTTP status code = " + response.getStatus());
    }

    private String createTwitterErrorString(Response response) {
        TwitterErrorResponse errorResponse = response.readEntity(TwitterErrorResponse.class);
        StringBuilder sb = new StringBuilder();
        for (TwitterError error : errorResponse.getErrors()) {
            sb.append("["+error.getCode()+": "+error.getMessage()+"] ");
        }
        return sb.toString();
    }

    @Override
    public List<OutputHandlerConfigOption> getConfigOptions() {
        List<OutputHandlerConfigOption> options = new ArrayList<>();
        options.add(new StringConfigOption("consumerKey", "Consumer Key", true, ""));
        options.add(new StringConfigOption("consumerSecret", "Consumer Secret", true, ""));
        options.add(new StringConfigOption("accessToken", "Access Token", true, ""));
        options.add(new StringConfigOption("accessTokenSecret", "Access Token Secret", true, ""));
        return options;
    }

    @Override
    public void checkConfig(Config config) throws InvalidConfigException {
        if (GenericValidator.isBlankOrNull(config.getString("consumerKey"))) {
            throw new FieldRequiredException("consumerKey");
        }
        if (GenericValidator.isBlankOrNull(config.getString("consumerSecret"))) {
            throw new FieldRequiredException("consumerSecret");
        }
        if (GenericValidator.isBlankOrNull(config.getString("accessToken"))) {
            throw new FieldRequiredException("accessToken");
        }
        if (GenericValidator.isBlankOrNull(config.getString("accessTokenSecret"))) {
            throw new FieldRequiredException("accessTokenSecret");
        }
    }

    @Override
    public void checkRecipientAddress(Address address) throws InvalidAddessException {
    }
}
