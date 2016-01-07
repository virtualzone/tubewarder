package net.weweave.tubewarder.service.request;

import net.weweave.tubewarder.service.model.AppTokenModel;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SetAppTokenRequest extends AbstractSetObjectRestRequest<AppTokenModel> {
}
