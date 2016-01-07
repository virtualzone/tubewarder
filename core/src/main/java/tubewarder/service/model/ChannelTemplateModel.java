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

    public static ChannelTemplateModel factory(ChannelTemplate channelTemplate, boolean includeTemplate) {
        ChannelTemplateModel model = new ChannelTemplateModel();
        model.id = channelTemplate.getExposableId();
        if (includeTemplate) {
            model.template = TemplateModel.factory(channelTemplate.getTemplate());
        }
        model.channel = ChannelModel.factory(channelTemplate.getChannel());
        model.subject = channelTemplate.getSubject();
        model.content = channelTemplate.getContent();
        model.senderAddress = channelTemplate.getSenderAddress();
        model.senderName = channelTemplate.getSenderName();
        return model;
    }

    public static ChannelTemplateModel factory(ChannelTemplate channelTemplate) {
        return factory(channelTemplate, true);
    }
}
