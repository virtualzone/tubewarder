package net.weweave.tubewarder.outputhandler.api;

import java.util.ArrayList;
import java.util.List;

/**
 * The item to be sent. This is passed to the IOutputHandler.process() method.
 */
public class SendItem {
    private Address sender;
    private Address recipient;
    private String subject;
    private String content;
    private final List<Attachment> attachments;

    public SendItem() {
        attachments = new ArrayList<>();
    }

    /**
     * @return The sender address
     */
    public Address getSender() {
        return sender;
    }

    public void setSender(Address sender) {
        this.sender = sender;
    }

    /**
     * @return The recipient address
     */
    public Address getRecipient() {
        return recipient;
    }

    public void setRecipient(Address recipient) {
        this.recipient = recipient;
    }

    /**
     * @return The rendered subject to process (may not be applicable for the concrete output handler)
     */
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * @return The rendered content to process
     */
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return A list of attachments (may not be applicable for the concrete output handler)
     */
    public List<Attachment> getAttachments() {
        return attachments;
    }
}
