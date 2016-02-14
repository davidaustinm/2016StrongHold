package org.usfirst.frc.team4003.robot.io;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team4003.robot.*;

public class DriverCamera implements Runnable, DashboardMatProvider {
	VideoCapture vcap;

    protected Mat dashboardImg = new Mat();
    
	public DriverCamera() {
		vcap = new VideoCapture();
        vcap.open(RobotMap.DRIVER_CAMERA);
        Timer.delay(2);
	}
	
	protected void setDashboardImg(Mat i) {
		synchronized(dashboardImg) {
			dashboardImg = i;
		}
	}
	
	public Mat getDashboardImg() {
		synchronized(dashboardImg) {
			return dashboardImg;
		}
	}
	
	public void run() {
		Mat img = new Mat();
		while(Thread.currentThread().isInterrupted() == false) {
			// TODO: We might want to rate limit this, maybe force a 41ms sleep for 24fps.
			vcap.read(img);
			setDashboardImg(img);
		}
	}
}