package org.onvif.unofficial.services;

import java.util.List;

import org.onvif.unofficial.NetOnvifDevice;
import org.onvif.unofficial.api.PTZService;
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

public class NetPTZService extends ServiceBase implements PTZService {

	public NetPTZService(NetOnvifDevice onvifDevice, ISoapClient client, String serviceUrl) {
		super(onvifDevice, client, serviceUrl);
	}

	/* (non-Javadoc)
	 * @see org.onvif.unofficial.services.PTZService#isPtzOperationsSupported(java.lang.String)
	 */
	@Override
	public boolean isPtzOperationsSupported(String profileToken) throws Exception {
		return getPTZConfiguration(profileToken) != null;
	}

	/* (non-Javadoc)
	 * @see org.onvif.unofficial.services.PTZService#getPTZConfiguration(java.lang.String)
	 */
	@Override
	public PTZConfiguration getPTZConfiguration(String profileToken) throws Exception {
		if (profileToken == null || profileToken.equals("")) {
			return null;
		}
		Profile profile = onvifDevice.getMediaService().getProfile(profileToken);
		if (profile == null) {
			throw new IllegalArgumentException("No profile available for token: " + profileToken);
		}
		if (profile.getPTZConfiguration() == null) {
			return null; // no PTZ support
		}

		return profile.getPTZConfiguration();
	}

	/* (non-Javadoc)
	 * @see org.onvif.unofficial.services.PTZService#getNodes()
	 */
	@Override
	public List<PTZNode> getNodes() throws Exception {
		GetNodesResponse response = client.processRequest(new GetNodes(), GetNodesResponse.class, serviceUrl, true);
		if (response == null)
			return null;
		return response.getPTZNode();
	}

	/* (non-Javadoc)
	 * @see org.onvif.unofficial.services.PTZService#getNode(java.lang.String)
	 */
	@Override
	public PTZNode getNode(String profileToken) throws Exception {
		return getNode(getPTZConfiguration(profileToken));
	}

	/* (non-Javadoc)
	 * @see org.onvif.unofficial.services.PTZService#getNode(org.onvif.ver10.schema.PTZConfiguration)
	 */
	@Override
	public PTZNode getNode(PTZConfiguration ptzConfiguration) throws Exception {
		GetNode request = new GetNode();
		if (ptzConfiguration == null) {
			return null; // no PTZ support
		}
		request.setNodeToken(ptzConfiguration.getNodeToken());
		GetNodeResponse response = client.processRequest(request, GetNodeResponse.class, serviceUrl, true);
		if (response == null)
			return null;
		return response.getPTZNode();
	}

	/* (non-Javadoc)
	 * @see org.onvif.unofficial.services.PTZService#getPanSpaces(java.lang.String)
	 */
	@Override
	public FloatRange getPanSpaces(String profileToken) throws Exception {
		PTZNode node = getNode(profileToken);

		PTZSpaces ptzSpaces = node.getSupportedPTZSpaces();
		return ptzSpaces.getAbsolutePanTiltPositionSpace().get(0).getXRange();
	}

	/* (non-Javadoc)
	 * @see org.onvif.unofficial.services.PTZService#getTiltSpaces(java.lang.String)
	 */
	@Override
	public FloatRange getTiltSpaces(String profileToken) throws Exception {
		PTZNode node = getNode(profileToken);

		PTZSpaces ptzSpaces = node.getSupportedPTZSpaces();
		return ptzSpaces.getAbsolutePanTiltPositionSpace().get(0).getYRange();
	}

	/* (non-Javadoc)
	 * @see org.onvif.unofficial.services.PTZService#getZoomSpaces(java.lang.String)
	 */
	@Override
	public FloatRange getZoomSpaces(String profileToken) throws Exception {
		PTZNode node = getNode(profileToken);

		PTZSpaces ptzSpaces = node.getSupportedPTZSpaces();
		return ptzSpaces.getAbsoluteZoomPositionSpace().get(0).getXRange();
	}

