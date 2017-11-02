package org.onvif.unofficial.devices;

import java.util.List;

import javax.xml.soap.SOAPException;

import org.onvif.unofficial.soapclient.ISoapClient;
import org.onvif.ver10.schema.FloatRange;
import org.onvif.ver10.schema.PTZConfiguration;
import org.onvif.ver10.schema.PTZNode;
import org.onvif.ver10.schema.PTZPreset;
import org.onvif.ver10.schema.PTZSpaces;
import org.onvif.ver10.schema.PTZSpeed;
import org.onvif.ver10.schema.PTZStatus;
import org.onvif.ver10.schema.PTZVector;
import org.onvif.ver10.schema.Profile;
import org.onvif.ver10.schema.Vector1D;
import org.onvif.ver10.schema.Vector2D;
import org.onvif.ver20.ptz.wsdl.AbsoluteMove;
import org.onvif.ver20.ptz.wsdl.AbsoluteMoveResponse;
import org.onvif.ver20.ptz.wsdl.ContinuousMove;
import org.onvif.ver20.ptz.wsdl.ContinuousMoveResponse;
import org.onvif.ver20.ptz.wsdl.GetNode;
import org.onvif.ver20.ptz.wsdl.GetNodeResponse;
import org.onvif.ver20.ptz.wsdl.GetNodes;
import org.onvif.ver20.ptz.wsdl.GetNodesResponse;
import org.onvif.ver20.ptz.wsdl.GetPresets;
import org.onvif.ver20.ptz.wsdl.GetPresetsResponse;
import org.onvif.ver20.ptz.wsdl.GetStatus;
import org.onvif.ver20.ptz.wsdl.GetStatusResponse;
import org.onvif.ver20.ptz.wsdl.GotoPreset;
import org.onvif.ver20.ptz.wsdl.GotoPresetResponse;
import org.onvif.ver20.ptz.wsdl.RelativeMove;
import org.onvif.ver20.ptz.wsdl.RelativeMoveResponse;
import org.onvif.ver20.ptz.wsdl.RemovePreset;
import org.onvif.ver20.ptz.wsdl.RemovePresetResponse;
import org.onvif.ver20.ptz.wsdl.SetHomePosition;
import org.onvif.ver20.ptz.wsdl.SetHomePositionResponse;
import org.onvif.ver20.ptz.wsdl.SetPreset;
import org.onvif.ver20.ptz.wsdl.SetPresetResponse;
import org.onvif.ver20.ptz.wsdl.Stop;
import org.onvif.ver20.ptz.wsdl.StopResponse;

public class PtzService {
	private static final DeviceSubclass TYPE = DeviceSubclass.PTZ;
	private OnvifDevice onvifDevice;
	private ISoapClient client;

	public PtzService(OnvifDevice onvifDevice) {
		this.onvifDevice = onvifDevice;
		this.client = onvifDevice.getSoap();
	}

	public boolean isPtzOperationsSupported(String profileToken) {
		return getPTZConfiguration(profileToken) != null;
	}

	/**
	 * @param profileToken
	 * @return If is null, PTZ operations are not supported
	 */
	public PTZConfiguration getPTZConfiguration(String profileToken) {
		if (profileToken == null || profileToken.equals("")) {
			return null;
		}
		Profile profile = onvifDevice.getDevices().getProfile(profileToken);
		if (profile == null) {
			throw new IllegalArgumentException("No profile available for token: " + profileToken);
		}
		if (profile.getPTZConfiguration() == null) {
			return null; // no PTZ support
		}

		return profile.getPTZConfiguration();
	}

	public List<PTZNode> getNodes() {
		try {
			GetNodesResponse response = client.processOnvifServiceRequest(new GetNodes(), GetNodesResponse.class,
					TYPE, true);
			if (response == null)
				return null;
			return response.getPTZNode();
		} catch (Exception e) {
			return null;
		}
	}

	public PTZNode getNode(String profileToken) {
		return getNode(getPTZConfiguration(profileToken));
	}

	public PTZNode getNode(PTZConfiguration ptzConfiguration) {
		GetNode request = new GetNode();

		if (ptzConfiguration == null) {
			return null; // no PTZ support
		}
		request.setNodeToken(ptzConfiguration.getNodeToken());

		try {
			GetNodeResponse response = client.processOnvifServiceRequest(request, GetNodeResponse.class, TYPE,
					true);
			if (response == null)
				return null;
			return response.getPTZNode();
		} catch (Exception e) {
			return null;
		}
	}

