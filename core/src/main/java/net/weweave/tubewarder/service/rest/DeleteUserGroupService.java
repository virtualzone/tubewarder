package net.weweave.tubewarder.service.rest;

import net.weweave.tubewarder.dao.ChannelDao;
import net.weweave.tubewarder.dao.TemplateDao;
import net.weweave.tubewarder.dao.UserGroupDao;
import net.weweave.tubewarder.domain.Session;
import net.weweave.tubewarder.domain.User;
import net.weweave.tubewarder.domain.UserGroup;
import net.weweave.tubewarder.exception.AuthRequiredException;
import net.weweave.tubewarder.exception.InvalidInputParametersException;
import net.weweave.tubewarder.exception.ObjectNotFoundException;
import net.weweave.tubewarder.exception.PermissionException;
import net.weweave.tubewarder.service.model.ErrorCode;
import net.weweave.tubewarder.service.request.AbstractIdRestRequest;
import net.weweave.tubewarder.service.response.AbstractResponse;
import org.apache.commons.validator.GenericValidator;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Collections;

@RequestScoped
@Path("/group/delete")
public class DeleteUserGroupService extends AbstractService {
    @Inject
    private UserGroupDao userGroupDao;

    @Inject
    private TemplateDao templateDao;

    @Inject
    private ChannelDao channelDao;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(JaxApplication.APPLICATION_JSON_UTF8)
    public AbstractResponse action(AbstractIdRestRequest request) {
        AbstractResponse response = new AbstractResponse();
        try {
            Session session = getSession(request.token);
            checkPermissions(session.getUser());
            validateInputParameters(request);
            deleteObject(request.id);
        } catch (InvalidInputParametersException e) {
            addErrorsToResponse(response, e);
        } catch (ObjectNotFoundException e) {
            response.error = ErrorCode.OBJECT_LOOKUP_ERROR;
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

    private void validateInputParameters(AbstractIdRestRequest request) throws InvalidInputParametersException {
        if (GenericValidator.isBlankOrNull(request.id)) {
            throw new InvalidInputParametersException("id", ErrorCode.FIELD_REQUIRED);
        }
    }

    private void deleteObject(String id) throws ObjectNotFoundException, InvalidInputParametersException {
        UserGroup group = getUserGroupDao().get(id);
        if (getChannelDao().getChannelIdsWithGroups(Collections.singletonList(group.getId())).size() > 0) {
            throw new InvalidInputParametersException();
        }
        if (getTemplateDao().getTemplateIdsWithGroups(Collections.singletonList(group.getId())).size() > 0) {
            throw new InvalidInputParametersException();
        }
        getUserGroupDao().delete(group);
    }

    public UserGroupDao getUserGroupDao() {
        return userGroupDao;
    }

    public void setUserGroupDao(UserGroupDao userGroupDao) {
        this.userGroupDao = userGroupDao;
    }

    public TemplateDao getTemplateDao() {
        return templateDao;
    }

    public void setTemplateDao(TemplateDao templateDao) {
        this.templateDao = templateDao;
    }

    public ChannelDao getChannelDao() {
        return channelDao;
    }

    public void setChannelDao(ChannelDao channelDao) {
        this.channelDao = channelDao;
    }
}
