package tubewarder.service.rest;

import org.apache.commons.validator.GenericValidator;
import tubewarder.dao.TemplateDao;
import tubewarder.domain.Template;
import tubewarder.exception.ObjectNotFoundException;
import tubewarder.service.model.ErrorCode;
import tubewarder.service.model.TemplateModel;
import tubewarder.service.response.GetTemplateResponse;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import java.util.List;

@RequestScoped
@Path("/template/get")
public class GetTemplateService {
    @Inject
    private TemplateDao templateDao;

    @GET
    @Produces(JaxApplication.APPLICATION_JSON_UTF8)
    public GetTemplateResponse action(@QueryParam("id") @DefaultValue("") String id) {
        GetTemplateResponse response = new GetTemplateResponse();

        try {
            setResponseList(response, id);
        } catch (Exception e) {
            response.error = ErrorCode.INVALID_INPUT_PARAMETERS;
        }

        return response;
    }

    private void setResponseList(GetTemplateResponse response, String id) throws ObjectNotFoundException {
        if (GenericValidator.isBlankOrNull(id)) {
            List<Template> templates = getTemplateDao().getAll();
            for (Template template : templates) {
                response.templates.add(TemplateModel.factory(template));
            }
        } else {
            Template template = getTemplateDao().get(id);
            response.templates.add(TemplateModel.factory(template));
        }
    }

    public TemplateDao getTemplateDao() {
        return templateDao;
    }

    public void setTemplateDao(TemplateDao templateDao) {
        this.templateDao = templateDao;
    }
}
