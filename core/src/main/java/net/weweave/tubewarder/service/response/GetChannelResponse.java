package net.weweave.tubewarder.service.response;

import net.weweave.tubewarder.service.model.ChannelModel;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class GetChannelResponse extends AbstractResponse {
    public List<ChannelModel> channels = new ArrayList<>();
}
