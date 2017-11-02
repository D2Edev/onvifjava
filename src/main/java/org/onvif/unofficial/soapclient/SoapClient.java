package org.onvif.unofficial.soapclient;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import org.onvif.unofficial.devices.DeviceSubclass;
import org.onvif.unofficial.devices.OnvifDevice;
import org.w3c.dom.Document;

public class SoapClient implements ISoapClient {

	private OnvifDevice onvifDevice;

	public SoapClient(OnvifDevice onvifDevice) {
		super();

		this.onvifDevice = onvifDevice;
	}

	@Override
	public <T extends SoapResponse> T processOnvifServiceRequest(SoapRequest request,
			Class<T> responseClass, DeviceSubclass type, boolean needsAuthentification)
			throws UnsupportedOperationException, SOAPException, ParserConfigurationException, JAXBException{
		return processRequest(request, responseClass, onvifDevice.getUri(type), needsAuthentification);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends SoapResponse> T processRequest(SoapRequest soapRequestElem, Class<T> responseClass,
			String soapUri, boolean needsAuthentification)
			throws UnsupportedOperationException, SOAPException, ParserConfigurationException, JAXBException{
		SOAPConnection soapConnection=null;
		try {
			SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
			soapConnection = soapConnectionFactory.createConnection();
			SOAPMessage soapMessage = processSoapMessage(soapRequestElem, needsAuthentification);
			SOAPMessage soapResponse = soapConnection.call(soapMessage, soapUri);
			Unmarshaller unmarshaller = JAXBContext.newInstance(responseClass).createUnmarshaller();
			return (T) unmarshaller.unmarshal(soapResponse.getSOAPBody().extractContentAsDocument());
		} finally {
			if(soapConnection!=null){
				soapConnection.close();
			}
		}
		
	}

	protected SOAPMessage processSoapMessage(Object soapRequestElem, boolean needAuthentification)
			throws SOAPException, ParserConfigurationException, JAXBException {
		MessageFactory messageFactory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
		SOAPMessage soapMessage = messageFactory.createMessage();

		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		Marshaller marshaller = JAXBContext.newInstance(soapRequestElem.getClass()).createMarshaller();
		marshaller.marshal(soapRequestElem, document);
		soapMessage.getSOAPBody().addDocument(document);

		// if (needAuthentification)
		addSoapHeader(soapMessage);

		soapMessage.saveChanges();
		return soapMessage;
	}

	protected void addSoapHeader(SOAPMessage soapMessage) throws SOAPException {
		onvifDevice.createNonce();
		String encrypedPassword = onvifDevice.getEncryptedPassword();
		if (encrypedPassword != null && onvifDevice.getUsername() != null) {

			SOAPPart sp = soapMessage.getSOAPPart();
			SOAPEnvelope se = sp.getEnvelope();
			SOAPHeader header = soapMessage.getSOAPHeader();
			se.addNamespaceDeclaration("wsse",
					"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");
			se.addNamespaceDeclaration("wsu",
					"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd");

			SOAPElement securityElem = header.addChildElement("Security", "wsse");
			// securityElem.setAttribute("SOAP-ENV:mustUnderstand", "1");

			SOAPElement usernameTokenElem = securityElem.addChildElement("UsernameToken", "wsse");

			SOAPElement usernameElem = usernameTokenElem.addChildElement("Username", "wsse");
			usernameElem.setTextContent(onvifDevice.getUsername());

			SOAPElement passwordElem = usernameTokenElem.addChildElement("Password", "wsse");
			passwordElem.setAttribute("Type",
					"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordDigest");
			passwordElem.setTextContent(encrypedPassword);

			SOAPElement nonceElem = usernameTokenElem.addChildElement("Nonce", "wsse");
			nonceElem.setAttribute("EncodingType",
					"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary");
			nonceElem.setTextContent(onvifDevice.getEncryptedNonce());

			SOAPElement createdElem = usernameTokenElem.addChildElement("Created", "wsu");
			createdElem.setTextContent(onvifDevice.getLastUTCTime());
		}
	}

}
