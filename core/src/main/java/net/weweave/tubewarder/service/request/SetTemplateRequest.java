package net.weweave.tubewarder.service.request;

import net.weweave.tubewarder.service.model.TemplateModel;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SetTemplateRequest extends AbstractSetObjectRestRequest<TemplateModel> {
}
