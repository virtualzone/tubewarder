package tubewarder.dao;

import tubewarder.domain.Template;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.TypedQuery;
import java.util.List;

@ApplicationScoped
public class TemplateDao extends AbstractDao<Template> {
    public List<Template> getAll() {
        TypedQuery<Template> query = getEntityManager().createQuery("SELECT t FROM Template t ORDER BY t.name", Template.class);
        return query.getResultList();
    }
}
