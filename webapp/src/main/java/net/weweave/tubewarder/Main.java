package net.weweave.tubewarder;

import org.apache.commons.cli.*;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.config.datasources.DataSource;
import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.container.DeploymentException;
import org.wildfly.swarm.container.JARArchive;
import org.wildfly.swarm.datasources.DatasourceArchive;
import org.wildfly.swarm.jaxrs.JAXRSArchive;

public class Main {
    public static void main(String[] args) throws Exception {
        CommandLine cmd = parseCommandLineArgs(args);
        String db = (cmd.hasOption("db") ? cmd.getOptionValue("db") : "h2");

        Container container = new Container();
        container.start();

        deployDbArtifact(container, db);
        deployDatasource(container, db, cmd);
        deployApp(container, db);
    }

    private static CommandLine parseCommandLineArgs(String[] args) throws ParseException {
        Options options = new Options();

        options.addOption("help", false, "print this message");

        Option db = Option.builder("db")
                .argName("type")
                .required(false)
                .hasArg()
                .desc("database type: h2, mysql (default: h2)")
                .build();
        options.addOption(db);

        Option h2 = Option.builder("h2")
                .argName("path")
                .required(false)
                .hasArg()
                .desc("path/url to the h2 database (default: mem)")
                .build();
        options.addOption(h2);

        Option mysql = Option.builder("mysql")
                .argName("path")
                .required(false)
                .hasArg()
                .desc("location of the mysql database (default: localhost:3306/tubewarder)")
                .build();
        options.addOption(mysql);

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

    private static void deployDatasource(Container container, String db, CommandLine cmd) throws Exception {
        String h2 = (cmd.hasOption("h2") ? cmd.getOptionValue("h2") : "mem:tubewarder");
        String mysql = (cmd.hasOption("mysql") ? cmd.getOptionValue("mysql") : "localhost:3306/tubewarder");
        String username = (cmd.hasOption("dbUser") ? cmd.getOptionValue("dbUser") : "tubewarder");
        String password = (cmd.hasOption("dbPass") ? cmd.getOptionValue("dbPass") : "tubewarder");

        String jdbcString = getJdbcString(db, h2, mysql);
        deployDatasourceFraction(container, db, jdbcString, username, password);
    }

    private static String getJdbcString(String db, String h2, String mysql) {
        String s = "jdbc:"+db+":";
        if ("h2".equals(db)) {
            s += h2;
        } else if ("mysql".equals(db)) {
            s += "//"+mysql+"?autoReconnect=true";
        }
        return s;
    }

    private static void deployDbArtifact(Container container, String db) throws Exception {
        if ("h2".equals(db)) {
            deployH2(container);
        } else if ("mysql".equals(db)) {
            deployMySql(container);
        }
    }

    private static void deployH2(Container container) throws Exception {
        container.deploy(Swarm.artifact("com.h2database:h2", "h2"));
    }

    private static void deployMySql(Container container) throws Exception {
        JavaArchive jar = Swarm.artifact("mysql:mysql-connector-java", "mysql");
        container.deploy(jar);
    }

    private static void deployDatasourceFraction(Container container, String db, String jdbcString, String username, String password) throws DeploymentException {
        JARArchive dsArchive = ShrinkWrap.create(JARArchive.class);
        DataSource ds = new DataSource("TubewarderDS");
        ds.connectionUrl(jdbcString);
        ds.driverName(getDriverName(db));
        ds.driverClass(getDriverClass(db));
        ds.userName(username);
        ds.password(password);
        dsArchive.as(DatasourceArchive.class).dataSource(ds);
        container.deploy(dsArchive);
    }

    private static String getDriverName(String db) {
        if ("h2".equals(db)) {
            return "h2";
        } else if ("mysql".equals(db)) {
            return "mysql_com.mysql.jdbc.Driver_5_1";
        }
        return "";
    }

    private static String getDriverClass(String db) {
        if ("h2".equals(db)) {
            return "h2";
        } else if ("mysql".equals(db)) {
            return "com.mysql.jdbc.Driver";
        }
        return "";
    }

    private static void deployApp(Container container, String db) throws Exception {
        JAXRSArchive appDeployment = ShrinkWrap.create(JAXRSArchive.class);
        appDeployment.addPackages(true, "net.weweave.tubewarder");
        appDeployment.addAsWebInfResource(new ClassLoaderAsset("META-INF/persistence-"+db+".xml", Main.class.getClassLoader()), "classes/META-INF/persistence.xml");
        appDeployment.addAllDependencies();
        appDeployment.staticContent();
        container.deploy(appDeployment);
    }
}
