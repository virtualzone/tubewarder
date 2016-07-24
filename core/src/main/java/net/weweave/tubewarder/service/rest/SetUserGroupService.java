package net.weweave.tubewarder.service.rest;

import net.weweave.tubewarder.dao.UserGroupDao;
import net.weweave.tubewarder.domain.Session;
import net.weweave.tubewarder.domain.User;
import net.weweave.tubewarder.domain.UserGroup;
import net.weweave.tubewarder.exception.AuthRequiredException;
import net.weweave.tubewarder.exception.InvalidInputParametersException;
import net.weweave.tubewarder.exception.ObjectNotFoundException;
import net.weweave.tubewarder.exception.PermissionException;
import net.weweave.tubewarder.service.model.ErrorCode;
import net.weweave.tubewarder.service.model.UserGroupModel;
import net.weweave.tubewarder.service.request.SetUserGroupRequest;
import net.weweave.tubewarder.service.response.SetObjectRestResponse;
import org.apache.commons.validator.GenericValidator;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@RequestScoped
@Path("/group/set")
public class SetUserGroupService extends AbstractSetObjectService<UserGroupModel, UserGroup> {
    @Inject
    private UserGroupDao userGroupDao;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(JaxApplication.APPLICATION_JSON_UTF8)
    public SetObjectRestResponse action(SetUserGroupRequest request) {
        SetObjectRestResponse response = new SetObjectRestResponse();
        try {
            Session session = getSession(request.token);
            checkPermissions(session.getUser());
            validateInputParameters(request.object);
            UserGroup object = createUpdateObject(request.object);
            response.id = object.getExposableId();
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

    @Override
    protected void validateInputParameters(UserGroupModel model) throws InvalidInputParametersException {
        if (GenericValidator.isBlankOrNull(model.name)) {
            throw new InvalidInputParametersException("name", ErrorCode.FIELD_REQUIRED);
        }

        // Check if object is to be created, but name already exists
        if (GenericValidator.isBlankOrNull(model.id)) {
            try {
                getObjectDao().getByName(model.name);
                throw new InvalidInputParametersException("name", ErrorCode.FIELD_NAME_ALREADY_EXISTS);
            } catch (ObjectNotFoundException e) {
                // This is okay
            }
        }

        // Check if object is to be updated, but (new) name already exists
        if (!GenericValidator.isBlankOrNull(model.id)) {
            try {
                UserGroup group = getObjectDao().getByName(model.name);
                // Match found - okay if it's the object to be updated itself
                if (!model.id.equals(group.getExposableId())) {
                    throw new InvalidInputParametersException("name", ErrorCode.FIELD_NAME_ALREADY_EXISTS);
                }
            } catch (ObjectNotFoundException e) {
                // This is okay
            }
        }
    }

    @Override
    protected UserGroup createObject(UserGroupModel model) throws ObjectNotFoundException {
        UserGroup object = new UserGroup();
        object.setName(model.name.trim());
        getObjectDao().store(object);
        return object;
    }

    @Override
    protected void updateObject(UserGroup object, UserGroupModel model) throws ObjectNotFoundException {
        object.setName(model.name);
        getObjectDao().update(object);
    }

    @Override
    protected UserGroupDao getObjectDao() {
        return userGroupDao;
    }
}
