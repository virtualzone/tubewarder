package net.weweave.tubewarder.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A request to be sent to a Tubewarder server.
 * Please read the docs at tubewarder.readthedocs.io.
 * @see <a href="http://tubewarder.readthedocs.io/en/latest/">tubewarder.readthedocs.io</a>
 */
public class SendRequest implements Serializable {
    private String token;
    private String template;
    private String channel;
    private Address recipient;
    private List<KeyValue> model = new ArrayList<>();
    private List<Attachment> attachments = new ArrayList<>();
    private String keyword;
    private String details;
    private Boolean echo = false;

    public SendRequest() {

    }

    public SendRequest(String token) {
        setToken(token);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Address getRecipient() {
        return recipient;
    }

    public void setRecipient(Address recipient) {
        this.recipient = recipient;
    }

    public List<KeyValue> getModel() {
        return model;
    }

    public void addModelParam(KeyValue kv) {
        this.model.add(kv);
    }

    public void setModelFromMap(Map<String, Object> map) {
        this.model = new ArrayList<>();
        for (Map.Entry<String, Object> e : map.entrySet()) {
            addModelParam(new KeyValue(e.getKey(), e.getValue()));
        }
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void addAttachment(Attachment attachment) {
        this.attachments.add(attachment);
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Boolean getEcho() {
        return echo;
    }

    public void setEcho(Boolean echo) {
        this.echo = echo;
    }
}
