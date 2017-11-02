package org.onvif.unofficial.soapclient;



import org.onvif.unofficial.devices.DeviceSubclass;

public interface ISoapClient {

	// O processSOAPDeviceRequest(I soapRequestElem, Class<O> responseClass,
	// boolean needsAuthentification)
	// throws SOAPException, ConnectException;
	//
	// O processSOAPPtzRequest(Object soapRequestElem, Object soapResponseElem,
	// boolean needsAuthentification)
	// throws SOAPException, ConnectException;
	//
	// Object processSOAPMediaRequest(Object soapRequestElem, Object
	// soapResponseElem, boolean needsAuthentification)
	// throws SOAPException, ConnectException;
	//
	// Object processSOAPImagingRequest(Object soapRequestElem, Object
	// soapResponseElem, boolean needsAuthentification)
	// throws SOAPException, ConnectException;
	//
	// Object processSOAPEventsRequest(Object soapRequestElem, Object
	// soapResponseElem, boolean needsAuthentification)
	// throws SOAPException, ConnectException;

	<T extends SoapResponse> T processOnvifServiceRequest(SoapRequest request, Class<T> responseClass,
			DeviceSubclass type, boolean needsAuthentification) throws Exception;

	<T extends SoapResponse> T processRequest(SoapRequest request, Class<T> responseClass, String soapUri,
			boolean needsAuthentification) throws Exception;

}