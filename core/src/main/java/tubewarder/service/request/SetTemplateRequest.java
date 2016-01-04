package tubewarder.service.request;

import tubewarder.service.model.TemplateModel;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SetTemplateRequest extends AbstractSetObjectRestRequest<TemplateModel> {
}
