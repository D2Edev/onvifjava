package org.onvif.unofficial;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.SOAPException;

import org.apache.commons.codec.binary.Base64;
import org.onvif.unofficial.services.DeviceManagementService;
import org.onvif.unofficial.services.ImagingService;
import org.onvif.unofficial.services.MediaService;
import org.onvif.unofficial.services.PtzService;
import org.onvif.unofficial.soapclient.ISoapClient;
import org.onvif.unofficial.soapclient.SoapClient;
import org.onvif.ver10.schema.Capabilities;


public class OnvifDevice {
	private final String domain;
	private String returnedDomain;
	private boolean isProxy;
	private String username, password, nonce, utcTime;
	private ISoapClient client;
	private DeviceManagementService devMngtService;
	private PtzService ptzService;
	private MediaService mediaService;
	private ImagingService imagingService;

	/**
	 * Initializes an Onvif device, e.g. a Network Video Transmitter (NVT) with
	 * logindata.
	 * 
	 * @param domain
	 *            The IP address of your device, you can also add a port but
	 *            noch protocol (e.g. http://)
	 * @param user
	 *            Username you need to login
	 * @param password
	 *            User's password to login
	 * @throws ConnectException
	 *             Exception gets thrown, if device isn't accessible or invalid
	 *             and doesn't answer to SOAP messages
	 * @throws SOAPException
	 * @throws ParserConfigurationException
	 * @throws UnsupportedOperationException
	 */
	public OnvifDevice(String domain, String user, String password) throws Exception {

		this.domain = domain;
		this.username = user;
		this.password = password;
		checkIfOnline();
		init();
	}

	/**
	 * Initializes an Onvif device, e.g. a Network Video Transmitter (NVT) with
	 * logindata.
	 * 
	 * @param hostIp
	 *            The IP address of your device, you can also add a port but
	 *            noch protocol (e.g. http://)
	 * @throws ParserConfigurationException
	 * @throws UnsupportedOperationException
	 * @throws ConnectException
	 *             Exception gets thrown, if device isn't accessible or invalid
	 *             and doesn't answer to SOAP messages
	 * @throws SOAPException
	 */
	public OnvifDevice(String hostIp) throws Exception {
		this(hostIp, null, null);
	}

	/**
	 * Internal function to check, if device is available.
	 * @throws Exception 
	 */
	private void checkIfOnline() throws Exception {
		Socket socket = null;
		try {
			String port = domain.contains(":") ? domain.substring(domain.indexOf(':') + 1) : "80";
			String ip = domain.contains(":") ? domain.substring(0, domain.indexOf(':')) : domain;
			SocketAddress sockaddr = new InetSocketAddress(ip, new Integer(port));
			socket = new Socket();
			socket.connect(sockaddr, 5000);
		} catch (Exception e) {
			throw e;
		} finally {
			if(socket!=null){
				try {
					socket.close();
				} catch (IOException nop) {
				}
			}
		}
	}

	/**
	 * Initalizes the addresses used for SOAP messages and to get the internal
	 * IP, if given IP is a proxy.
	 * 
	 * @throws Exception
	 */
	protected void init() throws Exception {

		this.client = new SoapClient(this);
		this.devMngtService = new DeviceManagementService(this,client, "http://" + domain + "/onvif/device_service");
		Capabilities capabilities = getDeviceManagementService().getCapabilities();

		if (capabilities == null) {
			throw new ConnectException("Capabilities not reachable.");
		}

		String localDeviceUri = capabilities.getDevice().getXAddr();

		if (localDeviceUri.startsWith("http://")) {
			returnedDomain = localDeviceUri.replace("http://", "");
			returnedDomain = returnedDomain.substring(0, returnedDomain.indexOf('/'));
		} else {
			throw new ConnectException("Unknown protocol");
		}

		if (!returnedDomain.equals(domain)) {
			isProxy = true;
		}

		if (capabilities.getMedia() != null && capabilities.getMedia().getXAddr() != null) {
			this.mediaService = new MediaService(this,client, replaceLocalIpWithProxyIp(capabilities.getMedia().getXAddr()));
		}

		if (capabilities.getPTZ() != null && capabilities.getPTZ().getXAddr() != null) {
			this.ptzService = new PtzService(this,client, replaceLocalIpWithProxyIp(capabilities.getPTZ().getXAddr()));
		}

		if (capabilities.getImaging() != null && capabilities.getImaging().getXAddr() != null) {
			this.imagingService = new ImagingService(this,client,
					replaceLocalIpWithProxyIp(capabilities.getImaging().getXAddr()));
		}

		// event uri - not used currently
		// if (capabilities.getMedia() != null &&
		// capabilities.getEvents().getXAddr() != null) {
		// replaceLocalIpWithProxyIp(capabilities.getEvents().getXAddr());
		// }
	}

	public String replaceLocalIpWithProxyIp(String original) {
		if (original.startsWith("http:///")) {
			original.replace("http:///", "http://" + domain);
		}

		if (isProxy) {
			return original.replace(returnedDomain, domain);
		}
		return original;
	}

	public String getUsername() {
		return username;
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
		String nonce = getNonce();
		String timestamp = getUTCTime();

		String beforeEncryption = nonce + timestamp + password;

		byte[] encryptedRaw;
		encryptedRaw = sha1(beforeEncryption);
		String encoded = Base64.encodeBase64String(encryptedRaw);
		return encoded;
	}

	private static byte[] sha1(String s) throws NoSuchAlgorithmException {
		MessageDigest SHA1 = null;
		SHA1 = MessageDigest.getInstance("SHA1");

		SHA1.reset();
		SHA1.update(s.getBytes());

		byte[] encryptedRaw = SHA1.digest();
		return encryptedRaw;
	}

	private String getNonce() {
		if (nonce == null) {
			createNonce();
		}
		return nonce;
	}

	public String getEncryptedNonce() {
		if (nonce == null) {
			createNonce();
		}
		return Base64.encodeBase64String(nonce.getBytes());
	}

	public void createNonce() {
		Random generator = new Random();
		nonce = "" + generator.nextInt();
	}

	public String getLastUTCTime() {
		return utcTime;
	}

	public String getUTCTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-d'T'HH:mm:ss'Z'");
		sdf.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));

		Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
		String utcTime = sdf.format(cal.getTime());
		this.utcTime = utcTime;
		return utcTime;
	}


	/**
	 * Is used for basic devices and requests of given Onvif Device
	 */
	public DeviceManagementService getDeviceManagementService() {
		return devMngtService;
	}

	/**
	 * Can be used for PTZ controlling requests, may not be supported by device!
	 */
	public PtzService getPtzService() {
		return ptzService;
	}

	/**
	 * Can be used to get media data from OnvifDevice
	 * 
	 * @return
	 */
	public MediaService getMediaService() {
		return mediaService;
	}

	/**
	 * Can be used to get media data from OnvifDevice
	 * 
	 * @return
	 */
	public ImagingService getImagingService() {
		return imagingService;
	}

	public Date getDate() throws Exception {
		return devMngtService.getDate();
	}

	public String getModel() throws Exception {
		return devMngtService.getDeviceInformation().getModel();
	}

	public String getHostname() throws Exception {
		return devMngtService.getHostname();
	}

	public String reboot() throws Exception {
		return devMngtService.reboot();
	}
}
