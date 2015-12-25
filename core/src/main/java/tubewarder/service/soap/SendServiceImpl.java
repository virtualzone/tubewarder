package tubewarder.service.soap;

import tubewarder.service.common.SendServiceCommon;
import tubewarder.service.model.AbstractResponse;
import tubewarder.service.model.SendModel;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;

@RequestScoped
@WebService(endpointInterface = "tubewarder.service.soap.SendService", serviceName = "ws/send", portName = "ws/send/port")
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
