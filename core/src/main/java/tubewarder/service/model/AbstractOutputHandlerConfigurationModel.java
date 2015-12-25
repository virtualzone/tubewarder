package tubewarder.service.model;

import tubewarder.domain.AbstractOutputHandlerConfiguration;
import tubewarder.domain.EmailOutputHandlerConfiguration;
import tubewarder.domain.SysoutOutputHandlerConfiguration;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public abstract class AbstractOutputHandlerConfigurationModel extends AbstractRestModel {
    public String name;
    public String type;

    public static AbstractOutputHandlerConfigurationModel factory(AbstractOutputHandlerConfiguration config) {
        AbstractOutputHandlerConfigurationModel model = null;
        if (config instanceof SysoutOutputHandlerConfiguration) {
            model = new SysoutOutputHandlerConfigurationModel();
            model.type = "SYSOUT";
            SysoutOutputHandlerConfigurationModel.completeFactory((SysoutOutputHandlerConfigurationModel)model, (SysoutOutputHandlerConfiguration)config);
        } else if (config instanceof EmailOutputHandlerConfiguration) {
            model = new EmailOutputHandlerConfigurationModel();
            model.type = "EMAIL";
            EmailOutputHandlerConfigurationModel.completeFactory((EmailOutputHandlerConfigurationModel)model, (EmailOutputHandlerConfiguration)config);
        }
        model.id = config.getExposableId();
        model.name = config.getName();
        return model;
    }
}
