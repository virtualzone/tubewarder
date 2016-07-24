package net.weweave.tubewarder.dao;

import net.weweave.tubewarder.domain.User;
import net.weweave.tubewarder.exception.ObjectNotFoundException;
import net.weweave.tubewarder.util.DbValueRetriever;

import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class UserDao extends AbstractDao<User> {
    public User getByUsername(String username) throws ObjectNotFoundException {
        TypedQuery<User> query = getEntityManager().createQuery("SELECT u FROM User u " +
                "WHERE u.username = :username AND u.enabled = TRUE", User.class);
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
                "WHERE u.allowSystemConfig = TRUE AND u.enabled = TRUE", User.class);
        query.setMaxResults(1);
        try {
            DbValueRetriever.getObjectOrException(query);
            return true;
        } catch (ObjectNotFoundException e) {
            return false;
        }
    }

    public Map<String, String> getUsersForAutocompleteQuery(String s) {
        Map<String, String> result = new LinkedHashMap<>();
        Query query = getEntityManager().createQuery("SELECT u.exposableId, CONCAT(u.username, ' (', u.displayName, ')') " +
                "FROM User u " +
                "WHERE LOWER(u.username) LIKE :s OR LOWER(u.displayName) LIKE :s " +
                "ORDER BY u.username ASC");
        query.setParameter("s", "%"+s.trim().toLowerCase()+"%");
        @SuppressWarnings("unchecked") List<Object[]> list = query.getResultList();
        for (Object[] o : list) {
            result.put((String)o[0], (String)o[1]);
        }
        return result;
    }
}
