package net.weweave.tubewarder.service.response;

import net.weweave.tubewarder.service.model.AddressModel;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SendServiceResponse extends AbstractResponse {
    public AddressModel recipient = new AddressModel();
    public String subject = "";
    public String content = "";
}
