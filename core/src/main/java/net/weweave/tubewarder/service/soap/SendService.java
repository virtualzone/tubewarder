package net.weweave.tubewarder.service.soap;

import net.weweave.tubewarder.service.model.SendModel;
import net.weweave.tubewarder.service.response.SendResponse;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;

@WebService
public interface SendService {
    @WebMethod
    SendResponse send(@XmlElement(name = "message", required = true) @WebParam(name = "message") SendModel sendModel);
}
