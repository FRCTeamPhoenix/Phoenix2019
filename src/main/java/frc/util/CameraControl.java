
package frc.util;

import java.awt.geom.Point2D;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.MjpegServer;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.UsbCameraInfo;
import edu.wpi.cscore.VideoException;
import edu.wpi.cscore.VideoMode;
import edu.wpi.cscore.VideoSink;
import edu.wpi.cscore.VideoSource;
import edu.wpi.cscore.VideoMode.PixelFormat;
import edu.wpi.first.cameraserver.CameraServer;


public class CameraControl{
	
	private Camera[] cameras;
	
	UsbCameraInfo[] cameraInfo;
	CameraServer cameraServer;
	MjpegServer mjpegServer;
	VideoSink videoSink;
	CvSink cameraSink;
	CvSource cvSource;
	int currentCamera;
	Mat img;

	//Initialize all the cameras
	public CameraControl(int resolutionX, int resolutionY, int fps){
		
		cameraServer = CameraServer.getInstance();
		// mjpegServer = new MjpegServer("serve_USB", 1181);
		cameraInfo = UsbCamera.enumerateUsbCameras();
		cameras = new Camera[cameraInfo.length];
		img = new Mat();
		cvSource = new CvSource("cv_source", PixelFormat.kBGR, resolutionX, resolutionY, fps);
		try {
			/*for (int i = 0; i < cameras.length; i++) {
				cameras[i] = new Camera(resolutionX, resolutionY, i, fps, cameraServer);
			}*/
			UsbCamera camera = cameraServer.startAutomaticCapture();
			camera.setResolution(resolutionX, resolutionY);
			cameraSink = cameraServer.getVideo();
			cvSource = cameraServer.putVideo("Processed", resolutionX, resolutionY);
			videoSink = cameraServer.getServer();
			
			currentCamera = 0;
			//videoSink.setSource(cameras[currentCamera].getCamera());
		} catch(VideoException e) {
			System.out.println("Camera Server Exception: "+e.getMessage());
		}
		
	}

	public void processFrame(){
		
		if(cameraSink.grabFrame(img) == 0){
			return;
		};

		Imgproc.line(img, new Point(0, 0), new Point(img.cols(), img.rows()), new Scalar(0, 0, 255));

		cvSource.putFrame(img);
	}


	public void switchCamera() {
		if (cameras.length < 2) {
			return;
		}
		currentCamera = (currentCamera + 1) % 2;
		videoSink.setSource(cameras[currentCamera].getCamera());
		cameraSink.setSource(cameras[currentCamera].getCamera());
	}
}
