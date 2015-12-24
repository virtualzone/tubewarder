package eio.service.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AttachmentModel {
    @XmlElement(required = true)
    public String filename;
    public String contentType;
    @XmlElement(required = true)
    public String payload;
}
