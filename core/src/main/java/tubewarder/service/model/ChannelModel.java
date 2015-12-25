package tubewarder.service.model;

import tubewarder.domain.Channel;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ChannelModel extends AbstractRestModel {
    public String name;
    public String outputHandler;
    public AbstractOutputHandlerConfigurationModel config;

    public static ChannelModel factory(Channel channel) {
        ChannelModel model = new ChannelModel();
        model.id = channel.getExposableId();
        model.name = channel.getName();
        model.outputHandler = channel.getOutputHandler().toString();
        model.config = AbstractOutputHandlerConfigurationModel.factory(channel.getConfig());
        return model;
    }
}
