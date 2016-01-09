package net.weweave.tubewarder.dao;

import net.weweave.tubewarder.domain.Session;
import net.weweave.tubewarder.exception.ObjectNotFoundException;
import org.apache.commons.validator.GenericValidator;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Stateless
public class SessionDao extends AbstractDao<Session> {
    public static final Integer TIMEOUT = 30;

    public Session getAndCleanup(String token) throws ObjectNotFoundException {
        if (GenericValidator.isBlankOrNull(token)) {
            throw new ObjectNotFoundException();
        }
        cleanup();
        Session session = get(token);
        session.setLastActionDate(new Date());
        update(session);
        return session;
    }

    public void cleanup() {
        Calendar c = new GregorianCalendar();
        c.add(Calendar.MINUTE, TIMEOUT * (-1));

        Query query = getEntityManager().createQuery("DELETE FROM Session s WHERE s.lastActionDate < :date");
        query.setParameter("date", c.getTime());
        query.executeUpdate();
    }
}
