package net.weweave.tubewarder.client;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.ConnectException;

/**
 * An implementation of {@link TubewarderClient} for sending messages to
 * a Tubewarder server via the REST API (/rs/send) by using JAX-RS (JEE application server required).
 */
public class TubewarderRestClient extends TubewarderClient {
    private final Client client;
    private final WebTarget target;

    public TubewarderRestClient(String uri, Client client) {
        super(uri);
        this.client = client;
        this.target = this.client.target(getUri() + "rs/send");
    }

    public TubewarderRestClient(String uri) {
        this(uri, ClientBuilder.newClient());
    }

    @Override
    public SendResponse send(SendRequest request) throws ConnectException {
        try {
            Response response = target.request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(request, MediaType.APPLICATION_JSON));
            if (response.getStatus() != Response.Status.OK.getStatusCode()) {
                throw new ConnectException("Got invalid HTTP status code " + response.getStatus());
            }
            return response.readEntity(SendResponse.class);
        } catch (WebApplicationException e) {
            throw new ConnectException("Could not connect to host ("+e.getMessage()+")");
        } catch (Exception e) {
            throw new ConnectException("Could not connect to host ("+e.getMessage()+")");
        }
    }
}
