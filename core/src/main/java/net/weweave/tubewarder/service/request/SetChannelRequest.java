package net.weweave.tubewarder.service.request;

import net.weweave.tubewarder.service.model.ChannelModel;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SetChannelRequest extends AbstractSetObjectRestRequest<ChannelModel> {
}
