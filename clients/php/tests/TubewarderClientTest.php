<?php
namespace Tubewarder;
use phpunit\framework\TestCase;
require('../src/TubewarderRestClient.php');

class TubewarderClientTest extends TestCase {
    public function testConstructorWithoutTrailingSlash() {
        $client = new TubewarderRestClient('http://localhost:8080');
        $this->assertEquals('http://localhost:8080/', $client->getUri());
    }

    public function testConstructorWithTrailingSlash() {
        $client = new TubewarderRestClient('http://localhost:8080/');
        $this->assertEquals('http://localhost:8080/', $client->getUri());
    }

    public function testSendRequest() {
        $sr = new SendRequest('464bed09-9875-45b3-9111-44f633de9d30');
        $sr->setChannel('Sysout');
        $sr->setTemplate('Dummy');
        $sr->setRecipient(new Address('noreply@weweave.net', 'weweave GbR'));
        $sr->setEcho(true);
        $sr->setKeyword('UnitTest');
        $sr->setDetails('php unit test testSendSuccess()');
        $sr->addModelParam('firstname', 'John');
        $sr->addModelParam('code', '123456');
        $attachment = new Attachment('test.txt');
        $attachment->setPayloadFromFile('test.txt');
        $attachment->setContentType('text/plain');
        $sr->addAttachment($attachment);

        $o = $sr->getObject();

        $this->assertNotEmpty($o);
        $this->assertEquals('464bed09-9875-45b3-9111-44f633de9d30', $o['token']);
        $this->assertEquals('Sysout', $o['channel']);
        $this->assertEquals('Dummy', $o['template']);
        $this->assertEquals(true, $o['echo']);
        $this->assertEquals('UnitTest', $o['keyword']);
        $this->assertEquals('php unit test testSendSuccess()', $o['details']);
        $this->assertNotEmpty($o['recipient']);
        $this->assertEquals('noreply@weweave.net', $o['recipient']['address']);
        $this->assertEquals('weweave GbR', $o['recipient']['name']);
        $this->assertEquals('firstname', $o['model'][0]['key']);
        $this->assertEquals('John', $o['model'][0]['value']);
        $this->assertEquals('code', $o['model'][1]['key']);
        $this->assertEquals('123456', $o['model'][1]['value']);
        $this->assertEquals('test.txt', $o['attachments'][0]['filename']);
        $this->assertEquals('text/plain', $o['attachments'][0]['contentType']);
        $this->assertEquals('VGhpcyBpcyBqdXN0IGEgdGVzdCBjYXNl', $o['attachments'][0]['payload']);
    }

    /**
     * The test cases below require a running Tubewarder Server Instance on
     * localhost port 8080. They are therefore disabled by default.
     */

    /*
    public function testSendInvalidRequest() {
        $client = new TubewarderRestClient('http://localhost:8080');
        $sr = new SendRequest('abcdefg');
        $res = $client->send($sr);
        $this->assertEquals(ErrorCode::INVALID_INPUT_PARAMETERS, $res->getError());
    }
    */

    /**
     * @expectedException \Exception
     */
     /*
    public function testInvalidHost() {
        $client = new TubewarderRestClient('http://localhost:8081');
        $sr = new SendRequest('abcdefg');
        $client->send($sr);
    }
    */

    /**
     * @expectedException \Exception
     */
     /*
    public function testInvalidPath() {
        $client = new TubewarderRestClient('http://localhost:8080/test');
        $sr = new SendRequest('abcdefg');
        $client->send($sr);
    }
    */

    /*
    public function testSendSuccess() {
        $client = new TubewarderRestClient('http://localhost:8080');
        $sr = new SendRequest('464bed09-9875-45b3-9111-44f633de9d30');
        $sr->setChannel('Sysout');
        $sr->setTemplate('Dummy');
        $sr->setRecipient(new Address('noreply@weweave.net', 'weweave GbR'));
        $sr->setEcho(true);
        $sr->setKeyword('UnitTest');
        $sr->setDetails('php unit test testSendSuccess()');
        $sr->addModelParam('firstname', 'John');
        $sr->addModelParam('code', '123456');
        $res = $client->send($sr);
        $this->assertEquals(ErrorCode::OK, $res->getError());
        $this->assertEquals('Important message for John', $res->getSubject());
        $this->assertEquals('Hello John, here is your activation code: 123456', $res->getContent());
        $this->assertInstanceOf(Address::class, $res->getRecipient());
        $this->assertEquals('noreply@weweave.net', $res->getRecipient()->getAddress());
        $this->assertEquals('weweave GbR', $res->getRecipient()->getName());
        $this->assertNotEmpty($res->getQueueId());
        $this->assertEmpty($res->getFieldErrors());
    }
    */
}
?>