package net.weweave.tubewarder;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.config.datasources.DataSource;
import org.wildfly.swarm.datasources.DatasourceArchive;
import org.wildfly.swarm.spi.api.JARArchive;
import org.wildfly.swarm.undertow.WARArchive;

import java.io.File;
import java.io.FilenameFilter;

public class TubewarderSwarm extends Swarm {
    private final Config config;

    public TubewarderSwarm(Config config) throws Exception {
        super();
        this.config = config;
    }

    public void deployTubewarder() throws Exception {
        setUndertow();
        start();
        deployDbArtifact();
        deployDatasource();
        deployApp();
    }

    public void setUndertow() {
        System.setProperty("swarm.http.port", getConfig().getHttpPort());
        System.setProperty("java.net.preferIPv4Stack", "true");
    }

    private void deployDatasource() throws Exception {
        String jdbcString = getJdbcString();
        deployDatasourceFraction(jdbcString);
    }

    private String getJdbcString() {
        String db = getConfig().getDb();
        String s = "jdbc:"+db+":";
        if ("h2".equals(db)) {
            s += getConfig().getH2Path();
        } else if ("mysql".equals(db)) {
            s += "//"+getConfig().getMySqlPath()+"?useSSL=false&amp;autoReconnect=true";
        } else if ("postgresql".equals(db)) {
            s += "//"+getConfig().getMySqlPath();
        }
        return s;
    }

    private void deployDbArtifact() throws Exception {
        String db = getConfig().getDb();
        if ("h2".equals(db)) {
            deployH2();
        } else if ("mysql".equals(db)) {
            deployMySql();
        } else if ("postgresql".equals(db)) {
            deployPostgreSql();
        }
    }

    private void deployH2() throws Exception {
        deploy(Swarm.artifact("com.h2database:h2", "h2"));
    }

    private void deployMySql() throws Exception {
        JavaArchive jar = Swarm.artifact("mysql:mysql-connector-java", "mysql");
        deploy(jar);
    }

    private void deployPostgreSql() throws Exception {
        JavaArchive jar = Swarm.artifact("org.postgresql:postgresql", "postgresql");
        deploy(jar);
    }

    private void deployDatasourceFraction(String jdbcString) throws Exception {
        JARArchive dsArchive = ShrinkWrap.create(JARArchive.class);
        DataSource ds = new DataSource("TubewarderDS");
        ds.connectionUrl(jdbcString);
        ds.driverName(getDriverName(getConfig().getDb()));
        ds.driverClass(getDriverClass(getConfig().getDb()));
        setDatasourceUserPass(ds);
        dsArchive.as(DatasourceArchive.class).dataSource(ds);
        deploy(dsArchive);
    }

    private void setDatasourceUserPass(DataSource ds) {
        String db = getConfig().getDb();
        if ("h2".equals(db)) {
            ds.userName(getConfig().getH2User());
            ds.password(getConfig().getH2Pass());
        } else if ("mysql".equals(db)) {
            ds.userName(getConfig().getMySqlUser());
            ds.password(getConfig().getMySqlPass());
        } else if ("postgresql".equals(db)) {
            ds.userName(getConfig().getPostgreSqlUser());
            ds.password(getConfig().getPostgreSqlPass());
        }
    }

    private String getDriverName(String db) {
        if ("h2".equals(db)) {
            return "h2";
        } else if ("mysql".equals(db)) {
            return "mysql_com.mysql.jdbc.Driver_5_1";
        } else if ("postgresql".equals(db)) {
            return "postgresql";
        }
        return "";
    }

    private String getDriverClass(String db) {
        if ("h2".equals(db)) {
            return "h2";
        } else if ("mysql".equals(db)) {
            return "com.mysql.jdbc.Driver";
        } else if ("postgresql".equals(db)) {
            return "org.postgresql.Driver";
        }
        return "";
    }

    private void deployApp() throws Exception {
        WARArchive appDeployment = ShrinkWrap.create(WARArchive.class, "tubewarder.war");
        appDeployment.addPackages(true, "net.weweave.tubewarder");
        appDeployment.addAsWebInfResource(new ClassLoaderAsset("META-INF/persistence-"+getConfig().getDb()+".xml", TubewarderSwarm.class.getClassLoader()), "classes/META-INF/persistence.xml");
        if ("postgresql".equals(getConfig().getDb())) {
            appDeployment.addAsWebInfResource(new ClassLoaderAsset("META-INF/" + getConfig().getDb() + "-mapping.xml", TubewarderSwarm.class.getClassLoader()), "classes/META-INF/postgresql-mapping.xml");
        }

        File[] libs = getAdditionalJars();
        for (File lib : libs) {
            appDeployment.addAsLibrary(lib, lib.getName());
        }

        appDeployment.addAllDependencies();
        appDeployment.staticContent();
        deploy(appDeployment);
    }

    private File[] getAdditionalJars() {
        File folder = new File("./libs");
        if (!folder.isDirectory()) {
            return new File[0];
        }
        File[] list = folder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if (name != null && name.toLowerCase().endsWith(".jar")) {
                    return true;
                }
                return false;
            }
        });
        return list;
    }

    public Config getConfig() {
        return config;
    }
}
