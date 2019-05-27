
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
	
	private UsbCamera[] cameras;
	private Thread m_processThread;
	
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
		
		//get a refrence to the camera server
		cameraServer = CameraServer.getInstance();
		//allocate list of cameras
		cameraInfo = UsbCamera.enumerateUsbCameras();
		cameras = new UsbCamera[cameraInfo.length];
		img = new Mat();

		try {
			//start capture of all connected cameras
			for (int i = 0; i < cameras.length; i++) {
				cameras[i] = cameraServer.startAutomaticCapture();
				cameras[i].setResolution(resolutionX, resolutionY);
			}
			//get a refrence to the active video
			cameraSink = cameraServer.getVideo();

			//Output for the processed image
			cvSource = cameraServer.putVideo("Main", resolutionX, resolutionY);
			
			currentCamera = 0;
		} catch(VideoException e) {
			System.out.println("Camera Server Exception: "+e.getMessage());
		}

		m_processThread = new Thread(()->{
			while(!Thread.interrupted()){
				if(cameraSink.grabFrame(img) == 0){
					//skip loop if frame timeout
					continue;
				};
		
				//process the image
				Imgproc.line(img, new Point(0, 0), new Point(img.cols(), img.rows()), new Scalar(0, 0, 255), 3, 8);
		
				//send the processed image to the drive station
				cvSource.putFrame(img);
			}
		});
		m_processThread.setDaemon(true);
		m_processThread.start();
	}

	//switches to the next camera in sequence
	public void nextCamera() {
		if (cameras.length < 2) {
			return;
		}
		currentCamera = (currentCamera + 1) % cameras.length;
		cameraSink.setSource(cameras[currentCamera]);
	}

	//switches to the previous camera in sequence
	public void previousCamera() {
		if (cameras.length < 2) {
			return;
		}
		currentCamera = currentCamera - 1;
		if(currentCamera < 0)
			currentCamera = cameras.length - 1;
		
		cameraSink.setSource(cameras[currentCamera]);
	}
}
