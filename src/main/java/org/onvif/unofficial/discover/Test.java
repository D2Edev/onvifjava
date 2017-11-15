package org.onvif.unofficial.discover;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import org.w3c.dom.Document;
import org.xmlsoap.schemas.soap.envelope.Envelope;
import org.xmlsoap.schemas.soap.envelope.Header;
import org.xmlsoap.schemas.ws._2005._04.discovery.ProbeType;

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
		message.writeTo(out);
		
		
	} catch (SOAPException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
}
