package tubewarder.service.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AbstractResponse {
    public Integer error = ErrorCode.OK;
}
