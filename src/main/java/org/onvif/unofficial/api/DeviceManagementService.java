package org.onvif.unofficial.api;

import java.util.Calendar;
import java.util.List;

import org.onvif.ver10.device.wsdl.GetDeviceInformationResponse;
import org.onvif.ver10.device.wsdl.Service;
import org.onvif.ver10.schema.Capabilities;
import org.onvif.ver10.schema.Dot11Capabilities;
import org.onvif.ver10.schema.Dot1XConfiguration;
import org.onvif.ver10.schema.NetworkInterface;
import org.onvif.ver10.schema.Scope;
import org.onvif.ver10.schema.User;

public interface DeviceManagementService {

	java.util.Date getDate() throws Exception;

	void setSystemDateAndTime(Calendar calendar) throws Exception;

	GetDeviceInformationResponse getDeviceInformation() throws Exception;

	String getHostname() throws Exception;

	void setHostname(String hostname) throws Exception;

	List<User> getUsers() throws Exception;

	Capabilities getCapabilities() throws Exception;

	List<Service> getServices(boolean includeCapability) throws Exception;

	List<Scope> getScopes() throws Exception;

	String reboot() throws Exception;

	List<NetworkInterface> getNetworkInterfaces() throws Exception;

	Dot11Capabilities getDot11Capabilities() throws Exception;

	List<Dot1XConfiguration> getDot1XConfigurations() throws Exception;

}