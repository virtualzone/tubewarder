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
                Template template = getObjectDao().getByName(model.name);
                // Match found - okay if it's the object to be updated itself
                if (!model.id.equals(template.getExposableId())) {
                    throw new InvalidInputParametersException();
                }
            } catch (ObjectNotFoundException e) {
                // This is okay
            }
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
