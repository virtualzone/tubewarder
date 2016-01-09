package net.weweave.tubewarder.service.rest;

import net.weweave.tubewarder.dao.UserDao;
import net.weweave.tubewarder.domain.User;
import net.weweave.tubewarder.exception.ObjectNotFoundException;
import net.weweave.tubewarder.service.model.ErrorCode;
import net.weweave.tubewarder.service.model.UserModel;
import net.weweave.tubewarder.service.response.GetUserResponse;
import org.apache.commons.validator.GenericValidator;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import java.util.List;

@RequestScoped
@Path("/user/get")
public class GetUserService {
    @Inject
    private UserDao userDao;

    @GET
    @Produces(JaxApplication.APPLICATION_JSON_UTF8)
    public GetUserResponse action(@QueryParam("id") @DefaultValue("") String id) {
        GetUserResponse response = new GetUserResponse();

        try {
            setResponseList(response, id);
        } catch (Exception e) {
            response.error = ErrorCode.INVALID_INPUT_PARAMETERS;
        }

        return response;
    }

    private void setResponseList(GetUserResponse response, String id) throws ObjectNotFoundException {
        if (GenericValidator.isBlankOrNull(id)) {
            List<User> users = getUserDao().getAll();
            for (User user : users) {
                response.users.add(UserModel.factory(user));
            }
        } else {
            User user = getUserDao().get(id);
            response.users.add(UserModel.factory(user));
        }
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
