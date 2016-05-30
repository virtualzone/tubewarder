package net.weweave.tubewarder.dao;

import net.weweave.tubewarder.domain.Channel;
import net.weweave.tubewarder.domain.User;
import net.weweave.tubewarder.exception.ObjectNotFoundException;
import net.weweave.tubewarder.util.DbValueRetriever;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
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

    public List<Long> getChannelIdsWithGroups(List<Long> groupIds) {
        TypedQuery<Long> query = getEntityManager().createQuery("SELECT c.id FROM Channel c " +
                "WHERE c.userGroup.id IN :groupIds", Long.class);
        query.setParameter("groupIds", groupIds);
        return query.getResultList();
    }

    public boolean canUserAcccessChannel(Channel channel, List<Long> userGroupMembershipIds) {
        if (channel == null ||
                channel.getUserGroup() == null ||
                userGroupMembershipIds.contains(channel.getUserGroup().getId())) {
            return true;
        }
        return false;
    }
}
