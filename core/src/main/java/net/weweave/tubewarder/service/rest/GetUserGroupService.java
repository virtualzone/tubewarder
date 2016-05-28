package net.weweave.tubewarder.service.rest;

import net.weweave.tubewarder.dao.UserDao;
import net.weweave.tubewarder.dao.UserGroupDao;
import net.weweave.tubewarder.domain.Session;
import net.weweave.tubewarder.domain.User;
import net.weweave.tubewarder.domain.UserGroup;
import net.weweave.tubewarder.exception.AuthRequiredException;
import net.weweave.tubewarder.exception.ObjectNotFoundException;
import net.weweave.tubewarder.exception.PermissionException;
import net.weweave.tubewarder.service.model.ErrorCode;
import net.weweave.tubewarder.service.model.UserGroupModel;
import net.weweave.tubewarder.service.response.GetUserGroupResponse;
import org.apache.commons.validator.GenericValidator;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import java.util.List;

@RequestScoped
@Path("/group/get")
public class GetUserGroupService extends AbstractService {
    @Inject
    private UserGroupDao userGroupDao;

    @Inject
    private UserDao userDao;

    @GET
    @Produces(JaxApplication.APPLICATION_JSON_UTF8)
    public GetUserGroupResponse action(@QueryParam("token") @DefaultValue("") String token,
                                  @QueryParam("id") @DefaultValue("") String id,
                                  @QueryParam("userId") @DefaultValue("") String userId) {
        GetUserGroupResponse response = new GetUserGroupResponse();

        try {
            Session session = getSession(token);
            checkPermissions(session.getUser());
            setResponseList(response, id, userId);
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

    private void setResponseList(GetUserGroupResponse response, String id, String userId) throws ObjectNotFoundException {
        if (!GenericValidator.isBlankOrNull(id)) {
            UserGroup group = getUserGroupDao().get(id);
            response.groups.add(UserGroupModel.factory(group));
        } else if (!GenericValidator.isBlankOrNull(userId)) {
            User user = getUserDao().get(userId);
            List<UserGroup> groups = getUserGroupDao().getGroupMemberships(user);
            for (UserGroup group : groups) {
                response.groups.add(UserGroupModel.factory(group));
            }
        } else {
            List<UserGroup> groups = getUserGroupDao().getAll();
            for (UserGroup group : groups) {
                response.groups.add(UserGroupModel.factory(group));
            }
        }
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
