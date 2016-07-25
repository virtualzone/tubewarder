package net.weweave.tubewarder.test;

import net.weweave.tubewarder.dao.ConfigItemDao;
import net.weweave.tubewarder.util.ConfigManager;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Before;
import org.junit.BeforeClass;

import javax.inject.Inject;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public abstract class AbstractServiceTest {
    @ArquillianResource
    public URL deploymentUrl;

    @Inject
    private DbTestAssist dbTestAssist;

    @Inject
    private ConfigItemDao configItemDao;

    @BeforeClass
    public static void beforeClass() {
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);
        java.util.logging.Logger.getLogger("org.apache.openejb.arquillian.common").setLevel(Level.SEVERE);
    }

    @Before
    public final void initialize() {
        getDbTestAssist().cleanDb();
        getConfigItemDao().setValue(ConfigManager.CONFIG_TERMS_ACCEPTED, true);
    }

    public String getUri(String serviceName) {
        return deploymentUrl + "rs/" + serviceName;
    }

    protected Map<String, Object> createMap(Object... content) {
        Map<String, Object> result = new HashMap<>();
        String key = "";
        int i = 1;
        for (Object o : content) {
            if (i%2 == 1) {
                key = (String)o;
            } else {
                result.put(key, o);
            }
            i++;
        }
        return result;
    }

    public DbTestAssist getDbTestAssist() {
        return dbTestAssist;
    }

    public void setDbTestAssist(DbTestAssist dbTestAssist) {
        this.dbTestAssist = dbTestAssist;
    }

    public ConfigItemDao getConfigItemDao() {
        return configItemDao;
    }

    public void setConfigItemDao(ConfigItemDao configItemDao) {
        this.configItemDao = configItemDao;
    }
}
