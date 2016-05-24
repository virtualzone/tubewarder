(function() {
    'use strict';
    
    var request = require('request');
    
    class Address {
        constructor(address, name) {
            this.address = address;
            this.name = name;
        }
        
        getAsObject() {
            return {
                address: this.address,
                name: this.name
            };
        }
    };
    
    class Attachment {
        constructor(filename) {
            this.filename = filename;
            this.contentType = '';
            this.payload = '';
        }
    };
    
    class SendRequest {
        constructor(token) {
            this.token = token;
            this.template = '';
            this.channel = '';
            this.recipient = new Address('', '');
            this.model = {};
            this.attachments = [];
            this.keyword = '';
            this.details = '';
            this.echo = false;
        }
        
        addModelParam(k, v) {
            this.model[k] = v;
        }
        
        createAttachment(filename) {
            var a = new Attachment(filename);
            this.attachments.push(a);
            return a;
        }
        
        getKeyValueModel() {
            var a = [];
            for (var k in this.model) {
                a.push({
                    'key': k,
                    'value': this.model[k]
                });
            }
            return a;
        }
        
        getAttachmentsAsObjectArray() {
            var o = [];
            for (var i=0; i<this.attachments.length; i++) {
                var a = this.attachments[i];
                o.push({
                    filename: a.filename,
                    contentType: a.contentType,
                    payload: a.payload
                });
            }
            return o;
        }
        
        getJson() {
            return {
                token: this.token,
                template: this.template,
                channel: this.channel,
                recipient: this.recipient.getAsObject(),
                model: this.getKeyValueModel(),
                attachments: this.getAttachmentsAsObjectArray(),
                keyword: this.keyword,
                details: this.details,
                echo: this.echo
            };
        }
    };
    
    class SendResponse {
        constructor(error) {
            this.error = error;
            this.recipient = new Address('', '');
            this.subject = '';
            this.content = '';
            this.queueId = '';
        }
    };
    
    module.exports = class TubewarderClient {
        constructor(uri) {
            this.uri = uri;
            if (!this.uri.endsWith('/')) {
                this.uri += '/';
            }
            
        }
        
        /**
         * Sends a message.
         * 
         * @param {SendRequest} sendRequest
         * @param {Function} cb
         * @returns {SendResponse}
         */
        send(sendRequest, cb) {
            var options = {
                url: this.uri + 'rs/send',
                json: sendRequest.getJson(),
                headers: {
                    'Content-Type': 'application/json'
                }
            };
            request.post(options, function(error, response, body) {
                var res = new SendResponse(body.error);
                res.recipient = new Address(body.recipient.address, body.recipient.name);
                res.subject = body.subject;
                res.content = body.content;
                res.queueId = body.queueId;
                cb(res);
            });
        }
        
        /**
         * Creates a new instance of SendRequest.
         * 
         * @param {String} token
         * @returns {SendRequest}
         */
        createSendRequest(token) {
            var sr = new SendRequest(token);
            return sr;
        }
    };
}());
