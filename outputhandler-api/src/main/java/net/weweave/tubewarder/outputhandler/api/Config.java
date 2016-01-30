package net.weweave.tubewarder.outputhandler.api;

import java.util.HashMap;

public class Config extends HashMap<String, Object> {
    public String getString(String key) {
        return (String)get(key);
    }

    public Integer getInt(String key) {
        return (Integer)get(key);
    }

    public Boolean getBool(String key) {
        return (Boolean)get(key);
    }
}
