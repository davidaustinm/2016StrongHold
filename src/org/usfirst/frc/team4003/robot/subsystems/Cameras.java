package org.usfirst.frc.team4003.robot.subsystems;

import java.util.Comparator;
import java.util.Vector;

import edu.wpi.first.wpilibj.command.Subsystem;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.*;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.USBCamera;

import org.usfirst.frc.team4003.robot.RobotMap;
import org.usfirst.frc.team4003.robot.commands.*;
import org.usfirst.frc.team4003.robot.io.Sensors;
import org.usfirst.frc.team4003.robot.io.Target;

/**
 *
 */
//public class Cameras extends Subsystem implements Runnable {
public class Cameras implements Runnable {
	Sensors sensors;
	USBCamera driver, target, current;
	Image image, trackingImage;
	private final int FPS = 10;
	private final int SLEEP_TIME = 0;
	private final int QUALITY = 20;
	long readyTime;
	boolean tracking = false;
	boolean imageProcessed = true;
	int exposure = 3;
	
	Thread processing;
	NIVision.ParticleFilterCriteria2 criteria[];
	NIVision.ParticleFilterOptions2 filterOptions;
	
	NIVision.Range hue, sat, val;
	boolean practiceValues = true;
	int dx = 5;
	NIVision.Rect[] rects = new NIVision.Rect[dx];
	public Cameras() {
		
		processing = new Thread(this);
		criteria = new NIVision.ParticleFilterCriteria2[1];
		criteria[0] = new NIVision.ParticleFilterCriteria2(NIVision.MeasurementType.MT_AREA, 
				100, 6000, 0, 0);
		filterOptions = new NIVision.ParticleFilterOptions2(0,0,1,1);
		sensors = Sensors.getInstance();
		
		for (int i = 1; i <= dx; i++) {
			rects[i-1] = new NIVision.Rect((int) Sensors.goalY - i, (int) Sensors.goalX - i, 2*i, 2*i);
		}
		
		if (practiceValues) {
			//hue = new NIVision.Range(75, 140);
			//sat = new NIVision.Range(150, 255);
			//val = new NIVision.Range(200, 255);
			hue = new NIVision.Range(90, 130); // 90, 130
			sat = new NIVision.Range(175, 255); // 175, 255
			val = new NIVision.Range(200, 255); // 200, 255
		} else {
			hue = new NIVision.Range(50, 94);
			sat = new NIVision.Range(20, 255);
			val = new NIVision.Range(200, 255);
		}
		
	}
	
	public void setTracking(boolean b) {
		tracking = b;
		if (!tracking) {
			sensors.setTarget(null);
			setTargetRectangle(null);
			centers = new Vector<Center>();
		}
	}
	
	public void initialize() {
		image = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
		trackingImage = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
		target = new USBCamera(RobotMap.TARGET_CAMERA_STRING);
		driver = new USBCamera(RobotMap.DRIVER_CAMERA_STRING);
		//driver = target;
		target.setFPS(FPS);
		driver.setFPS(FPS);
		target.setSize(320, 240);
		target.setExposureManual(exposure);
		target.setWhiteBalanceHoldCurrent();
		driver.setSize(320, 240);
		CameraServer.getInstance().setQuality(QUALITY);
		current = target;
		current.openCamera();
		current.startCapture();
		
		readyTime = System.currentTimeMillis() + SLEEP_TIME;
	}
	
	public void stopCamera() {
		current.stopCapture();
		//current.closeCamera();
	}
	
	public void restartCamera() {
		current.setSize(320, 240);
		if (current == target) {
			current.setExposureManual(exposure);	
			current.setWhiteBalanceHoldCurrent();
		}
		//current.openCamera();
		current.startCapture();
	}
	
	public synchronized void setImageProcessed(boolean b) {
		imageProcessed = b;
	}
	
	public synchronized boolean getImageProcessed() {
		return imageProcessed;
	}
	
	public synchronized void setTrackingImage(Image image) {
		//trackingImage = image;
		NIVision.imaqDuplicate(trackingImage, image);
	}
	
	public synchronized Image getTrackingImage() {
		return trackingImage;
	}
	
