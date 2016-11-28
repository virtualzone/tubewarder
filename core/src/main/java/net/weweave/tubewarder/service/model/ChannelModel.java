package net.weweave.tubewarder.service.model;

import net.weweave.tubewarder.domain.Channel;
import net.weweave.tubewarder.outputhandler.OutputHandlerConfigUtil;
import net.weweave.tubewarder.outputhandler.OutputHandlerFactory;
import net.weweave.tubewarder.outputhandler.api.Config;
import org.apache.commons.validator.GenericValidator;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ChannelModel extends AbstractRestModel {
    public String name;
    public UserGroupModel group;
    public String rewriteRecipientName;
    public String rewriteRecipientAddress;
    public String rewriteSubject;
    public String rewriteContent;
    public Config config;
    public String outputHandlerReadableName;

    public static ChannelModel factory(Channel channel, OutputHandlerFactory factory) {
        ChannelModel model = new ChannelModel();
        model.id = channel.getExposableId();
        model.name = channel.getName();
        model.group = (channel.getUserGroup() == null) ? null : UserGroupModel.factorySmall(channel.getUserGroup());
        model.rewriteRecipientName = (GenericValidator.isBlankOrNull(channel.getRewriteRecipientName()) ? "{{recipientName}}" : channel.getRewriteRecipientName());
        model.rewriteRecipientAddress = (GenericValidator.isBlankOrNull(channel.getRewriteRecipientAddress()) ? "{{recipientAddress}}" : channel.getRewriteRecipientAddress());
        model.rewriteSubject = (GenericValidator.isBlankOrNull(channel.getRewriteSubject()) ? "{{subject}}" : channel.getRewriteSubject());
        model.rewriteContent = (GenericValidator.isBlankOrNull(channel.getRewriteContent()) ? "{{content}}" : channel.getRewriteContent());
        model.config = OutputHandlerConfigUtil.configJsonStringToMap(channel.getConfigJson());
        model.outputHandlerReadableName = factory.getNameForId(model.config.getString("id"));
        return model;
    }
}
