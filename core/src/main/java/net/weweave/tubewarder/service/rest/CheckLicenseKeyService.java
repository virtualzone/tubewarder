package net.weweave.tubewarder.service.rest;

import net.weweave.tubewarder.dao.ConfigItemDao;
import net.weweave.tubewarder.domain.Session;
import net.weweave.tubewarder.domain.User;
import net.weweave.tubewarder.exception.AuthRequiredException;
import net.weweave.tubewarder.exception.InvalidInputParametersException;
import net.weweave.tubewarder.exception.ObjectNotFoundException;
import net.weweave.tubewarder.exception.PermissionException;
import net.weweave.tubewarder.service.model.ErrorCode;
import net.weweave.tubewarder.service.request.AbstractIdRestRequest;
import net.weweave.tubewarder.service.response.AbstractResponse;
import net.weweave.tubewarder.service.response.CheckLicenseResponse;
import net.weweave.tubewarder.util.ConfigManager;
import net.weweave.tubewarder.util.LicenseManager;
import org.apache.commons.validator.GenericValidator;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@RequestScoped
@Path("/checklicensekey")
public class CheckLicenseKeyService extends AbstractService {
    @Inject
    private LicenseManager licenseManager;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(JaxApplication.APPLICATION_JSON_UTF8)
    public AbstractResponse action(AbstractIdRestRequest request) {
        AbstractResponse response = new AbstractResponse();
        try {
            Session session = getSession(request.token);
            checkPermissions(session.getUser());
            validateInputParameters(request);
            checkLicenseKey(response, request.id);
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

    private void checkLicenseKey(AbstractResponse response, String licenseKey) {
        boolean valid = getLicenseManager().isLicensed(licenseKey);
        if (!valid) {
            response.error = ErrorCode.INVALID_INPUT_PARAMETERS;
        }
    }

    public LicenseManager getLicenseManager() {
        return licenseManager;
    }

    public void setLicenseManager(LicenseManager licenseManager) {
        this.licenseManager = licenseManager;
    }
}
