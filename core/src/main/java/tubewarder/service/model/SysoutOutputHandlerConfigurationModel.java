package tubewarder.service.model;

import tubewarder.domain.SysoutOutputHandlerConfiguration;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SysoutOutputHandlerConfigurationModel extends AbstractOutputHandlerConfigurationModel {
    public String prefix;
    public String suffix;

    public static void completeFactory(SysoutOutputHandlerConfigurationModel model, SysoutOutputHandlerConfiguration config) {
        model.prefix = config.getPrefix();
        model.suffix = config.getSuffix();
    }
}
