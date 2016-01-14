package net.weweave.tubewarder.test.rest;

import net.weweave.tubewarder.test.DbTestAssist;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;

import javax.inject.Inject;
import java.net.URL;

public abstract class AbstractRestTest {
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

    public String getUri() {
        return deploymentUrl + "rs/" + getServiceName();
    }

    public abstract String getServiceName();

    public DbTestAssist getDbTestAssist() {
        return dbTestAssist;
    }

    public void setDbTestAssist(DbTestAssist dbTestAssist) {
        this.dbTestAssist = dbTestAssist;
    }
}
