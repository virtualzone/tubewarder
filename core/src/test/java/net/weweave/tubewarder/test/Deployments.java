package net.weweave.tubewarder.test;

import org.eu.ingwar.tools.arquillian.extension.suite.annotations.ArquillianSuiteDeployment;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;

import java.io.File;

@ArquillianSuiteDeployment
public class Deployments {
    @Deployment
    public static WebArchive createDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class)
                .addPackages(true, "net.weweave.tubewarder")
                .addAsResource("META-INF/persistence.xml")
                .addAsWebInfResource("resources.xml")
                .addAsWebInfResource("openejb-jar.xml")
                .addAsLibrary(new File("../outputhandlers/sysout/target/outputhandler-sysout.jar"), "outputhandler-sysout.jar");
        return war;
    }
}
