package tubewarder.dao;

import tubewarder.domain.Channel;
import tubewarder.exception.ObjectNotFoundException;
import tubewarder.util.DbValueRetriever;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.TypedQuery;
import java.util.List;

@ApplicationScoped
public class ChannelDao extends AbstractDao<Channel> {
    public Channel getByName(String name) throws ObjectNotFoundException {
        TypedQuery<Channel> query = getEntityManager().createQuery("SELECT c FROM Channel c WHERE c.name = :name", Channel.class);
        query.setParameter("name", name);
        query.setMaxResults(1);
        return (Channel) DbValueRetriever.getObjectOrException(query);
    }

    public List<Channel> getAll() {
        TypedQuery<Channel> query = getEntityManager().createQuery("SELECT c FROM Channel c ORDER BY c.name", Channel.class);
        return query.getResultList();
    }
}
