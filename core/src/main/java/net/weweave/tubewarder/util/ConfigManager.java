package net.weweave.tubewarder.util;

import net.weweave.tubewarder.dao.ConfigItemDao;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class ConfigManager {
    public static final String CONFIG_SYSTEM_DEAD_SECONDS = "SYSTEM_DEAD_SECONDS";
    public static final String CONFIG_MAX_CONCURRENT_THREADS = "QUEUE_MAX_CONCURRENT_THREADS";
    public static final String CONFIG_MAX_RETRIES = "QUEUE_MAX_RETRIES";
    public static final String CONFIG_RETRY_WAIT_TIME_SECONDS = "QUEUE_RETRY_WAIT_TIME_SECONDS";
    public static final String CONFIG_LICENSE_KEY_1 = "LICENSE_KEY_1";
    public static final String CONFIG_LICENSE_KEY_2 = "LICENSE_KEY_2";
    public static final String CONFIG_LICENSE_KEY_3 = "LICENSE_KEY_3";
    public static final String CONFIG_LICENSE_KEY_4 = "LICENSE_KEY_4";
    public static final String CONFIG_TERMS_ACCEPTED = "TERMS_ACCEPTED";
    public static final String CONFIG_CORS_ENABLED= "CORS_ENABLED";

    @Inject
    private ConfigItemDao configItemDao;

    public void checkCreateConfig() {
        if (!getConfigItemDao().hasKey(CONFIG_MAX_CONCURRENT_THREADS)) {
            getConfigItemDao().setValue(CONFIG_MAX_CONCURRENT_THREADS, 10, "Max. concurrent threads");
        }
        if (!getConfigItemDao().hasKey(CONFIG_MAX_RETRIES)) {
            getConfigItemDao().setValue(CONFIG_MAX_RETRIES, 5, "Max. retries");
        }
        if (!getConfigItemDao().hasKey(CONFIG_RETRY_WAIT_TIME_SECONDS)) {
            getConfigItemDao().setValue(CONFIG_RETRY_WAIT_TIME_SECONDS, 5*60, "Retry wait time (seconds)");
        }
        if (!getConfigItemDao().hasKey(CONFIG_SYSTEM_DEAD_SECONDS)) {
            getConfigItemDao().setValue(CONFIG_SYSTEM_DEAD_SECONDS, 600, "Period after which system is considered down (seconds)");
        }
        for (int i=1; i<=4; i++) {
            if (!getConfigItemDao().hasKey("LICENSE_KEY_"+i)) {
                getConfigItemDao().setValue("LICENSE_KEY_"+i, "", "License Key for Instance #"+i);
            }
        }
        if (!getConfigItemDao().hasKey(CONFIG_TERMS_ACCEPTED)) {
            getConfigItemDao().setValue(CONFIG_TERMS_ACCEPTED, false, "I have read and accept the License Agreement");
        }
        if (!getConfigItemDao().hasKey(CONFIG_CORS_ENABLED)) {
            getConfigItemDao().setValue(CONFIG_CORS_ENABLED, false, "Enable Cross-origin resource sharing for REST Services");
        }
    }

    public ConfigItemDao getConfigItemDao() {
        return configItemDao;
    }

    public void setConfigItemDao(ConfigItemDao configItemDao) {
        this.configItemDao = configItemDao;
    }
}
