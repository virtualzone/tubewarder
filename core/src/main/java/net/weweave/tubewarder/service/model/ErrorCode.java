package net.weweave.tubewarder.service.model;

public class ErrorCode {
    public static final int OK = 0;
    public static final int INVALID_INPUT_PARAMETERS = 1;
    public static final int OBJECT_LOOKUP_ERROR = 2;
    public static final int PERMISSION_DENIED = 3;
    public static final int AUTH_REQUIRED = 4;
    public static final int TEMPLATE_CORRUPT = 5;
    public static final int MISSING_MODEL_PARAMETER = 6;

    public static final int FIELD_REQUIRED = 101;
    public static final int FIELD_NAME_ALREADY_EXISTS = 102;
    public static final int FIELD_INVALID = 103;
}
