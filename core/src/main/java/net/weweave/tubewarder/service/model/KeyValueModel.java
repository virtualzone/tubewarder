package net.weweave.tubewarder.service.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class KeyValueModel {
    @XmlElement(required = true)
    public String key;
    public Object value;
}
