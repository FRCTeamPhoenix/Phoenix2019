package frc.util;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;

public class Camera{
	
	private UsbCamera camera;
    
	//private CvSink cvSink;
	
	public Camera(int resolutionX, int resolutionY, int cameraIndex, int fps, CameraServer server){		
		camera = server.startAutomaticCapture(cameraIndex);
		camera.setResolution(resolutionX, resolutionY);
		camera.setFPS(fps);
		camera.setExposureAuto();
	}

	public UsbCamera getCamera() {
		return camera;
	}
	
	
}