package net.weweave.tubewarder.service.rest;

import net.weweave.tubewarder.exception.AuthRequiredException;
import net.weweave.tubewarder.service.model.ErrorCode;
import net.weweave.tubewarder.service.request.AbstractRestRequest;
import net.weweave.tubewarder.service.response.AbstractResponse;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@RequestScoped
@Path("/ping")
public class PingService extends AbstractService {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(JaxApplication.APPLICATION_JSON_UTF8)
    public AbstractResponse action(AbstractRestRequest request) {
        AbstractResponse response = new AbstractResponse();

        try {
            loadSession(request.token);
        } catch (Exception e) {
            response.error = ErrorCode.INVALID_INPUT_PARAMETERS;
        }

        return response;
    }

    private void loadSession(String token) throws AuthRequiredException {
        getSession(token);
    }
}
