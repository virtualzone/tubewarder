var should = require('chai').should();
var TubewarderClient = require('../index');

describe('#construct', function() {
    it('appends a trailing slash to a uri', function() {
        var client = new TubewarderClient('https://localhost:8080');
        client.uri.should.equal('https://localhost:8080/');
    });
    
    it('should not append a trailing slash if already present', function() {
        var client = new TubewarderClient('https://xyz:8080/');
        client.uri.should.equal('https://xyz:8080/');
    });
});

describe('#createSendRequest', function() {
    it('return a default-initialized object', function() {
        var client = new TubewarderClient('https://localhost:8080');
        var sr = client.createSendRequest('1234567890');
        sr.should.be.an('object');
        sr.token.should.equal('1234567890');
        sr.template.should.equal('');
        sr.channel.should.equal('');
        sr.recipient.address.should.equal('');
        sr.recipient.name.should.equal('');
        sr.model.should.be.an('object');
        sr.attachments.should.be.an('array');
        sr.keyword.should.equal('');
        sr.details.should.equal('');
        sr.echo.should.equal(false);
    });
});

describe('#SendRequest.addModelParam', function() {
    it('should add params to the model', function() {
        var client = new TubewarderClient('https://localhost:8080');
        var sr = client.createSendRequest('1234567890');
        sr.addModelParam('firstname', 'John');
        sr.addModelParam('lastname', 'Doe');
        sr.model.should.have.property('firstname', 'John');
        sr.model.should.have.property('lastname', 'Doe');
    });
});

describe('#SendRequest.createAttachment', function() {
    it('should add a new attachment to the request', function() {
        var client = new TubewarderClient('https://localhost:8080');
        var sr = client.createSendRequest('1234567890');
        var a1 = sr.createAttachment('file1.txt');
        a1.payload = '1234';
        a1.contentType = 'text/plain';
        var a2 = sr.createAttachment('file2.jpg');
        a2.payload = 'abcd';
        a2.contentType = 'image/jpeg';
        sr.attachments.should.have.length(2);
        sr.attachments[0].filename.should.equal('file1.txt');
        sr.attachments[1].filename.should.equal('file2.jpg');
    });
});
