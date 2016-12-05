
package net.weweave.tubewarder.test.soap.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for soapSendModel complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="soapSendModel">
 *   &lt;complexContent>
 *     &lt;extension base="{http://soap.service.tubewarder.weweave.net/}sendModel">
 *       &lt;sequence>
 *         &lt;element name="modelJson" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "soapSendModel", propOrder = {
    "modelJson"
})
public class SoapSendModel
    extends SendModel
{

    protected String modelJson;

    /**
     * Gets the value of the modelJson property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModelJson() {
        return modelJson;
    }

    /**
     * Sets the value of the modelJson property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModelJson(String value) {
        this.modelJson = value;
    }

}
