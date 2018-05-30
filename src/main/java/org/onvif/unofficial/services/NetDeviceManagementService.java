package org.onvif.unofficial.services;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.onvif.unofficial.NetOnvifDevice;
import org.onvif.unofficial.api.DeviceManagementService;
import org.onvif.unofficial.soapclient.ISoapClient;
import org.onvif.ver10.device.wsdl.GetCapabilities;
import org.onvif.ver10.device.wsdl.GetCapabilitiesResponse;
import org.onvif.ver10.device.wsdl.GetDeviceInformation;
import org.onvif.ver10.device.wsdl.GetDeviceInformationResponse;
import org.onvif.ver10.device.wsdl.GetDot11Capabilities;
import org.onvif.ver10.device.wsdl.GetDot11CapabilitiesResponse;
import org.onvif.ver10.device.wsdl.GetDot1XConfigurations;
import org.onvif.ver10.device.wsdl.GetDot1XConfigurationsResponse;
import org.onvif.ver10.device.wsdl.GetHostname;
import org.onvif.ver10.device.wsdl.GetHostnameResponse;
import org.onvif.ver10.device.wsdl.GetNetworkInterfaces;
import org.onvif.ver10.device.wsdl.GetNetworkInterfacesResponse;
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
import org.onvif.ver10.device.wsdl.SetSystemDateAndTime;
import org.onvif.ver10.device.wsdl.SetSystemDateAndTimeResponse;
import org.onvif.ver10.device.wsdl.SystemReboot;
import org.onvif.ver10.device.wsdl.SystemRebootResponse;
import org.onvif.ver10.schema.Capabilities;
import org.onvif.ver10.schema.Date;
import org.onvif.ver10.schema.DateTime;
import org.onvif.ver10.schema.Dot11Capabilities;
import org.onvif.ver10.schema.Dot1XConfiguration;
import org.onvif.ver10.schema.NetworkInterface;
import org.onvif.ver10.schema.Scope;
import org.onvif.ver10.schema.SetDateTimeType;
import org.onvif.ver10.schema.Time;
import org.onvif.ver10.schema.TimeZone;
import org.onvif.ver10.schema.User;

public class NetDeviceManagementService extends ServiceBase implements DeviceManagementService {

	public NetDeviceManagementService(NetOnvifDevice onvifDevice, ISoapClient client, String serviceUrl) {
		super(onvifDevice, client, serviceUrl);
	}

	/* (non-Javadoc)
	 * @see org.onvif.unofficial.services.DeviceManagementService#getDate()
	 */
	@Override
	public java.util.Date getDate() throws Exception {
		GetSystemDateAndTimeResponse response = client.processRequest(new GetSystemDateAndTime(),
				GetSystemDateAndTimeResponse.class, serviceUrl, true);
		Date date = response.getSystemDateAndTime().getUTCDateTime().getDate();
		Time time = response.getSystemDateAndTime().getUTCDateTime().getTime();
		Calendar cal = new GregorianCalendar(date.getYear(), date.getMonth() - 1, date.getDay(), time.getHour(),
				time.getMinute(), time.getSecond());
		return cal.getTime();
	}

	/* (non-Javadoc)
	 * @see org.onvif.unofficial.services.DeviceManagementService#setSystemDateAndTime(java.util.Calendar)
	 */
	@Override
	public void setSystemDateAndTime(Calendar calendar) throws Exception{
		SetSystemDateAndTime request=new SetSystemDateAndTime();
		request.setDateTimeType(SetDateTimeType.MANUAL);
		request.setDaylightSavings(calendar.getTimeZone().useDaylightTime());
		TimeZone tz= new TimeZone();
		tz.setTZ(calendar.getTimeZone().getID());
		request.setTimeZone(tz);
		DateTime dt=new DateTime();
		Date d=new Date();
		d.setDay(calendar.get(Calendar.DAY_OF_MONTH));
		d.setMonth(calendar.get(Calendar.MONTH));
		d.setYear(calendar.get(Calendar.YEAR));
		dt.setDate(d);
		Time t=new Time();
		t.setHour(calendar.get(Calendar.HOUR));
		t.setMinute(calendar.get(Calendar.MINUTE));
		t.setSecond(calendar.get(Calendar.SECOND));
		dt.setTime(t);
		request.setUTCDateTime(dt);
		client.processRequest(request, SetSystemDateAndTimeResponse.class, serviceUrl, true);
	}
	
