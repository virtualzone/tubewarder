package tubewarder.service.rest;

import org.apache.commons.validator.GenericValidator;
import tubewarder.dao.AppTokenDao;
import tubewarder.domain.AppToken;
import tubewarder.exception.ObjectNotFoundException;
import tubewarder.service.model.AppTokenModel;
import tubewarder.service.model.ErrorCode;
import tubewarder.service.response.GetAppTokenResponse;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import java.util.List;

@RequestScoped
@Path("/apptoken/get")
public class GetAppToken {
    @Inject
    private AppTokenDao appTokenDao;

    @GET
    @Produces(JaxApplication.APPLICATION_JSON_UTF8)
    public GetAppTokenResponse action(@QueryParam("id") @DefaultValue("") String id) {
        GetAppTokenResponse response = new GetAppTokenResponse();

        try {
            setResponseList(response, id);
        } catch (Exception e) {
            response.error = ErrorCode.INVALID_INPUT_PARAMETERS;
        }

        return response;
    }

    private void setResponseList(GetAppTokenResponse response, String id) throws ObjectNotFoundException {
        if (GenericValidator.isBlankOrNull(id)) {
            List<AppToken> appTokens = getAppTokenDao().getAll();
            for (AppToken appToken : appTokens) {
                response.tokens.add(AppTokenModel.factory(appToken));
            }
        } else {
            AppToken appToken = getAppTokenDao().get(id);
            response.tokens.add(AppTokenModel.factory(appToken));
        }
    }

    public AppTokenDao getAppTokenDao() {
        return appTokenDao;
    }

    public void setAppTokenDao(AppTokenDao appTokenDao) {
        this.appTokenDao = appTokenDao;
    }
}
