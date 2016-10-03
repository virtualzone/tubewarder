"use strict";
(function (ErrorCode) {
    ErrorCode[ErrorCode["OK"] = 0] = "OK";
    ErrorCode[ErrorCode["INVALID_INPUT_PARAMETERS"] = 1] = "INVALID_INPUT_PARAMETERS";
    ErrorCode[ErrorCode["OBJECT_LOOKUP_ERROR"] = 2] = "OBJECT_LOOKUP_ERROR";
    ErrorCode[ErrorCode["PERMISSION_DENIED"] = 3] = "PERMISSION_DENIED";
    ErrorCode[ErrorCode["AUTH_REQUIRED"] = 4] = "AUTH_REQUIRED";
    ErrorCode[ErrorCode["TEMPLATE_CORRUPT"] = 5] = "TEMPLATE_CORRUPT";
    ErrorCode[ErrorCode["MISSING_MODEL_PARAMETER"] = 6] = "MISSING_MODEL_PARAMETER";
    ErrorCode[ErrorCode["FIELD_REQUIRED"] = 101] = "FIELD_REQUIRED";
    ErrorCode[ErrorCode["FIELD_NAME_ALREADY_EXISTS"] = 102] = "FIELD_NAME_ALREADY_EXISTS";
    ErrorCode[ErrorCode["FIELD_INVALID"] = 103] = "FIELD_INVALID";
})(exports.ErrorCode || (exports.ErrorCode = {}));
var ErrorCode = exports.ErrorCode;
//# sourceMappingURL=error-code.js.map