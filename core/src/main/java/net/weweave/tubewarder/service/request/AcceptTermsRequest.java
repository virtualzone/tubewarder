package net.weweave.tubewarder.service.request;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AcceptTermsRequest extends AbstractRestRequest {
    public Boolean termsAccepted;
}
