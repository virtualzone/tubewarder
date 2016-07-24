package net.weweave.tubewarder.util;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LicenseRestResponse {
    @JsonProperty("success")
    public boolean success = true;
    @JsonProperty("error")
    public int error = 0;
}
