package eio.service.model;

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
}
