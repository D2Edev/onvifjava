package org.onvif.unofficial.discover;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.onvif.unofficial.discover.DeviceDiscoveryIPv4.DiscoverTask;

/**
 * Device discovery class to list local accessible devices probed per UDP probe
 * messages. Based on https://github.com/thhart/javaWsDiscovery
 */
public class DeviceDiscoveryIPv4 {

	public static String WS_DISCOVERY_SOAP_VERSION = "SOAP 1.2 Protocol";
	public static String WS_DISCOVERY_CONTENT_TYPE = "application/soap+xml";
	public static int WS_DISCOVERY_TIMEOUT = 4000;
	public static final int WS_DISCOVERY_PORT = 3702;
	public static final String WS_DISCOVERY_ADDRESS_IPv4 = "239.255.255.250";

	public static String WS_DISCOVERY_PROBE_MESSAGE = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:wsa=\"http://schemas.xmlsoap.org/ws/2004/08/addressing\" xmlns:tns=\"http://schemas.xmlsoap.org/ws/2005/04/discovery\"><soap:Header><wsa:Action>http://schemas.xmlsoap.org/ws/2005/04/discovery/Probe</wsa:Action><wsa:MessageID>urn:uuid:c032cfdd-c3ca-49dc-820e-ee6696ad63e2</wsa:MessageID><wsa:To>urn:schemas-xmlsoap-org:ws:2005:04:discovery</wsa:To></soap:Header><soap:Body><tns:Probe/></soap:Body></soap:Envelope>";
	private static final Random random = new SecureRandom();

	List<URL> getDiscoveredDevices(String interfaceName) throws SocketException, InterruptedException {
		Collection<InetAddress> addresses = getAvailableAddresses(interfaceName);
		System.out.println(addresses);
		List<URL> discovered = Collections.synchronizedList(new ArrayList<>());
		ExecutorService executorService = Executors.newCachedThreadPool();
		for (InetAddress inetAddress : addresses) {
			DiscoverTask task = new DiscoverTask(inetAddress, discovered);
			executorService.submit(task);
		}
		executorService.shutdown();
		executorService.awaitTermination(WS_DISCOVERY_TIMEOUT, TimeUnit.MILLISECONDS);
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

	public static void main(String[] args) {
		DeviceDiscoveryIPv4 d = new DeviceDiscoveryIPv4();
		try {
			d.getDiscoveredDevices(null);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static class DiscoverTask implements Runnable {

		public DiscoverTask(InetAddress inetAddress, List<URL> discovered) {
			// TODO Auto-generated constructor stub
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub

		}

	}
}
