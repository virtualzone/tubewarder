package net.weweave.tubewarder.service.soap;

import net.weweave.tubewarder.service.response.AbstractResponse;
import net.weweave.tubewarder.service.model.SendModel;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;

@WebService
public interface SendService {
    @WebMethod
    AbstractResponse send(@XmlElement(name = "message", required = true) @WebParam(name = "message") SendModel sendModel);
}
