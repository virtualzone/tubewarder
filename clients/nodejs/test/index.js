const should = require('chai').should();
const TubewarderClient = require('../index');

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
        sr.attachments[0].payload.should.equal('1234');
        sr.attachments[0].contentType.should.equal('text/plain');
        sr.attachments[1].filename.should.equal('file2.jpg');
        sr.attachments[1].payload.should.equal('abcd');
        sr.attachments[1].contentType.should.equal('image/jpeg');
    });
});

describe('#SendRequest.getJson', function() {
    it('should return the correct JSON for this request', function() {
        var client = new TubewarderClient('https://localhost:8080');
        
        var sr = client.createSendRequest('1234567890');
        sr.template = 'DOI';
        sr.channel = 'Email';
        sr.recipient.address = 'noreply@weweave.net';
        sr.recipient.name = 'weweave';
        sr.addModelParam('firstname', 'John');
        sr.addModelParam('lastname', 'Doe');
        sr.keyword = 'DOI123';
        sr.details = 'some uninterpreted text';
        sr.echo = true;
        
        var a1 = sr.createAttachment('file1.txt');
        a1.payload = '1234';
        a1.contentType = 'text/plain';
        
        var a2 = sr.createAttachment('file2.jpg');
        a2.payload = 'abcd';
        a2.contentType = 'image/jpeg';
        
        var json = sr.getJson();
        json.token.should.equal('1234567890');
        json.template.should.equal('DOI');
        json.channel.should.equal('Email');
        json.recipient.should.be.an('object');
        json.recipient.address.should.equal('noreply@weweave.net');
        json.recipient.name.should.equal('weweave');
        json.model.should.be.an('array');
        json.model.should.have.length(2);
        json.model[0].should.deep.equal({'key': 'firstname', 'value': 'John'});
        json.model[1].should.deep.equal({'key': 'lastname', 'value': 'Doe'});
        json.keyword.should.equal('DOI123');
        json.details.should.equal('some uninterpreted text');
        json.echo.should.equal(true);
        json.attachments.should.have.length(2);
        json.attachments[0].filename.should.equal('file1.txt');
        json.attachments[0].payload.should.equal('1234');
        json.attachments[0].contentType.should.equal('text/plain');
        json.attachments[1].filename.should.equal('file2.jpg');
        json.attachments[1].payload.should.equal('abcd');
        json.attachments[1].contentType.should.equal('image/jpeg');
    });
});

describe('#send', function() {
    /**
     * The test cases below are disabled by default because they require a running
     * Tubewarder Server. You can enable them on your behalf.
     */

    /*
    it('should send successfully', function(done) {
        var client = new TubewarderClient('http://localhost:8080');
        var sr = client.createSendRequest('your-access-token');
        sr.template = 'DOI';
        sr.channel = 'Email';
        sr.recipient.address = 'some@email.address';
        sr.recipient.name = 'some recipient name';
        sr.addModelParam('firstname', 'John');
        sr.addModelParam('lastname', 'Doe');
        sr.keyword = 'DOI123';
        sr.details = 'some uninterpreted text';
        sr.echo = true;
        client.send(sr, function(res) {
            res.should.be.an('object');
            done();
        }, function(e) {
            'this should not happen'.echo.should.equal(false);
            done();
        });
    });

    it('should throw a network error on wrong host', function(done) {
        var client = new TubewarderClient('http://localhoooost:8080');
        var sr = client.createSendRequest('your-access-token');
        client.send(sr, function(res) {
            'this should not happen'.echo.should.equal(false);
            done();
        }, function(e) {
            e.should.be.an('object');
            e.networkError.should.equal(true);
            e.networkErrorCode.should.equal('ENOTFOUND');
            done();
        });
    });

    it('should throw a network error on wrong protocol', function(done) {
        var client = new TubewarderClient('https://localhost:8080');
        var sr = client.createSendRequest('your-access-token');
        client.send(sr, function(res) {
            'this should not happen'.echo.should.equal(false);
            done();
        }, function(e) {
            e.should.be.an('object');
            e.networkError.should.equal(true);
            e.networkErrorCode.should.equal('EPROTO');
            done();
        });
    });

    it('should throw a http error on wrong path', function(done) {
        var client = new TubewarderClient('http://localhost:8080/something/');
        var sr = client.createSendRequest('your-access-token');
        client.send(sr, function(res) {
            'this should not happen'.echo.should.equal(false);
            done();
        }, function(e) {
            e.should.be.an('object');
            e.httpError.should.equal(true);
            e.httpStatusCode.should.equal(405);
            done();
        });
    });
    */
});
