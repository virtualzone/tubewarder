package net.weweave.tubewarder.outputhandler;

import java.io.Serializable;

public class FacebookError implements Serializable {
    private String message;
    private String type;
    private Long code;
    private Long error_subcode;
    private String fbtrace_id;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public Long getError_subcode() {
        return error_subcode;
    }

    public void setError_subcode(Long error_subcode) {
        this.error_subcode = error_subcode;
    }

    public String getFbtrace_id() {
        return fbtrace_id;
    }

    public void setFbtrace_id(String fbtrace_id) {
        this.fbtrace_id = fbtrace_id;
    }
}
