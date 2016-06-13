package net.weweave.tubewarder.service.rest;

import net.weweave.tubewarder.dao.SessionDao;
import net.weweave.tubewarder.domain.Session;
import net.weweave.tubewarder.exception.AuthRequiredException;
import net.weweave.tubewarder.exception.InvalidInputParametersException;
import net.weweave.tubewarder.exception.ObjectNotFoundException;
import net.weweave.tubewarder.service.model.ErrorCode;
import net.weweave.tubewarder.service.response.AbstractResponse;
import org.apache.commons.validator.GenericValidator;

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

    protected void addErrorsToResponse(AbstractResponse response, InvalidInputParametersException e) {
        response.error = ErrorCode.INVALID_INPUT_PARAMETERS;
        if (!GenericValidator.isBlankOrNull(e.getField())) {
            response.addFieldError(e.getField(), e.getErrorCode());
        }
    }

    public SessionDao getSessionDao() {
        return sessionDao;
    }

    public void setSessionDao(SessionDao sessionDao) {
        this.sessionDao = sessionDao;
    }
}
