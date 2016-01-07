package net.weweave.tubewarder.service.soap;

import net.weweave.tubewarder.service.common.SendServiceCommon;
import net.weweave.tubewarder.service.response.AbstractResponse;
import net.weweave.tubewarder.service.model.SendModel;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;

@RequestScoped
@WebService(endpointInterface = "net.weweave.tubewarder.service.soap.SendService", serviceName = "ws/send", portName = "ws/send/port")
public class SendServiceImpl implements SendService {
    @Inject
    private SendServiceCommon sendServiceCommon;

    @Override
    public AbstractResponse send(@XmlElement(name = "message", required = true) @WebParam(name = "message") SendModel sendModel) {
        return getSendServiceCommon().process(sendModel);
    }

    public SendServiceCommon getSendServiceCommon() {
        return sendServiceCommon;
    }

    public void setSendServiceCommon(SendServiceCommon sendServiceCommon) {
        this.sendServiceCommon = sendServiceCommon;
    }
}
