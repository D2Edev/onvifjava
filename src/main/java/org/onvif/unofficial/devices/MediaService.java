package org.onvif.unofficial.devices;

import java.net.ConnectException;
import java.util.List;

import javax.xml.soap.SOAPException;

import org.onvif.unofficial.soapclient.ISoapClient;
import org.onvif.ver10.media.wsdl.GetSnapshotUri;
import org.onvif.ver10.media.wsdl.GetSnapshotUriResponse;
import org.onvif.ver10.media.wsdl.GetStreamUri;
import org.onvif.ver10.media.wsdl.GetStreamUriResponse;
import org.onvif.ver10.media.wsdl.GetVideoEncoderConfigurationOptions;
import org.onvif.ver10.media.wsdl.GetVideoEncoderConfigurationOptionsResponse;
import org.onvif.ver10.media.wsdl.GetVideoSources;
import org.onvif.ver10.media.wsdl.GetVideoSourcesResponse;
import org.onvif.ver10.media.wsdl.SetVideoEncoderConfiguration;
import org.onvif.ver10.media.wsdl.SetVideoEncoderConfigurationResponse;
import org.onvif.ver10.schema.Profile;
import org.onvif.ver10.schema.StreamSetup;
import org.onvif.ver10.schema.StreamType;
import org.onvif.ver10.schema.Transport;
import org.onvif.ver10.schema.TransportProtocol;
import org.onvif.ver10.schema.VideoEncoderConfiguration;
import org.onvif.ver10.schema.VideoEncoderConfigurationOptions;
import org.onvif.ver10.schema.VideoSource;

public class MediaService {
	private OnvifDevice onvifDevice;
	private ISoapClient soap;

	public MediaService(OnvifDevice onvifDevice) {
		this.onvifDevice = onvifDevice;
		this.soap = onvifDevice.getSoap();
	}

	@Deprecated
	public String getHTTPStreamUri(int profileNumber) throws ConnectException, SOAPException {
		StreamSetup setup = new StreamSetup();
		setup.setStream(StreamType.RTP_UNICAST);
		Transport transport = new Transport();
		transport.setProtocol(TransportProtocol.HTTP);
		setup.setTransport(transport);
		return getStreamUri(setup, profileNumber);
	}

	public String getHTTPStreamUri(String profileToken) throws ConnectException, SOAPException {
		StreamSetup setup = new StreamSetup();
		setup.setStream(StreamType.RTP_UNICAST);
		Transport transport = new Transport();
		transport.setProtocol(TransportProtocol.HTTP);
		setup.setTransport(transport);
		return getStreamUri(profileToken, setup);
	}

	@Deprecated
	public String getUDPStreamUri(int profileNumber) throws ConnectException, SOAPException {
		StreamSetup setup = new StreamSetup();
		setup.setStream(StreamType.RTP_UNICAST);
		Transport transport = new Transport();
		transport.setProtocol(TransportProtocol.UDP);
		setup.setTransport(transport);
		return getStreamUri(setup, profileNumber);
	}

	public String getUDPStreamUri(String profileToken) throws ConnectException, SOAPException {
		StreamSetup setup = new StreamSetup();
		setup.setStream(StreamType.RTP_UNICAST);
		Transport transport = new Transport();
		transport.setProtocol(TransportProtocol.UDP);
		setup.setTransport(transport);
		return getStreamUri(profileToken, setup);
	}

	@Deprecated
	public String getTCPStreamUri(int profileNumber) throws ConnectException, SOAPException {
		StreamSetup setup = new StreamSetup();
		setup.setStream(StreamType.RTP_UNICAST);
		Transport transport = new Transport();
		transport.setProtocol(TransportProtocol.TCP);
		setup.setTransport(transport);
		return getStreamUri(setup, profileNumber);
	}

	public String getTCPStreamUri(String profileToken) throws ConnectException, SOAPException {
		StreamSetup setup = new StreamSetup();
		setup.setStream(StreamType.RTP_UNICAST);
		Transport transport = new Transport();
		transport.setProtocol(TransportProtocol.TCP);
		setup.setTransport(transport);
		return getStreamUri(profileToken, setup);
	}

	@Deprecated
	public String getRTSPStreamUri(int profileNumber) throws ConnectException, SOAPException {
		StreamSetup setup = new StreamSetup();
		setup.setStream(StreamType.RTP_UNICAST);
		Transport transport = new Transport();
		transport.setProtocol(TransportProtocol.TCP);
		setup.setTransport(transport);
		return getStreamUri(setup, profileNumber);
	}
	
