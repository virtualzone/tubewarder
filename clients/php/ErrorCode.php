<?php
namespace Tubewarder;

abstract class ErrorCode {
    const OK = 0;
    const INVALID_INPUT_PARAMETERS = 1;
    const OBJECT_LOOKUP_ERROR = 2;
    const PERMISSION_DENIED = 3;
    const AUTH_REQUIRED = 4;
    const TEMPLATE_CORRUPT = 5;
    const MISSING_MODEL_PARAMETER = 6;

    const FIELD_REQUIRED = 101;
    const FIELD_NAME_ALREADY_EXISTS = 102;
    const FIELD_INVALID = 103;
}