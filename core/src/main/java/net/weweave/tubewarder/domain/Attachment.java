package net.weweave.tubewarder.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Attachment extends AbstractPersistentObject {
    @ManyToOne
    private SendQueueItem sendQueueItem;
    private String filename;
    private String contentType;
    @Column(length = 100000)
    private String payload;

    public SendQueueItem getSendQueueItem() {
        return sendQueueItem;
    }

    public void setSendQueueItem(SendQueueItem sendQueueItem) {
        this.sendQueueItem = sendQueueItem;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
