package net.weweave.tubewarder.service.request;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AuthRequest extends AbstractRestRequest {
    public String username;
    public String password;
}
