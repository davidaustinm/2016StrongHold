package org.usfirst.frc.team4003.robot.io;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
	DigitalInput intakeLimitSwitch;
	AnalogInput turretTiltPot;
	DigitalInput auton0, auton1, auton2, auton3, auton4, auton5;
	
	public static final double ENCODERCOUNTSPERINCH = 22;
	
	protected boolean alignedToGoal = false;

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
	
	public static final int ROCKWALL = 0;
	public static final int ROUGHTERRAIN = 1;
	public static final int MOAT = 2;
	public static final int RAMPART = 3;
	public static final int CHEVAL = 4;
	public static final int PORTCULLIS = 5;
	public static final int DRAWBRIDGE = 6;
	public static final int SALLYPORT = 7;
	
	public static final int SPYBOT = 0;
	
	public static final double goalX = 173; // 180
	public static final double goalY = 149; // 140, was 128 before elimns, we want 123
	
	static Sensors sensors = null;
	public Sensors(){
		try {
			ahrs = new AHRS(SPI.Port.kMXP);
		} catch (RuntimeException ex ) {
			DriverStation.reportError("Error instantiating navX MXP:  " + ex.getMessage(), true);
    	}	
		
		leftDriveEncoder = new Encoder(RobotMap.LEFTDRIVEENCODERA, RobotMap.LEFTDRIVEENCODERB);
		rightDriveEncoder = new Encoder(RobotMap.RIGHTDRIVEENCODERA, RobotMap.RIGHTDRIVEENCODERB);
		
		conveyorSwitch = new DigitalInput(RobotMap.CONVEYORSWITCH);
		intakeSwitch = new DigitalInput(RobotMap.INTAKESWITCH);
		intakeLimitSwitch = new DigitalInput(RobotMap.INTAKELIMITSWITCH);
		turretResetSwitch = new DigitalInput(RobotMap.TURRETRESETSWITCH);
		
		
		auton0 = new DigitalInput(RobotMap.POSITIONAUTON0);
		auton1 = new DigitalInput(RobotMap.POSITIONAUTON1);
		auton2 = new DigitalInput(RobotMap.POSITIONAUTON2);
		auton3 = new DigitalInput(RobotMap.DEFENSEAUTON0);
		
		auton4 = new DigitalInput(RobotMap.DEFENSEAUTON1);
		auton5 = new DigitalInput(RobotMap.DEFENSEAUTON2);
		
	}
	
	public int getPosition() {
		int position = 0;
		if (!auton2.get()) position += 4;
		if (!auton1.get()) position += 2;
		if (!auton0.get()) position += 1;
		return position;
	}
	
	public double getFinalDrive() {
		if (getPosition() == 2) return 30;
		return 12;
	}
	public int getDefense() {
		int defense = 0;
		if (!auton5.get()) defense += 4;
		if (!auton4.get()) defense += 2;
		if (!auton3.get()) defense += 1;
		return defense;
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
		Target target = getTarget();
		if (target == null) return null;
		double W = -20.0 /target.width *(target.centerX - goalX);
		double angle =Math.atan(W /target.distance);
		angle = Math.toDegrees(angle);
		return new Double(angle);
	}
	public Double getTargetVAngle() {
		Target target = getTarget();
		if (target == null) return null;
		double H = -12.0 / target.height * (target.centerY - goalY);
		double angle = Math.atan(H/target.distance);
		angle = Math.toDegrees(angle);
		//SmartDashboard.putNumber("Target Y", target.centerY);
		return new Double(angle);
	}
	public Double[] getTargetData() {
		Target target = getTarget();
		if (target == null) return null;
		double W = -20.0 /target.width *(target.centerX - goalX);
		double hangle =Math.atan(W /target.distance);
		hangle = Math.toDegrees(hangle);
		
		double H = -12.0 / target.height * (target.centerY - goalY);
		double vangle = Math.atan(H/target.distance);
		vangle = Math.toDegrees(vangle);
		//SmartDashboard.putNumber("Target Y", target.centerY);
		//SmartDashboard.putNumber("Target X", target.centerX);
	
		return new Double[] {new Double(hangle), 
				new Double(vangle), 
				new Double(Math.abs(target.centerX - goalX)),
				new Double(Math.abs(target.centerY - goalY))};
	}
	
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
	public void displayDriveEncoders() {
		SmartDashboard.putNumber("Left Drive Encoder",  getLeftDriveEncoder());
		SmartDashboard.putNumber("Right Drive Encoder",  getRightDriveEncoder());
	}
	public double getAverageEncoder() {
		return (getLeftDriveEncoder() + getRightDriveEncoder())/2.0;
	}
	public boolean getConveyorSwitch() {
		return !conveyorSwitch.get() && !Robot.oi.turretConveyorSwitchOverride.get();
		//return false;
	}
	public boolean getIntakeSwitch() {
		return !intakeSwitch.get() && !Robot.oi.intakeSwitchOverride.get();
		//return false;
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
	public void displayTurretEncoders() {
		SmartDashboard.putNumber("Turret Tilt", Robot.turretTilt.getPosition());
		SmartDashboard.putNumber("Turret Spin", Robot.turretSpin.getPosition());
	}
	public void displayShooterSpeeds() {
		SmartDashboard.putNumber("Shooter 0", Robot.shooter.getShooter0Speed());
		SmartDashboard.putNumber("Shooter 1", Robot.shooter.getShooter1Speed());
	}
	/*
	public double getShooter0Speed() {
		return shooter0Speed;
	}
	public double getShooter1Speed() {
		return shooter1Speed;
	}
	*/
	public boolean getTurretResetSwitch() {
		return !turretResetSwitch.get() && !Robot.oi.turretConveyorSwitchOverride.get();
	}
	
	public boolean getIntakeLimitSwitch() {
		return !intakeLimitSwitch.get();
	}
	public double getTurretTiltPot() {
		return turretTiltPot.getVoltage();
	}
	public void displaySwitches() {
		SmartDashboard.putBoolean("Intake switch", getIntakeSwitch());
		SmartDashboard.putBoolean("Conveyor switch", getConveyorSwitch());
		SmartDashboard.putBoolean("Turret switch", getTurretResetSwitch());
		SmartDashboard.putBoolean("Intake Limit Switch",  getIntakeLimitSwitch());
	}
	public void displayOrientation() {
		SmartDashboard.putNumber("Pitch", getPitch());
		SmartDashboard.putNumber("Yaw", getYaw());
		SmartDashboard.putNumber("Roll", getRoll());
	}
	
	public void displayAutonSwitches() {
		SmartDashboard.putBoolean("Auton0", auton0.get());
		SmartDashboard.putBoolean("Auton1", auton1.get());
		SmartDashboard.putBoolean("Auton2", auton2.get());
		SmartDashboard.putBoolean("Auton3", auton3.get());
		SmartDashboard.putBoolean("Auton4", auton4.get());
		SmartDashboard.putBoolean("Auton5", auton5.get());
	}
	
	public void setAlignedToGoal(boolean b) {
		alignedToGoal = b;
	}
	
	public boolean getAlignedToGoal() {
		return alignedToGoal;
	}
}