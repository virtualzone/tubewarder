package net.weweave.tubewarder.service.model;

import net.weweave.tubewarder.domain.Channel;
import net.weweave.tubewarder.outputhandler.OutputHandlerConfig;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Map;

@XmlRootElement
public class ChannelModel extends AbstractRestModel {
    public String name;
    public Map<String, Object> config;

    public static ChannelModel factory(Channel channel) {
        ChannelModel model = new ChannelModel();
        model.id = channel.getExposableId();
        model.name = channel.getName();
        model.config = OutputHandlerConfig.configJsonStringToMap(channel.getConfigJson());
        return model;
    }
}
