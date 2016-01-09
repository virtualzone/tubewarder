package net.weweave.tubewarder.service.model;

import net.weweave.tubewarder.domain.User;
import net.weweave.tubewarder.util.DateTimeFormat;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserModel extends AbstractRestModel {
    public String displayName;
    public String username;
    public String password = "";
    public Boolean enabled;
    public String lastLogin;
    public Boolean allowAppTokens;
    public Boolean allowChannels;
    public Boolean allowTemplates;
    public Boolean allowUsers;

    public static UserModel factory(User user) {
        UserModel model = new UserModel();
        model.id = user.getExposableId();
        model.displayName = user.getDisplayName();
        model.username = user.getUsername();
        model.enabled = user.getEnabled();
        model.lastLogin = (user.getLastLogin() != null ? DateTimeFormat.format(user.getLastLogin()) : "");
        model.allowAppTokens = user.getAllowAppTokens();
        model.allowChannels = user.getAllowChannels();
        model.allowTemplates = user.getAllowTemplates();
        model.allowUsers = user.getAllowUsers();
        return model;
    }
}
