package net.weweave.tubewarder.service.response;

import net.weweave.tubewarder.service.model.UserModel;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AuthResponse extends AbstractResponse {
    public String token;
    public UserModel user;
}
