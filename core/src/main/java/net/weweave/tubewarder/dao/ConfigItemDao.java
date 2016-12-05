package net.weweave.tubewarder.dao;

import net.weweave.tubewarder.domain.ConfigItem;
import net.weweave.tubewarder.domain.ConfigItemType;
import net.weweave.tubewarder.exception.ObjectNotFoundException;
import net.weweave.tubewarder.util.DbValueRetriever;

import javax.ejb.*;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Lock(LockType.READ)
public class ConfigItemDao extends AbstractDao<ConfigItem> {
    private static final Logger LOG = Logger.getLogger(ConfigItemDao.class.getName());
    private final Map<String, String> cache = new ConcurrentHashMap<>();

    @Override
    public void initObject(ConfigItem obj) {
        // Nothing to do
    }

    public boolean hasKey(String key) {
        if (cache.containsKey(key)) {
            return true;
        } else {
            try {
                getRawItem(key);
                return true;
            } catch (ObjectNotFoundException e) {
                return false;
            }
        }
    }

    public void setValue(String key, String value, String label) {
        setRawItem(key, ConfigItemType.STRING, value, label);
    }

    public void setValue(String key, String value) {
        setValue(key, value, key);
    }

    public void setValue(String key, Integer value, String label) {
        setRawItem(key, ConfigItemType.INT, String.valueOf(value), label);
    }

    public void setValue(String key, Integer value) {
        setValue(key, value, key);
    }

    public void setValue(String key, Boolean value, String label) {
        setRawItem(key, ConfigItemType.BOOL, (value != null && value ? "1" : "0"), label);
    }

    public void setValue(String key, Boolean value) {
        setValue(key, value, key);
    }

    public String getString(String key) throws ObjectNotFoundException {
        if (cache.containsKey(key)) {
            return cache.get(key);
        }
        return getRawItem(key).getValue();
    }

    public String getString(String key, String defaultValue) {
        try {
            return getString(key);
        } catch (ObjectNotFoundException e) {
            return defaultValue;
        }
    }

    public Integer getInt(String key) throws ObjectNotFoundException {
        if (cache.containsKey(key)) {
            return Integer.valueOf(cache.get(key));
        }
        String value = getRawItem(key).getValue();
        return Integer.valueOf(value);
    }

    public Integer getInt(String key, Integer defaultValue) {
        try {
            return getInt(key);
        } catch (ObjectNotFoundException e) {
            return defaultValue;
        }
    }

    public Boolean getBool(String key) throws ObjectNotFoundException {
        String val;
        if (cache.containsKey(key)) {
            val = cache.get(key);
            return (val != null && "1".equals(val));
        }
        val = getRawItem(key).getValue();
        return (val != null && "1".equals(val));
    }

    public Boolean getBool(String key, Boolean defaultValue) {
        try {
            return getBool(key);
        } catch (ObjectNotFoundException e) {
            return defaultValue;
        }
    }

    public void remove(String key) {
        try {
            ConfigItem item = getRawItem(key);
            delete(item);
        } catch (ObjectNotFoundException e) {
            // Do nothing
        } finally {
            cache.remove(key);
        }
    }

    public List<ConfigItem> getAll() {
        TypedQuery<ConfigItem> query = getEntityManager().createQuery("SELECT i FROM ConfigItem i " +
                "ORDER BY i.label ASC", ConfigItem.class);
        List<ConfigItem> result = query.getResultList();
        initObject(result);
        return result;
    }

    public void clearCache() {
        cache.clear();
    }

    private void setRawItem(String key, ConfigItemType type, String value, String label) {
        LOG.info("Setting config entry: " + key + " = " + value);
        ConfigItem item;
        try {
            item = getRawItem(key);
        } catch (ObjectNotFoundException e) {
            item = createItem(key, label, type);
        }
        item.setValue(value);
        update(item);
        cache.put(key, item.getValue());
    }

    private ConfigItem createItem(String key, String label, ConfigItemType type) {
        ConfigItem item = new ConfigItem();
        item.setKey(key);
        item.setLabel(label);
        item.setType(type);
        store(item);
        return item;
    }

    private ConfigItem getRawItem(String key) throws ObjectNotFoundException {
        TypedQuery<ConfigItem> query = getEntityManager().createQuery("SELECT i FROM ConfigItem i " +
                "WHERE i.key = :key", ConfigItem.class);
        query.setParameter("key", key);
        query.setMaxResults(1);
        ConfigItem item = (ConfigItem) DbValueRetriever.getObjectOrException(query);
        cache.put(key, item.getValue());
        initObject(item);
        getEntityManager().detach(item);
        return item;
    }
}
