package net.weweave.tubewarder.outputhandler.api;

/**
 * A representation of an address. An address has:
 * <ul>
 *     <li>The name of the addressee</li>
 *     <li>The actual address</li>
 * </ul>
 */
public class Address {
    private String name;
    private String address;

    public Address(String name, String address) {
        setName(name);
        setAddress(address);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
