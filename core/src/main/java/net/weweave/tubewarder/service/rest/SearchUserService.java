package net.weweave.tubewarder.service.rest;

import net.weweave.tubewarder.dao.UserDao;
import net.weweave.tubewarder.domain.Session;
import net.weweave.tubewarder.domain.User;
import net.weweave.tubewarder.exception.AuthRequiredException;
import net.weweave.tubewarder.exception.ObjectNotFoundException;
import net.weweave.tubewarder.exception.PermissionException;
import net.weweave.tubewarder.service.model.ErrorCode;
import net.weweave.tubewarder.service.response.SearchUserResponse;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;

@RequestScoped
@Path("/user/search/{query}")
public class SearchUserService extends AbstractService {
    @Inject
    private UserDao userDao;

    @GET
    @Produces(JaxApplication.APPLICATION_JSON_UTF8)
    public SearchUserResponse action(@QueryParam("token") @DefaultValue("") String token,
                                     @PathParam("query") @DefaultValue("") String query) {
        SearchUserResponse response = new SearchUserResponse();

        try {
            Session session = getSession(token);
            checkPermissions(session.getUser());
            setResponseList(response, query);
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

    private void setResponseList(SearchUserResponse response, String query) throws ObjectNotFoundException {
        response.users = getUserDao().getUsersForAutocompleteQuery(query);
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
