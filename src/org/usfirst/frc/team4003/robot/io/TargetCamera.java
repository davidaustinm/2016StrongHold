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
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;
import org.opencv.highgui.Highgui;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team4003.robot.*;

public class TargetCamera implements Runnable, DashboardMatProvider {
	VideoCapture vcap;
	double angle = 68.5 / 2.0 * Math.PI / 180;
	double tanAngle = Math.tan(angle);
	volatile Target target = null;
	double minArea = 150;

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
		vcap = new VideoCapture();
		vcap.open(RobotMap.TARGET_CAMERA);
		Timer.delay(2);
		/* TODO: Find out why this doesn't work at all.
		vcap.set(Highgui.CV_CAP_PROP_FRAME_WIDTH, 640);
		vcap.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT, 480);
		*/
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
		while (Thread.currentThread().isInterrupted() == false) {
			long start = System.currentTimeMillis();
			vcap.read(origImg);
			Imgproc.resize(origImg, img, new Size(320, 180));
			if (Robot.isTargetTracking()) {

				Size size = img.size();
				SmartDashboard.putNumber("pixelwidth", size.width);
				SmartDashboard.putNumber("pixelheight", size.height);

				Imgproc.cvtColor(img, img, Imgproc.COLOR_BGR2HSV);
				Scalar lowerHSV = new Scalar(70, 32, 48);
				Scalar upperHSV = new Scalar(94, 255, 255);
				Core.inRange(img, lowerHSV, upperHSV, mask);

				List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
				Mat hierarchy = new Mat();
				Imgproc.findContours(mask, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

				Mat dash = new Mat();
				img.copyTo(dash);
				Imgproc.cvtColor(dash, dash, Imgproc.COLOR_HSV2BGR);

				List<Target> targets = new ArrayList<Target>();
				for (int c = 0; c < contours.size(); c++) {
					MatOfPoint cont = contours.get(c);
					Rect bounding = Imgproc.boundingRect(cont);
					if (Imgproc.boundingRect(cont).area() > minArea) {
						Core.rectangle(dash, bounding.br(), bounding.tl(), new Scalar(255, 255, 0));
						targets.add(new Target(cont));
						// Draw large enough contours in red
						Imgproc.drawContours(dash, contours, c, new Scalar(0, 0, 255));
					} else {
						// Draw small contours in black for debugging.
						Imgproc.drawContours(dash, contours, c, new Scalar(0, 0, 0));

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
					// Draw a circle in the middle-ish of our best target.
					Core.circle(dash, new Point(best.centerX, best.centerY), 8, new Scalar(0, 0, 255));
					updateDashboard(best);
				}

				// Now that we've marked up the image a bit set it to be the
				// version that goes on the dashboard.
				setDashboardImg(dash);

				setTarget(best);
				SmartDashboard.putNumber("Contour Count", targets.size());
				SmartDashboard.putNumber("Time", System.currentTimeMillis() - start);
			} else {
				setDashboardImg(img);
				setTarget(null);
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
	}
	boolean targetReady = false;
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
