package net.weweave.tubewarder.service.response;

import net.weweave.tubewarder.service.model.ErrorCode;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AbstractResponse {
    public Integer error = ErrorCode.OK;
}
