package tubewarder.service.request;

import tubewarder.service.model.ChannelTemplateModel;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SetChannelTemplateRequest extends AbstractSetObjectRestRequest<ChannelTemplateModel> {
}
