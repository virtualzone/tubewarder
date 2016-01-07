package net.weweave.tubewarder.util;

import net.weweave.tubewarder.exception.ObjectNotFoundException;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public class DbValueRetriever {
    public static Long getLongValueOrZero(TypedQuery<Long> query) {
        Long result = 0l;
        try {
            result = query.getSingleResult();
        } catch (NoResultException e) {
            result = 0l;
        } finally {
            if (result == null) {
                result = 0l;
            }
        }
        return result;
    }

    public static Integer getIntValueOrZero(TypedQuery<Integer> query) {
        Integer result = 0;
        try {
            result = query.getSingleResult();
        } catch (NoResultException e) {
            result = 0;
        } finally {
            if (result == null) {
                result = 0;
            }
        }
        return result;
    }

    public static Object getObjectOrException(Query query) throws ObjectNotFoundException {
        query.setMaxResults(1);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            throw new ObjectNotFoundException();
        }
    }
}
