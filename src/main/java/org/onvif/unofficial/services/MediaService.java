package org.onvif.unofficial.services;

import java.util.List;


import org.onvif.unofficial.OnvifDevice;
import org.onvif.unofficial.soapclient.ISoapClient;
import org.onvif.ver10.media.wsdl.CreateProfile;
import org.onvif.ver10.media.wsdl.CreateProfileResponse;
import org.onvif.ver10.media.wsdl.GetOSDs;
import org.onvif.ver10.media.wsdl.GetOSDsResponse;
import org.onvif.ver10.media.wsdl.GetProfile;
import org.onvif.ver10.media.wsdl.GetProfileResponse;
import org.onvif.ver10.media.wsdl.GetProfiles;
import org.onvif.ver10.media.wsdl.GetProfilesResponse;
import org.onvif.ver10.media.wsdl.GetSnapshotUri;
import org.onvif.ver10.media.wsdl.GetSnapshotUriResponse;
import org.onvif.ver10.media.wsdl.GetStreamUri;
import org.onvif.ver10.media.wsdl.GetStreamUriResponse;
import org.onvif.ver10.media.wsdl.GetVideoEncoderConfigurationOptions;
import org.onvif.ver10.media.wsdl.GetVideoEncoderConfigurationOptionsResponse;
import org.onvif.ver10.media.wsdl.GetVideoSourceConfiguration;
import org.onvif.ver10.media.wsdl.GetVideoSourceConfigurationResponse;
import org.onvif.ver10.media.wsdl.GetVideoSourceConfigurations;
import org.onvif.ver10.media.wsdl.GetVideoSourceConfigurationsResponse;
import org.onvif.ver10.media.wsdl.GetVideoSources;
import org.onvif.ver10.media.wsdl.GetVideoSourcesResponse;
import org.onvif.ver10.media.wsdl.SetVideoEncoderConfiguration;
import org.onvif.ver10.media.wsdl.SetVideoEncoderConfigurationResponse;
import org.onvif.ver10.schema.OSDConfiguration;
import org.onvif.ver10.schema.Profile;
import org.onvif.ver10.schema.StreamSetup;
import org.onvif.ver10.schema.StreamType;
import org.onvif.ver10.schema.Transport;
import org.onvif.ver10.schema.TransportProtocol;
import org.onvif.ver10.schema.VideoEncoderConfiguration;
import org.onvif.ver10.schema.VideoEncoderConfigurationOptions;
import org.onvif.ver10.schema.VideoSource;
import org.onvif.ver10.schema.VideoSourceConfiguration;

public class MediaService extends AbstractService {

	public MediaService(OnvifDevice onvifDevice, ISoapClient client, String serviceUrl) {
		super(onvifDevice, client, serviceUrl);
	}

	public String getHTTPStreamUri(String profileToken) throws Exception {
		StreamSetup setup = new StreamSetup();
		setup.setStream(StreamType.RTP_UNICAST);
		Transport transport = new Transport();
		transport.setProtocol(TransportProtocol.HTTP);
		setup.setTransport(transport);
		return getStreamUri(profileToken, setup);
	}

	public String getUDPStreamUri(String profileToken) throws Exception {
		StreamSetup setup = new StreamSetup();
		setup.setStream(StreamType.RTP_UNICAST);
		Transport transport = new Transport();
		transport.setProtocol(TransportProtocol.UDP);
		setup.setTransport(transport);
		return getStreamUri(profileToken, setup);
	}

	public String getTCPStreamUri(String profileToken) throws Exception {
		StreamSetup setup = new StreamSetup();
		setup.setStream(StreamType.RTP_UNICAST);
		Transport transport = new Transport();
		transport.setProtocol(TransportProtocol.TCP);
		setup.setTransport(transport);
		return getStreamUri(profileToken, setup);
	}

	public String getRTSPStreamUri(String profileToken) throws Exception {
		StreamSetup setup = new StreamSetup();
		setup.setStream(StreamType.RTP_UNICAST);
		Transport transport = new Transport();
		transport.setProtocol(TransportProtocol.RTSP);
		setup.setTransport(transport);
		return getStreamUri(profileToken, setup);
	}

