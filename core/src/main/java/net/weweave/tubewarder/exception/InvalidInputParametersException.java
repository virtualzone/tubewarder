package net.weweave.tubewarder.exception;

public class InvalidInputParametersException extends Exception {
    private String field;
    private Integer errorCode;

    public InvalidInputParametersException() {
        super();
    }

    public InvalidInputParametersException(String field, Integer errorCode) {
        super();
        this.field = field;
        this.errorCode = errorCode;
    }

    public InvalidInputParametersException(String message) {
        super(message);
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }
}
