package net.weweave.tubewarder.dao;

import net.weweave.tubewarder.domain.Template;
import net.weweave.tubewarder.exception.ObjectNotFoundException;
import net.weweave.tubewarder.util.DbValueRetriever;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

@Stateless
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

    public List<Long> getTemplateIdsWithGroups(List<Long> groupIds) {
        if (groupIds == null || groupIds.isEmpty()) {
            return new ArrayList<>();
        }
        TypedQuery<Long> query = getEntityManager().createQuery("SELECT t.id FROM Template t " +
                "WHERE t.userGroup.id IN :groupIds", Long.class);
        query.setParameter("groupIds", groupIds);
        return query.getResultList();
    }

    public boolean canUserAcccessTemplate(Template template, List<Long> userGroupMembershipIds) {
        return template == null ||
                template.getUserGroup() == null ||
                userGroupMembershipIds.contains(template.getUserGroup().getId());
    }
}
