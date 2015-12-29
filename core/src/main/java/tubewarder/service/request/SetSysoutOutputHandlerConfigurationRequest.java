package tubewarder.service.request;

import tubewarder.service.model.SysoutOutputHandlerConfigurationModel;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SetSysoutOutputHandlerConfigurationRequest extends AbstractSetObjectRestRequest<SysoutOutputHandlerConfigurationModel> {
}
