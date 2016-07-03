<?php
namespace Tubewarder;

class Attachment {
    private $filename;
    private $contentType;
    private $payload;

    public function __construct($filename = '', $payload = '', $contentType = '') {
        $this->filename = $filename;
        $this->payload = $payload;
        $this->contentType = $contentType;
    }

    /**
     * @return string
     */
    public function getFilename() {
        return $this->filename;
    }

    /**
     * @param string $filename
     */
    public function setFilename($filename) {
        $this->filename = $filename;
    }

    /**
     * @return string
     */
    public function getContentType() {
        return $this->contentType;
    }

    /**
     * @param string $contentType
     */
    public function setContentType($contentType) {
        $this->contentType = $contentType;
    }

    /**
     * @return string
     */
    public function getPayload() {
        return $this->payload;
    }

    /**
     * @param string $payload
     */
    public function setPayload($payload) {
        $this->payload = $payload;
    }

    /**
     * @param string $filename
     */
    public function setPayloadFromFile($filename) {
        $this->payload = base64_encode(file_get_contents($filename));
    }

    /**
     * @return array
     */
    public function getObject() {
        $o = array(
            'filename' => $this->getFilename(),
            'payload' => $this->getPayload(),
            'contentType' => $this->getContentType()
        );
        return $o;
    }
}