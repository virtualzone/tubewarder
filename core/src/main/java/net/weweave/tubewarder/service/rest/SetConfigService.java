package net.weweave.tubewarder.service.rest;

import net.weweave.tubewarder.dao.ConfigItemDao;
import net.weweave.tubewarder.domain.ConfigItemType;
import net.weweave.tubewarder.domain.Session;
import net.weweave.tubewarder.domain.User;
import net.weweave.tubewarder.exception.AuthRequiredException;
import net.weweave.tubewarder.exception.InvalidInputParametersException;
import net.weweave.tubewarder.exception.ObjectNotFoundException;
import net.weweave.tubewarder.exception.PermissionException;
import net.weweave.tubewarder.service.model.ConfigItemModel;
import net.weweave.tubewarder.service.model.ErrorCode;
import net.weweave.tubewarder.service.request.SetConfigRequest;
import net.weweave.tubewarder.service.response.AbstractResponse;
import net.weweave.tubewarder.service.response.SetObjectRestResponse;
import net.weweave.tubewarder.util.LicenseManager;
import org.apache.commons.validator.GenericValidator;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@RequestScoped
@Path("/config/set")
public class SetConfigService extends AbstractService {
    @Inject
    private ConfigItemDao configItemDao;

    @Inject
    private LicenseManager licenseManager;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(JaxApplication.APPLICATION_JSON_UTF8)
    public AbstractResponse action(SetConfigRequest request) {
        AbstractResponse response = new SetObjectRestResponse();
        try {
            Session session = getSession(request.token);
            checkPermissions(session.getUser());
            validateInputParameters(request.items);
            setValues(request.items);
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

    private void validateInputParameters(List<ConfigItemModel> items) throws InvalidInputParametersException {
        for (ConfigItemModel model : items) {
            if (GenericValidator.isBlankOrNull(model.key)) {
                throw new InvalidInputParametersException(model.key, ErrorCode.FIELD_REQUIRED);
            }
        }
    }

    private void setValues(List<ConfigItemModel> items) throws ObjectNotFoundException {
        for (ConfigItemModel model : items) {
            if (ConfigItemType.BOOL.toString().equals(model.type)) {
                getConfigItemDao().setValue(model.key, "1".equals(model.value) || "true".equals(model.value.toLowerCase()));
            } else if (ConfigItemType.INT.toString().equals(model.type)) {
                getConfigItemDao().setValue(model.key, Integer.valueOf(model.value));
            } else {
                getConfigItemDao().setValue(model.key, model.value);
            }
        }
        getLicenseManager().checkAllStoredKeys();
    }

    public ConfigItemDao getConfigItemDao() {
        return configItemDao;
    }

    public void setConfigItemDao(ConfigItemDao configItemDao) {
        this.configItemDao = configItemDao;
    }

    public LicenseManager getLicenseManager() {
        return licenseManager;
    }

    public void setLicenseManager(LicenseManager licenseManager) {
        this.licenseManager = licenseManager;
    }
}
