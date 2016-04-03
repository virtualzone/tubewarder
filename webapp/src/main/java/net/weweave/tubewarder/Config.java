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
        return getProperty("http.port", "8080");
    }

    public String getDb() {
        return getProperty("db", "h2");
    }

    public String getH2Path() {
        return getProperty("h2.path", "mem:tubewarder");
    }

    public String getH2User() {
        return getProperty("h2.username", "tubewarder");
    }

    public String getH2Pass() {
        return getProperty("h2.password", "tubewarder");
    }

    public String getMySqlPath() {
        return getProperty("mysql.path", "localhost:3306/tubewarder");
    }

    public String getMySqlUser() {
        return getProperty("mysql.username", "tubewarder");
    }

    public String getMySqlPass() {
        return getProperty("mysql.password", "tubewarder");
    }

    public String getPostgreSqlPath() {
        return getProperty("postgresql.path", "localhost:5432/tubewarder");
    }

    public String getPostgreSqlUser() {
        return getProperty("postgresql.username", "tubewarder");
    }

    public String getPostgreSqlPass() {
        return getProperty("postgresql.password", "tubewarder");
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
            System.out.println("--> mysql.password = " + getMySqlPass());
        } else if ("h2".equals(getDb())) {
            System.out.println("--> h2.path = " + getH2Path());
            System.out.println("--> h2.username = " + getH2User());
            System.out.println("--> h2.password = " + getH2Pass());
        } else if ("postgresql".equals(getDb())) {
            System.out.println("--> postgresql.path = " + getPostgreSqlPath());
            System.out.println("--> postgresql.username = " + getPostgreSqlUser());
            System.out.println("--> postgresql.password = " + getPostgreSqlPass());
        }
    }
}
