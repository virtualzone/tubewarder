<?php
namespace Tubewarder;

class SendResponse {
    private $error;
    private $recipient;
    private $subject;
    private $content;
    private $queueId;
    private $fieldErrors = array();

    public function __construct() {
        $this->recipient = new Address();
    }

    /**
     * @return int
     */
    public function getError() {
        return $this->error;
    }

    /**
     * @param int $error
     */
    public function setError($error) {
        $this->error = $error;
    }

    /**
     * @return Address
     */
    public function getRecipient() {
        return $this->recipient;
    }

    /**
     * @param Address $recipient
     */
    public function setRecipient($recipient) {
        $this->recipient = $recipient;
    }

    /**
     * @return string
     */
    public function getSubject() {
        return $this->subject;
    }

    /**
     * @param string $subject
     */
    public function setSubject($subject) {
        $this->subject = $subject;
    }

    /**
     * @return string
     */
    public function getContent() {
        return $this->content;
    }

    /**
     * @param string $content
     */
    public function setContent($content) {
        $this->content = $content;
    }

    /**
     * @return string
     */
    public function getQueueId() {
        return $this->queueId;
    }

    /**
     * @param string $queueId
     */
    public function setQueueId($queueId) {
        $this->queueId = $queueId;
    }

    /**
     * @return array
     */
    public function getFieldErrors() {
        return $this->fieldErrors;
    }

    /**
     * @param array $fieldErrors
     */
    public function setFieldErrors($fieldErrors) {
        $this->fieldErrors = $fieldErrors;
    }

    /**
     * @param array $o associative array
     * @return SendResponse
     */
    public static function createFromObject($o) {
        $res = new SendResponse();
        $res->setError($o['error']);
        if (isset($o['recipient']) && is_array($o['recipient'])) {
            if (isset($o['recipient']['address'])) {
                $res->getRecipient()->setAddress($o['recipient']['address']);
            }
            if (isset($o['recipient']['name'])) {
                $res->getRecipient()->setName($o['recipient']['name']);
            }
        }
        $res->setSubject($o['subject']);
        $res->setContent($o['content']);
        $res->setQueueId($o['queueId']);
        $res->setFieldErrors($o['fieldErrors']);
        return $res;
    }
}