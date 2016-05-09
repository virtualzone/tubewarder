package net.weweave.tubewarder.client;

import java.io.Serializable;
import java.util.Base64;

/**
 * An attachment for a message to send.
 * Payload is a Base64 encoded string.
 */
public class Attachment implements Serializable {
    private String filename;
    private String contentType;
    private String payload;

    public Attachment() {

    }

    public Attachment(String filename) {
        this.setFilename(filename);
    }

    public Attachment(String filename, byte[] bytes) {
        this.setFilename(filename);
        setPayload(bytes);
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

    public void setPayload(byte[] bytes) {
        this.payload = new String(Base64.getEncoder().encode(bytes));
    }
}
