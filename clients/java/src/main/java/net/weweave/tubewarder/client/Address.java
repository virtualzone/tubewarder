package net.weweave.tubewarder.client;

import java.io.Serializable;

public class Address implements Serializable {
    private String address;
    private String name;

    public Address() {

    }

    public Address(String address) {
        setAddress(address);
    }

    public Address(String address, String name) {
        setAddress(address);
        setName(name);;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
