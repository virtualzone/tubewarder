package net.weweave.tubewarder.service.rest;

import net.weweave.tubewarder.exception.ObjectNotFoundException;
import net.weweave.tubewarder.service.response.GetTemplateResponse;
import org.apache.commons.validator.GenericValidator;
import net.weweave.tubewarder.dao.TemplateDao;
import net.weweave.tubewarder.domain.Template;
import net.weweave.tubewarder.service.model.ErrorCode;
import net.weweave.tubewarder.service.model.TemplateModel;

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
