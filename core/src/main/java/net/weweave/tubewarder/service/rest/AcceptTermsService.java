package net.weweave.tubewarder.service.rest;

import net.weweave.tubewarder.dao.ConfigItemDao;
import net.weweave.tubewarder.exception.InvalidInputParametersException;
import net.weweave.tubewarder.service.model.ErrorCode;
import net.weweave.tubewarder.service.request.AcceptTermsRequest;
import net.weweave.tubewarder.service.response.AbstractResponse;
import net.weweave.tubewarder.service.response.SetObjectRestResponse;
import net.weweave.tubewarder.util.ConfigManager;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@RequestScoped
@Path("/acceptterms")
public class AcceptTermsService extends AbstractService {
    @Inject
    private ConfigItemDao configItemDao;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(JaxApplication.APPLICATION_JSON_UTF8)
    public AbstractResponse action(AcceptTermsRequest request) {
        AbstractResponse response = new SetObjectRestResponse();
        try {
            validateInputParameters(request);
            acceptTerms();
        } catch (InvalidInputParametersException e) {
            addErrorsToResponse(response, e);
        }
        return response;
    }

    private void validateInputParameters(AcceptTermsRequest request) throws InvalidInputParametersException {
        if (request.termsAccepted == null || !request.termsAccepted) {
            throw new InvalidInputParametersException("termsAccepted", ErrorCode.FIELD_REQUIRED);
        }
    }

    private void acceptTerms() {
        getConfigItemDao().setValue(ConfigManager.CONFIG_TERMS_ACCEPTED, true);
    }

    public ConfigItemDao getConfigItemDao() {
        return configItemDao;
    }

    public void setConfigItemDao(ConfigItemDao configItemDao) {
        this.configItemDao = configItemDao;
    }
}
