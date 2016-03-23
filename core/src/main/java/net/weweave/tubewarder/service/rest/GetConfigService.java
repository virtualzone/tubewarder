package net.weweave.tubewarder.service.rest;

import net.weweave.tubewarder.dao.ConfigItemDao;
import net.weweave.tubewarder.domain.ConfigItem;
import net.weweave.tubewarder.domain.Session;
import net.weweave.tubewarder.domain.User;
import net.weweave.tubewarder.exception.AuthRequiredException;
import net.weweave.tubewarder.exception.ObjectNotFoundException;
import net.weweave.tubewarder.exception.PermissionException;
import net.weweave.tubewarder.service.model.ConfigItemModel;
import net.weweave.tubewarder.service.model.ErrorCode;
import net.weweave.tubewarder.service.response.GetConfigResponse;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import java.util.List;

@RequestScoped
@Path("/config/get")
public class GetConfigService extends AbstractService {
    @Inject
    private ConfigItemDao configItemDao;

    @GET
    @Produces(JaxApplication.APPLICATION_JSON_UTF8)
    public GetConfigResponse action(@QueryParam("token") @DefaultValue("") String token) {
        GetConfigResponse response = new GetConfigResponse();

        try {
            Session session = getSession(token);
            checkPermissions(session.getUser());
            setResponseList(response);
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
                !user.getAllowUsers()) {
            throw new PermissionException();
        }
    }

    private void setResponseList(GetConfigResponse response) throws ObjectNotFoundException {
        List<ConfigItem> items = getConfigItemDao().getAll();
        for (ConfigItem item : items) {
            response.items.add(ConfigItemModel.factory(item));
        }
    }

    public ConfigItemDao getConfigItemDao() {
        return configItemDao;
    }

    public void setConfigItemDao(ConfigItemDao configItemDao) {
        this.configItemDao = configItemDao;
    }
}
