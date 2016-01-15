package net.weweave.tubewarder.service.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import net.weweave.tubewarder.domain.SysoutOutputHandlerConfiguration;
import net.weweave.tubewarder.domain.AbstractOutputHandlerConfiguration;
import net.weweave.tubewarder.domain.EmailOutputHandlerConfiguration;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@JsonTypeInfo(use= JsonTypeInfo.Id.CUSTOM, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonTypeIdResolver(OutputHandlerConfigurationModelResolver.class)
public abstract class AbstractOutputHandlerConfigurationModel extends AbstractRestModel {
    public String name;
    //public String type;

    public static AbstractOutputHandlerConfigurationModel factory(AbstractOutputHandlerConfiguration config) {
        AbstractOutputHandlerConfigurationModel model = null;
        if (config instanceof SysoutOutputHandlerConfiguration) {
            model = new SysoutOutputHandlerConfigurationModel();
            SysoutOutputHandlerConfigurationModel.completeFactory((SysoutOutputHandlerConfigurationModel)model, (SysoutOutputHandlerConfiguration)config);
        } else if (config instanceof EmailOutputHandlerConfiguration) {
            model = new EmailOutputHandlerConfigurationModel();
            EmailOutputHandlerConfigurationModel.completeFactory((EmailOutputHandlerConfigurationModel)model, (EmailOutputHandlerConfiguration)config);
        }
        model.id = config.getExposableId();
        model.name = config.getName();
        return model;
    }
}
