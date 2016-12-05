package net.weweave.tubewarder.service.model;

import com.fasterxml.jackson.databind.JsonNode;
import net.weweave.tubewarder.outputhandler.api.Attachment;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public abstract class SendModel {
    @XmlElement(required = true)
    public String token;
    @XmlElement(required = true)
    public String template;
    @XmlElement(required = true)
    public String channel;
    @XmlElement(required = true)
    public AddressModel recipient;
    public List<AttachmentModel> attachments;
    public String keyword;
    public String details;
    public Boolean echo = false;

    public List<Attachment> attachmentModelToList() {
        List<Attachment> result = new ArrayList<>();
        if (attachments != null) {
            for (AttachmentModel model : attachments) {
                result.add(model.toAttachment());
            }
        }
        return result;
    }

    public abstract JsonNode getModelAsJsonNode() throws IOException;
}