	long nextStartTime = 0;
	Vector<Center> centers = new Vector<Center>();
	int numCenters = 1;
	public synchronized void addCenter(Center c) {
		if (centers.size() >= numCenters) {
			centers.removeElementAt(0);
		}
		centers.add(c);
	}
	
	public synchronized Center getAverageCenter() {
		if (centers.size()==0) return null;
		double x = 0, y = 0;
		for (int i = 0; i < centers.size(); i++) {
			x += centers.get(i).x;
			y += centers.get(i).y;
		}
		return new Center(x/numCenters, y/numCenters);
	}
	
	int count = 0;
	public void pushImage() {
		//if (tracking && !getImageProcessed()) return;
		if (current == null) return;
		current.setSize(320, 240);
		current.getImage(image);
		//SmartDashboard.putNumber("count", count);
    	//count ++;
		
		if (tracking && getImageProcessed() && processing.isAlive() == false) {
			//SmartDashboard.putNumber("Hello", 0);
			setTrackingImage(image);
			setImageProcessed(false);
			processing = new Thread(this);
			processing.start();
			//nextStartTime = System.currentTimeMillis() + 75;
			
		} 
		
		//NIVision.GetImageSizeResult size = NIVision.imaqGetImageSize(image);
		//SmartDashboard.putNumber("Image width", size.width);
		//SmartDashboard.putNumber("Image height", size.height);
		for (int i = 0; i < rects.length; i++) {
			NIVision.imaqDrawShapeOnImage(image, image, rects[i],
                DrawMode.DRAW_VALUE, ShapeMode.SHAPE_RECT, 0.5f);	
		}
		NIVision.Rect target = getTargetRectangle();
		if (target != null && tracking) {
			NIVision.imaqDrawShapeOnImage(image, image, target,
	                DrawMode.DRAW_VALUE, ShapeMode.SHAPE_RECT, 0.5f);	
		}
		Center avgCenter = getAverageCenter();
		if (avgCenter != null && tracking) {
			NIVision.Rect center = new NIVision.Rect((int)avgCenter.y - dx, (int)avgCenter.x-dx, 2*dx, 2*dx);
			NIVision.imaqDrawShapeOnImage(image, image, center,
                DrawMode.DRAW_VALUE, ShapeMode.SHAPE_OVAL, 0.5f);	
		}
		
		CameraServer.getInstance().setImage(image);
		
	}
	NIVision.Rect targetRectangle = null;
	public synchronized NIVision.Rect getTargetRectangle() {
		return targetRectangle;
	}
	public synchronized void setTargetRectangle(NIVision.Rect rect) {
		targetRectangle = rect;
	}
	
	
	
	// From opencv code
	//Scalar lowerHSV = new Scalar(50, 20, 200); //50, 127, 200
	//Scalar upperHSV = new Scalar(94, 255, 255); // 94, 207, 255
	
	public void run() {
		long start = System.currentTimeMillis();
		Image image = getTrackingImage();
		Image mask = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_U8, 0);
		NIVision.imaqColorThreshold(mask, image, 128, NIVision.ColorMode.HSV, hue, sat, val);
		
		int imaqError = NIVision.imaqParticleFilter4(mask, mask, criteria, filterOptions, null);
		int numParticles = NIVision.imaqCountParticles(mask, 1);
		
		//SmartDashboard.putNumber("Particles", numParticles);
		
