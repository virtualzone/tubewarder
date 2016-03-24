package net.weweave.tubewarder.dao;

import net.weweave.tubewarder.domain.User;
import net.weweave.tubewarder.exception.ObjectNotFoundException;
import net.weweave.tubewarder.util.DbValueRetriever;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class UserDao extends AbstractDao<User> {
    public User getByUsername(String username) throws ObjectNotFoundException {
        TypedQuery<User> query = getEntityManager().createQuery("SELECT u FROM User u " +
                "WHERE u.username = :username AND u.enabled = 1", User.class);
        query.setParameter("username", username);
        return (User) DbValueRetriever.getObjectOrException(query);
    }

    public List<User> getAll() {
        TypedQuery<User> query = getEntityManager().createQuery("SELECT u FROM User u " +
                "ORDER BY u.username ASC", User.class);
        return query.getResultList();
    }

    public Boolean existsAnyAdminUser() {
        TypedQuery<User> query = getEntityManager().createQuery("SELECT u FROM User u " +
                "WHERE u.allowSystemConfig = 1 AND u.enabled = 1", User.class);
        query.setMaxResults(1);
        try {
            DbValueRetriever.getObjectOrException(query);
            return true;
        } catch (ObjectNotFoundException e) {
            return false;
        }
    }
}
