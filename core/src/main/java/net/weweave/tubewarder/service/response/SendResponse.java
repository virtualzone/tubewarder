package net.weweave.tubewarder.service.response;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SendResponse extends AbstractResponse {
    public String subject = "";
    public String content = "";
}
