package tubewarder.service.response;

import tubewarder.service.model.TemplateModel;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class GetTemplateResponse extends AbstractResponse {
    public List<TemplateModel> templates = new ArrayList<>();
}
