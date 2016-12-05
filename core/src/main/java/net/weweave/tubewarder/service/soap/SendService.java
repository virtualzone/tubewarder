package net.weweave.tubewarder.service.soap;

import net.weweave.tubewarder.service.model.SoapSendModel;
import net.weweave.tubewarder.service.response.SendServiceResponse;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;

@WebService
public interface SendService {
    @WebMethod
    SendServiceResponse send(@XmlElement(name = "message", required = true) @WebParam(name = "message") SoapSendModel sendModel);
}
