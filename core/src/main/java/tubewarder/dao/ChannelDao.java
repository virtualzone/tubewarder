package tubewarder.dao;

import tubewarder.domain.Channel;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.TypedQuery;
import java.util.List;

@ApplicationScoped
public class ChannelDao extends AbstractDao<Channel> {
    public List<Channel> getAll() {
        TypedQuery<Channel> query = getEntityManager().createQuery("SELECT c FROM Channel c ORDER BY c.name", Channel.class);
        return query.getResultList();
    }
}
