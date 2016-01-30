package org.usfirst.frc.team4003.robot.io;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SPI;

import org.usfirst.frc.team4003.robot.*;

public class Sensors {
	AHRS ahrs;
	static Sensors sensors = null;
	public Sensors(){
		try {
			ahrs = new AHRS(SPI.Port.kMXP);
		} catch (RuntimeException ex ) {
			DriverStation.reportError("Error instantiating navX MXP:  " + ex.getMessage(), true);
    	}	
	}
	
	public static Sensors getInstance(){
		if (sensors == null) sensors = new Sensors();
		return sensors;
	}
	
	public double getYaw(){
		return -ahrs.getYaw();
	}
	
	public double getRoll(){
		return ahrs.getRoll();
	}
	
	public double getPitch(){
		return ahrs.getPitch();
	}
	
	public void resetYaw(){
		ahrs.zeroYaw();
	}
	public double getAngle() {
		return -ahrs.getAngle();
	}
	public Double getTargetAngle(){
		Target target = Robot.camera.getTarget();
		if (target == null) return null;
		double W =20.0 /target.width *(target.centerX -80);
		double angle =Math.atan(W /target.distance) *180 /Math.PI;
		return new Double(getYaw() -angle);
	}
}
