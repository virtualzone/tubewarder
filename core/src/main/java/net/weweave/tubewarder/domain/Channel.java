package net.weweave.tubewarder.domain;

import javax.persistence.*;

@Entity
public class Channel extends AbstractPersistentObject {
    @Column(unique = true)
    private String name;
    @Column(length = 100000)
    private String configJson;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConfigJson() {
        return configJson;
    }

    public void setConfigJson(String configJson) {
        this.configJson = configJson;
    }
}
