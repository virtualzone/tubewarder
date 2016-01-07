package net.weweave.tubewarder.service.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AddressModel {
    @XmlElement(required = true)
    public String address;
    public String name;
}
