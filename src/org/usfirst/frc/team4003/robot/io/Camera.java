package org.usfirst.frc.team4003.robot.io;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Camera implements Runnable {
	VideoCapture vcap;
	Thread thread;
    double angle = 68.5/2.0 * Math.PI/180;
    double tanAngle = Math.tan(angle);
    volatile Target target = null;
    
	public Camera() {
		vcap = new VideoCapture();
        vcap.open(0);
        Timer.delay(2);
        
        thread = new Thread(this);
	}
	
	public void run() {
		while(Thread.currentThread().isInterrupted() == false) {
			long start = System.currentTimeMillis();
			Mat img = new Mat();
			vcap.read(img);
        
			Size size = img.size();
			SmartDashboard.putNumber("pixelwidth", size.width);
			SmartDashboard.putNumber("pixelheight", size.height);
        
			//Mat imghsv = new Mat();
			Imgproc.cvtColor(img, img, Imgproc.COLOR_BGR2HSV);
			Scalar lowerHSV = new Scalar(70, 64, 96);
			Scalar upperHSV = new Scalar(94, 255, 255);
			Mat mask = new Mat();
			Core.inRange(img, lowerHSV, upperHSV, mask);
        
			List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
			Mat hierarchy = new Mat();
			Imgproc.findContours(mask, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
        
			List<Target> targets = new ArrayList<Target>();
			for (int c = 0; c<contours.size(); c++) {
				MatOfPoint cont = contours.get(c);
				if (Imgproc.contourArea(cont)>50) {
					targets.add(new Target(cont));
				}
			}
			Target best = null;
			if (targets.size() > 0) {
				best = targets.get(0);
				double error = best.getError();
				for (int i = 1; i < targets.size(); i++) {
					if (targets.get(i).getError() < error) {
						best = targets.get(i);
						error = best.getError();
					}
				}
				updateDashboard(best);
			}
			setTarget(best);
			SmartDashboard.putNumber("Contour Count", targets.size());
			SmartDashboard.putNumber("Time", System.currentTimeMillis()-start);
		}
	}
	
	public synchronized void setTarget(Target t) {
		target = t;
	}
	
	public synchronized Target getTarget() {
		return target;
	}
	
	public void updateDashboard(Target best) {
		SmartDashboard.putNumber("centerx", best.centerX);
		SmartDashboard.putNumber("centery", best.centerY);
		SmartDashboard.putNumber("width", best.width);
		SmartDashboard.putNumber("height", best.height);
		SmartDashboard.putNumber("aspectRatio", best.aspectRatio);
		SmartDashboard.putNumber("distance", best.distance);
	}

}
