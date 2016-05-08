package net.weweave.tubewarder.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

public class TubewarderRestClient extends TubewarderClient {
    private final Client client;
    private final WebTarget target;

    public TubewarderRestClient(String uri) {
        super(uri);
        client = ClientBuilder.newClient();
        target = client.target(getUri() + "rs/send");
    }

    @Override
    public SendResponse send(SendRequest request) {
        return target.request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(request, MediaType.APPLICATION_JSON), SendResponse.class);
    }
}
