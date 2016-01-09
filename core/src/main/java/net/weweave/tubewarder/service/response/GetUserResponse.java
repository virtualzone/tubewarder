package net.weweave.tubewarder.service.response;

import net.weweave.tubewarder.service.model.UserModel;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class GetUserResponse extends AbstractResponse {
    public List<UserModel> users = new ArrayList<>();
}
