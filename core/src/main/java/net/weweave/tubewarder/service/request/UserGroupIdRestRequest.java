package net.weweave.tubewarder.service.request;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserGroupIdRestRequest extends AbstractRestRequest {
    public String userId;
    public String groupId;
}
