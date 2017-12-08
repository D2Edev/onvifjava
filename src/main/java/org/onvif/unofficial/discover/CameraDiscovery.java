package org.onvif.unofficial.discover;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Device discovery class to list local accessible devices probed per UDP probe
 * messages for NetworkVideoTransmitter type.
 * description: http://specs.xmlsoap.org/ws/2005/04/discovery/ws-discovery.pdf
 * probe and probe matches processed semi-manually
 * (otherwise use org.xmlsoap.schemas.ws._2005._04.discovery models) 
 * Based on https://github.com/thhart/javaWsDiscovery
 */
public class CameraDiscovery {

	public static final String WS_DISCOVERY_SOAP_VERSION = "SOAP 1.2 Protocol";
//	public static String WS_DISCOVERY_CONTENT_TYPE = "application/soap+xml";
	public static final int WS_DISCOVERY_TIMEOUT = 4000;
	public static final int WS_DISCOVERY_PORT = 3702;
	public static final String WS_DISCOVERY_ADDRESS_IPv4 = "239.255.255.250";
	private static final Random random = new SecureRandom();

	public Set<URL> getDiscoveredDevices(String interfaceName) throws SocketException, InterruptedException {
		return getDiscoveredDevices(interfaceName, WS_DISCOVERY_TIMEOUT);

	}
	
	public Set<URL> getDiscoveredDevices(String interfaceName, int timeout) throws SocketException, InterruptedException {
		Collection<InetAddress> addresses = getAvailableAddresses(interfaceName);
		Set<URL> discovered = Collections.synchronizedSet(new HashSet<>());
		ExecutorService executorService = Executors.newCachedThreadPool();
		for (InetAddress inetAddress : addresses) {
			int port = random.nextInt(20000) + 40000;
			DiscoverTask task = new DiscoverTask(inetAddress, port, discovered,timeout);
			executorService.submit(task);
		}
		executorService.shutdown();
		executorService.awaitTermination(timeout, TimeUnit.MILLISECONDS);
		return discovered;
	}

	private Collection<InetAddress> getAvailableAddresses(String interfaceName) throws SocketException {
		boolean useAll = false;
		if (interfaceName == null || interfaceName.isEmpty()) {
			useAll = true;
		}
		final Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
		ArrayList<InetAddress> addresses = new ArrayList<>();
		while (interfaces.hasMoreElements()) {
			NetworkInterface iface = interfaces.nextElement();
			try {
				if (iface.isLoopback())
					continue;
			} catch (SocketException e) {
				continue;
			}
			if (useAll) {
				for (InterfaceAddress address : iface.getInterfaceAddresses()) {
					addConditional(address.getAddress(), addresses);
				}
			} else {
				if (iface.getName().startsWith(interfaceName)) {
					for (InterfaceAddress address : iface.getInterfaceAddresses()) {
						addConditional(address.getAddress(), addresses);
					}
				}
			}
		}
		return addresses;
	}

	private void addConditional(InetAddress address, ArrayList<InetAddress> addresses) {
		if (address instanceof Inet6Address)
			return;
		addresses.add(address);
	}


	public static class DiscoverTask implements Runnable {

		final String uuid = UUID.randomUUID().toString();
		private InetAddress inetAddress;
		private int port;
		private Set<URL> discovered;
		CountDownLatch serverStartFlag = new CountDownLatch(1);
		CountDownLatch serverStopFlag = new CountDownLatch(1);
		private int timeout;

		public DiscoverTask(InetAddress inetAddress, int port, Set<URL> discovered, int timeout) {
			this.inetAddress = inetAddress;
			this.port = port;
			this.discovered = discovered;
			this.timeout=timeout;
		}

