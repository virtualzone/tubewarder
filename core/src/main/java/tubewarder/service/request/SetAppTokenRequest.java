package tubewarder.service.request;

import tubewarder.service.model.AppTokenModel;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SetAppTokenRequest extends AbstractSetObjectRestRequest<AppTokenModel> {
}
