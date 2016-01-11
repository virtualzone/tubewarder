package net.weweave.tubewarder.service.rest;

import net.weweave.tubewarder.dao.EmailOutputHandlerConfigurationDao;
import net.weweave.tubewarder.domain.Session;
import net.weweave.tubewarder.domain.User;
import net.weweave.tubewarder.exception.AuthRequiredException;
import net.weweave.tubewarder.exception.InvalidInputParametersException;
import net.weweave.tubewarder.exception.ObjectNotFoundException;
import net.weweave.tubewarder.exception.PermissionException;
import net.weweave.tubewarder.service.model.ErrorCode;
import net.weweave.tubewarder.service.request.SetEmailOutputHandlerConfigurationRequest;
import org.apache.commons.validator.GenericValidator;
import net.weweave.tubewarder.domain.EmailOutputHandlerConfiguration;
import net.weweave.tubewarder.domain.MailSecurity;
import net.weweave.tubewarder.service.model.EmailOutputHandlerConfigurationModel;
import net.weweave.tubewarder.service.response.SetObjectRestResponse;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@RequestScoped
@Path("/emailoutputhandlerconfiguration/set")
public class SetEmailOutputHandlerConfigurationService extends AbstractSetObjectService<EmailOutputHandlerConfigurationModel, EmailOutputHandlerConfiguration> {
    @Inject
    private EmailOutputHandlerConfigurationDao outputHandlerConfigurationDao;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(JaxApplication.APPLICATION_JSON_UTF8)
    public SetObjectRestResponse action(SetEmailOutputHandlerConfigurationRequest request) {
        SetObjectRestResponse response = new SetObjectRestResponse();
        try {
            Session session = getSession(request.token);
            checkPermissions(session.getUser());
            validateInputParameters(request.object);
            EmailOutputHandlerConfiguration object = createUpdateObject(request.object);
            response.id = object.getExposableId();
        } catch (InvalidInputParametersException e) {
            response.error = ErrorCode.INVALID_INPUT_PARAMETERS;
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
                !user.getAllowChannels()) {
            throw new PermissionException();
        }
    }

    @Override
    protected void validateInputParameters(EmailOutputHandlerConfigurationModel model) throws InvalidInputParametersException {
        if (GenericValidator.isBlankOrNull(model.smtpServer) ||
                !GenericValidator.isInRange(model.port, 1, 65535) ||
                !GenericValidator.matchRegexp(model.security, "NONE|SSL|TLS") ||
                GenericValidator.isBlankOrNull(model.contentType)) {
            throw new InvalidInputParametersException();
        }
    }

    @Override
    protected EmailOutputHandlerConfiguration createObject(EmailOutputHandlerConfigurationModel model) throws ObjectNotFoundException {
        EmailOutputHandlerConfiguration object = new EmailOutputHandlerConfiguration();
        getObjectDao().store(object);
        return object;
    }

    @Override
    protected void updateObject(EmailOutputHandlerConfiguration object, EmailOutputHandlerConfigurationModel model) throws ObjectNotFoundException {
        object.setSmtpServer(model.smtpServer);
        object.setPort(model.port);
        object.setAuth(model.auth);
        object.setUsername(model.username);
        object.setPassword(model.password);
        object.setSecurity(MailSecurity.valueOf(model.security));
        object.setContentType(model.contentType);
        getObjectDao().update(object);
    }

    @Override
    protected EmailOutputHandlerConfigurationDao getObjectDao() {
        return outputHandlerConfigurationDao;
    }
}
