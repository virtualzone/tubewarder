package net.weweave.tubewarder.service.rest;

import net.weweave.tubewarder.dao.SendQueueItemDao;
import net.weweave.tubewarder.domain.Session;
import net.weweave.tubewarder.domain.User;
import net.weweave.tubewarder.exception.AuthRequiredException;
import net.weweave.tubewarder.exception.PermissionException;
import net.weweave.tubewarder.outputhandler.SendQueueScheduler;
import net.weweave.tubewarder.service.model.ErrorCode;
import net.weweave.tubewarder.service.model.QueueStatusModel;
import net.weweave.tubewarder.service.response.GetQueueStatusResponse;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import java.util.logging.Logger;

@RequestScoped
@Path("/queue/status")
public class GetQueueStatusService extends AbstractService {
    private static final Logger LOG = Logger.getLogger(GetQueueStatusService.class.getName());

    @Inject
    private SendQueueItemDao sendQueueItemDao;

    @Inject
    private SendQueueScheduler sendQueueScheduler;

    @GET
    @Produces(JaxApplication.APPLICATION_JSON_UTF8)
    public GetQueueStatusResponse action(@QueryParam("token") @DefaultValue("") String token) {
        GetQueueStatusResponse response = new GetQueueStatusResponse();

        try {
            Session session = getSession(token);
            checkPermissions(session.getUser());
            LOG.info("Compiling queue status for token = " + token);
            buildResponse(response);
        } catch (PermissionException e) {
            response.error = ErrorCode.PERMISSION_DENIED;
        } catch (AuthRequiredException e) {
            response.error = ErrorCode.AUTH_REQUIRED;
        }

        return response;
    }

    private void checkPermissions(User user) throws PermissionException {
        if (user == null ||
                !user.getAllowSystemConfig()) {
            throw new PermissionException();
        }
    }

    private void buildResponse(GetQueueStatusResponse response) {
        QueueStatusModel status = response.status;
        status.inDatabase = getSendQueueItemDao().getTotalCount().intValue();
        status.inQueue = getSendQueueScheduler().getNumItemsInQueue();
        status.currentThreads = getSendQueueScheduler().getCurrentThreads();
        status.retry = getSendQueueItemDao().getRetryCount().intValue();
    }

    public SendQueueItemDao getSendQueueItemDao() {
        return sendQueueItemDao;
    }

    public void setSendQueueItemDao(SendQueueItemDao sendQueueItemDao) {
        this.sendQueueItemDao = sendQueueItemDao;
    }

    public SendQueueScheduler getSendQueueScheduler() {
        return sendQueueScheduler;
    }

    public void setSendQueueScheduler(SendQueueScheduler sendQueueScheduler) {
        this.sendQueueScheduler = sendQueueScheduler;
    }
}
