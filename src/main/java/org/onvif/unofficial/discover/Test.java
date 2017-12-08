package org.onvif.unofficial.discover;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

public class Test {
public static void main(String[] args) {
	try {
		MessageFactory messageFactory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
		SOAPMessage message=messageFactory.createMessage();
		SOAPPart part = message.getSOAPPart();
		SOAPEnvelope envelope = part.getEnvelope();
		envelope.addNamespaceDeclaration("wsa", "http://schemas.xmlsoap.org/ws/2004/08/addressing");
		envelope.addNamespaceDeclaration("tns", "http://schemas.xmlsoap.org/ws/2005/04/discovery");
		QName action=envelope.createQName("Action","wsa");
		QName mid=envelope.createQName("MessageID", "wsa");
		QName to=envelope.createQName("To", "wsa");
		QName probe=envelope.createQName("Probe", "tns");
		SOAPHeader header = envelope.getHeader();
		SOAPElement actionEl=header.addChildElement(action);
		actionEl.setTextContent("http://schemas.xmlsoap.org/ws/2005/04/discovery/Probe");
		SOAPElement messIsEl=header.addChildElement(mid);
		messIsEl.setTextContent("urn:uuid:c032cfdd-c3ca-49dc-820e-ee6696ad63e2");
		SOAPElement toEl=header.addChildElement(to);
		toEl.setTextContent("urn:schemas-xmlsoap-org:ws:2005:04:discovery");
		SOAPBody body = envelope.getBody();
		SOAPElement probeEl=body.addChildElement(probe);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		message.writeTo(System.out);
		
		
	} catch (SOAPException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
}
