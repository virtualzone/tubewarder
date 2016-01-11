package net.weweave.tubewarder.service.rest;

import net.weweave.tubewarder.domain.Session;
import net.weweave.tubewarder.domain.SysoutOutputHandlerConfiguration;
import net.weweave.tubewarder.domain.User;
import net.weweave.tubewarder.exception.AuthRequiredException;
import net.weweave.tubewarder.exception.InvalidInputParametersException;
import net.weweave.tubewarder.exception.ObjectNotFoundException;
import net.weweave.tubewarder.exception.PermissionException;
import net.weweave.tubewarder.service.model.ErrorCode;
import net.weweave.tubewarder.service.model.SysoutOutputHandlerConfigurationModel;
import net.weweave.tubewarder.service.request.SetSysoutOutputHandlerConfigurationRequest;
import net.weweave.tubewarder.dao.SysoutOutputHandlerConfigurationDao;
import net.weweave.tubewarder.service.response.SetObjectRestResponse;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@RequestScoped
@Path("/sysoutoutputhandlerconfiguration/set")
public class SetSysoutOutputHandlerConfigurationService extends AbstractSetObjectService<SysoutOutputHandlerConfigurationModel, SysoutOutputHandlerConfiguration> {
    @Inject
    private SysoutOutputHandlerConfigurationDao outputHandlerConfigurationDao;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(JaxApplication.APPLICATION_JSON_UTF8)
    
    public SetObjectRestResponse action(SetSysoutOutputHandlerConfigurationRequest request) {
        SetObjectRestResponse response = new SetObjectRestResponse();
        try {
            Session session = getSession(request.token);
            checkPermissions(session.getUser());
            validateInputParameters(request.object);
            SysoutOutputHandlerConfiguration object = createUpdateObject(request.object);
            response.id = object.getExposableId();
        } catch (InvalidInputParametersException e) {
            response.error = ErrorCode.INVALID_INPUT_PARAMETERS;
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

    @Override
    protected void validateInputParameters(SysoutOutputHandlerConfigurationModel model) throws InvalidInputParametersException {

    }

    @Override
    protected SysoutOutputHandlerConfiguration createObject(SysoutOutputHandlerConfigurationModel model) throws ObjectNotFoundException {
        SysoutOutputHandlerConfiguration object = new SysoutOutputHandlerConfiguration();
        getObjectDao().store(object);
        return object;
    }

    @Override
    protected void updateObject(SysoutOutputHandlerConfiguration object, SysoutOutputHandlerConfigurationModel model) throws ObjectNotFoundException {
        object.setPrefix(model.prefix);
        object.setSuffix(model.suffix);
        getObjectDao().update(object);
    }

    @Override
    protected SysoutOutputHandlerConfigurationDao getObjectDao() {
        return outputHandlerConfigurationDao;
    }
}
