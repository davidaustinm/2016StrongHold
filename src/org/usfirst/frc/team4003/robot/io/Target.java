package org.usfirst.frc.team4003.robot.io;

import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;

public class Target {
	double centerX, centerY;
	double width, height;
	double distance, area;
	double aspectRatio;
	double error;
	
	double angle = 68.5/2.0 * Math.PI/180;
    double tanAngle = Math.tan(angle);
    
	public Target(MatOfPoint contour) {
		Rect rectangle = Imgproc.boundingRect(contour);
		width = rectangle.width;
		height = rectangle.height;
		aspectRatio = width/height;
		area = rectangle.width * rectangle.height;
		
		error = Math.abs(1.2-aspectRatio);
		
		Moments moments = Imgproc.moments(contour);
    	centerX = moments.get_m10()/moments.get_m00();
    	centerY = moments.get_m01()/moments.get_m00();
    	distance = 20*100.0/(rectangle.width * tanAngle);
	}
	
	public double getError() {
		return error;
	}
	
}
