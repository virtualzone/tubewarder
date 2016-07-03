<?php
// Include the required files
require('../src/TubewarderRestClient.php');

// Import the classes
use Tubewarder\TubewarderRestClient as TubewarderRestClient;
use Tubewarder\SendRequest as SendRequest;
use Tubewarder\Address as Address;
use Tubewarder\ErrorCode as ErrorCode;

// Create a new client
$client = new TubewarderRestClient('http://localhost:8080');

// Prepare the send request
$sr = new SendRequest('464bed09-9875-45b3-9111-44f633de9d30');
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