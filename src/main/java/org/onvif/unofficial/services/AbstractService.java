package org.onvif.unofficial.services;

import org.onvif.unofficial.OnvifDevice;
import org.onvif.unofficial.soapclient.ISoapClient;

public abstract class AbstractService {
	protected OnvifDevice onvifDevice;
	protected ISoapClient client;
	protected String serviceUrl;

	public AbstractService(OnvifDevice onvifDevice, ISoapClient client, String serviceUrl) {
		super();
		this.onvifDevice = onvifDevice;
		this.client = client;
		this.serviceUrl = serviceUrl;
	}
	
	

}
