package net.weweave.tubewarder.service.rest;

import org.apache.commons.validator.GenericValidator;
import net.weweave.tubewarder.dao.ChannelTemplateDao;
import net.weweave.tubewarder.domain.ChannelTemplate;
import net.weweave.tubewarder.exception.InvalidInputParametersException;
import net.weweave.tubewarder.exception.ObjectNotFoundException;
import net.weweave.tubewarder.service.model.ErrorCode;
import net.weweave.tubewarder.service.request.AbstractIdRestRequest;
import net.weweave.tubewarder.service.response.AbstractResponse;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@RequestScoped
@Path("/channeltemplate/delete")
public class DeleteChannelTemplateService {
    @Inject
    private ChannelTemplateDao channelTemplateDao;

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
        ChannelTemplate channelTemplate = getChannelTemplateDao().get(id);
        getChannelTemplateDao().delete(channelTemplate);
    }

    public ChannelTemplateDao getChannelTemplateDao() {
        return channelTemplateDao;
    }

    public void setChannelTemplateDao(ChannelTemplateDao channelTemplateDao) {
        this.channelTemplateDao = channelTemplateDao;
    }
}
