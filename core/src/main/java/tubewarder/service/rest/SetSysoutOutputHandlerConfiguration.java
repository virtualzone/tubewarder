package tubewarder.service.rest;

import tubewarder.dao.SysoutOutputHandlerConfigurationDao;
import tubewarder.domain.SysoutOutputHandlerConfiguration;
import tubewarder.exception.InvalidInputParametersException;
import tubewarder.exception.ObjectNotFoundException;
import tubewarder.service.model.ErrorCode;
import tubewarder.service.request.SetSysoutOutputHandlerConfigurationRequest;
import tubewarder.service.response.SetObjectRestResponse;
import tubewarder.service.model.SysoutOutputHandlerConfigurationModel;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@RequestScoped
@Path("/sysoutoutputhandlerconfiguration/set")
public class SetSysoutOutputHandlerConfiguration extends AbstractSetObjectService<SysoutOutputHandlerConfigurationModel, SysoutOutputHandlerConfiguration> {
    @Inject
    private SysoutOutputHandlerConfigurationDao outputHandlerConfigurationDao;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(JaxApplication.APPLICATION_JSON_UTF8)
    
    public SetObjectRestResponse action(SetSysoutOutputHandlerConfigurationRequest request) {
        SetObjectRestResponse response = new SetObjectRestResponse();
        try {
            validateInputParameters(request.object);
            SysoutOutputHandlerConfiguration object = createUpdateObject(request.object);
            response.id = object.getExposableId();
        } catch (InvalidInputParametersException e) {
            response.error = ErrorCode.INVALID_INPUT_PARAMETERS;
        } catch (ObjectNotFoundException e) {
            response.error = ErrorCode.OBJECT_LOOKUP_ERROR;
        }
        return response;
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
