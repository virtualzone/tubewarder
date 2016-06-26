Tubewarder NodeJS Client
========================

NodeJS Client for the Tubewarder Central Messaging Gateway. For more information, see: http://weweave.net/products/tubewarder/

## Installation
```
npm install tubewarder-client --save
```

## Usage
```
const fs = require('fs');
const TubewarderClient = require('tubewarder-client');

var client = new TubewarderClient('https://localhost:8080');
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

var a1 = sr.createAttachment('file1.txt');
a1.contentType = 'text/plain';
a1.payload = new Buffer(fs.readFileSync('file.txt')).toString('base64');

client.send(sr, function(res) {
    if (!res.error) {
        console.log('Recipient address: ' + res.recipient.address);
        console.log('Recipient name:    ' + res.recipient.name);
        console.log('Subject:           ' + res.subject);
        console.log('Body:              ' + res.body);
        console.log('Queue ID:          ' + res.queueId);
    } else {
        console.log('Application error code: ' + res.error);
    }
}, function(e) {
    if (e.networkError) {
        console.log('A network error occurred: ' + e.networkErrorCode);
    } else if (e.httpError) {
        console.log('An http error occurred: ' + e.httpStatusCode);
    }
});
```

## Tests
```
npm test
```
