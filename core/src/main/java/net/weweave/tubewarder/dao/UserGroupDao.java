package net.weweave.tubewarder.dao;

import net.weweave.tubewarder.domain.User;
import net.weweave.tubewarder.domain.UserGroup;
import net.weweave.tubewarder.exception.ObjectNotFoundException;
import net.weweave.tubewarder.util.DbValueRetriever;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class UserGroupDao extends AbstractDao<UserGroup> {
    @Inject
    private UserDao userDao;

    @Override
    public void initObject(UserGroup obj) {
        if (obj != null) {
            getUserDao().initObject(obj.getMembers());
        }
    }

    public UserGroup getByName(String name) throws ObjectNotFoundException {
        TypedQuery<UserGroup> query = getEntityManager().createQuery("SELECT ug FROM UserGroup ug " +
                "WHERE ug.name = :name", UserGroup.class);
        query.setParameter("name", name);
        UserGroup result = (UserGroup) DbValueRetriever.getObjectOrException(query);
        initObject(result);
        return result;
    }

    public List<UserGroup> getAll() {
        TypedQuery<UserGroup> query = getEntityManager().createQuery("SELECT ug FROM UserGroup ug " +
                "ORDER BY ug.name ASC", UserGroup.class);
        List<UserGroup> result = query.getResultList();
        initObject(result);
        return result;
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
        group.getMembers().remove(user);
        update(group);
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
        List<UserGroup> result = query.getResultList();
        initObject(result);
        return result;
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

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
