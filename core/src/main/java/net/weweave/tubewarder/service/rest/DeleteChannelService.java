package net.weweave.tubewarder.service.rest;

import net.weweave.tubewarder.exception.InvalidInputParametersException;
import net.weweave.tubewarder.exception.ObjectNotFoundException;
import net.weweave.tubewarder.service.response.AbstractResponse;
import org.apache.commons.validator.GenericValidator;
import net.weweave.tubewarder.dao.ChannelDao;
import net.weweave.tubewarder.domain.Channel;
import net.weweave.tubewarder.service.model.ErrorCode;
import net.weweave.tubewarder.service.request.AbstractIdRestRequest;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@RequestScoped
@Path("/channel/delete")
public class DeleteChannelService {
    @Inject
    private ChannelDao channelDao;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(JaxApplication.APPLICATION_JSON_UTF8)
    public AbstractResponse action(AbstractIdRestRequest request) {
        AbstractResponse response = new AbstractResponse();
        try {
            validateInputParameters(request);
            deleteObject(request.id);
        } catch (InvalidInputParametersException e) {
            response.error = ErrorCode.INVALID_INPUT_PARAMETERS;
        } catch (ObjectNotFoundException e) {
            response.error = ErrorCode.OBJECT_LOOKUP_ERROR;
        }
        return response;
    }

    private void validateInputParameters(AbstractIdRestRequest request) throws InvalidInputParametersException {
        if (GenericValidator.isBlankOrNull(request.id)) {
            throw new InvalidInputParametersException();
        }
    }

    private void deleteObject(String id) throws ObjectNotFoundException {
        Channel channel = getChannelDao().get(id);
        getChannelDao().delete(channel);
    }

    public ChannelDao getChannelDao() {
        return channelDao;
    }

    public void setChannelDao(ChannelDao channelDao) {
        this.channelDao = channelDao;
    }
}
