package org.onvif.unofficial.devices;

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
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

import javax.xml.soap.SOAPException;

import org.apache.commons.codec.binary.Base64;
import org.onvif.unofficial.soapclient.ISoapClient;
import org.onvif.unofficial.soapclient.SoapClient;
import org.onvif.ver10.schema.Capabilities;


/**
 * 
 * @author Robin Dick
 * 
 */
public class OnvifDevice {
	private final String domain;
	private String returnedIp;

	private boolean isProxy;

	private String username, password, nonce, utcTime;

	private Map<DeviceSubclass,String> uriMap=new HashMap<>();

	private ISoapClient client;

	private BaseService baseService;
	private PtzService ptzService;
	private MediaService mediaService;
	private ImagingService imagingService;

	
	/**
	 * Initializes an Onvif device, e.g. a Network Video Transmitter (NVT) with
	 * logindata.
	 * 
	 * @param hostIp
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
	 */
	public OnvifDevice(String hostIp, String user, String password) throws ConnectException, SOAPException {
		
		this.domain = hostIp;

		if (!isOnline()) {
			throw new ConnectException("Host not available.");
		}
		uriMap.put(DeviceSubclass.BASE, "http://" + domain + "/onvif/device_service");
		this.username = user;
		this.password = password;

		this.client = new SoapClient(this);
		this.baseService = new BaseService(this);
		this.ptzService = new PtzService(this);
		this.mediaService = new MediaService(this);
		this.imagingService = new ImagingService(this);
		
		init();
	}

	/**
	 * Initializes an Onvif device, e.g. a Network Video Transmitter (NVT) with
	 * logindata.
	 * 
	 * @param hostIp
	 *            The IP address of your device, you can also add a port but
	 *            noch protocol (e.g. http://)
	 * @throws ConnectException
	 *             Exception gets thrown, if device isn't accessible or invalid
	 *             and doesn't answer to SOAP messages
	 * @throws SOAPException 
	 */
	public OnvifDevice(String hostIp) throws ConnectException, SOAPException {
		this(hostIp, null, null);
	}

	/**
	 * Internal function to check, if device is available and answers to ping
	 * requests.
	 */
	private boolean isOnline() {
		String port = domain.contains(":") ? domain.substring(domain.indexOf(':') + 1) : "80";
		String ip = domain.contains(":") ? domain.substring(0, domain.indexOf(':')) : domain;
		
		Socket socket = null;
		try {
			SocketAddress sockaddr = new InetSocketAddress(ip, new Integer(port));
			socket = new Socket();

			socket.connect(sockaddr, 5000);
		}
		catch (NumberFormatException | IOException e) {
			return false;
		}
		finally {
			try {
				if (socket != null) {
					socket.close();
				}
			}
			catch (IOException ex) {
			}
		}
		return true;
	}

	/**
	 * Initalizes the addresses used for SOAP messages and to get the internal
	 * IP, if given IP is a proxy.
	 * 
	 * @throws ConnectException
	 *             Get thrown if device doesn't give answers to
	 *             GetCapabilities()
	 * @throws SOAPException 
	 */
	protected void init() throws ConnectException, SOAPException {
		Capabilities capabilities = getDevices().getCapabilities();

		if (capabilities == null) {
			throw new ConnectException("Capabilities not reachable.");
		}

		String localDeviceUri = capabilities.getDevice().getXAddr();

		if (localDeviceUri.startsWith("http://")) {
			returnedIp = localDeviceUri.replace("http://", "");
			returnedIp = returnedIp.substring(0, returnedIp.indexOf('/'));
		}
		else {
			throw new ConnectException("Unknown protocol");
		}
			
		if (!returnedIp.equals(domain)) {
			isProxy = true;
		}

		if (capabilities.getMedia() != null && capabilities.getMedia().getXAddr() != null) {
			uriMap.put(DeviceSubclass.MEDIA,replaceLocalIpWithProxyIp(capabilities.getMedia().getXAddr()));
		}

		if (capabilities.getPTZ() != null && capabilities.getPTZ().getXAddr() != null) {
			uriMap.put(DeviceSubclass.PTZ, replaceLocalIpWithProxyIp(capabilities.getPTZ().getXAddr()));
		}
		
		if (capabilities.getImaging() != null && capabilities.getImaging().getXAddr() != null) {
			uriMap.put(DeviceSubclass.IMAGING, replaceLocalIpWithProxyIp(capabilities.getImaging().getXAddr()));
		}

		if (capabilities.getMedia() != null && capabilities.getEvents().getXAddr() != null) {
			uriMap.put(DeviceSubclass.EVENT, replaceLocalIpWithProxyIp(capabilities.getEvents().getXAddr()));
		}
	}

	public String replaceLocalIpWithProxyIp(String original) {
		if (original.startsWith("http:///")) {
			original.replace("http:///", "http://"+domain);
		}
		
		if (isProxy) {
			return original.replace(returnedIp, domain);
		}
		return original;
	}

	public String getUsername() {
		return username;
	}

	public String getEncryptedPassword() {
		return encryptPassword();
	}

	/**
	 * Returns encrypted version of given password like algorithm like in WS-UsernameToken
	 */
	public String encryptPassword() {
		String nonce = getNonce();
		String timestamp = getUTCTime();

		String beforeEncryption = nonce + timestamp + password;

		byte[] encryptedRaw;
		try {
			encryptedRaw = sha1(beforeEncryption);
		}
		catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
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

	public ISoapClient getSoap() {
		return client;
	}

	/**
	 * Is used for basic devices and requests of given Onvif Device
	 */
	public BaseService getDevices() {
		return baseService;
	}

	/**
	 * Can be used for PTZ controlling requests, may not be supported by device!
	 */
	public PtzService getPtz() {
		return ptzService;
	}

	/**
	 * Can be used to get media data from OnvifDevice
	 * @return
	 */
	public MediaService getMedia() {
		return mediaService;
	}

	/**
	 * Can be used to get media data from OnvifDevice
	 * @return
	 */
	public ImagingService getImaging() {
		return imagingService;
	}
	
	public Date getDate() {
		return baseService.getDate();
	}
	
	public String getName() {
		return baseService.getDeviceInformation().getModel();
	}
	
	public String getHostname() {
		return baseService.getHostname();
	}
	
	public String reboot() throws ConnectException, SOAPException {
		return baseService.reboot();
	}

	public String getUri(DeviceSubclass type) {
		// TODO Auto-generated method stub
		return null;
	}
}
