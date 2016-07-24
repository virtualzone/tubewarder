package net.weweave.tubewarder.util;

import net.weweave.tubewarder.dao.ConfigItemDao;
import org.apache.commons.validator.GenericValidator;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.engines.URLConnectionEngine;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

@Startup
@Singleton
public class LicenseManager {
    private static final Logger LOG = Logger.getLogger(LicenseManager.class.getName());
    private Boolean licensed = false;

    @Inject
    private ConfigItemDao configItemDao;

    @PostConstruct
    public void init() {
        checkAllStoredKeys();
    }

    public void checkAllStoredKeys() {
        boolean licensed = true;
        boolean hasAnyKey = false;
        for (int i=1; i<=4; i++) {
            String licenseKey = getConfigItemDao().getString("LICENSE_KEY_"+i, "");
            if (!GenericValidator.isBlankOrNull(licenseKey)) {
                licensed = licensed && isLicensed(licenseKey);
                hasAnyKey = true;
            }
        }
        this.licensed = hasAnyKey && licensed;
    }

    public boolean isLicensed(String licenseKey) {
        LOG.info("Checking validity of license key " + licenseKey);
        // Client client = ClientBuilder.newClient();
        // Using ResteasyClient here in order to deal with weweave.net's SNI
        ResteasyClient client = new ResteasyClientBuilder().httpEngine(new URLConnectionEngine()).build();
        WebTarget target = client.target("https://weweave.net/rest/checklicensekey");
        try {
            Form form = new Form();
            form.param("licenseKey", licenseKey);
            Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED));
            if (response.getStatus() != Response.Status.OK.getStatusCode()) {
                LOG.warning("Got invalid HTTP status code " + response.getStatus());
                return true; // Assumption in favor of customer
            }
            LicenseRestResponse data = response.readEntity(LicenseRestResponse.class);
            LOG.info("Validity of license key " + licenseKey + " is " + data.success);
            return data.success;
        } catch (WebApplicationException e) {
            LOG.warning("Could not connect to host ("+e.getMessage()+")");
            return true; // Assumption in favor of customer
        } catch (Exception e) {
            LOG.warning("Could not connect to host ("+e.getMessage()+")");
            return true; // Assumption in favor of customer
        }
    }

    public boolean isLicensed() {
        return this.licensed;
    }

    public ConfigItemDao getConfigItemDao() {
        return configItemDao;
    }

    public void setConfigItemDao(ConfigItemDao configItemDao) {
        this.configItemDao = configItemDao;
    }
}
