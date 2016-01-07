package net.weweave.tubewarder;

import org.apache.commons.cli.*;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.config.datasources.DataSource;
import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.container.DeploymentException;
import org.wildfly.swarm.container.JARArchive;
import org.wildfly.swarm.datasources.DatasourceArchive;
import org.wildfly.swarm.jaxrs.JAXRSArchive;
import org.wildfly.swarm.jpa.JPAFraction;

public class Main {
    public static void main(String[] args) throws Exception {
        CommandLine cmd = parseCommandLineArgs(args);

        Container container = new Container();
        container.start();

        container.deploy(Swarm.artifact("com.h2database:h2", "h2"));
        deployDatasource(container, cmd);
        container.fraction(new JPAFraction().inhibitDefaultDatasource().defaultDatasource("jboss/datasources/TubewarderDS"));
        deployApp(container);
    }

    private static CommandLine parseCommandLineArgs(String[] args) throws ParseException {
        Options options = new Options();

        options.addOption("help", false, "print this message");

        Option db = Option.builder("db")
                .argName("path")
                .required(false)
                .hasArg()
                .desc("path/url to the h2 database (default: mem)")
                .build();
        options.addOption(db);

        Option dbUsername = Option.builder("dbUser")
                .argName("username")
                .required(false)
                .hasArg()
                .desc("username for the h2 database (default: tubewarder)")
                .build();
        options.addOption(dbUsername);

        Option dbPassword = Option.builder("dbPass")
                .argName("password")
                .required(false)
                .hasArg()
                .desc("password for the h2 database (default: tubewarder)")
                .build();
        options.addOption(dbPassword);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        if (cmd.hasOption("help")) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("tubewarder", options);
            System.exit(0);
        }

        return cmd;
    }

    private static void deployDatasource(Container container, CommandLine cmd) throws DeploymentException {
        String url = (cmd.hasOption("db") ? cmd.getOptionValue("db") : "mem:tubewarder");
        String username = (cmd.hasOption("dbUser") ? cmd.getOptionValue("dbUser") : "tubewarder");
        String password = (cmd.hasOption("dbPass") ? cmd.getOptionValue("dbPass") : "tubewarder");

        JARArchive dsArchive = ShrinkWrap.create(JARArchive.class);
        DataSource ds = new DataSource("TubewarderDS");
        ds.connectionUrl("jdbc:h2:"+url);
        ds.driverName("h2");
        ds.userName(username);
        ds.password(password);
        dsArchive.as(DatasourceArchive.class).dataSource(ds);
        container.deploy(dsArchive);
    }

    private static void deployApp(Container container) throws Exception {
        JAXRSArchive appDeployment = ShrinkWrap.create(JAXRSArchive.class);
        appDeployment.addPackage("net.weweave.tubewarder.dao");
        appDeployment.addPackage("net.weweave.tubewarder.domain");
        appDeployment.addPackage("net.weweave.tubewarder.exception");
        appDeployment.addPackage("net.weweave.tubewarder.service");
        appDeployment.addPackage("net.weweave.tubewarder.service.common");
        appDeployment.addPackage("net.weweave.tubewarder.service.model");
        appDeployment.addPackage("net.weweave.tubewarder.service.request");
        appDeployment.addPackage("net.weweave.tubewarder.service.response");
        appDeployment.addPackage("net.weweave.tubewarder.service.rest");
        appDeployment.addPackage("net.weweave.tubewarder.service.soap");
        appDeployment.addPackage("net.weweave.tubewarder.util");
        appDeployment.addPackage("net.weweave.tubewarder.util.output");
        appDeployment.addAsWebInfResource(new ClassLoaderAsset("META-INF/persistence.xml", Main.class.getClassLoader()), "classes/META-INF/persistence.xml");
        appDeployment.addAllDependencies();
        appDeployment.staticContent();
        container.deploy(appDeployment);
    }
}
