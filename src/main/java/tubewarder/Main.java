package tubewarder;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.config.datasources.DataSource;
import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.container.JARArchive;
import org.wildfly.swarm.datasources.DatasourceArchive;
import org.wildfly.swarm.jaxrs.JAXRSArchive;
import org.wildfly.swarm.jpa.JPAFraction;

public class Main {
    public static void main(String[] args) throws Exception {
        Container container = new Container();
        container.start();

        container.deploy(Swarm.artifact("com.h2database:h2", "h2"));

        JARArchive dsArchive = ShrinkWrap.create(JARArchive.class);
        DataSource ds = new DataSource("TubewarderDS");
        ds.connectionUrl("jdbc:h2:mem:tubewarder;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
        ds.driverName("h2");
        ds.userName("tubewarder");
        ds.password("tubewarder");
        dsArchive.as(DatasourceArchive.class).dataSource(ds);
        container.deploy(dsArchive);

        container.fraction(new JPAFraction().inhibitDefaultDatasource().defaultDatasource("jboss/datasources/TubewarderDS"));

        JAXRSArchive appDeployment = ShrinkWrap.create(JAXRSArchive.class);
        appDeployment.addPackage("tubewarder.dao");
        appDeployment.addPackage("tubewarder.domain");
        appDeployment.addPackage("tubewarder.exception");
        appDeployment.addPackage("tubewarder.service");
        appDeployment.addPackage("tubewarder.service.common");
        appDeployment.addPackage("tubewarder.service.model");
        appDeployment.addPackage("tubewarder.service.rest");
        appDeployment.addPackage("tubewarder.service.soap");
        appDeployment.addPackage("tubewarder.util");
        appDeployment.addPackage("tubewarder.util.output");
        appDeployment.addAsWebInfResource(new ClassLoaderAsset("META-INF/load.sql", Main.class.getClassLoader()), "classes/META-INF/load.sql");
        appDeployment.addAsWebInfResource(new ClassLoaderAsset("META-INF/persistence.xml", Main.class.getClassLoader()), "classes/META-INF/persistence.xml");
        appDeployment.addAllDependencies();
        container.deploy(appDeployment);
    }
}
