package tubewarder.service.model;

import com.fasterxml.jackson.annotation.JsonTypeId;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import tubewarder.domain.AbstractOutputHandlerConfiguration;
import tubewarder.domain.EmailOutputHandlerConfiguration;
import tubewarder.domain.SysoutOutputHandlerConfiguration;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@JsonTypeInfo(use= JsonTypeInfo.Id.CUSTOM, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonTypeIdResolver(OutputHandlerConfigurationModelResolver.class)
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
