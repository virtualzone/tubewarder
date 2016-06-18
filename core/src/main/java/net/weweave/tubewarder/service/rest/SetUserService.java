package net.weweave.tubewarder.service.rest;

import net.weweave.tubewarder.dao.UserDao;
import net.weweave.tubewarder.domain.Session;
import net.weweave.tubewarder.domain.User;
import net.weweave.tubewarder.exception.AuthRequiredException;
import net.weweave.tubewarder.exception.InvalidInputParametersException;
import net.weweave.tubewarder.exception.ObjectNotFoundException;
import net.weweave.tubewarder.exception.PermissionException;
import net.weweave.tubewarder.service.model.ErrorCode;
import net.weweave.tubewarder.service.model.UserModel;
import net.weweave.tubewarder.service.request.SetUserRequest;
import net.weweave.tubewarder.service.response.SetObjectRestResponse;
import net.weweave.tubewarder.util.PasswordPolicy;
import org.apache.commons.validator.GenericValidator;
import org.mindrot.jbcrypt.BCrypt;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@RequestScoped
@Path("/user/set")
public class SetUserService extends AbstractSetObjectService<UserModel, User> {
    @Inject
    private UserDao userDao;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(JaxApplication.APPLICATION_JSON_UTF8)
    public SetObjectRestResponse action(SetUserRequest request) {
        SetObjectRestResponse response = new SetObjectRestResponse();
        try {
            Session session = getSession(request.token);
            checkPermissions(session.getUser());
            validateInputParameters(request.object);
            User object = createUpdateObject(request.object);
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
    protected void validateInputParameters(UserModel model) throws InvalidInputParametersException {
        if (GenericValidator.isBlankOrNull(model.username)) {
            throw new InvalidInputParametersException("username", ErrorCode.FIELD_REQUIRED);
        }
        if (GenericValidator.isBlankOrNull(model.displayName)) {
            throw new InvalidInputParametersException("displayName", ErrorCode.FIELD_REQUIRED);
        }

        // Password is required if user is to be created
        if (GenericValidator.isBlankOrNull(model.id) && GenericValidator.isBlankOrNull(model.password)) {
            throw new InvalidInputParametersException("password", ErrorCode.FIELD_REQUIRED);
        }

        // Check if password matches policy
        if (!GenericValidator.isBlankOrNull(model.password) && !PasswordPolicy.matches(model.password)) {
            throw new InvalidInputParametersException("password", ErrorCode.FIELD_INVALID);
        }

        // Check if object is to be created, but username already exists
        if (GenericValidator.isBlankOrNull(model.id)) {
            try {
                getObjectDao().getByUsername(model.username);
                throw new InvalidInputParametersException("username", ErrorCode.FIELD_NAME_ALREADY_EXISTS);
            } catch (ObjectNotFoundException e) {
                // This is okay
            }
        }

        // Check if object is to be updated, but (new) username already exists
        if (!GenericValidator.isBlankOrNull(model.id)) {
            try {
                User user = getObjectDao().getByUsername(model.username);
                // Match found - okay if it's the object to be updated itself
                if (!model.id.equals(user.getExposableId())) {
                    throw new InvalidInputParametersException("username", ErrorCode.FIELD_NAME_ALREADY_EXISTS);
                }
            } catch (ObjectNotFoundException e) {
                // This is okay
            }
        }
    }

    @Override
    protected User createObject(UserModel model) throws ObjectNotFoundException {
        User object = new User();
        object.setUsername(model.username.trim());
        getObjectDao().store(object);
        return object;
    }

    @Override
    protected void updateObject(User object, UserModel model) throws ObjectNotFoundException {
        object.setUsername(model.username.trim());
        object.setDisplayName(model.displayName.trim());
        if (!GenericValidator.isBlankOrNull(model.password)) {
            object.setHashedPassword(BCrypt.hashpw(model.password, BCrypt.gensalt()));
        }
        object.setEnabled(model.enabled);
        object.setAllowAppTokens(model.allowAppTokens);
        object.setAllowChannels(model.allowChannels);
        object.setAllowTemplates(model.allowTemplates);
        object.setAllowSystemConfig(model.allowSystemConfig);
        object.setAllowLogs(model.allowLogs);
        getObjectDao().update(object);
    }

    @Override
    protected UserDao getObjectDao() {
        return userDao;
    }
}
