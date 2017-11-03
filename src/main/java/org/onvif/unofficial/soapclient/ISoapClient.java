package org.onvif.unofficial.soapclient;



import org.onvif.unofficial.devices.DeviceSubclass;

public interface ISoapClient {

	<T extends SoapResponse> T processOnvifServiceRequest(SoapRequest request, Class<T> responseClass,
			DeviceSubclass type, boolean needsAuthentification) throws Exception;

	<T extends SoapResponse> T processRequest(SoapRequest request, Class<T> responseClass, String soapUri,
			boolean needsAuthentification) throws Exception;

}