	/* (non-Javadoc)
	 * @see org.onvif.unofficial.services.PTZService#isAbsoluteMoveSupported(java.lang.String)
	 */
	@Override
	public boolean isAbsoluteMoveSupported(String profileToken) throws Exception {
		Profile profile = onvifDevice.getMediaService().getProfile(profileToken);
		if (profile.getPTZConfiguration().getDefaultAbsolutePantTiltPositionSpace() != null) {
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.onvif.unofficial.services.PTZService#absoluteMove(java.lang.String, float, float, float)
	 */
	@Override
	public boolean absoluteMove(String profileToken, float x, float y, float zoom) throws Exception {
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
		AbsoluteMoveResponse response = client.processRequest(request, AbsoluteMoveResponse.class, serviceUrl, true);
		if (response == null) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see org.onvif.unofficial.services.PTZService#isRelativeMoveSupported(java.lang.String)
	 */
	@Override
	public boolean isRelativeMoveSupported(String profileToken) throws Exception {
		if (onvifDevice.getMediaService() == null)
			return false;
		Profile profile = onvifDevice.getMediaService().getProfile(profileToken);
		PTZConfiguration ptzConf = profile.getPTZConfiguration();
		if (ptzConf == null)
			return false;
		if (ptzConf.getDefaultRelativePanTiltTranslationSpace() == null)
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see org.onvif.unofficial.services.PTZService#relativeMove(java.lang.String, float, float, float)
	 */
	@Override
	public boolean relativeMove(String profileToken, float x, float y, float zoom) throws Exception {

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
		RelativeMoveResponse response = client.processRequest(request, RelativeMoveResponse.class, serviceUrl, true);
		if (response == null) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see org.onvif.unofficial.services.PTZService#isContinuosMoveSupported(java.lang.String)
	 */
	@Override
	public boolean isContinuosMoveSupported(String profileToken) throws Exception {
		if (onvifDevice.getMediaService() == null)
			return false;
		Profile profile = onvifDevice.getMediaService().getProfile(profileToken);
		PTZConfiguration ptzConf = profile.getPTZConfiguration();
		if (ptzConf == null)
			return false;
		if (ptzConf.getDefaultContinuousPanTiltVelocitySpace() == null)
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see org.onvif.unofficial.services.PTZService#continuousMove(java.lang.String, float, float, float)
	 */
	@Override
	public boolean continuousMove(String profileToken, float x, float y, float zoom) throws Exception {
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
		ContinuousMoveResponse response = client.processRequest(request, ContinuousMoveResponse.class, serviceUrl,
				true);
		if (response == null) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see org.onvif.unofficial.services.PTZService#stopMove(java.lang.String)
	 */
	@Override
	public boolean stopMove(String profileToken) throws Exception {
		Stop request = new Stop();
		request.setPanTilt(true);
		request.setZoom(true);
		request.setProfileToken(profileToken);
		StopResponse response = client.processRequest(request, StopResponse.class, serviceUrl, true);
		if (response == null)
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see org.onvif.unofficial.services.PTZService#getStatus(java.lang.String)
	 */
	@Override
	public PTZStatus getStatus(String profileToken) throws Exception {
		GetStatus request = new GetStatus();
		request.setProfileToken(profileToken);
		GetStatusResponse response = client.processRequest(request, GetStatusResponse.class, serviceUrl, true);
		if (response == null) {
			return null;
		}
		return response.getPTZStatus();
	}

	/* (non-Javadoc)
	 * @see org.onvif.unofficial.services.PTZService#getPosition(java.lang.String)
	 */
	@Override
	public PTZVector getPosition(String profileToken) throws Exception {
		PTZStatus status = getStatus(profileToken);
		if (status == null) {
			return null;
		}
		return status.getPosition();
	}

	/* (non-Javadoc)
	 * @see org.onvif.unofficial.services.PTZService#setHomePosition(java.lang.String)
	 */
	@Override
	public boolean setHomePosition(String profileToken) throws Exception {
		SetHomePosition request = new SetHomePosition();
		request.setProfileToken(profileToken);
		SetHomePositionResponse response = client.processRequest(request, SetHomePositionResponse.class, serviceUrl,
				true);
		if (response == null) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see org.onvif.unofficial.services.PTZService#getPresets(java.lang.String)
	 */
	@Override
	public List<PTZPreset> getPresets(String profileToken) throws Exception {
		GetPresets request = new GetPresets();
		request.setProfileToken(profileToken);
		GetPresetsResponse response = client.processRequest(request, GetPresetsResponse.class, serviceUrl, true);
		if (response == null) {
			return null;
		}
		return response.getPreset();
	}

	/* (non-Javadoc)
	 * @see org.onvif.unofficial.services.PTZService#setPreset(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String setPreset(String presetName, String presetToken, String profileToken) throws Exception {
		SetPreset request = new SetPreset();
		request.setProfileToken(profileToken);
		request.setPresetName(presetName);
		request.setPresetToken(presetToken);
		SetPresetResponse response = client.processRequest(request, SetPresetResponse.class, serviceUrl, true);
		if (response == null) {
			return null;
		}
		return response.getPresetToken();
	}

	/* (non-Javadoc)
	 * @see org.onvif.unofficial.services.PTZService#setPreset(java.lang.String, java.lang.String)
	 */
	@Override
	public String setPreset(String presetName, String profileToken) throws Exception {
		return setPreset(presetName, null, profileToken);
	}

	/* (non-Javadoc)
	 * @see org.onvif.unofficial.services.PTZService#removePreset(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean removePreset(String presetToken, String profileToken) throws Exception {
		RemovePreset request = new RemovePreset();
		request.setProfileToken(profileToken);
		request.setPresetToken(presetToken);
		RemovePresetResponse response = client.processRequest(request, RemovePresetResponse.class, serviceUrl, true);
		if (response == null) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see org.onvif.unofficial.services.PTZService#gotoPreset(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean gotoPreset(String presetToken, String profileToken) throws Exception {
		GotoPreset request = new GotoPreset();
		request.setProfileToken(profileToken);
		request.setPresetToken(presetToken);
		GotoPresetResponse response = client.processRequest(request, GotoPresetResponse.class, serviceUrl, true);
		if (response == null) {
			return false;
		}
		return true;
	}
}
