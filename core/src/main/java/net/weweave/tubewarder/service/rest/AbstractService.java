package net.weweave.tubewarder.service.rest;

import net.weweave.tubewarder.dao.SessionDao;
import net.weweave.tubewarder.domain.Session;
import net.weweave.tubewarder.exception.AuthRequiredException;
import net.weweave.tubewarder.exception.ObjectNotFoundException;

import javax.inject.Inject;

public abstract class AbstractService {
    @Inject
    private SessionDao sessionDao;

    public Session getSession(String token) throws AuthRequiredException {
        try {
            return getSessionDao().getAndCleanup(token);
        } catch (ObjectNotFoundException e) {
            throw new AuthRequiredException();
        }
    }

    public SessionDao getSessionDao() {
        return sessionDao;
    }

    public void setSessionDao(SessionDao sessionDao) {
        this.sessionDao = sessionDao;
    }
}
