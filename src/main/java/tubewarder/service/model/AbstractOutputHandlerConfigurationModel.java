package tubewarder.service.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public abstract class AbstractOutputHandlerConfigurationModel extends AbstractRestModel {
    public String name;
    public String type;
}
