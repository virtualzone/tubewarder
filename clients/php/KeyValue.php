<?php
namespace Tubewarder;

class KeyValue {
    private $key;
    private $value;

    public function __construct($key, $value = '') {
        $this->setKey($key);
        $this->setValue($value);
    }

    /**
     * @return string
     */
    public function getKey() {
        return $this->key;
    }

    /**
     * @param string $key
     */
    public function setKey($key) {
        $this->key = $key;
    }

    /**
     * @return mixed
     */
    public function getValue() {
        return $this->value;
    }

    /**
     * @param mixed $value
     */
    public function setValue($value) {
        $this->value = $value;
    }

    /**
     * @return array
     */
    public function getObject() {
        $o = array(
            'key' => $this->getKey(),
            'value' => $this->getValue()
        );
        return $o;
    }
}