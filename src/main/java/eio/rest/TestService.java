package eio.rest;

import eio.dao.TemplateDao;
import eio.domain.Template;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@RequestScoped
@Path("/test")
public class TestService {
    @Inject
    private TemplateDao templateDao;

    @GET
    public String action() {
        Template template = new Template();
        getTemplateDao().store(template);
        template.setName("Hallo");
        getTemplateDao().update(template);

        return "Hallo Test!";
    }

    public TemplateDao getTemplateDao() {
        return templateDao;
    }

    public void setTemplateDao(TemplateDao templateDao) {
        this.templateDao = templateDao;
    }
}
