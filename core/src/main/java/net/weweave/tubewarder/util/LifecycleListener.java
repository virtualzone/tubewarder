package net.weweave.tubewarder.util;

import net.weweave.tubewarder.dao.SystemDao;
import net.weweave.tubewarder.dao.UserDao;
import net.weweave.tubewarder.domain.User;
import net.weweave.tubewarder.outputhandler.OutputHandlerFactory;
import net.weweave.tubewarder.outputhandler.SendQueueScheduler;
import org.mindrot.jbcrypt.BCrypt;

import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class LifecycleListener implements ServletContextListener {
    @Inject
    private UserDao userDao;

    @Inject
    private OutputHandlerFactory outputHandlerFactory;

    @Inject
    private SendQueueScheduler sendQueueScheduler;

    @Inject
    private SystemDao systemDao;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        checkCreateAdmin();
        getSystemDao().updateAliveStatus(SystemIdentifier.getIdentifier());
        getOutputHandlerFactory().init(servletContextEvent.getServletContext());
        getSendQueueScheduler().recover();
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

    public OutputHandlerFactory getOutputHandlerFactory() {
        return outputHandlerFactory;
    }

    public void setOutputHandlerFactory(OutputHandlerFactory outputHandlerFactory) {
        this.outputHandlerFactory = outputHandlerFactory;
    }

    public SendQueueScheduler getSendQueueScheduler() {
        return sendQueueScheduler;
    }

    public void setSendQueueScheduler(SendQueueScheduler sendQueueScheduler) {
        this.sendQueueScheduler = sendQueueScheduler;
    }

    public SystemDao getSystemDao() {
        return systemDao;
    }

    public void setSystemDao(SystemDao systemDao) {
        this.systemDao = systemDao;
    }
}
