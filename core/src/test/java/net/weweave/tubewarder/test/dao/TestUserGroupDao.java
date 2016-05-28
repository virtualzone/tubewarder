package net.weweave.tubewarder.test.dao;

import net.weweave.tubewarder.dao.UserDao;
import net.weweave.tubewarder.dao.UserGroupDao;
import net.weweave.tubewarder.domain.User;
import net.weweave.tubewarder.domain.UserGroup;
import net.weweave.tubewarder.exception.ObjectNotFoundException;
import net.weweave.tubewarder.test.AbstractServiceTest;
import net.weweave.tubewarder.test.DbTestAssist;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.List;

@RunWith(Arquillian.class)
public class TestUserGroupDao extends AbstractServiceTest {
    @Inject
    private UserGroupDao userGroupDao;

    @Inject
    private UserDao userDao;

    @Inject
    private DbTestAssist dbTestAssist;

    @Test
    public void testGetGroupByName() throws ObjectNotFoundException {
        UserGroup group1 = new UserGroup();
        group1.setName("g1");
        getUserGroupDao().store(group1);

        UserGroup group2 = new UserGroup();
        group2.setName("g2");
        getUserGroupDao().store(group2);

        Assert.assertEquals("g1", getUserGroupDao().getByName("g1").getName());
        Assert.assertEquals("g2", getUserGroupDao().getByName("g2").getName());
    }

    @Test(expected = ObjectNotFoundException.class)
    public void testInvalidGetGroupByName() throws ObjectNotFoundException {
        getUserGroupDao().getByName("g123");
    }

    @Test
    public void testAddUserToGroup() throws ObjectNotFoundException {
        // Create three users
        User u1 = new User();
        u1.setUsername("u1");
        getUserDao().store(u1);

        User u2 = new User();
        u2.setUsername("u2");
        getUserDao().store(u2);

        User u3 = new User();
        u3.setUsername("u3");
        getUserDao().store(u3);

        // Create two groups
        UserGroup group1 = new UserGroup();
        group1.setName("g1");
        getUserGroupDao().store(group1);

        UserGroup group2 = new UserGroup();
        group2.setName("g2");
        getUserGroupDao().store(group2);

        // Add u1 and u2 to g1
        getUserGroupDao().addUserToGroup(u1, group1);
        getUserGroupDao().addUserToGroup(u2, group1);

        // Add u2 and u3 to g2
        getUserGroupDao().addUserToGroup(u2, group2);
        getUserGroupDao().addUserToGroup(u3, group2);

        // Check if group1 has u1 und u2 as members
        group1 = getUserGroupDao().getByName("g1");
        Assert.assertEquals(2, group1.getMembers().size());
        Assert.assertEquals("u1", group1.getMembers().get(0).getUsername());
        Assert.assertEquals("u2", group1.getMembers().get(1).getUsername());

        // Check if group2 has u2 und u3 as members
        group2 = getUserGroupDao().getByName("g2");
        Assert.assertEquals(2, group2.getMembers().size());
        Assert.assertEquals("u2", group2.getMembers().get(0).getUsername());
        Assert.assertEquals("u3", group2.getMembers().get(1).getUsername());
    }

    @Test
    public void testRemoveUserFromGroup() throws ObjectNotFoundException {
        // Create three users
        User u1 = new User();
        u1.setUsername("u1");
        getUserDao().store(u1);

        User u2 = new User();
        u2.setUsername("u2");
        getUserDao().store(u2);

        User u3 = new User();
        u3.setUsername("u3");
        getUserDao().store(u3);

        // Create one group
        UserGroup group = new UserGroup();
        group.setName("g1");
        getUserGroupDao().store(group);

        // Add all users to the group
        getUserGroupDao().addUserToGroup(u1, group);
        getUserGroupDao().addUserToGroup(u2, group);
        getUserGroupDao().addUserToGroup(u3, group);

        // Remove u2 from group and check
        getUserGroupDao().removeUserFromGroup(u2, group);
        UserGroup checkGroup = getUserGroupDao().getByName("g1");
        Assert.assertEquals(2, checkGroup.getMembers().size());
        Assert.assertEquals("u1", checkGroup.getMembers().get(0).getUsername());
        Assert.assertEquals("u3", checkGroup.getMembers().get(1).getUsername());

        // Remove u1 from group and check
        getUserGroupDao().removeUserFromGroup(u1, group);
        checkGroup = getUserGroupDao().getByName("g1");
        Assert.assertEquals(1, checkGroup.getMembers().size());
        Assert.assertEquals("u3", checkGroup.getMembers().get(0).getUsername());
    }