	public FloatRange getPanSpaces(String profileToken) {
		PTZNode node = getNode(profileToken);

		PTZSpaces ptzSpaces = node.getSupportedPTZSpaces();
		return ptzSpaces.getAbsolutePanTiltPositionSpace().get(0).getXRange();
	}

	public FloatRange getTiltSpaces(String profileToken) {
		PTZNode node = getNode(profileToken);

		PTZSpaces ptzSpaces = node.getSupportedPTZSpaces();
		return ptzSpaces.getAbsolutePanTiltPositionSpace().get(0).getYRange();
	}

	public FloatRange getZoomSpaces(String profileToken) {
		PTZNode node = getNode(profileToken);

		PTZSpaces ptzSpaces = node.getSupportedPTZSpaces();
		return ptzSpaces.getAbsoluteZoomPositionSpace().get(0).getXRange();
	}

	public boolean isAbsoluteMoveSupported(String profileToken) {
		Profile profile = onvifDevice.getDevices().getProfile(profileToken);
		try {
			if (profile.getPTZConfiguration().getDefaultAbsolutePantTiltPositionSpace() != null) {
				return true;
			}
		} catch (NullPointerException e) {
		}
		return false;
	}

	/**
	 * 
	 * @param x
	 *            Pan-Position
	 * @param y
	 *            Tilt-Position
	 * @param zoom
	 *            Zoom
	 * @see getPanSpaces(), getTiltSpaces(), getZoomSpaces()
	 * @return True if move successful
	 * @throws SOAPException
	 */
	public boolean absoluteMove(String profileToken, float x, float y, float zoom) throws SOAPException {
		PTZNode node = getNode(profileToken);
		if (node != null) {
			FloatRange xRange = node.getSupportedPTZSpaces().getAbsolutePanTiltPositionSpace().get(0).getXRange();
			FloatRange yRange = node.getSupportedPTZSpaces().getAbsolutePanTiltPositionSpace().get(0).getYRange();
			FloatRange zRange = node.getSupportedPTZSpaces().getAbsoluteZoomPositionSpace().get(0).getXRange();

			if (zoom < zRange.getMin() || zoom > zRange.getMax()) {
				throw new IllegalArgumentException("Bad value for zoom: " + zoom);
			}
			if (x < xRange.getMin() || x > xRange.getMax()) {
				throw new IllegalArgumentException("Bad value for pan:/x " + x);
			}
			if (y < yRange.getMin() || y > yRange.getMax()) {
				throw new IllegalArgumentException("Bad value for tilt/y: " + y);
			}
		}

		AbsoluteMove request = new AbsoluteMove();

		Vector2D panTiltVector = new Vector2D();
		panTiltVector.setX(x);
		panTiltVector.setY(y);
		Vector1D zoomVector = new Vector1D();
		zoomVector.setX(zoom);

		PTZVector ptzVector = new PTZVector();
		ptzVector.setPanTilt(panTiltVector);
		ptzVector.setZoom(zoomVector);
		request.setPosition(ptzVector);

		request.setProfileToken(profileToken);

		try {
			AbsoluteMoveResponse response = client.processOnvifServiceRequest(request, AbsoluteMoveResponse.class,
					TYPE, true);
			if (response == null) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	public boolean isRelativeMoveSupported(String profileToken) {
		Profile profile = onvifDevice.getDevices().getProfile(profileToken);
		try {
			if (profile.getPTZConfiguration().getDefaultRelativePanTiltTranslationSpace() != null) {
				return true;
			}
		} catch (NullPointerException e) {
		}
		return false;
	}

	public boolean relativeMove(String profileToken, float x, float y, float zoom) {

		RelativeMove request = new RelativeMove();
		Vector2D panTiltVector = new Vector2D();
		panTiltVector.setX(x);
		panTiltVector.setY(y);
		Vector1D zoomVector = new Vector1D();
		zoomVector.setX(zoom);

		PTZVector translation = new PTZVector();
		translation.setPanTilt(panTiltVector);
		translation.setZoom(zoomVector);

		request.setProfileToken(profileToken);
		request.setTranslation(translation);

		try {
			RelativeMoveResponse response = client.processOnvifServiceRequest(request, RelativeMoveResponse.class,
					TYPE, true);
			if (response == null) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	public boolean isContinuosMoveSupported(String profileToken) {
		Profile profile = onvifDevice.getDevices().getProfile(profileToken);
		try {
			if (profile.getPTZConfiguration().getDefaultContinuousPanTiltVelocitySpace() != null) {
				return true;
			}
		} catch (NullPointerException e) {
		}
		return false;
	}

	public boolean continuousMove(String profileToken, float x, float y, float zoom) {
		ContinuousMove request = new ContinuousMove();

		Vector2D panTiltVector = new Vector2D();
		panTiltVector.setX(x);
		panTiltVector.setY(y);
		Vector1D zoomVector = new Vector1D();
		zoomVector.setX(zoom);

		PTZSpeed ptzSpeed = new PTZSpeed();
		ptzSpeed.setPanTilt(panTiltVector);
		ptzSpeed.setZoom(zoomVector);
		request.setVelocity(ptzSpeed);

		request.setProfileToken(profileToken);

		try {
			ContinuousMoveResponse response = client.processOnvifServiceRequest(request,
					ContinuousMoveResponse.class, TYPE, true);
			if (response == null) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	public boolean stopMove(String profileToken) {
		Stop request = new Stop();
		request.setPanTilt(true);
		request.setZoom(true);

		request.setProfileToken(profileToken);

		try {
			StopResponse response = client.processOnvifServiceRequest(request, StopResponse.class, TYPE, true);
			if (response == null) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public PTZStatus getStatus(String profileToken) {
		GetStatus request = new GetStatus();

		request.setProfileToken(profileToken);

		try {
			GetStatusResponse response = client.processOnvifServiceRequest(request, GetStatusResponse.class, TYPE,
					true);
			if (response == null) {
				return null;
			}

			return response.getPTZStatus();
		} catch (Exception e) {
			return null;
		}

	}

	public PTZVector getPosition(String profileToken) {
		PTZStatus status = getStatus(profileToken);

		if (status == null) {
			return null;
		}

		return status.getPosition();
	}

	public boolean setHomePosition(String profileToken) {
		SetHomePosition request = new SetHomePosition();
		request.setProfileToken(profileToken);
		try {
			SetHomePositionResponse response = client.processOnvifServiceRequest(request,
					SetHomePositionResponse.class, TYPE, true);
			if (response == null) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	public List<PTZPreset> getPresets(String profileToken) {
		GetPresets request = new GetPresets();

		request.setProfileToken(profileToken);

		try {
			GetPresetsResponse response = client.processOnvifServiceRequest(request, GetPresetsResponse.class,
					TYPE, true);
			if (response == null) {
				return null;
			}

			return response.getPreset();
		} catch (Exception e) {
			return null;
		}

	}

	public String setPreset(String presetName, String presetToken, String profileToken) {
		SetPreset request = new SetPreset();

		request.setProfileToken(profileToken);
		request.setPresetName(presetName);
		request.setPresetToken(presetToken);

		try {
			SetPresetResponse response = client.processOnvifServiceRequest(request, SetPresetResponse.class, TYPE,
					true);
			if (response == null) {
				return null;
			}
			return response.getPresetToken();
		} catch (Exception e) {
			return null;
		}

	}

	public String setPreset(String presetName, String profileToken) {
		return setPreset(presetName, null, profileToken);
	}

	public boolean removePreset(String presetToken, String profileToken) {
		RemovePreset request = new RemovePreset();

		request.setProfileToken(profileToken);
		request.setPresetToken(presetToken);

		try {
			RemovePresetResponse response = client.processOnvifServiceRequest(request, RemovePresetResponse.class,
					TYPE, true);
			if (response == null) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	public boolean gotoPreset(String presetToken, String profileToken) {
		GotoPreset request = new GotoPreset();

		request.setProfileToken(profileToken);
		request.setPresetToken(presetToken);

		try {
			GotoPresetResponse response = client.processOnvifServiceRequest(request, GotoPresetResponse.class, TYPE, true);
			if (response == null) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}


		return true;
	}
}
