package net.weweave.tubewarder.outputhandler;

import net.weweave.tubewarder.outputhandler.api.*;
import net.weweave.tubewarder.outputhandler.api.configoption.OutputHandlerConfigOption;
import net.weweave.tubewarder.outputhandler.api.configoption.StringConfigOption;
import org.apache.commons.validator.GenericValidator;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@OutputHandler(id="FACEBOOK", name="Facebook")
public class FacebookOutputHandler implements IOutputHandler {
    @Override
    public void process(Config config, SendItem sendItem) throws TemporaryProcessingException, PermanentProcessingException {
        FacebookMessageRequest req = createRequest(sendItem);
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("https://graph.facebook.com/v2.6/me/messages?access_token="+config.getString("accessToken"));
        Response response = target.request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(req, MediaType.APPLICATION_JSON));
        handleResponse(response);
    }

    private FacebookMessageRequest createRequest(SendItem item) {
        FacebookRecipient recipient = new FacebookRecipient(item.getRecipient().getAddress());
        FacebookMessage message = new FacebookMessage(item.getContent());
        FacebookMessageRequest req = new FacebookMessageRequest(recipient, message);
        return req;
    }

    private void handleResponse(Response response) throws TemporaryProcessingException, PermanentProcessingException {
        if (response.getStatus() != Response.Status.OK.getStatusCode()) {
            FacebookMessageResponse resp = null;
            try {
                resp = response.readEntity(FacebookMessageResponse.class);
            } catch (Exception e) {
                throw new TemporaryProcessingException("Got invalid HTTP status code " + response.getStatus());
            }
            handleError(resp);
        }
    }

    private void handleError(FacebookMessageResponse resp) throws TemporaryProcessingException, PermanentProcessingException {
        if (resp.getError() != null && resp.getError().getCode() != null) {
            if (resp.getError().getCode() == 1200) {
                throw new TemporaryProcessingException("Internal Facebook Error");
            } else {
                throw new PermanentProcessingException("Facebook Error Code = " + resp.getError().getCode());
            }
        }
    }

    @Override
    public List<OutputHandlerConfigOption> getConfigOptions() {
        List<OutputHandlerConfigOption> options = new ArrayList<>();
        options.add(new StringConfigOption("accessToken", "Access Token", true, ""));
        return options;
    }

    @Override
    public void checkConfig(Config config) throws InvalidConfigException {
        if (GenericValidator.isBlankOrNull(config.getString("accessToken"))) {
            throw new FieldRequiredException("accessToken");
        }
    }

    @Override
    public void checkRecipientAddress(Address address) throws InvalidAddessException {
    }
}
