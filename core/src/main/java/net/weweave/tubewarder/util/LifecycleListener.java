package net.weweave.tubewarder.util;

import net.weweave.tubewarder.dao.UserDao;
import net.weweave.tubewarder.domain.User;
import org.mindrot.jbcrypt.BCrypt;

import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class LifecycleListener implements ServletContextListener {
    @Inject
    private UserDao userDao;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        checkCreateAdmin();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

    private void checkCreateAdmin() {
        if (!getUserDao().existsAnyAdminUser()) {
            User user = new User();
            user.setUsername("admin");
            user.setDisplayName("System Administrator");
            user.setHashedPassword(BCrypt.hashpw("admin", BCrypt.gensalt()));
            user.setEnabled(true);
            user.setAllowAppTokens(true);
            user.setAllowChannels(true);
            user.setAllowTemplates(true);
            user.setAllowSystemConfig(true);
            user.setAllowLogs(true);
            getUserDao().store(user);
        }
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