	/* (non-Javadoc)
	 * @see org.onvif.unofficial.services.DeviceManagementService#getDeviceInformation()
	 */
	@Override
	public GetDeviceInformationResponse getDeviceInformation() throws Exception {
		return client.processRequest(new GetDeviceInformation(), GetDeviceInformationResponse.class, serviceUrl, true);
	}

	/* (non-Javadoc)
	 * @see org.onvif.unofficial.services.DeviceManagementService#getHostname()
	 */
	@Override
	public String getHostname() throws Exception {
		GetHostnameResponse response = client.processRequest(new GetHostname(), GetHostnameResponse.class, serviceUrl,
				true);
		return response.getHostnameInformation().getName();
	}

	/* (non-Javadoc)
	 * @see org.onvif.unofficial.services.DeviceManagementService#setHostname(java.lang.String)
	 */
	@Override
	public void setHostname(String hostname) throws Exception {
		SetHostname request = new SetHostname();
		request.setName(hostname);
		client.processRequest(request, SetHostnameResponse.class, serviceUrl, true);
	}

	/* (non-Javadoc)
	 * @see org.onvif.unofficial.services.DeviceManagementService#getUsers()
	 */
	@Override
	public List<User> getUsers() throws Exception {
		GetUsersResponse response = client.processRequest(new GetUsers(), GetUsersResponse.class, serviceUrl, true);
		return response.getUser();
	}

	/* (non-Javadoc)
	 * @see org.onvif.unofficial.services.DeviceManagementService#getCapabilities()
	 */
	@Override
	public Capabilities getCapabilities() throws Exception {
		GetCapabilitiesResponse response = client.processRequest(new GetCapabilities(), GetCapabilitiesResponse.class,
				serviceUrl, false);
		return response.getCapabilities();
	}

	/* (non-Javadoc)
	 * @see org.onvif.unofficial.services.DeviceManagementService#getServices(boolean)
	 */
	@Override
	public List<Service> getServices(boolean includeCapability) throws Exception {
		GetServices request = new GetServices();
		request.setIncludeCapability(includeCapability);
		GetServicesResponse response = client.processRequest(request, GetServicesResponse.class, serviceUrl, true);
		if (response == null) {
			return null;
		}
		return response.getService();
	}

	/* (non-Javadoc)
	 * @see org.onvif.unofficial.services.DeviceManagementService#getScopes()
	 */
	@Override
	public List<Scope> getScopes() throws Exception {
		GetScopesResponse response = client.processRequest(new GetScopes(), GetScopesResponse.class, serviceUrl, true);
		if (response == null) {
			return null;
		}
		return response.getScopes();
	}

	/* (non-Javadoc)
	 * @see org.onvif.unofficial.services.DeviceManagementService#reboot()
	 */
	@Override
	public String reboot() throws Exception {
		SystemRebootResponse response = client.processRequest(new SystemReboot(), SystemRebootResponse.class,
				serviceUrl, true);
		if (response == null) {
			return null;
		}
		return response.getMessage();
	}

	/* (non-Javadoc)
	 * @see org.onvif.unofficial.services.DeviceManagementService#getNetworkInterfaces()
	 */
	@Override
	public List<NetworkInterface> getNetworkInterfaces() throws Exception {
		GetNetworkInterfacesResponse response = client.processRequest(new GetNetworkInterfaces(),
				GetNetworkInterfacesResponse.class, serviceUrl, true);
		if (response == null)
			return null;
		return response.getNetworkInterfaces();

	}

	/* (non-Javadoc)
	 * @see org.onvif.unofficial.services.DeviceManagementService#getDot11Capabilities()
	 */
	@Override
	public Dot11Capabilities getDot11Capabilities() throws Exception {
		GetDot11CapabilitiesResponse response = client.processRequest(new GetDot11Capabilities(),
				GetDot11CapabilitiesResponse.class, serviceUrl, true);
		if (response == null)
			return null;
		return response.getCapabilities();
	}

	/* (non-Javadoc)
	 * @see org.onvif.unofficial.services.DeviceManagementService#getDot1XConfigurations()
	 */
	@Override
	public List<Dot1XConfiguration> getDot1XConfigurations() throws Exception {
		GetDot1XConfigurationsResponse response = client.processRequest(new GetDot1XConfigurations(),
				GetDot1XConfigurationsResponse.class, serviceUrl, true);
		if (response == null)
			return null;
		return response.getDot1XConfiguration();
	}
}
