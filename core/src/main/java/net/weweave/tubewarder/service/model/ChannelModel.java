package net.weweave.tubewarder.service.model;

import net.weweave.tubewarder.domain.Channel;
import net.weweave.tubewarder.outputhandler.OutputHandlerConfigUtil;
import net.weweave.tubewarder.outputhandler.api.Config;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ChannelModel extends AbstractRestModel {
    public String name;
    public Config config;

    public static ChannelModel factory(Channel channel) {
        ChannelModel model = new ChannelModel();
        model.id = channel.getExposableId();
        model.name = channel.getName();
        model.config = OutputHandlerConfigUtil.configJsonStringToMap(channel.getConfigJson());
        return model;
    }
}
