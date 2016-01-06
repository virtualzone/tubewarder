package tubewarder.dao;

import tubewarder.domain.Template;
import tubewarder.exception.ObjectNotFoundException;
import tubewarder.util.DbValueRetriever;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.TypedQuery;
import java.util.List;

@ApplicationScoped
public class TemplateDao extends AbstractDao<Template> {
    public Template getByName(String name) throws ObjectNotFoundException {
        TypedQuery<Template> query = getEntityManager().createQuery("SELECT t FROM Template t WHERE t.name = :name", Template.class);
        query.setParameter("name", name);
        query.setMaxResults(1);
        return (Template) DbValueRetriever.getObjectOrException(query);
    }

    public List<Template> getAll() {
        TypedQuery<Template> query = getEntityManager().createQuery("SELECT t FROM Template t ORDER BY t.name", Template.class);
        return query.getResultList();
    }
}
