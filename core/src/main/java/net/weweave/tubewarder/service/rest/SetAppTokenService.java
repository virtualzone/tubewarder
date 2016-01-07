package net.weweave.tubewarder.service.rest;

import net.weweave.tubewarder.exception.ObjectNotFoundException;
import net.weweave.tubewarder.dao.AppTokenDao;
import net.weweave.tubewarder.domain.AppToken;
import net.weweave.tubewarder.exception.InvalidInputParametersException;
import net.weweave.tubewarder.service.model.AppTokenModel;
import net.weweave.tubewarder.service.model.ErrorCode;
import net.weweave.tubewarder.service.request.SetAppTokenRequest;
import net.weweave.tubewarder.service.response.SetObjectRestResponse;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@RequestScoped
@Path("/apptoken/set")
public class SetAppTokenService extends AbstractSetObjectService<AppTokenModel, AppToken> {
    @Inject
    private AppTokenDao appTokenDao;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(JaxApplication.APPLICATION_JSON_UTF8)
    public SetObjectRestResponse action(SetAppTokenRequest request) {
        SetObjectRestResponse response = new SetObjectRestResponse();
        try {
            validateInputParameters(request.object);
            AppToken object = createUpdateObject(request.object);
            response.id = object.getExposableId();
        } catch (InvalidInputParametersException e) {
            response.error = ErrorCode.INVALID_INPUT_PARAMETERS;
        } catch (ObjectNotFoundException e) {
            response.error = ErrorCode.OBJECT_LOOKUP_ERROR;
        }
        return response;
    }

    @Override
    protected void validateInputParameters(AppTokenModel model) throws InvalidInputParametersException {

    }

    @Override
    protected AppToken createObject(AppTokenModel model) throws ObjectNotFoundException {
        AppToken object = new AppToken();
        getObjectDao().store(object);
        return object;
    }

    @Override
    protected void updateObject(AppToken object, AppTokenModel model) throws ObjectNotFoundException {
        object.setName(model.name);
        getObjectDao().update(object);
    }

    @Override
    protected AppTokenDao getObjectDao() {
        return appTokenDao;
    }
}
