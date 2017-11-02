
package org.oasis.open.docs.wsn.bw_2;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "NotificationConsumerService", targetNamespace = "http://docs.oasis-open.org/wsn/bw-2", wsdlLocation = "file:/home/ddmitry/temp/bw-2.wsdl")
public class NotificationConsumerService
    extends Service
{

    private final static URL NOTIFICATIONCONSUMERSERVICE_WSDL_LOCATION;
    private final static WebServiceException NOTIFICATIONCONSUMERSERVICE_EXCEPTION;
    private final static QName NOTIFICATIONCONSUMERSERVICE_QNAME = new QName("http://docs.oasis-open.org/wsn/bw-2", "NotificationConsumerService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("file:/home/ddmitry/temp/bw-2.wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        NOTIFICATIONCONSUMERSERVICE_WSDL_LOCATION = url;
        NOTIFICATIONCONSUMERSERVICE_EXCEPTION = e;
    }

    public NotificationConsumerService() {
        super(__getWsdlLocation(), NOTIFICATIONCONSUMERSERVICE_QNAME);
    }

    public NotificationConsumerService(WebServiceFeature... features) {
        super(__getWsdlLocation(), NOTIFICATIONCONSUMERSERVICE_QNAME, features);
    }

    public NotificationConsumerService(URL wsdlLocation) {
        super(wsdlLocation, NOTIFICATIONCONSUMERSERVICE_QNAME);
    }

    public NotificationConsumerService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, NOTIFICATIONCONSUMERSERVICE_QNAME, features);
    }

    public NotificationConsumerService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public NotificationConsumerService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns NotificationConsumer
     */
    @WebEndpoint(name = "NotificationConsumerPort")
    public NotificationConsumer getNotificationConsumerPort() {
        return super.getPort(new QName("http://docs.oasis-open.org/wsn/bw-2", "NotificationConsumerPort"), NotificationConsumer.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns NotificationConsumer
     */
    @WebEndpoint(name = "NotificationConsumerPort")
    public NotificationConsumer getNotificationConsumerPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://docs.oasis-open.org/wsn/bw-2", "NotificationConsumerPort"), NotificationConsumer.class, features);
    }

    private static URL __getWsdlLocation() {
        if (NOTIFICATIONCONSUMERSERVICE_EXCEPTION!= null) {
            throw NOTIFICATIONCONSUMERSERVICE_EXCEPTION;
        }
        return NOTIFICATIONCONSUMERSERVICE_WSDL_LOCATION;
    }

}