		if(numParticles > 0) {
			//Measure particles and sort by particle size
			Vector<ParticleReport> particles = new Vector<ParticleReport>();
			for(int particleIndex = 0; particleIndex < numParticles; particleIndex++) {
				ParticleReport par = new ParticleReport();
				par.Area = NIVision.imaqMeasureParticle(mask, particleIndex, 0, NIVision.MeasurementType.MT_AREA);
				par.ImageArea = NIVision.imaqMeasureParticle(mask, particleIndex, 0, NIVision.MeasurementType.MT_AREA_BY_IMAGE_AREA);
				par.BoundingRectTop = NIVision.imaqMeasureParticle(mask, particleIndex, 0, NIVision.MeasurementType.MT_BOUNDING_RECT_TOP);
				par.BoundingRectLeft = NIVision.imaqMeasureParticle(mask, particleIndex, 0, NIVision.MeasurementType.MT_BOUNDING_RECT_LEFT);
				par.BoundingRectBottom = NIVision.imaqMeasureParticle(mask, particleIndex, 0, NIVision.MeasurementType.MT_BOUNDING_RECT_BOTTOM);
				par.BoundingRectRight = NIVision.imaqMeasureParticle(mask, particleIndex, 0, NIVision.MeasurementType.MT_BOUNDING_RECT_RIGHT);
				particles.add(par);
			}
			
			//particles.sort(null);
			ParticleReport best = particles.get(0);
			for (int index = 0; index < numParticles; index++) {
				ParticleReport next = particles.get(index);
				
				//SmartDashboard.putNumber("percent fill", next.getPercentFill());
				if (next.Area > 1000 || next.getPercentFill() > 0.6) continue;
				if (next.getError() < best.getError()) best = particles.get(index);
			}
			if (best.Area > 1000 || best.getPercentFill() > 0.6) {
				sensors.setTarget(null);
				setTargetRectangle(null);
				
			} else {
				//SmartDashboard.putNumber("Aspect Ratio", best.getAspectRatio());
				//SmartDashboard.putNumber("Area", best.getArea());		
				setTargetRectangle(best.getBoundingRect());
				addCenter(best.getCenter());
				
				Target t = new Target();
				t.width = best.BoundingRectRight - best.BoundingRectLeft;
				t.height = best.BoundingRectBottom - best.BoundingRectTop;
				t.aspectRatio = t.width / t.height;
				t.area = t.width * t.height;
				t.error = best.getError();
				Center center = best.getCenter();
				t.centerX = center.x;
				t.centerY = center.y;
				t.distance = 20 * 200 / (t.width * Math.tan(0.6));
				sensors.setTarget(t);
				//SmartDashboard.putNumber("area", t.area);
				//SmartDashboard.putNumber("percent fill", best.Area/ (t.area));
				//SmartDashboard.putNumber("area", best.Area);
			}
		} else {
			sensors.setTarget(null);
			setTargetRectangle(null);
		}
		setImageProcessed(true);
		long time = System.currentTimeMillis() - start;
		//SmartDashboard.putNumber("Time", time);
		
	}
	
	public void toggle() {
		try{
			//current.stopCapture();
			//current.closeCamera();
			if (current == target) current = driver;
			else current = target;
			//current.openCamera();
			//current.startCapture();
			readyTime = System.currentTimeMillis() + SLEEP_TIME;
		} catch(Exception e){
			e.printStackTrace();
		}
		
	}
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
/*
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new CamerasCommand());
    }
*/ 
    public class Center {
    	public double x,y;
    	public Center(double x, double y) {
    		this.x = x;
    		this.y = y;
    	}
    }
    
    public class ParticleReport implements Comparator<ParticleReport>, Comparable<ParticleReport>{
		double PercentAreaToImageArea;
		double ImageArea;
		double Area;
		double BoundingRectLeft;
		double BoundingRectTop;
		double BoundingRectRight;
		double BoundingRectBottom;
		public NIVision.Rect getBoundingRect() {
			return new NIVision.Rect((int) BoundingRectTop, (int) BoundingRectLeft,
						(int) (BoundingRectBottom - BoundingRectTop),
						(int) (BoundingRectRight - BoundingRectLeft));
		}
		
		public Center getCenter() {
			return new Center((BoundingRectLeft + BoundingRectRight)/2.0, 
					BoundingRectTop);
		}
		public double getAspectRatio() {
			return Math.abs((BoundingRectLeft - BoundingRectRight) / (BoundingRectBottom - BoundingRectTop));
		}
		
		public double getArea() {
			return Area;
		}
		
		public double getPercentFill() {
			double width = Math.abs(BoundingRectRight - BoundingRectLeft);
			double height = Math.abs(BoundingRectBottom - BoundingRectTop);
			return Area/ (width * height);
		}
		public double getError() {
			return Math.abs(2.0 - getAspectRatio());
		}
		double target = 2.0;
		public int compareTo(ParticleReport r)
		{
			return (int)(100* (Math.abs(target - r.getAspectRatio()) - 
							   Math.abs(target - getAspectRatio())));
		}
		
		public int compare(ParticleReport r1, ParticleReport r2)
		{
			return r1.compareTo(r2);
		}
	};
}

