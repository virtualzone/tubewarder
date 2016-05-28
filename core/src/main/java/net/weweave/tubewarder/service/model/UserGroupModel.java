package net.weweave.tubewarder.service.model;

import net.weweave.tubewarder.domain.UserGroup;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserGroupModel extends AbstractRestModel {
    public String name;

    public static UserGroupModel factory(UserGroup group) {
        UserGroupModel model = new UserGroupModel();
        model.id = group.getExposableId();
        model.name = group.getName();
        return model;
    }
}
