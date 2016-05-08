package net.weweave.tubewarder.client;

import java.io.Serializable;

public class KeyValue implements Serializable {
    private String key;
    private Object value;

    public KeyValue() {

    }

    public KeyValue(String key, Object value) {
        setKey(key);
        setValue(value);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
