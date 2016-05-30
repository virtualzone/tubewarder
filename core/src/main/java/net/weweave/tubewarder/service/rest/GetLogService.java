package net.weweave.tubewarder.service.rest;

import net.weweave.tubewarder.dao.LogDao;
import net.weweave.tubewarder.domain.Log;
import net.weweave.tubewarder.domain.Session;
import net.weweave.tubewarder.domain.User;
import net.weweave.tubewarder.exception.AuthRequiredException;
import net.weweave.tubewarder.exception.InvalidInputParametersException;
import net.weweave.tubewarder.exception.ObjectNotFoundException;
import net.weweave.tubewarder.exception.PermissionException;
import net.weweave.tubewarder.service.model.ErrorCode;
import net.weweave.tubewarder.service.model.LogModel;
import net.weweave.tubewarder.service.response.GetLogResponse;
import net.weweave.tubewarder.util.DateTimeFormat;
import org.apache.commons.validator.GenericValidator;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import java.util.Date;
import java.util.List;

@RequestScoped
@Path("/log/get")
public class GetLogService extends AbstractService {
    @Inject
    private LogDao logDao;

    @GET
    @Produces(JaxApplication.APPLICATION_JSON_UTF8)
    public GetLogResponse action(@QueryParam("token") @DefaultValue("") String token,
                                 @QueryParam("id") @DefaultValue("") String id,
                                 @QueryParam("startDate") @DefaultValue("") String startDate,
                                 @QueryParam("endDate") @DefaultValue("") String endDate,
                                 @QueryParam("keyword") @DefaultValue("") String keyword,
                                 @QueryParam("searchString") @DefaultValue("") String searchString,
                                 @QueryParam("firstResult") @DefaultValue("0") Integer firstResult,
                                 @QueryParam("maxResults") @DefaultValue("1000") Integer maxResults) {
        GetLogResponse response = new GetLogResponse();

        try {
            Session session = getSession(token);
            checkPermissions(session.getUser());
            validateInputParameters(id, startDate, endDate, firstResult, maxResults);
            setResponseList(session.getUser(), response, id, DateTimeFormat.parse(startDate), DateTimeFormat.parse(endDate), keyword, searchString, firstResult, maxResults);
        } catch (ObjectNotFoundException e) {
            response.error = ErrorCode.OBJECT_LOOKUP_ERROR;
        } catch (PermissionException e) {
            response.error = ErrorCode.PERMISSION_DENIED;
        } catch (AuthRequiredException e) {
            response.error = ErrorCode.AUTH_REQUIRED;
        } catch (InvalidInputParametersException e) {
            response.error = ErrorCode.INVALID_INPUT_PARAMETERS;
        }

        return response;
    }

    private void checkPermissions(User user) throws PermissionException {
        if (user == null ||
                !user.getAllowLogs()) {
            throw new PermissionException();
        }
    }

    protected void validateInputParameters(String id,
                                           String startDate,
                                           String endDate,
                                           Integer firstResult,
                                           Integer maxResults)
            throws InvalidInputParametersException {
        if (!GenericValidator.isBlankOrNull(id)) {
            return;
        }
        if (GenericValidator.isBlankOrNull(startDate) || GenericValidator.isBlankOrNull(endDate)) {
            throw new InvalidInputParametersException();
        }
        if (firstResult < 0) {
            throw new InvalidInputParametersException();
        }
        if (maxResults < 1) {
            throw new InvalidInputParametersException();
        }
        if (!DateTimeFormat.isDateTime(startDate) || !DateTimeFormat.isDateTime(endDate)) {
            throw new InvalidInputParametersException();
        }
    }

    private void setResponseList(
            User user,
            GetLogResponse response,
            String id,
            Date startDate,
            Date endDate,
            String keyword,
            String searchString,
            Integer firstResult,
            Integer maxResults) throws ObjectNotFoundException {
        if (GenericValidator.isBlankOrNull(id)) {
            List<Log> logs = getLogDao().getLogs(user, startDate, endDate, keyword, searchString, firstResult, maxResults);
            for (Log log : logs) {
                response.logs.add(LogModel.factory(log, false));
            }
        } else {
            Log log = getLogDao().get(id);
            response.logs.add(LogModel.factory(log, true));
        }
    }

    public LogDao getLogDao() {
        return logDao;
    }

    public void setLogDao(LogDao logDao) {
        this.logDao = logDao;
    }
}
