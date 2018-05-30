package org.onvif.unofficial.soapclient;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
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

import org.w3c.dom.Document;

public class SoapClient implements ISoapClient {

//	private NetOnvifDevice onvifDevice;
	private SOAPConnectionFactory soapConnectionFactory;
	private MessageFactory messageFactory;
	private Map<Class<?>, Unmarshaller> unmarshallers = new HashMap<>();
	private DocumentBuilder documentBuilder;
	private Map<Class<?>, Marshaller> marshallers = new HashMap<>();
	private String username;
	private String password;
	private String nonce;

	public SoapClient(String username, String password) throws Exception {
		this.username=username;
		this.password=password;
		soapConnectionFactory = SOAPConnectionFactory.newInstance();
		messageFactory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
		documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	}

	@SuppressWarnings("unchecked")
	@Override
	// blocking
	public <T> T processRequest(Object request, Class<T> responseClass, String soapUri, boolean needsAuthentification)
			throws Exception {
		SOAPConnection soapConnection = null;
		try {
			soapConnection = soapConnectionFactory.createConnection();
			SOAPMessage soapMessage = processSoapMessage(request, needsAuthentification);
			SOAPMessage soapResponse = soapConnection.call(soapMessage, soapUri);
			// get new each time? - no
			Unmarshaller unmarshaller = getUnmarshaller(responseClass);
			return (T) unmarshaller.unmarshal(soapResponse.getSOAPBody().extractContentAsDocument());
		} finally {
			if (soapConnection != null) {
				soapConnection.close();
			}
		}

	}

	private SOAPMessage processSoapMessage(Object request, boolean needAuthentification) throws Exception {
		// get new each time?

		SOAPMessage soapMessage = messageFactory.createMessage();
		Document document = documentBuilder.newDocument();
		// get new each time?
		Marshaller marshaller = getMarshaller(request.getClass());
		marshaller.marshal(request, document);
		soapMessage.getSOAPBody().addDocument(document);
		if (needAuthentification) {
			addSoapHeader(soapMessage);
		}
		soapMessage.saveChanges();
		return soapMessage;
	}

	protected void addSoapHeader(SOAPMessage soapMessage) throws SOAPException, NoSuchAlgorithmException {
		createNonce();
		String encrypedPassword = getEncryptedPassword();
		if (encrypedPassword != null && username != null) {

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
			usernameElem.setTextContent(username);

			SOAPElement passwordElem = usernameTokenElem.addChildElement("Password", "wsse");
			passwordElem.setAttribute("Type",
					"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordDigest");
			passwordElem.setTextContent(encrypedPassword);

			SOAPElement nonceElem = usernameTokenElem.addChildElement("Nonce", "wsse");
			nonceElem.setAttribute("EncodingType",
					"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary");
			nonceElem.setTextContent(getEncryptedNonce());

			SOAPElement createdElem = usernameTokenElem.addChildElement("Created", "wsu");
			createdElem.setTextContent(getUTCTime());
		}
	}

	private Marshaller getMarshaller(Class<?> requestClass) throws JAXBException {
		Marshaller m = null;
		if (marshallers.containsKey(requestClass)) {
			m = marshallers.get(requestClass);
		} else {
			m = JAXBContext.newInstance(requestClass).createMarshaller();
			marshallers.put(requestClass, m);
		}
		return m;
	}

	private Unmarshaller getUnmarshaller(Class<?> responseClass) throws JAXBException {
		Unmarshaller u = null;
		if (unmarshallers.containsKey(responseClass)) {
			u = unmarshallers.get(responseClass);
		} else {
			u = JAXBContext.newInstance(responseClass).createUnmarshaller();
			unmarshallers.put(responseClass, u);
		}
		return u;
	}


	private String getEncryptedNonce() {
		if (nonce == null) {
			createNonce();
		}
		return Base64.getEncoder().encodeToString(nonce.getBytes());
	}

	private void createNonce() {
		Random generator = new Random();
		nonce = "" + generator.nextInt();
	}
	
	public String getEncryptedPassword() throws NoSuchAlgorithmException {
		return encryptPassword();
	}

	/**
	 * Returns encrypted version of given password like algorithm like in
	 * WS-UsernameToken
	 * 
	 * @throws NoSuchAlgorithmException
	 */
	public String encryptPassword() throws NoSuchAlgorithmException {
		String timestamp = getUTCTime();
		String beforeEncryption = nonce + timestamp + password;
		byte[] encryptedRaw;
		encryptedRaw = sha1(beforeEncryption);
//		String encoded = Base64.encodeBase64String(encryptedRaw);
		String encoded = Base64.getEncoder().encodeToString(encryptedRaw);
		return encoded;
	}

	public String getUTCTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-d'T'HH:mm:ss'Z'");
		sdf.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));

		Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
		String utcTime = sdf.format(cal.getTime());
		return utcTime;
	}
	

	private static byte[] sha1(String s) throws NoSuchAlgorithmException {
		MessageDigest SHA1 = null;
		SHA1 = MessageDigest.getInstance("SHA1");
	
		SHA1.reset();
		SHA1.update(s.getBytes());
	
		byte[] encryptedRaw = SHA1.digest();
		return encryptedRaw;
	}
}
