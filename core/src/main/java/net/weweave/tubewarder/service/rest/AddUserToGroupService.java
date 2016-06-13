package net.weweave.tubewarder.service.rest;

import net.weweave.tubewarder.dao.UserDao;
import net.weweave.tubewarder.dao.UserGroupDao;
import net.weweave.tubewarder.domain.Session;
import net.weweave.tubewarder.domain.User;
import net.weweave.tubewarder.domain.UserGroup;
import net.weweave.tubewarder.exception.AuthRequiredException;
import net.weweave.tubewarder.exception.InvalidInputParametersException;
import net.weweave.tubewarder.exception.ObjectNotFoundException;
import net.weweave.tubewarder.exception.PermissionException;
import net.weweave.tubewarder.service.model.ErrorCode;
import net.weweave.tubewarder.service.request.UserGroupIdRestRequest;
import net.weweave.tubewarder.service.response.AbstractResponse;
import org.apache.commons.validator.GenericValidator;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@RequestScoped
@Path("/group/adduser")
public class AddUserToGroupService extends AbstractService {
    @Inject
    private UserGroupDao userGroupDao;

    @Inject
    private UserDao userDao;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(JaxApplication.APPLICATION_JSON_UTF8)
    public AbstractResponse action(UserGroupIdRestRequest request) {
        AbstractResponse response = new AbstractResponse();
        try {
            Session session = getSession(request.token);
            checkPermissions(session.getUser());
            validateInputParameters(request);
            addUserToGroup(request.userId, request.groupId);
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

    private void validateInputParameters(UserGroupIdRestRequest request) throws InvalidInputParametersException {
        if (GenericValidator.isBlankOrNull(request.userId)) {
            throw new InvalidInputParametersException("userId", ErrorCode.FIELD_REQUIRED);
        }
        if (GenericValidator.isBlankOrNull(request.groupId)) {
            throw new InvalidInputParametersException("groupId", ErrorCode.FIELD_REQUIRED);
        }
    }

    private void addUserToGroup(String userId, String groupId) throws ObjectNotFoundException {
        UserGroup group = getUserGroupDao().get(groupId);
        User user = getUserDao().get(userId);
        getUserGroupDao().addUserToGroup(user, group);
    }

    public UserGroupDao getUserGroupDao() {
        return userGroupDao;
    }

    public void setUserGroupDao(UserGroupDao userGroupDao) {
        this.userGroupDao = userGroupDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
