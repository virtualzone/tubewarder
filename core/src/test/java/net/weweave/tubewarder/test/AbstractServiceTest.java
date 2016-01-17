package net.weweave.tubewarder.test;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;

import javax.inject.Inject;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractServiceTest {
    @ArquillianResource
    public URL deploymentUrl;

    @Inject
    private DbTestAssist dbTestAssist;

    @Before
    public final void initialize() {
        getDbTestAssist().cleanDb();
    }

    @Deployment
    public static WebArchive createDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class)
                .addPackages(true, "net.weweave.tubewarder")
                .addAsResource("META-INF/persistence.xml")
                .addAsWebInfResource("resources.xml")
                .addAsWebInfResource("openejb-jar.xml");
        return war;
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
}
