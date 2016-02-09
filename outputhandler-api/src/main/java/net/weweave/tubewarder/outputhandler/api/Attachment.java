package net.weweave.tubewarder.outputhandler.api;

/**
 * An (email) attachment. An attachment has:
 * <ul>
 *     <li>A filename (without path)</li>
 *     <li>A content type (MIME)</li>
 *     <li>The actual payload, encoded in Base64</li>
 * </ul>
 */
public class Attachment {
    private String filename;
    private String contentType;
    private String payload;

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
