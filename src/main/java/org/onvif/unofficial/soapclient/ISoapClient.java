package org.onvif.unofficial.soapclient;

public interface ISoapClient {

	<T> T processRequest(Object request, Class<T> responseClass, String soapUri,
			boolean needsAuthentification) throws Exception;

}