	public String getRTSPStreamUri(String profileToken) throws ConnectException, SOAPException {
		StreamSetup setup = new StreamSetup();
		setup.setStream(StreamType.RTP_UNICAST);
		Transport transport = new Transport();
		transport.setProtocol(TransportProtocol.TCP);
		setup.setTransport(transport);
		return getStreamUri(profileToken, setup);
	}
	
	@Deprecated
	public String getStreamUri(StreamSetup streamSetup, int profileNumber) throws ConnectException, SOAPException {
		Profile profile = onvifDevice.getDevices().getProfiles().get(profileNumber);
		return getStreamUri(profile, streamSetup);
	}

	@Deprecated
	public String getStreamUri(Profile profile, StreamSetup streamSetup) throws ConnectException, SOAPException {
		return getStreamUri(profile.getToken(), streamSetup);
	}

	public String getStreamUri(String profileToken, StreamSetup streamSetup) throws SOAPException, ConnectException {
		GetStreamUri request = new GetStreamUri();
		GetStreamUriResponse response = new GetStreamUriResponse();

		request.setProfileToken(profileToken);
		request.setStreamSetup(streamSetup);

		try {
			response = (GetStreamUriResponse) soap.processSOAPMediaRequest(request, response, false);
		}
		catch (SOAPException | ConnectException e) {
			throw e;
		}

		if (response == null) {
			return null;
		}

		return onvifDevice.replaceLocalIpWithProxyIp(response.getMediaUri().getUri());
	}

	public static VideoEncoderConfiguration getVideoEncoderConfiguration(Profile profile) {
		return profile.getVideoEncoderConfiguration();
	}

	public VideoEncoderConfigurationOptions getVideoEncoderConfigurationOptions(String profileToken) throws SOAPException, ConnectException {
		GetVideoEncoderConfigurationOptions request = new GetVideoEncoderConfigurationOptions();
		GetVideoEncoderConfigurationOptionsResponse response = new GetVideoEncoderConfigurationOptionsResponse();

		request.setProfileToken(profileToken);

		try {
			response = (GetVideoEncoderConfigurationOptionsResponse) soap.processSOAPMediaRequest(request, response, false);
		}
		catch (SOAPException | ConnectException e) {
			throw e;
		}

		if (response == null) {
			return null;
		}

		return response.getOptions();
	}

	public boolean setVideoEncoderConfiguration(VideoEncoderConfiguration videoEncoderConfiguration) throws SOAPException, ConnectException {
		SetVideoEncoderConfiguration request = new SetVideoEncoderConfiguration();
		SetVideoEncoderConfigurationResponse response = new SetVideoEncoderConfigurationResponse();

		request.setConfiguration(videoEncoderConfiguration);
		request.setForcePersistence(true);

		try {
			response = (SetVideoEncoderConfigurationResponse) soap.processSOAPMediaRequest(request, response, true);
		}
		catch (SOAPException | ConnectException e) {
			throw e;
		}

		if (response == null) {
			return false;
		}

		return true;
	}

	public String getSceenshotUri(String profileToken) throws SOAPException, ConnectException {
		return getSnapshotUri(profileToken);
	}

	public String getSnapshotUri(String profileToken) throws SOAPException, ConnectException {
		GetSnapshotUri request = new GetSnapshotUri();
		GetSnapshotUriResponse response = new GetSnapshotUriResponse();

		request.setProfileToken(profileToken);

		try {
			response = (GetSnapshotUriResponse) soap.processSOAPMediaRequest(request, response, true);
		}
		catch (SOAPException | ConnectException e) {
			throw e;
		}
		
		if (response == null || response.getMediaUri() == null) {
			return null;
		}
		
		return onvifDevice.replaceLocalIpWithProxyIp(response.getMediaUri().getUri());
	}

	public List<VideoSource> getVideoSources() throws SOAPException, ConnectException {
		GetVideoSources request = new GetVideoSources();
		GetVideoSourcesResponse response = new GetVideoSourcesResponse();

		try {
			response = (GetVideoSourcesResponse) soap.processSOAPMediaRequest(request, response, false);
		}
		catch (SOAPException | ConnectException e) {
			throw e;
		}

		if (response == null) {
			return null;
		}

		return response.getVideoSources();
	}
}
