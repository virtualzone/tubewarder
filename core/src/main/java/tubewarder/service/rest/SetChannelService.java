package tubewarder.service.rest;

import org.apache.commons.validator.GenericValidator;
import tubewarder.dao.AbstractOutputHandlerConfigurationDao;
import tubewarder.dao.ChannelDao;
import tubewarder.domain.AbstractOutputHandlerConfiguration;
import tubewarder.domain.Channel;
import tubewarder.domain.OutputHandler;
import tubewarder.exception.InvalidInputParametersException;
import tubewarder.exception.ObjectNotFoundException;
import tubewarder.service.model.ChannelModel;
import tubewarder.service.model.ErrorCode;
import tubewarder.service.request.SetChannelRequest;
import tubewarder.service.response.SetObjectRestResponse;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@RequestScoped
@Path("/channel/set")
public class SetChannelService extends AbstractSetObjectService<ChannelModel, Channel> {
    @Inject
    private ChannelDao channelDao;

    @Inject
    private AbstractOutputHandlerConfigurationDao abstractOutputHandlerConfigurationDao;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(JaxApplication.APPLICATION_JSON_UTF8)
    public SetObjectRestResponse action(SetChannelRequest request) {
        SetObjectRestResponse response = new SetObjectRestResponse();
        try {
            validateInputParameters(request.object);
            Channel object = createUpdateObject(request.object);
            response.id = object.getExposableId();
        } catch (InvalidInputParametersException e) {
            response.error = ErrorCode.INVALID_INPUT_PARAMETERS;
        } catch (ObjectNotFoundException e) {
            response.error = ErrorCode.OBJECT_LOOKUP_ERROR;
        }
        return response;
    }

    @Override
    protected void validateInputParameters(ChannelModel model) throws InvalidInputParametersException {
        if (GenericValidator.isBlankOrNull(model.name)) {
            throw new InvalidInputParametersException();
        }
    }

    @Override
    protected Channel createObject(ChannelModel model) throws ObjectNotFoundException {
        Channel object = new Channel();
        getObjectDao().store(object);
        return object;
    }

    @Override
    protected void updateObject(Channel object, ChannelModel model) throws ObjectNotFoundException {
        AbstractOutputHandlerConfiguration config = getAbstractOutputHandlerConfigurationDao().get(model.config.id);

        object.setName(model.name);
        object.setOutputHandler(OutputHandler.valueOf(model.outputHandler));
        object.setConfig(config);
        getObjectDao().update(object);
    }

    @Override
    protected ChannelDao getObjectDao() {
        return channelDao;
    }

    public AbstractOutputHandlerConfigurationDao getAbstractOutputHandlerConfigurationDao() {
        return abstractOutputHandlerConfigurationDao;
    }

    public void setAbstractOutputHandlerConfigurationDao(AbstractOutputHandlerConfigurationDao abstractOutputHandlerConfigurationDao) {
        this.abstractOutputHandlerConfigurationDao = abstractOutputHandlerConfigurationDao;
    }
}
