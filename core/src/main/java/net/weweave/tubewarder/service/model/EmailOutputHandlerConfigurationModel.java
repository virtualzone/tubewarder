package net.weweave.tubewarder.service.model;

import net.weweave.tubewarder.domain.EmailOutputHandlerConfiguration;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class EmailOutputHandlerConfigurationModel extends AbstractOutputHandlerConfigurationModel {
    public String smtpServer;
    public Integer port;
    public Boolean auth;
    public String username;
    public String password;
    public String security;
    public String contentType;

    public static void completeFactory(EmailOutputHandlerConfigurationModel model, EmailOutputHandlerConfiguration config) {
        model.smtpServer = config.getSmtpServer();
        model.port = config.getPort();
        model.auth = config.getAuth();
        model.username = config.getUsername();
        model.password = config.getPassword();
        model.security = config.getSecurity().toString();
        model.contentType = config.getContentType();
    }
}
