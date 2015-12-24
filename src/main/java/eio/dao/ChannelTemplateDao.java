package eio.dao;

import eio.domain.ChannelTemplate;
import eio.exception.ObjectNotFoundException;
import eio.util.DbValueRetriever;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.TypedQuery;

@ApplicationScoped
public class ChannelTemplateDao extends AbstractDao<ChannelTemplate> {
    public ChannelTemplate getChannelTemplate(String name, String channel) throws ObjectNotFoundException {
        TypedQuery<ChannelTemplate> query = getEntityManager().createQuery("SELECT ct FROM ChannelTemplate ct " +
                "WHERE ct.template.name = :name AND ct.channel.name = :channel", ChannelTemplate.class);
        query.setMaxResults(1);
        query.setParameter("name", name);
        query.setParameter("channel", channel);
        return (ChannelTemplate) DbValueRetriever.getObjectOrException(query);
    }
}
