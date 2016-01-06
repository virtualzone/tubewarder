package tubewarder.service.rest;

import org.apache.commons.validator.GenericValidator;
import tubewarder.dao.ChannelTemplateDao;
import tubewarder.dao.TemplateDao;
import tubewarder.domain.ChannelTemplate;
import tubewarder.domain.Template;
import tubewarder.exception.ObjectNotFoundException;
import tubewarder.service.model.ChannelTemplateModel;
import tubewarder.service.model.ErrorCode;
import tubewarder.service.response.GetChannelTemplateResponse;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;

@RequestScoped
@Path("/channeltemplate/get")
public class GetChannelTemplateService {
    @Inject
    private ChannelTemplateDao channelTemplateDao;

    @Inject
    private TemplateDao templateDao;

    @GET
    @Produces(JaxApplication.APPLICATION_JSON_UTF8)
    public GetChannelTemplateResponse action(@QueryParam("id") @DefaultValue("") String id,
                                             @QueryParam("templateId") @DefaultValue("") String templateId) {
        GetChannelTemplateResponse response = new GetChannelTemplateResponse();

        try {
            setResponseList(response, id, templateId);
        } catch (Exception e) {
            response.error = ErrorCode.INVALID_INPUT_PARAMETERS;
        }

        return response;
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