    @Test
    public void testGetGroupMemberships() {
        // Create three users
        User u1 = new User();
        u1.setUsername("u1");
        getUserDao().store(u1);

        User u2 = new User();
        u2.setUsername("u2");
        getUserDao().store(u2);

        User u3 = new User();
        u3.setUsername("u3");
        getUserDao().store(u3);

        // Create two groups
        UserGroup group1 = new UserGroup();
        group1.setName("g1");
        getUserGroupDao().store(group1);

        UserGroup group2 = new UserGroup();
        group2.setName("g2");
        getUserGroupDao().store(group2);

        // Add u1 and u2 to g1
        getUserGroupDao().addUserToGroup(u1, group1);
        getUserGroupDao().addUserToGroup(u2, group1);

        // Add u2 and u3 to g2
        getUserGroupDao().addUserToGroup(u2, group2);
        getUserGroupDao().addUserToGroup(u3, group2);

        // Check
        List<UserGroup> u1Groups = getUserGroupDao().getGroupMemberships(u1);
        List<UserGroup> u2Groups = getUserGroupDao().getGroupMemberships(u2);
        List<UserGroup> u3Groups = getUserGroupDao().getGroupMemberships(u3);

        // u1 is in g1
        Assert.assertEquals(1, u1Groups.size());
        Assert.assertEquals("g1", u1Groups.get(0).getName());

        // u2 is in g1 and g2
        Assert.assertEquals(2, u2Groups.size());
        Assert.assertEquals("g1", u2Groups.get(0).getName());
        Assert.assertEquals("g2", u2Groups.get(1).getName());

        // u3 is in g2
        Assert.assertEquals(1, u3Groups.size());
        Assert.assertEquals("g2", u3Groups.get(0).getName());
    }

    @Test
    public void testGetAll() {
        // Create two groups
        UserGroup group1 = new UserGroup();
        group1.setName("g1");
        getUserGroupDao().store(group1);

        UserGroup group2 = new UserGroup();
        group2.setName("g2");
        getUserGroupDao().store(group2);

        // Check
        List<UserGroup> all = getUserGroupDao().getAll();
        Assert.assertEquals(2, all.size());
        Assert.assertEquals("g1", all.get(0).getName());
        Assert.assertEquals("g2", all.get(1).getName());
    }

    @Test
    public void testIsUserMemberOfGroup() throws ObjectNotFoundException {
        // Create three users
        User u1 = new User();
        u1.setUsername("u1");
        getUserDao().store(u1);

        User u2 = new User();
        u2.setUsername("u2");
        getUserDao().store(u2);

        User u3 = new User();
        u3.setUsername("u3");
        getUserDao().store(u3);

        // Create one group
        UserGroup group = new UserGroup();
        group.setName("g1");
        getUserGroupDao().store(group);

        // Add u1 and u2 users to the group
        getUserGroupDao().addUserToGroup(u1, group);
        getUserGroupDao().addUserToGroup(u2, group);

        // Check
        group = getUserGroupDao().getByName("g1");
        Assert.assertTrue(getUserGroupDao().isUserMemberOfGroup(u1, group));
        Assert.assertTrue(getUserGroupDao().isUserMemberOfGroup(u2, group));
        Assert.assertFalse(getUserGroupDao().isUserMemberOfGroup(u3, group));
    }

    public UserGroupDao getUserGroupDao() {
        return userGroupDao;
    }

    public void setUserGroupDao(UserGroupDao userGroupDao) {
        this.userGroupDao = userGroupDao;
    }

    @Override
    public DbTestAssist getDbTestAssist() {
        return dbTestAssist;
    }

    @Override
    public void setDbTestAssist(DbTestAssist dbTestAssist) {
        this.dbTestAssist = dbTestAssist;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
