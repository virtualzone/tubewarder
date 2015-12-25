package tubewarder.service.response;

import tubewarder.service.model.ErrorCode;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AbstractResponse {
    public Integer error = ErrorCode.OK;
}
