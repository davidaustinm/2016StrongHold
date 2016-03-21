package org.usfirst.frc.team4003.robot.io;

import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import org.usfirst.frc.team4003.robot.RobotMap;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;

public class Target {
	public double centerX, centerY;
	public double width, height;
	public double distance, area;
	public double aspectRatio;
	public double error;
	
	double angle = 68.5/2.0 * Math.PI/180;
    double tanAngle = Math.tan(angle);

	protected double halfHypot = Math.sqrt(Math.pow(RobotMap.TARGET_CAMERA_H,  2) + Math.pow(RobotMap.TARGET_CAMERA_W, 2)) / 2.0;
    public Target() {
    	
    }
	public Target(MatOfPoint contour) {
		Rect rectangle = Imgproc.boundingRect(contour);
		width = rectangle.width;
		height = rectangle.height;
		aspectRatio = width/height;
		area = rectangle.width * rectangle.height;
		
		//error = Math.abs((RobotMap.TARGET_TAPE_WIDTH / RobotMap.TARGET_TAPE_HEIGHT) - aspectRatio);
		// Because we're looking up at the target 1.6 isn't really the best aspect ratio.  Looks more like 2.0 on
		// the outer defense line and up to 3.0-ish if you're getting really close to it.
		error = Math.abs(2.0 - aspectRatio);
		
		/*
		Moments moments = Imgproc.moments(contour);
    	centerX = moments.get_m10()/moments.get_m00();
    	centerY = moments.get_m01()/moments.get_m00();
    	*/
		centerX = rectangle.tl().x + rectangle.width/2.0;
		centerY = rectangle.tl().y;
    	distance = 20 * halfHypot / (rectangle.width * tanAngle);
	}
	
	public double getError() {
		return error;
	}
	
}
