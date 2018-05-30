package org.onvif.unofficial.services;

import org.onvif.unofficial.NetOnvifDevice;
import org.onvif.unofficial.soapclient.ISoapClient;

public abstract class ServiceBase {
	protected NetOnvifDevice onvifDevice;
	protected ISoapClient client;
	protected String serviceUrl;

	public ServiceBase(NetOnvifDevice onvifDevice, ISoapClient client, String serviceUrl) {
		super();
		this.onvifDevice = onvifDevice;
		this.client = client;
		this.serviceUrl = serviceUrl;
	}
	
	

}
