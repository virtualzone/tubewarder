package net.weweave.tubewarder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config extends Properties {
    public Config(File file) throws IOException {
        super();
        load(new FileInputStream(file));
    }

    public String getHttpPort() {
        return getEnvProperty("http.port", "8080");
    }

    public String getDb() {
        return getEnvProperty("db", "h2");
    }

    public String getH2Path() {
        return getEnvProperty("h2.path", "mem:tubewarder");
    }

    public String getH2User() {
        return getEnvProperty("h2.username", "tubewarder");
    }

    public String getH2Pass() {
        return getEnvProperty("h2.password", "tubewarder");
    }

    public String getMySqlPath() {
        return getEnvProperty("mysql.path", "localhost:3306/tubewarder");
    }

    public String getMySqlUser() {
        return getEnvProperty("mysql.username", "tubewarder");
    }

    public String getMySqlPass() {
        return getEnvProperty("mysql.password", "tubewarder");
    }

    public String getPostgreSqlPath() {
        return getEnvProperty("postgresql.path", "localhost:5432/tubewarder");
    }

    public String getPostgreSqlUser() {
        return getEnvProperty("postgresql.username", "tubewarder");
    }

    public String getPostgreSqlPass() {
        return getEnvProperty("postgresql.password", "tubewarder");
    }

    private String getEnvProperty(String key, String defaultValue) {
        String result = System.getenv("TUBEWARDER_"+key.toUpperCase().replace('.', '_'));
        if (result == null) {
            result = getProperty(key, defaultValue);
        }
        return result;
    }

    public void checkConfig() throws IllegalArgumentException {
        String db = getDb();
        if (!("h2".equals(db) || "mysql".equals(db) || "postgresql".equals(db))) {
            throw new IllegalArgumentException("Invalid setting: db = " + db);
        }
    }

    public void printConfig() {
        System.out.println("Tubewarder Bootstrap Configuration:");
        System.out.println("--> http.port = " + getHttpPort());
        System.out.println("--> db = " + getDb());
        if ("mysql".equals(getDb())) {
            System.out.println("--> mysql.path = " + getMySqlPath());
            System.out.println("--> mysql.username = " + getMySqlUser());
            System.out.println("--> mysql.password = ******");
        } else if ("h2".equals(getDb())) {
            System.out.println("--> h2.path = " + getH2Path());
            System.out.println("--> h2.username = " + getH2User());
            System.out.println("--> h2.password = ******");
        } else if ("postgresql".equals(getDb())) {
            System.out.println("--> postgresql.path = " + getPostgreSqlPath());
            System.out.println("--> postgresql.username = " + getPostgreSqlUser());
            System.out.println("--> postgresql.password = ******");
        }
    }
}
