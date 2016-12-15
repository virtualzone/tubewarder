package net.weweave.tubewarder.outputhandler;

import java.io.Serializable;

public class FacebookRecipient implements Serializable {
    private String id;

    public FacebookRecipient(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
