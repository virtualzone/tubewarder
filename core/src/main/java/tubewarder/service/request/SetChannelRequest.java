package tubewarder.service.request;

import tubewarder.service.model.ChannelModel;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SetChannelRequest extends AbstractSetObjectRestRequest<ChannelModel> {
}
