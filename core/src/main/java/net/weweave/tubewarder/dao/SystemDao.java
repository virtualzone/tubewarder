package net.weweave.tubewarder.dao;

import net.weweave.tubewarder.domain.System;
import net.weweave.tubewarder.exception.ObjectNotFoundException;
import net.weweave.tubewarder.util.DbValueRetriever;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@Stateless
public class SystemDao extends AbstractDao<System> {
    public boolean isMasterOfAliveSystems(Integer systemId, int minLastAliveSeconds) {
        List<System> alive = getAllAlive(minLastAliveSeconds);
        if (alive != null && alive.size() > 0 && alive.get(0).getSystemId().equals(systemId)) {
            return true;
        }
        return false;
    }

    public List<System> getAllDeadWithQueueItems(int minLastAliveSeconds) {
        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.SECOND, (-1)*minLastAliveSeconds);
        TypedQuery<System> query = getEntityManager().createQuery("SELECT s FROM System s " +
                "WHERE s.lastAlive <= :lastAlive AND " +
                    "s.systemId IN (SELECT i.systemId FROM SendQueueItem i GROUP BY i.systemId) " +
                "ORDER BY s.systemId ASC", System.class);
        query.setParameter("lastAlive", cal.getTime());
        return query.getResultList();
    }

    public List<System> getAllAlive(int minLastAliveSeconds) {
        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.SECOND, (-1)*minLastAliveSeconds);
        TypedQuery<System> query = getEntityManager().createQuery("SELECT s FROM System s " +
                "WHERE s.lastAlive > :lastAlive " +
                "ORDER BY s.systemId ASC", System.class);
        query.setParameter("lastAlive", cal.getTime());
        return query.getResultList();
    }

    public System getSystemById(Integer systemId) throws ObjectNotFoundException {
        TypedQuery<System> query = getEntityManager().createQuery("SELECT s FROM System s " +
                "WHERE s.systemId = :id", System.class);
        query.setParameter("id", systemId);
        return (System) DbValueRetriever.getObjectOrException(query);
    }

    public void updateAliveStatus(Integer systemId) {
        Date now = new Date();
        System system;
        try {
            system = getSystemById(systemId);
            system.setLastAlive(now);
            update(system);
        } catch (ObjectNotFoundException e) {
            system = new System();
            system.setSystemId(systemId);
            system.setFirstAlive(now);
            system.setLastAlive(now);
            store(system);
        }
    }
}
