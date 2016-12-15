package net.weweave.tubewarder.outputhandler;

import java.io.Serializable;

public class FacebookMessage implements Serializable {
    private String text;

    public FacebookMessage(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
