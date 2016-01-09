package net.weweave.tubewarder.service.request;

import net.weweave.tubewarder.service.model.UserModel;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SetUserRequest extends AbstractSetObjectRestRequest<UserModel> {
}
