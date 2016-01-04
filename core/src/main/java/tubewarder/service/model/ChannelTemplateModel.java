package tubewarder.service.model;

import tubewarder.domain.ChannelTemplate;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ChannelTemplateModel extends AbstractRestModel {
    public TemplateModel template;
    public ChannelModel channel;
    public String subject;
    public String content;
    public String senderAddress;
    public String senderName;

    public static ChannelTemplateModel factory(ChannelTemplate channelTemplate) {
        ChannelTemplateModel model = new ChannelTemplateModel();
        model.template = TemplateModel.factory(channelTemplate.getTemplate());
        model.channel = ChannelModel.factory(channelTemplate.getChannel());
        model.subject = channelTemplate.getSubject();
        model.content = channelTemplate.getContent();
        model.senderAddress = channelTemplate.getSenderAddress();
        model.senderName = channelTemplate.getSenderName();
        return model;
    }
}
