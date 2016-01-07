package net.weweave.tubewarder.service.response;

import net.weweave.tubewarder.service.model.ChannelTemplateModel;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class GetChannelTemplateResponse extends AbstractResponse {
    public List<ChannelTemplateModel> channelTemplates = new ArrayList<>();
}
