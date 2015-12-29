package tubewarder.service.request;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AbstractIdRestRequest extends AbstractRestRequest {
    public String id;
}
