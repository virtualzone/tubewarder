package net.weweave.tubewarder.service.rest;

import net.weweave.tubewarder.dao.ConfigItemDao;
import net.weweave.tubewarder.exception.AuthRequiredException;
import net.weweave.tubewarder.service.model.ErrorCode;
import net.weweave.tubewarder.service.request.AbstractRestRequest;
import net.weweave.tubewarder.service.response.AbstractResponse;
import net.weweave.tubewarder.service.response.CheckLicenseResponse;
import net.weweave.tubewarder.util.ConfigManager;
import net.weweave.tubewarder.util.LicenseManager;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@RequestScoped
@Path("/checklicense")
public class CheckLicenseService extends AbstractService {
    @Inject
    private ConfigItemDao configItemDao;

    @Inject
    private LicenseManager licenseManager;

    @GET
    @Produces(JaxApplication.APPLICATION_JSON_UTF8)
    public CheckLicenseResponse action() {
        CheckLicenseResponse response = new CheckLicenseResponse();
        compileResponse(response);
        return response;
    }

    private void compileResponse(CheckLicenseResponse response) {
        response.termsAccepted = getConfigItemDao().getBool(ConfigManager.CONFIG_TERMS_ACCEPTED, false);
        response.licensed = getLicenseManager().isLicensed();
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