		@Override
		public void run() {
			try {
				final DatagramSocket server = new DatagramSocket(port, inetAddress);
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							server.setSoTimeout(timeout);;
							long timerStarted = System.currentTimeMillis();
							DatagramPacket packet = new DatagramPacket(new byte[4096], 4096);
							while (System.currentTimeMillis() - timerStarted < timeout) {
								serverStartFlag.countDown();
								server.receive(packet);
								parseSoapResponseForUrls(Arrays.copyOf(packet.getData(), packet.getLength()));
							}
						} catch (SocketException e) {
							serverStartFlag.countDown();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							serverStartFlag.countDown();
						} finally {
							if (server != null) {
								server.close();
								serverStopFlag.countDown();
							}
						}
					}

				}).start();
				try {
					serverStartFlag.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				try {
					byte[] content= createProbeXML();
					DatagramPacket probe = new DatagramPacket(content, content.length,
							InetAddress.getByName(WS_DISCOVERY_ADDRESS_IPv4), WS_DISCOVERY_PORT);
					server.send(probe);
					serverStopFlag.await(timeout,TimeUnit.MILLISECONDS);
				} catch (SOAPException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (SocketException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		private void parseSoapResponseForUrls(byte[] data) {
//			System.out.println(new String(data));
			try {
				MessageFactory factory= MessageFactory.newInstance(WS_DISCOVERY_SOAP_VERSION);
				final MimeHeaders headers = new MimeHeaders();
//				headers.addHeader("Content-type", WS_DISCOVERY_CONTENT_TYPE);
				SOAPMessage message = factory.createMessage(headers, new ByteArrayInputStream(data));
				SOAPPart part=message.getSOAPPart();
				SOAPEnvelope env=part.getEnvelope();
				SOAPBody body=message.getSOAPBody();
				NodeList list=body.getElementsByTagNameNS("http://schemas.xmlsoap.org/ws/2005/04/discovery", "XAddrs");
				int items=list.getLength();
				if(items<1)return;
				for (int i = 0; i < items; i++) {
					Node n=list.item(i);
					String raw=n.getTextContent();
					//may contain several
					String []addrArray=raw.split(" ");
					for (String string : addrArray) {
						URL url=new URL(string);
						discovered.add(url);						
					}
				}
			} catch (Exception e) {
				System.out.println("Parse failed");
				e.printStackTrace();
			}

		}

		private byte[] createProbeXML() throws SOAPException, IOException {
			MessageFactory messageFactory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
			SOAPMessage message = messageFactory.createMessage();
			SOAPPart part = message.getSOAPPart();
			SOAPEnvelope envelope = part.getEnvelope();
			envelope.addNamespaceDeclaration("wsa", "http://schemas.xmlsoap.org/ws/2004/08/addressing");
			envelope.addNamespaceDeclaration("tns", "http://schemas.xmlsoap.org/ws/2005/04/discovery");
			envelope.addNamespaceDeclaration("nns", "http://www.onvif.org/ver10/network/wsdl");
			QName action = envelope.createQName("Action", "wsa");
			QName mid = envelope.createQName("MessageID", "wsa");
			QName to = envelope.createQName("To", "wsa");
			QName probe = envelope.createQName("Probe", "tns");
			QName types = envelope.createQName("Types", "tns");
			QName tramsmitter=envelope.createQName("NetworkVideoTransmitter", "nns");
			SOAPHeader header = envelope.getHeader();
			SOAPElement actionEl = header.addChildElement(action);
			actionEl.setTextContent("http://schemas.xmlsoap.org/ws/2005/04/discovery/Probe");
			SOAPElement messIsEl = header.addChildElement(mid);
			messIsEl.setTextContent("urn:uuid:" + UUID.randomUUID().toString());
			SOAPElement toEl = header.addChildElement(to);
			toEl.setTextContent("urn:schemas-xmlsoap-org:ws:2005:04:discovery");
			SOAPBody body = envelope.getBody();
			SOAPElement probeEl = body.addChildElement(probe);
			SOAPElement typesEl=probeEl.addChildElement(types);
			typesEl.setTextContent("nns:NetworkVideoTransmitter");
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			message.writeTo(out);
			return out.toByteArray();
		}

	}
	

	public static void main(String[] args) {
		CameraDiscovery d = new CameraDiscovery();
		try {
			Set<URL> urls=d.getDiscoveredDevices(null,1000);
			for (URL url : urls) {
				System.out.println(url);
			}
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Set<URL> getURLs(String interfaceName) throws SocketException, InterruptedException {
		CameraDiscovery d = new CameraDiscovery();
		return d.getDiscoveredDevices(interfaceName);
	}
	
	public static Set<URL> getURLs(String interfaceName, int timeout) throws SocketException, InterruptedException {
		CameraDiscovery d = new CameraDiscovery();
		return d.getDiscoveredDevices(interfaceName,timeout);
	}

	
}
