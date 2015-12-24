package eio.service.rest;

import eio.dao.SysoutOutputHandlerConfigurationDao;
import eio.domain.AbstractOutputHandlerConfiguration;
import eio.domain.SysoutOutputHandlerConfiguration;
import eio.exception.InvalidInputParametersException;
import eio.exception.ObjectNotFoundException;
import eio.service.model.*;

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
    
    public AbstractResponse action(SysoutOutputHandlerConfigurationModel model) {
        AbstractResponse response = new AbstractResponse();
        try {
            validateInputParameters(model);
            createUpdateObject(model);
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
