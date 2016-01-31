
package net.weweave.tubewarder.test.soap.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for sendServiceResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="sendServiceResponse">
 *   &lt;complexContent>
 *     &lt;extension base="{http://soap.service.tubewarder.weweave.net/}abstractResponse">
 *       &lt;sequence>
 *         &lt;element name="recipient" type="{http://soap.service.tubewarder.weweave.net/}addressModel" minOccurs="0"/>
 *         &lt;element name="subject" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="content" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sendServiceResponse", propOrder = {
    "recipient",
    "subject",
    "content"
})
public class SendServiceResponse
    extends AbstractResponse
{

    protected AddressModel recipient;
    protected String subject;
    protected String content;

    /**
     * Gets the value of the recipient property.
     * 
     * @return
     *     possible object is
     *     {@link AddressModel }
     *     
     */
    public AddressModel getRecipient() {
        return recipient;
    }

    /**
     * Sets the value of the recipient property.
     * 
     * @param value
     *     allowed object is
     *     {@link AddressModel }
     *     
     */
    public void setRecipient(AddressModel value) {
        this.recipient = value;
    }

    /**
     * Gets the value of the subject property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Sets the value of the subject property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubject(String value) {
        this.subject = value;
    }

    /**
     * Gets the value of the content property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the value of the content property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContent(String value) {
        this.content = value;
    }

}
