package org.usfirst.frc.team4003.robot.io;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.*;

import org.usfirst.frc.team4003.robot.*;

public class Sensors {
	AHRS ahrs;
	Encoder leftDriveEncoder;
	Encoder rightDriveEncoder;
	Encoder shooter0Encoder;
	Encoder shooter1Encoder;
	Encoder turretEncoder;
	DigitalInput conveyorSwitch;
	DigitalInput intakeSwitch;
	DigitalInput turretResetSwitch;
	AnalogInput turretTiltPot;
	
	public static final double ENCODERCOUNTSPERINCH = 135;
	
	int lastLeftEncoder = 0;
	int lastRightEncoder = 0;
	int lastShooter0Encoder = 0;
	int lastShooter1Encoder = 0;
	long lastShooterTime = 0;
	double shooter0Speed = 0;
	double shooter1Speed = 0;
	double robotX = 0;
	double robotY = 0;
	double rollBaseLine = 0;
	double pitchBaseLine = 0;
	
	static Sensors sensors = null;
	public Sensors(){
		try {
			ahrs = new AHRS(SPI.Port.kMXP);
		} catch (RuntimeException ex ) {
			DriverStation.reportError("Error instantiating navX MXP:  " + ex.getMessage(), true);
    	}	
		
		leftDriveEncoder = new Encoder(RobotMap.LEFTDRIVEENCODERA, RobotMap.LEFTDRIVEENCODERB);
		rightDriveEncoder = new Encoder(RobotMap.RIGHTDRIVEENCODERA, RobotMap.RIGHTDRIVEENCODERB);
		/*
		shooter0Encoder = new Encoder(RobotMap.SHOOTER0ENCODERA, RobotMap.SHOOTER0ENCODERB);
		shooter1Encoder = new Encoder(RobotMap.SHOOTER1ENCODERA, RobotMap.SHOOTER1ENCODERB);
		turretEncoder = new Encoder(RobotMap.TURRETENCODERA, RobotMap.TURRETENCODERB);
		conveyorSwitch = new DigitalInput(RobotMap.CONVEYORSWITCH);
		intakeSwitch = new DigitalInput(RobotMap.INTAKESWITCH);
		turretResetSwitch = new DigitalInput(RobotMap.TURRETRESETSWITCH);
		turretTiltPot = new AnalogInput(RobotMap.TURRETTILT);
		*/
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
	public void setBaseLines() {
		rollBaseLine = getRoll();
		pitchBaseLine = getPitch();
	}
	public double getRollBaseLine() {
		return rollBaseLine;
	}
	public double getPitchBaseLine() {
		return pitchBaseLine;
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
	public void resetEncoders() {
		leftDriveEncoder.reset();
		rightDriveEncoder.reset();
	}
	public int getLeftDriveEncoder() {
		return -leftDriveEncoder.get();
	}
	public int getRightDriveEncoder() {
		return rightDriveEncoder.get();
	}
	public boolean getConveyorSwitch() {
		return conveyorSwitch.get();
	}
	public boolean getIntakeSwitch() {
		return intakeSwitch.get();
	}
	public int changeInLeftEncoder() {
		int current = getLeftDriveEncoder();
		int change = current - lastLeftEncoder;
		lastLeftEncoder = current;
		return change;
	}
	public int changeInRightEncoder() {
		int current = getRightDriveEncoder();
		int change = current - lastRightEncoder;
		lastRightEncoder = current;
		return change;
	}
	public void resetPosition() {
		robotX = 0;
		robotY = 0;
	}
	public void updatePosition() {
		double heading = Math.toRadians(getYaw());
		double distance = (changeInLeftEncoder() + changeInRightEncoder())/2.0;
		robotX += distance * Math.cos(heading);
		robotY += distance * Math.sin(heading);
	}
	public double getPositionX() {
		return robotX / ENCODERCOUNTSPERINCH;
	}
	public double getPositionY() {
		return robotY / ENCODERCOUNTSPERINCH;
	}
	public int getTurretEncoder() {
		return turretEncoder.get();
	}
	public void setLastShooterTime() {
		lastShooterTime = System.currentTimeMillis();
	}
	public void updateShooterSpeeds() {
		long currentTime = System.currentTimeMillis();
		long changeInTime = currentTime - lastShooterTime;
		lastShooterTime = currentTime;
		
		int current0 = shooter0Encoder.get();
		int change0 = current0 - lastShooter0Encoder;
		lastShooter0Encoder = current0;
		shooter0Speed = change0 / (double) changeInTime;
		
		int current1 = shooter1Encoder.get();
		int change1 = current1 - lastShooter1Encoder;
		lastShooter1Encoder = current1;
		shooter1Speed = change1 / (double) changeInTime;
	}
	public double getShooter0Speed() {
		return shooter0Speed;
	}
	public double getShooter1Speed() {
		return shooter1Speed;
	}
	public boolean getTurretResetSwitch() {
		return turretResetSwitch.get();
	}
	public double getTurretTiltPot() {
		return turretTiltPot.getVoltage();
	}
}
