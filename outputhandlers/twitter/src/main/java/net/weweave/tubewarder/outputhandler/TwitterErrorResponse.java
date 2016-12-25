package net.weweave.tubewarder.outputhandler;

import java.util.ArrayList;
import java.util.List;

public class TwitterErrorResponse {
    private List<TwitterError> errors = new ArrayList<>();

    public List<TwitterError> getErrors() {
        return errors;
    }

    public void setErrors(List<TwitterError> errors) {
        this.errors = errors;
    }
}
