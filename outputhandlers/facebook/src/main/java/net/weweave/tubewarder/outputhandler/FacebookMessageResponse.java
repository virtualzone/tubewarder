package net.weweave.tubewarder.outputhandler;

import java.io.Serializable;

public class FacebookMessageResponse implements Serializable {
    private String recipient_id;
    private String message_id;
    private FacebookError error;

    public String getRecipient_id() {
        return recipient_id;
    }

    public void setRecipient_id(String recipient_id) {
        this.recipient_id = recipient_id;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public FacebookError getError() {
        return error;
    }

    public void setError(FacebookError error) {
        this.error = error;
    }
}
