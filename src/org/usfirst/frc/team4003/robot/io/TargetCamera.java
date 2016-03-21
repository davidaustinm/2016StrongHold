package org.usfirst.frc.team4003.robot.io;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.core.Rect;
//import org.opencv.highgui.VideoCapture;
//import org.opencv.imgproc.Imgproc;
//import org.opencv.highgui.Highgui;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;
import org.opencv.imgproc.Imgproc;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team4003.robot.*;

public class TargetCamera implements Runnable, DashboardMatProvider {
	VideoCapture vcap;
	double angle = 68.5 / 2.0 * Math.PI / 180;
	double tanAngle = Math.tan(angle);
	Sensors sensors;
	double minArea = 150;
	Target lastBest = null;

	protected Mat dashboardImg = new Mat();

	/*
	 * Let's sleep for a bit between reads. At 24fps that's 42ms. While actually
	 * running the targeting code the sleep won't happen as that loop generally
	 * takes about 100ms to complete resulting in some lag. If lag is an issue
	 * because of this mechanism just up the framerate. 1000/60 is probably as
	 * fast as we'd want to try and go.
	 */
	protected int minSleepTime = 1000 / 24;

	public TargetCamera() {
		sensors = Sensors.getInstance();
		try {
			vcap = new VideoCapture();
			vcap.open(RobotMap.TARGET_CAMERA);
			vcap.set(Videoio.CV_CAP_PROP_FRAME_WIDTH, 320);
			vcap.set(Videoio.CV_CAP_PROP_FRAME_HEIGHT, 240);
			Timer.delay(2);
		} catch (Exception ex) {
			//System.out.println("Target Camera Error: " + ex.getMessage());
		}

		//vcap.set(15, -9);
		/* TODO: Find out why this doesn't work at all. */
		//vcap.set(Highgui.CV_CAP_PROP_FRAME_WIDTH, 320);
		//vcap.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT, 240);
		
	}

	protected void setDashboardImg(Mat i) {
		synchronized (dashboardImg) {
			dashboardImg = i;
		}
	}

	public Mat getDashboardImg() {
		synchronized (dashboardImg) {
			return dashboardImg;
		}
	}

	public void run() {
		Mat img = new Mat();
		Mat mask = new Mat();
		Mat origImg = new Mat();
		Mat hierarchy = new Mat();
		Mat dash = new Mat();
		Mat dashOut = new Mat();
		while (Thread.currentThread().isInterrupted() == false) {
			long start = System.currentTimeMillis();
			vcap.read(origImg);
			Imgproc.resize(origImg, img, new Size(320, 240));
			//img = origImg;
			//Imgproc.resize(origImg, img, new Size(640, 360));
			if (Robot.isTargetTracking()) {

				Size size = img.size();
				SmartDashboard.putNumber("pixelwidth", size.width);
				SmartDashboard.putNumber("pixelheight", size.height);

				Imgproc.cvtColor(img, img, Imgproc.COLOR_BGR2HSV);
				//Scalar lowerHSV = new Scalar(70, 32, 48);
				//Scalar upperHSV = new Scalar(94, 255, 255);
				Scalar lowerHSV = new Scalar(50, 20, 200); //50, 127, 200
				Scalar upperHSV = new Scalar(94, 255, 255); // 94, 207, 255
				
				// worked during lunch:  50, 20, 200    94, 64, 220
				Core.inRange(img, lowerHSV, upperHSV, mask);

				List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
				Imgproc.findContours(mask, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

				img.copyTo(dash);
				Imgproc.cvtColor(dash, dashOut, Imgproc.COLOR_HSV2BGR);

				List<Target> targets = new ArrayList<Target>();
				for (int c = 0; c < contours.size(); c++) {
					MatOfPoint cont = contours.get(c);
					Rect bounding = Imgproc.boundingRect(cont);
					if (Imgproc.boundingRect(cont).area() > minArea) {
						Imgproc.rectangle(dashOut, bounding.br(), bounding.tl(), new Scalar(255, 255, 0));
						targets.add(new Target(cont));
						// Draw large enough contours in red
						Imgproc.drawContours(dashOut, contours, c, new Scalar(0, 0, 255));
					} else {
						// Draw small contours in black for debugging.
						Imgproc.drawContours(dashOut, contours, c, new Scalar(0, 0, 0));

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
					
					// Run through again and compare to the lastBest
					if (lastBest != null) {
						for (int i = 0; i < targets.size(); i++) {
							if (targets.get(i).getError() < best.getError() + 0.4
									&& distance(targets.get(i)) < distance(best)){
								best = targets.get(i);
							}
								
						}
					}
					// Draw a circle in the middle-ish of our best target.
					Imgproc.circle(dashOut, new Point(best.centerX, best.centerY), 8, new Scalar(0, 0, 255), 6);
					updateDashboard(best);
				} 
				lastBest = best;
				// Now that we've marked up the image a bit set it to be the
				// version that goes on the dashboard.
				setDashboardImg(dashOut);
				if (best != null) updateDashboard(best);
				sensors.setTarget(best);
				//SmartDashboard.putNumber("Contour Count", targets.size());
				//SmartDashboard.putNumber("Time", System.currentTimeMillis() - start);
			} else {
				Point tl = new Point(Sensors.goalX - 10, Sensors.goalY - 5);
				Point br = new Point(Sensors.goalX + 10, Sensors.goalY + 5);
				Imgproc.rectangle(img, tl, br, new Scalar(0, 0, 255), 4);
				setDashboardImg(img);
				sensors.setTarget(null);
			}

			// If the loop ran faster than minSleepTime let's sleep.
			/* JJB: Let's not.  It didn't seem to help frame rate so let's just burn as
			 * fast as possible unless we have a reason not to.
			long stop = start + minSleepTime;
			long now = System.currentTimeMillis();
			if (stop > now) {
				try { 
					Thread.sleep(stop - now);
				} catch (InterruptedException ex) { 
					// Just eat it.
				}

				}
			}
			*/
		}
		lastBest = null;
	}
	public double distance(Target t) {
		return Math.abs(t.centerX - Sensors.goalX) + Math.abs(t.centerY - Sensors.goalY);
	}
	/*
	volatile boolean targetReady = false;
	volatile Target target = null;
	public synchronized void setTargetReady(boolean t) {
		targetReady = t;
	}

	public synchronized boolean getTargetReady() {
		return targetReady;
	}
	public synchronized void setTarget(Target t) {
		target = t;
		setTargetReady(true);
	}

	public synchronized Target getTarget() {
		setTargetReady(false);
		return target;
	}
	*/

	public void updateDashboard(Target best) {
		SmartDashboard.putNumber("centerx", best.centerX);
		SmartDashboard.putNumber("centery", best.centerY);
		SmartDashboard.putNumber("width", best.width);
		SmartDashboard.putNumber("height", best.height);
		SmartDashboard.putNumber("aspectRatio", best.aspectRatio);
		SmartDashboard.putNumber("distance", best.distance);
		SmartDashboard.putNumber("area", best.area);
	}

}
