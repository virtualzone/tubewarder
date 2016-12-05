package net.weweave.tubewarder.service.model;

import net.weweave.tubewarder.domain.ChannelTemplate;
import net.weweave.tubewarder.outputhandler.OutputHandlerFactory;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ChannelTemplateModel extends AbstractRestModel {
    public TemplateModel template;
    public ChannelModel channel;
    public String subject;
    public String content;
    public String senderAddress;
    public String senderName;

    public static ChannelTemplateModel factory(ChannelTemplate channelTemplate, OutputHandlerFactory factory, boolean includeTemplate) {
        ChannelTemplateModel model = new ChannelTemplateModel();
        model.id = channelTemplate.getExposableId();
        if (includeTemplate) {
            model.template = TemplateModel.factory(channelTemplate.getTemplate(), null, factory);
        }
        model.channel = ChannelModel.factory(channelTemplate.getChannel(), factory);
        model.subject = channelTemplate.getSubject();
        model.content = channelTemplate.getContent();
        model.senderAddress = channelTemplate.getSenderAddress();
        model.senderName = channelTemplate.getSenderName();
        return model;
    }

    public static ChannelTemplateModel factory(ChannelTemplate channelTemplate, OutputHandlerFactory factory) {
        return factory(channelTemplate, factory, true);
    }
}
