package net.weweave.tubewarder.service.rest;

import net.weweave.tubewarder.domain.Session;
import net.weweave.tubewarder.domain.User;
import net.weweave.tubewarder.exception.AuthRequiredException;
import net.weweave.tubewarder.exception.PermissionException;
import org.apache.commons.validator.GenericValidator;
import net.weweave.tubewarder.dao.ChannelTemplateDao;
import net.weweave.tubewarder.dao.TemplateDao;
import net.weweave.tubewarder.domain.ChannelTemplate;
import net.weweave.tubewarder.domain.Template;
import net.weweave.tubewarder.exception.ObjectNotFoundException;
import net.weweave.tubewarder.service.model.ChannelTemplateModel;
import net.weweave.tubewarder.service.model.ErrorCode;
import net.weweave.tubewarder.service.response.GetChannelTemplateResponse;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;

@RequestScoped
@Path("/channeltemplate/get")
public class GetChannelTemplateService extends AbstractService {
    @Inject
    private ChannelTemplateDao channelTemplateDao;

    @Inject
    private TemplateDao templateDao;

    @GET
    @Produces(JaxApplication.APPLICATION_JSON_UTF8)
    public GetChannelTemplateResponse action(@QueryParam("token") @DefaultValue("") String token,
                                             @QueryParam("id") @DefaultValue("") String id,
                                             @QueryParam("templateId") @DefaultValue("") String templateId) {
        GetChannelTemplateResponse response = new GetChannelTemplateResponse();

        try {
            Session session = getSession(token);
            checkPermissions(session.getUser());
            setResponseList(response, id, templateId);
        } catch (ObjectNotFoundException e) {
            response.error = ErrorCode.INVALID_INPUT_PARAMETERS;
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

    private void setResponseList(GetChannelTemplateResponse response, String id, String templateId) throws ObjectNotFoundException {
        if (!GenericValidator.isBlankOrNull(id)) {
            ChannelTemplate channelTemplate = getChannelTemplateDao().get(id);
            response.channelTemplates.add(ChannelTemplateModel.factory(channelTemplate));
        } else if (!GenericValidator.isBlankOrNull(templateId)) {
            Template template = getTemplateDao().get(templateId);
            for (ChannelTemplate channelTemplate : template.getChannelTemplates()) {
                response.channelTemplates.add(ChannelTemplateModel.factory(channelTemplate));
            }
        }
    }

    public ChannelTemplateDao getChannelTemplateDao() {
        return channelTemplateDao;
    }

    public void setChannelTemplateDao(ChannelTemplateDao channelTemplateDao) {
        this.channelTemplateDao = channelTemplateDao;
    }

    public TemplateDao getTemplateDao() {
        return templateDao;
    }

    public void setTemplateDao(TemplateDao templateDao) {
        this.templateDao = templateDao;
    }
}
