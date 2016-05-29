package net.weweave.tubewarder.service.response;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Map;

@XmlRootElement
public class SearchUserResponse extends AbstractResponse {
    public Map<String, String> users;
}
