package net.weweave.tubewarder.service.response;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CheckLicenseResponse extends AbstractResponse {
    public Boolean termsAccepted = false;
    public Boolean licensed = false;
}