	public String getStreamUri(String profileToken, StreamSetup streamSetup) throws Exception {
		GetStreamUri request = new GetStreamUri();
		request.setProfileToken(profileToken);
		request.setStreamSetup(streamSetup);
		GetStreamUriResponse response = client.processRequest(request, GetStreamUriResponse.class, serviceUrl, true);
		if (response == null) {
			return null;
		}

		return onvifDevice.replaceLocalIpWithProxyIp(response.getMediaUri().getUri());
	}

	public static VideoEncoderConfiguration getVideoEncoderConfiguration(Profile profile) {
		return profile.getVideoEncoderConfiguration();
	}

	public VideoEncoderConfigurationOptions getVideoEncoderConfigurationOptions(String profileToken) throws Exception {
		GetVideoEncoderConfigurationOptions request = new GetVideoEncoderConfigurationOptions();
		request.setProfileToken(profileToken);
		GetVideoEncoderConfigurationOptionsResponse response = client.processRequest(request,
				GetVideoEncoderConfigurationOptionsResponse.class, serviceUrl, true);
		if (response == null) {
			return null;
		}
		return response.getOptions();
	}

	public boolean setVideoEncoderConfiguration(VideoEncoderConfiguration videoEncoderConfiguration) throws Exception {
		SetVideoEncoderConfiguration request = new SetVideoEncoderConfiguration();
		request.setConfiguration(videoEncoderConfiguration);
		request.setForcePersistence(true);
		SetVideoEncoderConfigurationResponse response = client.processRequest(request,
				SetVideoEncoderConfigurationResponse.class, serviceUrl, true);
		if (response == null) {
			return false;
		}
		return true;
	}

	public String getSnapshotUri(String profileToken) throws Exception {
		GetSnapshotUri request = new GetSnapshotUri();
		request.setProfileToken(profileToken);
		GetSnapshotUriResponse response = client.processRequest(request, GetSnapshotUriResponse.class, serviceUrl,
				true);
		if (response == null || response.getMediaUri() == null) {
			return null;
		}
		return onvifDevice.replaceLocalIpWithProxyIp(response.getMediaUri().getUri());
	}

	public List<VideoSource> getVideoSources() throws Exception {
		GetVideoSources request = new GetVideoSources();
		GetVideoSourcesResponse response = client.processRequest(request, GetVideoSourcesResponse.class, serviceUrl,
				true);
		if (response == null) {
			return null;
		}
		return response.getVideoSources();
	}

	public List<Profile> getProfiles() throws Exception {
		return client.processRequest(new GetProfiles(), GetProfilesResponse.class, serviceUrl, true).getProfiles();
	}

	public Profile getProfile(String profileToken) throws Exception {
		GetProfile request = new GetProfile();
		request.setProfileToken(profileToken);
		GetProfileResponse response = client.processRequest(request, GetProfileResponse.class, serviceUrl, true);
		if (response == null) {
			return null;
		}
		return response.getProfile();
	}

	public Profile createProfile(String name) throws Exception {
		CreateProfile request = new CreateProfile();
		request.setName(name);
		CreateProfileResponse response=client.processRequest(request, CreateProfileResponse.class, serviceUrl, true); 
		if (response == null) {
			return null;
		}
		return response.getProfile();
		
	}
	
	
	public List<OSDConfiguration> getOSDs(String videoSourceConfigurationToken) throws Exception{
		GetOSDs request=new GetOSDs();
		request.setConfigurationToken(videoSourceConfigurationToken);
		GetOSDsResponse response=client.processRequest(request, GetOSDsResponse.class, serviceUrl, true);
		if(response==null)return null;
		return response.getOSDs();
	}
	
	public List<VideoSourceConfiguration> getVideoSourceConfigurations() throws Exception{
		GetVideoSourceConfigurationsResponse response= client.processRequest(new GetVideoSourceConfigurations(), GetVideoSourceConfigurationsResponse.class, serviceUrl, true);
		if(response==null)return null;
		return response.getConfigurations();
	}
	
	public VideoSourceConfiguration getVideoSourceConfiguration(String configToken) throws Exception{
		GetVideoSourceConfiguration request=new GetVideoSourceConfiguration();
		request.setConfigurationToken(configToken);
		GetVideoSourceConfigurationResponse response= client.processRequest(request, GetVideoSourceConfigurationResponse.class, serviceUrl, true);
		if(response==null)return null;
		return response.getConfiguration();
	}
	
	
}
