package org.onvif.unofficial.api;

public interface OnvifDevice {

	DeviceManagementService getDeviceManagementService();

	PTZService getPTZService();

	MediaService getMediaService();

	DeviceIOService getDeviceIOService();

}