package net.weweave.tubewarder.test;

import net.weweave.tubewarder.dao.*;
import net.weweave.tubewarder.domain.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class TestSendServiceCommon {
    @Inject
    private ChannelDao channelDao;

    @Inject
    private ChannelTemplateDao channelTemplateDao;

    @Inject
    private TemplateDao templateDao;

    @Inject
    private AppTokenDao appTokenDao;

    @Inject
    private SysoutOutputHandlerConfigurationDao configurationDao;

    public AppToken createAppToken() {
        AppToken token = new AppToken();
        token.setName("Default");
        getAppTokenDao().store(token);
        return token;
    }

    public Channel createChannel(String name) {
        SysoutOutputHandlerConfiguration config = new SysoutOutputHandlerConfiguration();
        config.setPrefix("Debug: [");
        config.setSuffix("]");
        getConfigurationDao().store(config);

        Channel channel = new Channel();
        channel.setName(name);
        channel.setOutputHandler(OutputHandler.SYSOUT);
        channel.setConfig(config);
        getChannelDao().store(channel);
        return channel;
    }

    public Channel createChannel() {
        return createChannel("sms");
    }

    public Template createTemplate() {
        Template template = new Template();
        template.setName("DOI");
        getTemplateDao().store(template);
        return template;
    }

    public ChannelTemplate createChannelTemplate(Template template, Channel channel, String subject, String content) {
        ChannelTemplate ct = new ChannelTemplate();
        ct.setTemplate(template);
        ct.setChannel(channel);
        ct.setSubject(subject);
        ct.setContent(content);
        ct.setSenderName("weweave");
        ct.setSenderAddress("noreply@weweave.net");
        getChannelTemplateDao().store(ct);
        return ct;
    }

    public ChannelDao getChannelDao() {
        return channelDao;
    }

    public void setChannelDao(ChannelDao channelDao) {
        this.channelDao = channelDao;
    }

    public ChannelTemplateDao getChannelTemplateDao() {
        return channelTemplateDao;
    }

    public void setChannelTemplateDao(ChannelTemplateDao channelTemplateDao) {
        this.channelTemplateDao = channelTemplateDao;
    }

    public TemplateDao getTemplateDao() {
        return templateDao;
    }

    public void setTemplateDao(TemplateDao templateDao) {
        this.templateDao = templateDao;
    }

    public AppTokenDao getAppTokenDao() {
        return appTokenDao;
    }

    public void setAppTokenDao(AppTokenDao appTokenDao) {
        this.appTokenDao = appTokenDao;
    }

    public SysoutOutputHandlerConfigurationDao getConfigurationDao() {
        return configurationDao;
    }

    public void setConfigurationDao(SysoutOutputHandlerConfigurationDao configurationDao) {
        this.configurationDao = configurationDao;
    }
}
