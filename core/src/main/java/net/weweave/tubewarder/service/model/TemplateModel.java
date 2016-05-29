package net.weweave.tubewarder.service.model;

import net.weweave.tubewarder.domain.ChannelTemplate;
import net.weweave.tubewarder.domain.Template;
import net.weweave.tubewarder.outputhandler.OutputHandlerFactory;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class TemplateModel extends AbstractRestModel {
    public String name;
    public UserGroupModel group;
    public List<ChannelTemplateModel> channelTemplates = new ArrayList<>();

    public static TemplateModel factory(Template template, OutputHandlerFactory factory) {
        TemplateModel model = new TemplateModel();
        model.id = template.getExposableId();
        model.name = template.getName();
        model.group = (template.getUserGroup() == null) ? null : UserGroupModel.factorySmall(template.getUserGroup());
        for (ChannelTemplate channelTemplate : template.getChannelTemplates()) {
            model.channelTemplates.add(ChannelTemplateModel.factory(channelTemplate, factory, false));
        }
        return model;
    }
}
