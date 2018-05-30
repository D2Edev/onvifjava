package org.onvif.unofficial.api;

import java.util.List;

import org.onvif.ver10.schema.FloatRange;
import org.onvif.ver10.schema.PTZConfiguration;
import org.onvif.ver10.schema.PTZNode;
import org.onvif.ver10.schema.PTZPreset;
import org.onvif.ver10.schema.PTZStatus;
import org.onvif.ver10.schema.PTZVector;

public interface PTZService {

	boolean isPtzOperationsSupported(String profileToken) throws Exception;

	/**
	 * @param profileToken
	 * @return If is null, PTZ operations are not supported
	 * @throws Exception
	 */
	PTZConfiguration getPTZConfiguration(String profileToken) throws Exception;

	List<PTZNode> getNodes() throws Exception;

	PTZNode getNode(String profileToken) throws Exception;

	PTZNode getNode(PTZConfiguration ptzConfiguration) throws Exception;

	FloatRange getPanSpaces(String profileToken) throws Exception;

	FloatRange getTiltSpaces(String profileToken) throws Exception;

	FloatRange getZoomSpaces(String profileToken) throws Exception;

	boolean isAbsoluteMoveSupported(String profileToken) throws Exception;

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
	 * @throws Exception
	 */
	boolean absoluteMove(String profileToken, float x, float y, float zoom) throws Exception;

	boolean isRelativeMoveSupported(String profileToken) throws Exception;

	boolean relativeMove(String profileToken, float x, float y, float zoom) throws Exception;

	boolean isContinuosMoveSupported(String profileToken) throws Exception;

	boolean continuousMove(String profileToken, float x, float y, float zoom) throws Exception;

	boolean stopMove(String profileToken) throws Exception;

	PTZStatus getStatus(String profileToken) throws Exception;

	PTZVector getPosition(String profileToken) throws Exception;

	boolean setHomePosition(String profileToken) throws Exception;

	List<PTZPreset> getPresets(String profileToken) throws Exception;

	String setPreset(String presetName, String presetToken, String profileToken) throws Exception;

	String setPreset(String presetName, String profileToken) throws Exception;

	boolean removePreset(String presetToken, String profileToken) throws Exception;

	boolean gotoPreset(String presetToken, String profileToken) throws Exception;

}