package org.onvif.unofficial.devices;

import java.net.ConnectException;

import javax.xml.soap.SOAPException;

import org.onvif.unofficial.soapclient.ISoapClient;
import org.onvif.ver10.schema.AbsoluteFocus;
import org.onvif.ver10.schema.FocusMove;
import org.onvif.ver10.schema.ImagingOptions20;
import org.onvif.ver10.schema.ImagingSettings20;
import org.onvif.ver20.imaging.wsdl.GetImagingSettings;
import org.onvif.ver20.imaging.wsdl.GetImagingSettingsResponse;
import org.onvif.ver20.imaging.wsdl.GetOptions;
import org.onvif.ver20.imaging.wsdl.GetOptionsResponse;
import org.onvif.ver20.imaging.wsdl.Move;
import org.onvif.ver20.imaging.wsdl.MoveResponse;
import org.onvif.ver20.imaging.wsdl.SetImagingSettings;
import org.onvif.ver20.imaging.wsdl.SetImagingSettingsResponse;


public class ImagingService {
//	@SuppressWarnings("unused")
	private OnvifDevice onvifDevice;
	private ISoapClient client;

	public ImagingService(OnvifDevice onvifDevice) {
		this.onvifDevice = onvifDevice;
		this.client = onvifDevice.getSoap();
	}

	public ImagingOptions20 getOptions(String videoSourceToken) {
		if (videoSourceToken == null) {
			return null;
		}

		GetOptions request = new GetOptions();
		GetOptionsResponse response = new GetOptionsResponse();

		request.setVideoSourceToken(videoSourceToken);

		try {
			response = (GetOptionsResponse) client.processSOAPImagingRequest(request, response, false);
		}
		catch (SOAPException | ConnectException e) {
			e.printStackTrace();
			return null;
		}

		if (response == null) {
			return null;
		}

		return response.getImagingOptions();
	}

	public boolean moveFocus(String videoSourceToken, float absoluteFocusValue) {
		if (videoSourceToken == null) {
			return false;
		}

		Move request = new Move();
		MoveResponse response = new MoveResponse();

		AbsoluteFocus absoluteFocus = new AbsoluteFocus();
		absoluteFocus.setPosition(absoluteFocusValue);

		FocusMove focusMove = new FocusMove();
		focusMove.setAbsolute(absoluteFocus);

		request.setVideoSourceToken(videoSourceToken);
		request.setFocus(focusMove);

		try {
			response = (MoveResponse) client.processSOAPImagingRequest(request, response, true);
		}
		catch (SOAPException | ConnectException e) {
			e.printStackTrace();
			return false;
		}

		if (response == null) {
			return false;
		}

		return true;
	}

	public ImagingSettings20 getImagingSettings(String videoSourceToken) {
		if (videoSourceToken == null) {
			return null;
		}

		GetImagingSettings request = new GetImagingSettings();
		GetImagingSettingsResponse response = new GetImagingSettingsResponse();

		request.setVideoSourceToken(videoSourceToken);

		try {
			response = (GetImagingSettingsResponse) client.processSOAPImagingRequest(request, response, true);
		}
		catch (SOAPException | ConnectException e) {
			e.printStackTrace();
			return null;
		}

		if (response == null) {
			return null;
		}

		return response.getImagingSettings();
	}

	public boolean setImagingSettings(String videoSourceToken, ImagingSettings20 imagingSettings) {
		if (videoSourceToken == null) {
			return false;
		}

		SetImagingSettings request = new SetImagingSettings();
		SetImagingSettingsResponse response = new SetImagingSettingsResponse();

		request.setVideoSourceToken(videoSourceToken);
		request.setImagingSettings(imagingSettings);

		try {
			response = (SetImagingSettingsResponse) client.processSOAPImagingRequest(request, response, true);
		}
		catch (SOAPException | ConnectException e) {
			e.printStackTrace();
			return false;
		}

		if (response == null) {
			return false;
		}

		return true;
	}
}
