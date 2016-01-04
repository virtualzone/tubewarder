package tubewarder.service.rest;

import org.apache.commons.validator.GenericValidator;
import tubewarder.dao.TemplateDao;
import tubewarder.domain.Template;
import tubewarder.exception.InvalidInputParametersException;
import tubewarder.exception.ObjectNotFoundException;
import tubewarder.service.model.ErrorCode;
import tubewarder.service.request.AbstractIdRestRequest;
import tubewarder.service.response.AbstractResponse;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@RequestScoped
@Path("/template/delete")
public class DeleteTemplateService {
    @Inject
    private TemplateDao templateDao;

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
        Template template = getTemplateDao().get(id);
        getTemplateDao().delete(template);
    }

    public TemplateDao getTemplateDao() {
        return templateDao;
    }

    public void setTemplateDao(TemplateDao templateDao) {
        this.templateDao = templateDao;
    }
}
