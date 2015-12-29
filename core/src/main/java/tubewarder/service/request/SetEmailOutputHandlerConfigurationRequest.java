package tubewarder.service.request;

import tubewarder.service.model.EmailOutputHandlerConfigurationModel;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SetEmailOutputHandlerConfigurationRequest extends AbstractSetObjectRestRequest<EmailOutputHandlerConfigurationModel> {
}
