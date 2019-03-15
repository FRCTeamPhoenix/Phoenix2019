
package frc.util;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.UsbCameraInfo;
import edu.wpi.cscore.VideoSink;
import edu.wpi.first.cameraserver.CameraServer;


public class CameraControl{
	
	private Camera[] cameras;
	
	UsbCameraInfo[] cameraInfo;
	CameraServer cameraServer;
	VideoSink videoSink;
	int currentCamera;
	
	//Initialize all the cameras
	public CameraControl(int resolutionX, int resolutionY, int fps){
		
		cameraServer = CameraServer.getInstance();
		cameraInfo = UsbCamera.enumerateUsbCameras();
		cameras = new Camera[cameraInfo.length];
		for (int i = 0; i < cameras.length; i++) {
			cameras[i] = new Camera(resolutionX, resolutionY, i, fps, cameraServer);
		}
		videoSink = cameraServer.getServer();
		currentCamera = 0;
		videoSink.setSource(cameras[currentCamera].getCamera());
		
	}
	

	public void switchCamera() {
		currentCamera = (currentCamera + 1) % 2;
		videoSink.setSource(cameras[currentCamera].getCamera());
	}
}
