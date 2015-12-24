package eio;

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
        DataSource ds = new DataSource("EioDS");
        ds.connectionUrl("jdbc:h2:mem:eio;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
        ds.driverName("h2");
        ds.userName("eio");
        ds.password("eio");
        dsArchive.as(DatasourceArchive.class).dataSource(ds);
        container.deploy(dsArchive);

        container.fraction(new JPAFraction().inhibitDefaultDatasource().defaultDatasource("jboss/datasources/EioDS"));

        JAXRSArchive appDeployment = ShrinkWrap.create(JAXRSArchive.class);
        appDeployment.addPackage("eio.dao");
        appDeployment.addPackage("eio.domain");
        appDeployment.addPackage("eio.exception");
        appDeployment.addPackage("eio.service");
        appDeployment.addPackage("eio.service.model");
        appDeployment.addPackage("eio.service.rest");
        appDeployment.addPackage("eio.service.soap");
        appDeployment.addPackage("eio.util");
        appDeployment.addPackage("eio.util.output");
        appDeployment.addAsWebInfResource(new ClassLoaderAsset("META-INF/load.sql", Main.class.getClassLoader()), "classes/META-INF/load.sql");
        appDeployment.addAsWebInfResource(new ClassLoaderAsset("META-INF/persistence.xml", Main.class.getClassLoader()), "classes/META-INF/persistence.xml");
        appDeployment.addAllDependencies();
        container.deploy(appDeployment);

    }
}
