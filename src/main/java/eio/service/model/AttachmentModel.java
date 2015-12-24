package eio.service.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AttachmentModel {
    public String filename;
    public String contentType;
    public String payload;
}
