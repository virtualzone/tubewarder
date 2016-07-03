<?php
namespace Tubewarder;

class Address {
    private $address;
    private $name;

    public function __construct($address = '', $name = '') {
        $this->address = $address;
        $this->name = $name;
    }

    /**
     * @return string
     */
    public function getAddress() {
        return $this->address;
    }

    /**
     * @param string $address
     */
    public function setAddress($address) {
        $this->address = $address;
    }

    /**
     * @return string
     */
    public function getName() {
        return $this->name;
    }

    /**
     * @param string $name
     */
    public function setName($name) {
        $this->name = $name;
    }

    /**
     * @return array
     */
    public function getObject() {
        $o = array(
            'address' => $this->getAddress(),
            'name' => $this->getName()
        );
        return $o;
    }
}