package net.weweave.tubewarder.dao;

import net.weweave.tubewarder.domain.User;
import net.weweave.tubewarder.domain.UserGroup;
import net.weweave.tubewarder.exception.ObjectNotFoundException;
import net.weweave.tubewarder.util.DbValueRetriever;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class UserGroupDao extends AbstractDao<UserGroup> {
    public UserGroup getByName(String name) throws ObjectNotFoundException {
        TypedQuery<UserGroup> query = getEntityManager().createQuery("SELECT ug FROM UserGroup ug " +
                "WHERE ug.name = :name", UserGroup.class);
        query.setParameter("name", name);
        return (UserGroup) DbValueRetriever.getObjectOrException(query);
    }

    public List<UserGroup> getAll() {
        TypedQuery<UserGroup> query = getEntityManager().createQuery("SELECT ug FROM UserGroup ug " +
                "ORDER BY ug.name ASC", UserGroup.class);
        return query.getResultList();
    }

    public void addUserToGroup(User user, UserGroup group) {
        if (!isUserMemberOfGroup(user, group)) {
            try {
                group = get(group.getId());
            } catch (ObjectNotFoundException e) {
                // Should never happen
            }
            group.getMembers().add(user);
            update(group);
        }
    }

    public void removeUserFromGroup(User user, UserGroup group) {
        try {
            group = get(group.getId());
        } catch (ObjectNotFoundException e) {
            // Should never happen
        }
        for (int i=0; i<group.getMembers().size(); i++) {
            User current = group.getMembers().get(i);
            if (current != null && current.getId().equals(user.getId())) {
                group.getMembers().remove(i);
                update(group);
                return;
            }
        }
    }

    public boolean isUserMemberOfGroup(User user, UserGroup group) {
        for (User current : group.getMembers()) {
            if (current != null && current.getId().equals(user.getId())) {
                return true;
            }
        }
        return false;
    }

    public List<UserGroup> getGroupMemberships(User user) {
        TypedQuery<UserGroup> query = getEntityManager().createQuery("SELECT ug FROM UserGroup ug, User u " +
                "WHERE u MEMBER OF ug.members AND u.id = :userId " +
                "ORDER BY ug.name ASC", UserGroup.class);
        query.setParameter("userId", user.getId());
        return query.getResultList();
    }

    public List<Long> getGroupMembershipIds(User user) {
        TypedQuery<Long> query = getEntityManager().createQuery("SELECT ug.id FROM UserGroup ug, User u " +
                "WHERE u MEMBER OF ug.members AND u.id = :userId " +
                "ORDER BY ug.name ASC", Long.class);
        query.setParameter("userId", user.getId());
        return query.getResultList();
    }

    public Boolean existsAnyGroup() {
        TypedQuery<UserGroup> query = getEntityManager().createQuery("SELECT ug FROM UserGroup ug", UserGroup.class);
        query.setMaxResults(1);
        try {
            DbValueRetriever.getObjectOrException(query);
            return true;
        } catch (ObjectNotFoundException e) {
            return false;
        }
    }
}
