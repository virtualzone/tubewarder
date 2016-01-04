package tubewarder.service.rest;

import org.apache.commons.validator.GenericValidator;
import tubewarder.dao.TemplateDao;
import tubewarder.domain.Template;
import tubewarder.exception.InvalidInputParametersException;
import tubewarder.exception.ObjectNotFoundException;
import tubewarder.service.model.ErrorCode;
import tubewarder.service.model.TemplateModel;
import tubewarder.service.request.SetTemplateRequest;
import tubewarder.service.response.SetObjectRestResponse;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@RequestScoped
@Path("/template/set")
public class SetTemplateService extends AbstractSetObjectService<TemplateModel, Template> {
    @Inject
    private TemplateDao templateDao;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(JaxApplication.APPLICATION_JSON_UTF8)
    public SetObjectRestResponse action(SetTemplateRequest request) {
        SetObjectRestResponse response = new SetObjectRestResponse();
        try {
            validateInputParameters(request.object);
            Template object = createUpdateObject(request.object);
            response.id = object.getExposableId();
        } catch (InvalidInputParametersException e) {
            response.error = ErrorCode.INVALID_INPUT_PARAMETERS;
        } catch (ObjectNotFoundException e) {
            response.error = ErrorCode.OBJECT_LOOKUP_ERROR;
        }
        return response;
    }

    @Override
    protected void validateInputParameters(TemplateModel model) throws InvalidInputParametersException {
        if (GenericValidator.isBlankOrNull(model.name)) {
            throw new InvalidInputParametersException();
        }
    }

    @Override
    protected Template createObject(TemplateModel model) throws ObjectNotFoundException {
        Template object = new Template();
        getObjectDao().store(object);
        return object;
    }

    @Override
    protected void updateObject(Template object, TemplateModel model) throws ObjectNotFoundException {
        object.setName(model.name);
        getObjectDao().update(object);
    }

    @Override
    public TemplateDao getObjectDao() {
        return templateDao;
    }
}
