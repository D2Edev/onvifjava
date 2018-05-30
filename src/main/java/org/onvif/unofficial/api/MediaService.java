package org.onvif.unofficial.api;

import java.util.List;

import org.onvif.ver10.schema.OSDConfiguration;
import org.onvif.ver10.schema.Profile;
import org.onvif.ver10.schema.StreamSetup;
import org.onvif.ver10.schema.VideoEncoderConfiguration;
import org.onvif.ver10.schema.VideoEncoderConfigurationOptions;
import org.onvif.ver10.schema.VideoSource;
import org.onvif.ver10.schema.VideoSourceConfiguration;

public interface MediaService {

	String getHTTPStreamUri(String profileToken) throws Exception;

	String getUDPStreamUri(String profileToken) throws Exception;

	String getTCPStreamUri(String profileToken) throws Exception;

	String getRTSPStreamUri(String profileToken) throws Exception;

	String getStreamUri(String profileToken, StreamSetup streamSetup) throws Exception;

	VideoEncoderConfigurationOptions getVideoEncoderConfigurationOptions(String profileToken) throws Exception;

	boolean setVideoEncoderConfiguration(VideoEncoderConfiguration videoEncoderConfiguration) throws Exception;

	String getSnapshotUri(String profileToken) throws Exception;

	List<VideoSource> getVideoSources() throws Exception;

	List<Profile> getProfiles() throws Exception;

	Profile getProfile(String profileToken) throws Exception;

	Profile createProfile(String name) throws Exception;

	List<OSDConfiguration> getOSDs(String videoSourceConfigurationToken) throws Exception;

	List<VideoSourceConfiguration> getVideoSourceConfigurations() throws Exception;

	VideoSourceConfiguration getVideoSourceConfiguration(String configToken) throws Exception;

}