package net.weweave.tubewarder.test.dao;

import net.weweave.tubewarder.dao.ConfigItemDao;
import net.weweave.tubewarder.domain.ConfigItem;
import net.weweave.tubewarder.domain.ConfigItemType;
import net.weweave.tubewarder.exception.ObjectNotFoundException;
import net.weweave.tubewarder.test.AbstractServiceTest;
import net.weweave.tubewarder.test.DbTestAssist;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.UUID;

@RunWith(Arquillian.class)
public class TestConfigItemDao extends AbstractServiceTest {
    @Inject
    private ConfigItemDao configItemDao;

    @Inject
    private DbTestAssist dbTestAssist;

    @Test
    public void testSetGetRemoveString() throws ObjectNotFoundException {
        Assert.assertEquals(false, getConfigItemDao().hasKey("key1"));
        getConfigItemDao().setValue("key1", "value 1");
        Assert.assertEquals(true, getConfigItemDao().hasKey("key1"));
        Assert.assertEquals("value 1", getConfigItemDao().getString("key1"));
        getConfigItemDao().remove("key1");
        Assert.assertEquals(false, getConfigItemDao().hasKey("key1"));
    }

    @Test
    public void testSetGetRemoveInt() throws ObjectNotFoundException {
        Assert.assertEquals(false, getConfigItemDao().hasKey("key2"));
        getConfigItemDao().setValue("key2", 12345);
        Assert.assertEquals(true, getConfigItemDao().hasKey("key2"));
        Assert.assertEquals(new Integer(12345), getConfigItemDao().getInt("key2"));
        getConfigItemDao().remove("key2");
        Assert.assertEquals(false, getConfigItemDao().hasKey("key2"));
    }

    @Test(expected = ObjectNotFoundException.class)
    public void testGetNonExistingString() throws ObjectNotFoundException {
        getConfigItemDao().getString("key3");
    }

    @Test(expected = ObjectNotFoundException.class)
    public void testGetNonExistingInt() throws ObjectNotFoundException {
        getConfigItemDao().getInt("key4");
    }

    @Test
    public void testGetDefaultValueString() throws ObjectNotFoundException {
        Assert.assertEquals("test", getConfigItemDao().getString("key5", "test"));
        getConfigItemDao().setValue("key5", "value 5");
        Assert.assertEquals("value 5", getConfigItemDao().getString("key5", "test"));
    }

    @Test
    public void testGetDefaultValueInt() throws ObjectNotFoundException {
        Assert.assertEquals(new Integer(999), getConfigItemDao().getInt("key6", 999));
        getConfigItemDao().setValue("key6", 888);
        Assert.assertEquals(new Integer(888), getConfigItemDao().getInt("key6", 999));
    }

    @Test
    public void testGetStringCached() throws ObjectNotFoundException {
        getConfigItemDao().setValue("key7", "value 1");
        Assert.assertEquals("value 1", getConfigItemDao().getString("key7"));
        getDbTestAssist().execUpdate("UPDATE ConfigItem i SET i.value = 'value 2' WHERE i.key = 'key7'");
        Assert.assertEquals("value 1", getConfigItemDao().getString("key7"));
        getConfigItemDao().setValue("key7", "value 3");
        Assert.assertEquals("value 3", getConfigItemDao().getString("key7"));
    }

    @Test
    public void testGetIntCached() throws ObjectNotFoundException {
        getConfigItemDao().setValue("key8", 111);
        Assert.assertEquals(new Integer(111), getConfigItemDao().getInt("key8"));
        getDbTestAssist().execUpdate("UPDATE ConfigItem i SET i.value = '222' WHERE i.key = 'key8'");
        Assert.assertEquals(new Integer(111), getConfigItemDao().getInt("key8"));
        getConfigItemDao().setValue("key8", 333);
        Assert.assertEquals(new Integer(333), getConfigItemDao().getInt("key8"));
    }

    @Test
    public void testHasKeyCached() throws ObjectNotFoundException {
        Assert.assertEquals(false, getConfigItemDao().hasKey("key9"));
        getConfigItemDao().setValue("key9", "value 1");
        Assert.assertEquals(true, getConfigItemDao().hasKey("key9"));
        getDbTestAssist().execUpdate("DELETE FROM ConfigItem i WHERE i.key = 'key9'");
        Assert.assertEquals(true, getConfigItemDao().hasKey("key9"));
        getConfigItemDao().clearCache();
        Assert.assertEquals(false, getConfigItemDao().hasKey("key9"));
    }

    public ConfigItemDao getConfigItemDao() {
        return configItemDao;
    }

    public void setConfigItemDao(ConfigItemDao configItemDao) {
        this.configItemDao = configItemDao;
    }

    @Override
    public DbTestAssist getDbTestAssist() {
        return dbTestAssist;
    }

    @Override
    public void setDbTestAssist(DbTestAssist dbTestAssist) {
        this.dbTestAssist = dbTestAssist;
    }
}
