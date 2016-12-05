<?php
namespace Tubewarder;
require('Attachment.php');
require('Address.php');

class SendRequest {
    private $token;
    private $template;
    private $channel;
    private $recipient;
    private $model = array();
    private $attachments = array();
    private $keyword;
    private $details;
    private $echo = false;

    public function __construct($token = '') {
        $this->token = $token;
        $this->recipient = new Address();
    }

    /**
     * @return string
     */
    public function getToken() {
        return $this->token;
    }

    /**
     * @param string $token
     */
    public function setToken($token) {
        $this->token = $token;
    }

    /**
     * @return string
     */
    public function getTemplate() {
        return $this->template;
    }

    /**
     * @param string $template
     */
    public function setTemplate($template) {
        $this->template = $template;
    }

    /**
     * @return string
     */
    public function getChannel() {
        return $this->channel;
    }

    /**
     * @param string $channel
     */
    public function setChannel($channel) {
        $this->channel = $channel;
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
    public function setRecipient(Address $recipient) {
        $this->recipient = $recipient;
    }

    /**
     * @return array
     */
    public function getModel() {
        return $this->model;
    }

    /**
     * @param string $key
     * @param mixed $value
     */
    public function addModelParam($key, $value) {
        $this->model[$key] = $value;
    }

    /**
     * @return array
     */
    public function getAttachments() {
        return $this->attachments;
    }

    /**
     * @param Attachment $attachment
     */
    public function addAttachment(Attachment $attachment){
        $this->attachments[] = $attachment;
    }

    /**
     * @return string
     */
    public function getKeyword() {
        return $this->keyword;
    }

    /**
     * @param string $keyword
     */
    public function setKeyword($keyword) {
        $this->keyword = $keyword;
    }

    /**
     * @return string
     */
    public function getDetails() {
        return $this->details;
    }

    /**
     * @param string $details
     */
    public function setDetails($details) {
        $this->details = $details;
    }

    /**
     * @return boolean
     */
    public function isEcho() {
        return $this->echo;
    }

    /**
     * @param boolean $echo
     */
    public function setEcho($echo) {
        $this->echo = $echo;
    }

    /**
     * @return array
     */
    public function getObject() {
        $attachmentsArray = array();
        foreach ($this->getAttachments() as $a) {
            $attachmentsArray[] = $a->getObject();
        }
        $o = array(
            'token' => $this->getToken(),
            'template' => $this->getTemplate(),
            'channel' => $this->getChannel(),
            'recipient' => $this->getRecipient()->getObject(),
            'model' => $this->getModel(),
            'attachments' => $attachmentsArray,
            'keyword' => $this->getKeyword(),
            'details' => $this->getDetails(),
            'echo' => $this->isEcho()
        );
        return $o;
    }
}
?>
