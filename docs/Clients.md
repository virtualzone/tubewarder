# Clients for sending to Tubewarder
Instead of directly dealing with the Send API, you can instead use pre-implemented client libraries. These libraries hide the SOAP and REST interfaces from the developer, so you can focus on the actual sending of messages.

The following client libraries are currently available:

* Java
* NodeJS (NPM)
* PHP

Libs for more languages will follow. In the meantime, you can of course connect directly to the Send API.


# Java
Tubewarder' is Java Client currently supports the REST API. It employs the JAX-RS 2.0 API, so you will need a JEE6 or newer application server to use it.

To get started with the Java Client library, the easiest way is to include the following dependency in your Maven pom.xml:

```
<dependency>
    <groupId>net.weweave.tubewarder</groupId>
    <artifactId>client</artifactId>
    <version>1.0.5</version>
</dependency>
```

Your source code might look like this:

```
TubewarderClient client = new TubewarderRestClient("http://your-tubewarder-server");
SendRequest req = new SendRequest("your-app-token");
req.setChannel("Email");
req.setTemplate("Welcome");
req.addModelParam(new KeyValue("firstname", "..."));
SendResponse resp = client.send(req);
```

# NodeJS (NPM)
The [NodeJS/NPM Client](https://www.npmjs.com/package/tubewarder-client) supports the REST API. It is written in ECMAScript 2015 (ES6), so make sure you're using an up-to-date version of NodeJS (successfully tested in NodeJS 6 and newer versions).

Here is an example code which covers all the relevant functionalities of the client library:

```
const fs = require('fs');
const TubewarderClient = require('tubewarder-client');

var client = new TubewarderClient('https://localhost:8080');
var sr = client.createSendRequest('your-app-token');
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

Note that the fs library is only required in this example as the attached file is loaded with it. You may not require it in your case.

# PHP
The [PHP client](https://weweave.net/products/tubewarder/) (get it from the "Download" tab) supports the REST API. It requires PHP >= 5.3.

Here is an example taken from the "examples" folder included in the downloadable ZIP file linked above.

```
<?php
// Include the required files
require('libs/tubewarder/TubewarderRestClient.php');

// Import the classes
use Tubewarder\TubewarderRestClient as TubewarderRestClient;
use Tubewarder\SendRequest as SendRequest;
use Tubewarder\Address as Address;
use Tubewarder\ErrorCode as ErrorCode;

// Create a new client
$client = new TubewarderRestClient('http://localhost:8080');

// Prepare the send request
$sr = new SendRequest('your-app-token');
$sr->setChannel('Sysout');
$sr->setTemplate('Dummy');
$sr->setRecipient(new Address('noreply@weweave.net', 'weweave GbR'));
$sr->setEcho(true);
$sr->setKeyword('UnitTest');
$sr->setDetails('php unit test testSendSuccess()');
$sr->addModelParam('firstname', 'John');
$sr->addModelParam('code', '123456');

try {
    // Send the request
    $res = $client->send($sr);
    // We received a response code from the server
    if ($res->getError() == ErrorCode::OK) {
        echo "Success!\n";
    } else {
        echo "Error code: ".$res->getError()."\n";
    }
} catch (Exception $e) {
    // Could not connect to the server
    echo "An errror occurred while connecting to the Tubewarder Server!\n";
}
?>
```