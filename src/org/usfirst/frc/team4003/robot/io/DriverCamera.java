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

    /* Let's sleep for a bit between reads. At 24fps that's 42ms.
     * If lag is an issue because of this mechanism just up the framerate. 1000/60 is
     * probably as fast as we'd want to try and go.
     */
    protected int minSleepTime = 1000/24;
    
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
		Mat smallImg = new Mat();
		while(Thread.currentThread().isInterrupted() == false) {
			long start = System.currentTimeMillis();
			vcap.read(img);
			Imgproc.resize(img, smallImg, new Size(320, 180));

			setDashboardImg(smallImg);

			// If the loop ran faster than minSleepTime let's sleep.
			long stop = start + minSleepTime;
			long now = System.currentTimeMillis();
			if (stop > now) {
				/*
				try {
					Thread.sleep(stop - now);
				} catch (InterruptedException ex) {
					// I EAT EXCEPTIONS. OHM NOM NOM NOM!
				}
				*/
			}
		}
	}
}