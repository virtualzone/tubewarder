package net.weweave.tubewarder.outputhandler;

import java.io.Serializable;

public class FacebookMessageRequest implements Serializable {
    private FacebookRecipient recipient;
    private FacebookMessage message;

    public FacebookMessageRequest(FacebookRecipient recipient, FacebookMessage message) {
        this.recipient = recipient;
        this.message = message;
    }

    public FacebookRecipient getRecipient() {
        return recipient;
    }

    public void setRecipient(FacebookRecipient recipient) {
        this.recipient = recipient;
    }

    public FacebookMessage getMessage() {
        return message;
    }

    public void setMessage(FacebookMessage message) {
        this.message = message;
    }
}
