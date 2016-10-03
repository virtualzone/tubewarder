"use strict";
var __extends = (this && this.__extends) || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
};
var rest_response_1 = require('./rest-response');
var AuthResponse = (function (_super) {
    __extends(AuthResponse, _super);
    function AuthResponse() {
        _super.apply(this, arguments);
    }
    return AuthResponse;
}(rest_response_1.RestResponse));
exports.AuthResponse = AuthResponse;
//# sourceMappingURL=auth-response.js.map