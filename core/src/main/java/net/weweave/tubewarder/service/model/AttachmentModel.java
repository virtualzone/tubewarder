package net.weweave.tubewarder.service.model;

import net.weweave.tubewarder.outputhandler.api.Attachment;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AttachmentModel {
    @XmlElement(required = true)
    public String filename;
    public String contentType;
    @XmlElement(required = true)
    public String payload;

    public Attachment toAttachment() {
        Attachment attachment = new Attachment();
        attachment.setContentType(contentType);
        attachment.setFilename(filename);
        attachment.setPayload(payload);
        return attachment;
    }
}
