package org.onvif.unofficial.api;

import org.onvif.ver10.schema.ImagingOptions20;
import org.onvif.ver10.schema.ImagingSettings20;

public interface DeviceIOService {

	ImagingOptions20 getOptions(String videoSourceToken) throws Exception;

	boolean moveFocus(String videoSourceToken, float absoluteFocusValue) throws Exception;

	ImagingSettings20 getImagingSettings(String videoSourceToken) throws Exception;

	boolean setImagingSettings(String videoSourceToken, ImagingSettings20 imagingSettings) throws Exception;

}