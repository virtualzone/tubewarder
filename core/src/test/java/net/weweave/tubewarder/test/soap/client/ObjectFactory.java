
package net.weweave.tubewarder.test.soap.client;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the net.weweave.tubewarder.test.soap.client package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _KeyValueModel_QNAME = new QName("http://soap.service.tubewarder.weweave.net/", "keyValueModel");
    private final static QName _SendResponse_QNAME = new QName("http://soap.service.tubewarder.weweave.net/", "sendResponse");
    private final static QName _SendServiceResponse_QNAME = new QName("http://soap.service.tubewarder.weweave.net/", "sendServiceResponse");
    private final static QName _Send_QNAME = new QName("http://soap.service.tubewarder.weweave.net/", "send");
    private final static QName _AbstractResponse_QNAME = new QName("http://soap.service.tubewarder.weweave.net/", "abstractResponse");
    private final static QName _AddressModel_QNAME = new QName("http://soap.service.tubewarder.weweave.net/", "addressModel");
    private final static QName _AttachmentModel_QNAME = new QName("http://soap.service.tubewarder.weweave.net/", "attachmentModel");
    private final static QName _SendModel_QNAME = new QName("http://soap.service.tubewarder.weweave.net/", "sendModel");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: net.weweave.tubewarder.test.soap.client
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link AbstractResponse }
     * 
     */
    public AbstractResponse createAbstractResponse() {
        return new AbstractResponse();
    }

    /**
     * Create an instance of {@link AttachmentModel }
     * 
     */
    public AttachmentModel createAttachmentModel() {
        return new AttachmentModel();
    }

    /**
     * Create an instance of {@link AddressModel }
     * 
     */
    public AddressModel createAddressModel() {
        return new AddressModel();
    }

    /**
     * Create an instance of {@link SendModel }
     * 
     */
    public SendModel createSendModel() {
        return new SendModel();
    }

    /**
     * Create an instance of {@link KeyValueModel }
     * 
     */
    public KeyValueModel createKeyValueModel() {
        return new KeyValueModel();
    }

    /**
     * Create an instance of {@link SendResponse }
     * 
     */
    public SendResponse createSendResponse() {
        return new SendResponse();
    }

    /**
     * Create an instance of {@link SendServiceResponse }
     * 
     */
    public SendServiceResponse createSendServiceResponse() {
        return new SendServiceResponse();
    }

    /**
     * Create an instance of {@link Send }
     * 
     */
    public Send createSend() {
        return new Send();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link KeyValueModel }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://soap.service.tubewarder.weweave.net/", name = "keyValueModel")
    public JAXBElement<KeyValueModel> createKeyValueModel(KeyValueModel value) {
        return new JAXBElement<KeyValueModel>(_KeyValueModel_QNAME, KeyValueModel.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://soap.service.tubewarder.weweave.net/", name = "sendResponse")
    public JAXBElement<SendResponse> createSendResponse(SendResponse value) {
        return new JAXBElement<SendResponse>(_SendResponse_QNAME, SendResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendServiceResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://soap.service.tubewarder.weweave.net/", name = "sendServiceResponse")
    public JAXBElement<SendServiceResponse> createSendServiceResponse(SendServiceResponse value) {
        return new JAXBElement<SendServiceResponse>(_SendServiceResponse_QNAME, SendServiceResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Send }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://soap.service.tubewarder.weweave.net/", name = "send")
    public JAXBElement<Send> createSend(Send value) {
        return new JAXBElement<Send>(_Send_QNAME, Send.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://soap.service.tubewarder.weweave.net/", name = "abstractResponse")
    public JAXBElement<AbstractResponse> createAbstractResponse(AbstractResponse value) {
        return new JAXBElement<AbstractResponse>(_AbstractResponse_QNAME, AbstractResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddressModel }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://soap.service.tubewarder.weweave.net/", name = "addressModel")
    public JAXBElement<AddressModel> createAddressModel(AddressModel value) {
        return new JAXBElement<AddressModel>(_AddressModel_QNAME, AddressModel.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AttachmentModel }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://soap.service.tubewarder.weweave.net/", name = "attachmentModel")
    public JAXBElement<AttachmentModel> createAttachmentModel(AttachmentModel value) {
        return new JAXBElement<AttachmentModel>(_AttachmentModel_QNAME, AttachmentModel.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendModel }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://soap.service.tubewarder.weweave.net/", name = "sendModel")
    public JAXBElement<SendModel> createSendModel(SendModel value) {
        return new JAXBElement<SendModel>(_SendModel_QNAME, SendModel.class, null, value);
    }

}
