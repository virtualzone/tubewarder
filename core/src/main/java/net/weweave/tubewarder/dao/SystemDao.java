package net.weweave.tubewarder.dao;

import net.weweave.tubewarder.domain.System;
import net.weweave.tubewarder.exception.ObjectNotFoundException;
import net.weweave.tubewarder.util.DbValueRetriever;
import net.weweave.tubewarder.util.SystemIdentifier;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

@Stateless
public class SystemDao extends AbstractDao<System> {
    public System getNext() {
        Integer id = SystemIdentifier.getIdentifier();
        return getNext(id);
    }

    public System getNext(Integer systemId) {
        List<System> all = getAll();
        for (System system : all) {
            if (system.getSystemId() < systemId) {
                return system;
            }
        }
        return all.get(0);
    }

    public List<System> getAll() {
        TypedQuery<System> query = getEntityManager().createQuery("SELECT s FROM System s " +
                "ORDER BY s.systemId ASC", System.class);
        return query.getResultList();
    }

    public System getSystemById(Integer systemId) throws ObjectNotFoundException {
        TypedQuery<System> query = getEntityManager().createQuery("SELECT s FROM System s " +
                "WHERE s.systemId = :id", System.class);
        query.setParameter("id", systemId);
        return (System) DbValueRetriever.getObjectOrException(query);
    }

    public void updateAliveStatus() {
        Integer id = SystemIdentifier.getIdentifier();
        Date now = new Date();
        System system;
        try {
            system = getSystemById(id);
            system.setLastAlive(now);
            update(system);
        } catch (ObjectNotFoundException e) {
            system = new System();
            system.setSystemId(id);
            system.setFirstAlive(now);
            system.setLastAlive(now);
            store(system);
        }
    }
}
