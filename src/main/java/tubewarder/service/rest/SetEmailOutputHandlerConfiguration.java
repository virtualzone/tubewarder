package tubewarder.service.rest;

import tubewarder.dao.EmailOutputHandlerConfigurationDao;
import tubewarder.domain.EmailOutputHandlerConfiguration;
import tubewarder.domain.MailSecurity;
import tubewarder.exception.InvalidInputParametersException;
import tubewarder.exception.ObjectNotFoundException;
import tubewarder.service.model.AbstractResponse;
import tubewarder.service.model.EmailOutputHandlerConfigurationModel;
import tubewarder.service.model.ErrorCode;
import org.apache.commons.validator.GenericValidator;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@RequestScoped
@Path("/emailoutputhandlerconfiguration/set")
public class SetEmailOutputHandlerConfiguration extends AbstractSetObjectService<EmailOutputHandlerConfigurationModel, EmailOutputHandlerConfiguration> {
    @Inject
    private EmailOutputHandlerConfigurationDao outputHandlerConfigurationDao;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(JaxApplication.APPLICATION_JSON_UTF8)
    
    public AbstractResponse action(EmailOutputHandlerConfigurationModel model) {
        AbstractResponse response = new AbstractResponse();
        try {
            validateInputParameters(model);
            createUpdateObject(model);
        } catch (InvalidInputParametersException e) {
            response.error = ErrorCode.INVALID_INPUT_PARAMETERS;
        } catch (ObjectNotFoundException e) {
            response.error = ErrorCode.OBJECT_LOOKUP_ERROR;
        }
        return response;
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
