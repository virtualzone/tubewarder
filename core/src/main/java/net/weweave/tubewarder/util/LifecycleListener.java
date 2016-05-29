package net.weweave.tubewarder.util;

import net.weweave.tubewarder.dao.*;
import net.weweave.tubewarder.domain.Channel;
import net.weweave.tubewarder.domain.Template;
import net.weweave.tubewarder.domain.User;
import net.weweave.tubewarder.domain.UserGroup;
import net.weweave.tubewarder.outputhandler.OutputHandlerFactory;
import net.weweave.tubewarder.outputhandler.SendQueueScheduler;
import org.mindrot.jbcrypt.BCrypt;

import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.List;

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

    @Inject
    private ConfigManager configManager;

    @Inject
    private AliveStatusUpdater aliveStatusUpdater;

    @Inject
    private UserGroupDao userGroupDao;

    @Inject
    private TemplateDao templateDao;

    @Inject
    private ChannelDao channelDao;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        getConfigManager().checkCreateConfig();
        checkCreateAdmin();
        checkCreateGroups();
        getAliveStatusUpdater().initAliveStatus();
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

    private void checkCreateGroups() {
        if (!getUserGroupDao().existsAnyGroup()) {
            UserGroup group = new UserGroup();
            group.setName("Default");
            // Add all users
            List<User> allUsers = getUserDao().getAll();
            for (User user : allUsers) {
                group.getMembers().add(user);
            }
            getUserGroupDao().store(group);
            // Update channels and groups
            assignAllChannelsToGroup(group);
            assignAllTemplatesToGroup(group);
        }
    }

    private void assignAllTemplatesToGroup(UserGroup group) {
        List<Template> templates = getTemplateDao().getAll();
        for (Template template : templates) {
            template.setUserGroup(group);
            getTemplateDao().update(template);
        }
    }

    private void assignAllChannelsToGroup(UserGroup group) {
        List<Channel> channels = getChannelDao().getAll();
        for (Channel channel : channels) {
            channel.setUserGroup(group);
            getChannelDao().update(channel);
        }
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public void setConfigManager(ConfigManager configManager) {
        this.configManager = configManager;
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

    public AliveStatusUpdater getAliveStatusUpdater() {
        return aliveStatusUpdater;
    }

    public void setAliveStatusUpdater(AliveStatusUpdater aliveStatusUpdater) {
        this.aliveStatusUpdater = aliveStatusUpdater;
    }

    public UserGroupDao getUserGroupDao() {
        return userGroupDao;
    }

    public void setUserGroupDao(UserGroupDao userGroupDao) {
        this.userGroupDao = userGroupDao;
    }

    public TemplateDao getTemplateDao() {
        return templateDao;
    }

    public void setTemplateDao(TemplateDao templateDao) {
        this.templateDao = templateDao;
    }

    public ChannelDao getChannelDao() {
        return channelDao;
    }

    public void setChannelDao(ChannelDao channelDao) {
        this.channelDao = channelDao;
    }
}
