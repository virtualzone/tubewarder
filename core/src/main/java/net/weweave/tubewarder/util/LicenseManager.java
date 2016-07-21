package net.weweave.tubewarder.util;

import net.weweave.tubewarder.dao.ConfigItemDao;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

@Startup
@Singleton
public class LicenseManager {
    private Boolean licensed = false;

    @Inject
    private ConfigItemDao configItemDao;

    @PostConstruct
    public void init() {
        // TODO
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
