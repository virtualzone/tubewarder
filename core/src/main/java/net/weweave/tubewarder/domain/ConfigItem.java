package net.weweave.tubewarder.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
public class ConfigItem extends AbstractPersistentObject {
    @Column(unique = true, name = "config_key")
    private String key;
    private String label;
    @Enumerated(EnumType.STRING)
    @Column(name = "config_type")
    private ConfigItemType type;
    @Column(name = "config_value")
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public ConfigItemType getType() {
        return type;
    }

    public void setType(ConfigItemType type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
