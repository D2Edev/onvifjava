package org.onvif.unofficial.devices;

import java.net.ConnectException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.soap.SOAPException;

import org.onvif.unofficial.soapclient.ISoapClient;
import org.onvif.ver10.device.wsdl.GetCapabilities;
import org.onvif.ver10.device.wsdl.GetCapabilitiesResponse;
import org.onvif.ver10.device.wsdl.GetDeviceInformation;
import org.onvif.ver10.device.wsdl.GetDeviceInformationResponse;
import org.onvif.ver10.device.wsdl.GetHostname;
import org.onvif.ver10.device.wsdl.GetHostnameResponse;
import org.onvif.ver10.device.wsdl.GetScopes;
import org.onvif.ver10.device.wsdl.GetScopesResponse;
import org.onvif.ver10.device.wsdl.GetServices;
import org.onvif.ver10.device.wsdl.GetServicesResponse;
import org.onvif.ver10.device.wsdl.GetSystemDateAndTime;
import org.onvif.ver10.device.wsdl.GetSystemDateAndTimeResponse;
import org.onvif.ver10.device.wsdl.GetUsers;
import org.onvif.ver10.device.wsdl.GetUsersResponse;
import org.onvif.ver10.device.wsdl.Service;
import org.onvif.ver10.device.wsdl.SetHostname;
import org.onvif.ver10.device.wsdl.SetHostnameResponse;
import org.onvif.ver10.device.wsdl.SystemReboot;
import org.onvif.ver10.device.wsdl.SystemRebootResponse;
import org.onvif.ver10.media.wsdl.CreateProfile;
import org.onvif.ver10.media.wsdl.CreateProfileResponse;
import org.onvif.ver10.media.wsdl.GetProfile;
import org.onvif.ver10.media.wsdl.GetProfileResponse;
import org.onvif.ver10.media.wsdl.GetProfiles;
import org.onvif.ver10.media.wsdl.GetProfilesResponse;
import org.onvif.ver10.schema.Capabilities;
import org.onvif.ver10.schema.Date;
import org.onvif.ver10.schema.Profile;
import org.onvif.ver10.schema.Scope;
import org.onvif.ver10.schema.Time;
import org.onvif.ver10.schema.User;

public class BaseService {

	private static final DeviceSubclass TYPE = DeviceSubclass.BASE;
	private ISoapClient soap;
	private OnvifDevice onvifDevice;

	public BaseService(OnvifDevice onvifDevice) {
		this.onvifDevice = onvifDevice;
		this.soap = onvifDevice.getSoap();
	}

	public java.util.Date getDate() {

		try {
			GetSystemDateAndTimeResponse response = soap.processOnvifServiceRequest(new GetSystemDateAndTime(),
					GetSystemDateAndTimeResponse.class, TYPE, true);
			Date date = response.getSystemDateAndTime().getUTCDateTime().getDate();
			Time time = response.getSystemDateAndTime().getUTCDateTime().getTime();
			Calendar cal = new GregorianCalendar(date.getYear(), date.getMonth() - 1, date.getDay(), time.getHour(),
					time.getMinute(), time.getSecond());

			return cal.getTime();
		} catch (Exception e) {
			return null;
		}

	}

	public GetDeviceInformationResponse getDeviceInformation() {
		try {
			return soap.processOnvifServiceRequest(new GetDeviceInformation(), GetDeviceInformationResponse.class, TYPE,
					true);
		} catch (Exception e) {
			return null;
		}

	}

	public String getHostname() {
		try {
			GetHostnameResponse response = soap.processOnvifServiceRequest(new GetHostname(), GetHostnameResponse.class,
					TYPE, true);
			return response.getHostnameInformation().getName();
		} catch (Exception e) {
			return null;
		}

	}

	public boolean setHostname(String hostname) {
		SetHostname request = new SetHostname();
		request.setName(hostname);
		try {
			soap.processOnvifServiceRequest(request, SetHostnameResponse.class, TYPE, true);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public List<User> getUsers() {
		try {
			GetUsersResponse response = soap.processOnvifServiceRequest(new GetUsers(), GetUsersResponse.class, TYPE,
					true);
			return response.getUser();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public Capabilities getCapabilities() throws ConnectException, SOAPException {
		GetCapabilities getCapabilities = new GetCapabilities();
		GetCapabilitiesResponse response = new GetCapabilitiesResponse();

		response = (GetCapabilitiesResponse) soap.processRequest(getCapabilities, response, onvifDevice.getDeviceUri(),
				false);

		if (response == null) {
			return null;
		}

		return response.getCapabilities();
	}

	public List<Profile> getProfiles() {
		GetProfiles request = new GetProfiles();
		GetProfilesResponse response = new GetProfilesResponse();

		try {
			response = (GetProfilesResponse) soap.processSOAPMediaRequest(request, response, true);
		} catch (SOAPException | ConnectException e) {
			e.printStackTrace();
			return null;
		}

		if (response == null) {
			return null;
		}

		return response.getProfiles();
	}

	public Profile getProfile(String profileToken) {
		GetProfile request = new GetProfile();
		GetProfileResponse response = new GetProfileResponse();

		request.setProfileToken(profileToken);

		try {
			response = (GetProfileResponse) soap.processSOAPMediaRequest(request, response, true);
		} catch (SOAPException | ConnectException e) {
			e.printStackTrace();
			return null;
		}

		if (response == null) {
			return null;
		}

		return response.getProfile();
	}

	public Profile createProfile(String name) {
		CreateProfile request = new CreateProfile();
		CreateProfileResponse response = new CreateProfileResponse();

		request.setName(name);

		try {
			response = (CreateProfileResponse) soap.processSOAPMediaRequest(request, response, true);
		} catch (SOAPException | ConnectException e) {
			e.printStackTrace();
			return null;
		}

		if (response == null) {
			return null;
		}

		return response.getProfile();
	}

	public List<Service> getServices(boolean includeCapability) {
		GetServices request = new GetServices();
		GetServicesResponse response = new GetServicesResponse();

		request.setIncludeCapability(includeCapability);

		try {
			response = (GetServicesResponse) soap.processSOAPDeviceRequest(request, response, true);
		} catch (SOAPException | ConnectException e) {
			e.printStackTrace();
			return null;
		}

		if (response == null) {
			return null;
		}

		return response.getService();
	}

	public List<Scope> getScopes() {
		GetScopes request = new GetScopes();
		GetScopesResponse response = new GetScopesResponse();

		try {
			response = (GetScopesResponse) soap.processSOAPMediaRequest(request, response, true);
		} catch (SOAPException | ConnectException e) {
			e.printStackTrace();
			return null;
		}

		if (response == null) {
			return null;
		}

		return response.getScopes();
	}

	public String reboot() throws ConnectException, SOAPException {
		SystemReboot request = new SystemReboot();
		SystemRebootResponse response = new SystemRebootResponse();

		try {
			response = (SystemRebootResponse) soap.processSOAPMediaRequest(request, response, true);
		} catch (SOAPException | ConnectException e) {
			throw e;
		}

		if (response == null) {
			return null;
		}

		return response.getMessage();
	}
}
