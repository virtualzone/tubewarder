(function() {
    'use strict';
    
    var request = require('request');
    var querystring = require('querystring');
    
    class Address {
        constructor(address, name) {
            this.address = address;
            this.name = name;
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
            let a = new Attachment(filename);
            this.attachments.push(a);
            return a;
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
         * @param {Object} sendRequest
         * @returns {Object}
         */
        send(sendRequest) {
            var options = {
                url: this.uri + 'rs/send',
                form: {
                    token: sendRequest.token,
                    template: sendRequest.template,
                    channel: sendRequest.channel
                },
                json: true
            };
            request.post(options, function(error, response, body) {
                console.log(response);
            });
        }
        
        createSendRequest(token) {
            let sr = new SendRequest(token);
            return sr;
        }
    };
}());
