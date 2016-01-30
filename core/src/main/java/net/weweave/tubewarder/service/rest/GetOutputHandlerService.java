package net.weweave.tubewarder.service.rest;

import net.weweave.tubewarder.domain.Session;
import net.weweave.tubewarder.domain.User;
import net.weweave.tubewarder.exception.AuthRequiredException;
import net.weweave.tubewarder.exception.ObjectNotFoundException;
import net.weweave.tubewarder.exception.PermissionException;
import net.weweave.tubewarder.outputhandler.api.IOutputHandler;
import net.weweave.tubewarder.outputhandler.OutputHandlerFactory;
import net.weweave.tubewarder.service.model.ErrorCode;
import net.weweave.tubewarder.service.model.OutputHandlerModel;
import net.weweave.tubewarder.service.response.GetOutputHandlerResponse;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RequestScoped
@Path("/outputhandler/get")
public class GetOutputHandlerService extends AbstractService {
    @Inject
    private OutputHandlerFactory outputHandlerFactory;

    @GET
    @Produces(JaxApplication.APPLICATION_JSON_UTF8)
    public GetOutputHandlerResponse action(@QueryParam("token") @DefaultValue("") String token) {
        GetOutputHandlerResponse response = new GetOutputHandlerResponse();

        try {
            Session session = getSession(token);
            checkPermissions(session.getUser());
            setResponseList(response);
        } catch (ObjectNotFoundException e) {
            response.error = ErrorCode.OBJECT_LOOKUP_ERROR;
        } catch (PermissionException e) {
            response.error = ErrorCode.PERMISSION_DENIED;
        } catch (AuthRequiredException e) {
            response.error = ErrorCode.AUTH_REQUIRED;
        }

        return response;
    }

    private void checkPermissions(User user) throws PermissionException {
        if (user == null ||
                !user.getAllowChannels()) {
            throw new PermissionException();
        }
    }

    private void setResponseList(GetOutputHandlerResponse response) throws ObjectNotFoundException {
        Set<String> handlers = getOutputHandlerFactory().getOutputHandlerIds();
        Map<String, Object> config = new HashMap<>();
        for (String id : handlers) {
            config.put("id", id);
            IOutputHandler handler = getOutputHandlerFactory().getOutputHandler(config);
            response.outputHandlers.add(OutputHandlerModel.factory(handler));
        }
    }

    public OutputHandlerFactory getOutputHandlerFactory() {
        return outputHandlerFactory;
    }

    public void setOutputHandlerFactory(OutputHandlerFactory outputHandlerFactory) {
        this.outputHandlerFactory = outputHandlerFactory;
    }
}
