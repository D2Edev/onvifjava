package org.onvif.unofficial.onvifjava;

import java.net.ConnectException;

import javax.xml.soap.SOAPException;

import org.junit.Before;
import org.junit.Test;
import org.onvif.unofficial.devices.OnvifDevice;

import junit.framework.TestCase;

public class TestHelper extends TestCase {
	
	private OnvifDevice device;
	
	@Before
	public void init() throws ConnectException, SOAPException{
	}


	@Test
	public void testGetUTCTime() {
		try {
			device=new OnvifDevice("192.168.0.65");
		} catch (ConnectException | SOAPException e) {
			// TODO Auto-generated catch block
			fail(e.getMessage());
		}
		String time=device.getLastUTCTime();
		System.out.println(time);
		assertNotNull(time);
	}

}
