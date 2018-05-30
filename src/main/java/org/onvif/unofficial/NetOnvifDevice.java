package org.onvif.unofficial;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.SOAPException;

import org.onvif.unofficial.services.NetDeviceManagementService;
import org.onvif.unofficial.api.DeviceIOService;
import org.onvif.unofficial.api.DeviceManagementService;
import org.onvif.unofficial.api.MediaService;
import org.onvif.unofficial.api.OnvifDevice;
import org.onvif.unofficial.api.PTZService;
import org.onvif.unofficial.services.NetDeviceIOService;
import org.onvif.unofficial.services.NetMediaService;
import org.onvif.unofficial.services.NetPTZService;
import org.onvif.unofficial.soapclient.ISoapClient;
import org.onvif.unofficial.soapclient.SoapClient;
import org.onvif.ver10.schema.Capabilities;


public class NetOnvifDevice implements OnvifDevice {
	private final String domain;
	private String returnedDomain;
	private boolean isProxy;
	private String username, password;
	private ISoapClient client;
	private DeviceManagementService devMngtService;
	private PTZService ptzService;
	private MediaService mediaService;
	private DeviceIOService imagingService;

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
	public NetOnvifDevice(String domain, String user, String password) throws Exception {

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
	public NetOnvifDevice(String hostIp) throws Exception {
		this(hostIp, null, null);
	}


	/* (non-Javadoc)
	 * @see org.onvif.unofficial.OnvifDevice#getDeviceManagementService()
	 */
	@Override
	public DeviceManagementService getDeviceManagementService() {
		return devMngtService;
	}


	/* (non-Javadoc)
	 * @see org.onvif.unofficial.OnvifDevice#getPTZService()
	 */
	@Override
	public PTZService getPTZService() {
		return ptzService;
	}


	/* (non-Javadoc)
	 * @see org.onvif.unofficial.OnvifDevice#getMediaService()
	 */
	@Override
	public MediaService getMediaService() {
		return mediaService;
	}

	/* (non-Javadoc)
	 * @see org.onvif.unofficial.OnvifDevice#getDeviceIOService()
	 */
	@Override
	public DeviceIOService getDeviceIOService() {
		return imagingService;
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

	/**
	 * Initalizes the addresses used for SOAP messages and to get the internal
	 * IP, if given IP is a proxy.
	 * 
	 * @throws Exception
	 */
	private void init() throws Exception {
	
		this.client = new SoapClient(username,password);
		this.devMngtService = new NetDeviceManagementService(this,client, "http://" + domain + "/onvif/device_service");
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
			this.mediaService = new NetMediaService(this,client, replaceLocalIpWithProxyIp(capabilities.getMedia().getXAddr()));
		}
	
		if (capabilities.getPTZ() != null && capabilities.getPTZ().getXAddr() != null) {
			this.ptzService = new NetPTZService(this,client, replaceLocalIpWithProxyIp(capabilities.getPTZ().getXAddr()));
		}
	
		if (capabilities.getImaging() != null && capabilities.getImaging().getXAddr() != null) {
			this.imagingService = new NetDeviceIOService(this,client,
					replaceLocalIpWithProxyIp(capabilities.getImaging().getXAddr()));
		}
	
		// event uri - not used currently
		// if (capabilities.getMedia() != null &&
		// capabilities.getEvents().getXAddr() != null) {
		// replaceLocalIpWithProxyIp(capabilities.getEvents().getXAddr());
		// }
	}

	/**
	 * Internal function to check if device is available.
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


}
