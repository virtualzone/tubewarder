package net.weweave.tubewarder.service.rest;

import net.weweave.tubewarder.domain.Session;
import net.weweave.tubewarder.domain.User;
import net.weweave.tubewarder.exception.AuthRequiredException;
import net.weweave.tubewarder.exception.ObjectNotFoundException;
import net.weweave.tubewarder.exception.PermissionException;
import net.weweave.tubewarder.outputhandler.OutputHandlerFactory;
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
public class GetTemplateService extends AbstractService {
    @Inject
    private TemplateDao templateDao;

    @Inject
    private OutputHandlerFactory outputHandlerFactory;

    @GET
    @Produces(JaxApplication.APPLICATION_JSON_UTF8)
    public GetTemplateResponse action(@QueryParam("token") @DefaultValue("") String token,
                                      @QueryParam("id") @DefaultValue("") String id) {
        GetTemplateResponse response = new GetTemplateResponse();

        try {
            Session session = getSession(token);
            checkPermissions(session.getUser());
            setResponseList(response, id);
        } catch (ObjectNotFoundException e) {
            response.error = ErrorCode.OBJECT_LOOKUP_ERROR;
        } catch (PermissionException e) {
            response.error = ErrorCode.PERMISSION_DENIED;
        } catch (AuthRequiredException e) {
            response.error = ErrorCode.AUTH_REQUIRED;
        }

        return response;
    }

    private void checkPermissions(User user) throws PermissionException {
        if (user == null ||
                !user.getAllowTemplates()) {
            throw new PermissionException();
        }
    }

    private void setResponseList(GetTemplateResponse response, String id) throws ObjectNotFoundException {
        if (GenericValidator.isBlankOrNull(id)) {
            List<Template> templates = getTemplateDao().getAll();
            for (Template template : templates) {
                response.templates.add(TemplateModel.factory(template, getOutputHandlerFactory()));
            }
        } else {
            Template template = getTemplateDao().get(id);
            response.templates.add(TemplateModel.factory(template, getOutputHandlerFactory()));
        }
    }

    public TemplateDao getTemplateDao() {
        return templateDao;
    }

    public void setTemplateDao(TemplateDao templateDao) {
        this.templateDao = templateDao;
    }

    public OutputHandlerFactory getOutputHandlerFactory() {
        return outputHandlerFactory;
    }

    public void setOutputHandlerFactory(OutputHandlerFactory outputHandlerFactory) {
        this.outputHandlerFactory = outputHandlerFactory;
    }
}
