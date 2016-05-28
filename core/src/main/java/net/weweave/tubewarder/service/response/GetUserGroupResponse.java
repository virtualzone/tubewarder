package net.weweave.tubewarder.service.response;

import net.weweave.tubewarder.service.model.UserGroupModel;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class GetUserGroupResponse extends AbstractResponse {
    public List<UserGroupModel> groups = new ArrayList<>();
}
