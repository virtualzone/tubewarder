package net.weweave.tubewarder.service.request;

import net.weweave.tubewarder.service.model.UserGroupModel;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SetUserGroupRequest extends AbstractSetObjectRestRequest<UserGroupModel> {
}
