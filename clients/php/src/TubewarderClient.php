<?php
namespace Tubewarder;
require('SendRequest.php');
require('SendResponse.php');
require('ErrorCode.php');

/**
 * Class TubewarderClient
 * @package Tubewarder
 * @version 1.0.0
 * @link http://tubewarder.readthedocs.io
 */
abstract class TubewarderClient {
    private $uri;

    public function __construct($uri) {
        $this->uri = $uri;
        if (substr($this->uri, strlen($this->uri)-1) !== '/') {
            $this->uri .= '/';
        }
    }

    /**
     * @return string
     */
    public function getUri() {
        return $this->uri;
    }

    /**
     * @param SendRequest $request
     * @return SendResponse
     */
    public abstract function send(SendRequest $request);
}
?>