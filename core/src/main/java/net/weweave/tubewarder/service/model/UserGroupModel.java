package net.weweave.tubewarder.service.model;

import net.weweave.tubewarder.domain.User;
import net.weweave.tubewarder.domain.UserGroup;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class UserGroupModel extends AbstractRestModel {
    public String name;
    public List<UserModel> members = new ArrayList<>();

    public static UserGroupModel factory(UserGroup group) {
        UserGroupModel model = factorySmall(group);
        for (User user : group.getMembers()) {
            model.members.add(UserModel.factory(user));
        }
        return model;
    }

    public static UserGroupModel factorySmall(UserGroup group) {
        UserGroupModel model = new UserGroupModel();
        model.id = group.getExposableId();
        model.name = group.getName();
        return model;
    }
}
