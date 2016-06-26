package net.weweave.tubewarder.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A response returned by the send() method of a {@link TubewarderClient}.
 */
public class SendResponse implements Serializable {
    private Integer error;
    private Address recipient;
    private String subject;
    private String content;
    private String queueId;
    public Map<String, ArrayList<Integer>> fieldErrors = new HashMap<>();

    public Integer getError() {
        return error;
    }

    public void setError(Integer error) {
        this.error = error;
    }

    public Address getRecipient() {
        return recipient;
    }

    public void setRecipient(Address recipient) {
        this.recipient = recipient;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getQueueId() {
        return queueId;
    }

    public void setQueueId(String queueId) {
        this.queueId = queueId;
    }

    public Map<String, ArrayList<Integer>> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(Map<String, ArrayList<Integer>> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }
}
