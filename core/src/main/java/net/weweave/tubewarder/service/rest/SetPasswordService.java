package net.weweave.tubewarder.service.rest;

import net.weweave.tubewarder.dao.UserDao;
import net.weweave.tubewarder.domain.Session;
import net.weweave.tubewarder.domain.User;
import net.weweave.tubewarder.exception.AuthRequiredException;
import net.weweave.tubewarder.exception.InvalidInputParametersException;
import net.weweave.tubewarder.exception.PermissionException;
import net.weweave.tubewarder.service.model.ErrorCode;
import net.weweave.tubewarder.service.request.SetPasswordRequest;
import net.weweave.tubewarder.service.response.AbstractResponse;
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
@Path("/user/setpassword")
public class SetPasswordService extends AbstractService {
    @Inject
    private UserDao userDao;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(JaxApplication.APPLICATION_JSON_UTF8)
    public AbstractResponse action(SetPasswordRequest request) {
        AbstractResponse response = new AbstractResponse();
        try {
            Session session = getSession(request.token);
            checkPermissions(session.getUser());
            validateInputParameters(request.password);
            updateUserPassword(session.getUser(), request.password);
        } catch (InvalidInputParametersException e) {
            addErrorsToResponse(response, e);
        } catch (PermissionException e) {
            response.error = ErrorCode.PERMISSION_DENIED;
        } catch (AuthRequiredException e) {
            response.error = ErrorCode.AUTH_REQUIRED;
        }
        return response;
    }

    private void checkPermissions(User user) throws PermissionException {
        if (user == null) {
            throw new PermissionException();
        }
    }

    private void validateInputParameters(String password) throws InvalidInputParametersException {
        if (GenericValidator.isBlankOrNull(password)) {
            throw new InvalidInputParametersException("password", ErrorCode.FIELD_REQUIRED);
        }
        if (!PasswordPolicy.matches(password)) {
            throw new InvalidInputParametersException("password", ErrorCode.FIELD_INVALID);
        }
    }

    private void updateUserPassword(User user, String password) {
        user.setHashedPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        getUserDao().update(user);
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
