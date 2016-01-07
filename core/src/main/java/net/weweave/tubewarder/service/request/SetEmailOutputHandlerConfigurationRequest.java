package net.weweave.tubewarder.service.request;

import net.weweave.tubewarder.service.model.EmailOutputHandlerConfigurationModel;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SetEmailOutputHandlerConfigurationRequest extends AbstractSetObjectRestRequest<EmailOutputHandlerConfigurationModel> {
}
