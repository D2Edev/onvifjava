package org.onvif.unofficial.onvifjava;

import java.net.ConnectException;

import javax.xml.soap.SOAPException;

import org.junit.Before;
import org.junit.Test;
import org.onvif.unofficial.OnvifDevice;

import junit.framework.TestCase;

public class TestHelper extends TestCase {
	
	private OnvifDevice device;
	
	@Before
	public void init() throws ConnectException, SOAPException{
	}


	@Test
	public void testGetUTCTime() {
		assertNotNull("implementation will follow");
	}

}
