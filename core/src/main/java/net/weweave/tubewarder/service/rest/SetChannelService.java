package net.weweave.tubewarder.service.rest;

import net.weweave.tubewarder.dao.AbstractOutputHandlerConfigurationDao;
import net.weweave.tubewarder.dao.ChannelDao;
import net.weweave.tubewarder.domain.Channel;
import net.weweave.tubewarder.domain.OutputHandler;
import net.weweave.tubewarder.exception.InvalidInputParametersException;
import net.weweave.tubewarder.exception.ObjectNotFoundException;
import net.weweave.tubewarder.service.model.ChannelModel;
import net.weweave.tubewarder.service.model.ErrorCode;
import net.weweave.tubewarder.service.request.SetChannelRequest;
import net.weweave.tubewarder.service.response.SetObjectRestResponse;
import org.apache.commons.validator.GenericValidator;
import net.weweave.tubewarder.domain.AbstractOutputHandlerConfiguration;

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

        // Check if object is to be created, but name already exists
        if (GenericValidator.isBlankOrNull(model.id)) {
            try {
                getObjectDao().getByName(model.name);
                throw new InvalidInputParametersException();
            } catch (ObjectNotFoundException e) {
                // This is okay
            }
        }

        // Check if object is to be updated, but (new) name already exists
        if (!GenericValidator.isBlankOrNull(model.id)) {
            try {
                Channel channel = getObjectDao().getByName(model.name);
                // Match found - okay if it's the object to be updated itself
                if (!model.id.equals(channel.getExposableId())) {
                    throw new InvalidInputParametersException();
                }
            } catch (ObjectNotFoundException e) {
                // This is okay
            }
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
