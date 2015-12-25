package tubewarder.service.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SysoutOutputHandlerConfigurationModel extends AbstractOutputHandlerConfigurationModel {
    public String prefix;
    public String suffix;
}
