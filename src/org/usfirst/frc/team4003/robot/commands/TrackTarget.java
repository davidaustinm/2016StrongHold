package org.usfirst.frc.team4003.robot.commands;

import org.usfirst.frc.team4003.robot.Robot;
import org.usfirst.frc.team4003.robot.commands.actions.*;
import org.usfirst.frc.team4003.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc.team4003.robot.io.*;

import edu.wpi.first.wpilibj.smartdashboard.*;;
/**
 *
 */
public class TrackTarget extends Command {
	Sensors sensors;
	TrisonicsPID tiltPID, spinPID;
	TargetCamera camera;
	
	public static final int NONE = 0;
	public static final int LEFT = 1;
	public static final int RIGHT = 2;
	public static final int UP = 3;
	public static final int DOWN = 4;
	static int tiltHint = NONE;
	static int spinHint = NONE;
	static int hintTimeOut = 3000;
	static int hintStart = 1000;
	static long targetLastSeen = 0;
	
	public static void setTiltHint(int hint) {tiltHint = hint;}
	public static void setSpinHint(int hint) {spinHint = hint;}
	
	static boolean auton = false;
	public static void setAuton(boolean b) {
		auton = b;
	}
	
    public TrackTarget() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.turretTilt);
    	requires(Robot.turretSpin);
    	camera = Robot.targetCamera;
    	sensors = Sensors.getInstance();
    	double tiltKi = 0;
    	if (auton) tiltKi = 0.001;
    	tiltPID = new TrisonicsPID(0.038, tiltKi, 0.00);
    	tiltPID.setTarget(0);
    	double spinKi = 0;
    	if (auton) spinKi = 0.001;
    	spinPID = new TrisonicsPID(0.023, spinKi, 0.00);
    	tiltPID.setTarget(0);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	targetLastSeen = System.currentTimeMillis();
    }

    // Called repeatedly when this Command is scheduled to run
    double htolerance = 4;
    double vtolerance = 4;
    
    protected void execute() {
    	SmartDashboard.putNumber("spinHint", spinHint);
    	if (camera.getTargetReady() == false) return;
    	
    	long currentTime = System.currentTimeMillis();
    	Double[] targetData = sensors.getTargetData();
    	SmartDashboard.putNumber("spinHint", spinHint);
    	if (targetData == null) {

    		if (tiltHint == DOWN) {
    			if (currentTime > targetLastSeen + hintTimeOut) {
    				Robot.turretTilt.setPower(0);
    				tiltHint = NONE;
    			}
    			else Robot.turretTilt.setPower(-0.3);
    		}
    		
    		if (spinHint != NONE && currentTime > targetLastSeen + hintStart) {
    			if (currentTime < targetLastSeen + hintStart + hintTimeOut) {
    				if (spinHint == LEFT) 
    					Robot.turretSpin.setPower(0.4, RobotMap.COUNTERCLOCKWISE);
    				else 
    					Robot.turretSpin.setPower(0.4, RobotMap.CLOCKWISE);
    			} else {
    				Robot.turretSpin.setPower(0);
    				spinHint = NONE;
    			}
    		
    		}
    		return;
    	} 
    	targetLastSeen = currentTime;
    	double hPixelError = targetData[2];
    	double vPixelError = targetData[3];
    	SmartDashboard.putNumber("herror", hPixelError);
    	SmartDashboard.putNumber("verror", vPixelError);
    	//hPixelError = 0; // TODO
    	//hPixelError = 0;
    	//vPixelError = 0;
    	//SmartDashboard.putNumber("TrackTarget Sensor ID:", System.identityHashCode(sensors));
    	if ((Math.abs(hPixelError) < htolerance) && (Math.abs(vPixelError) < vtolerance)) {
    		SmartDashboard.putString("On goal?", "YES!");
    		sensors.setAlignedToGoal(true);
    		WaitUntilAligned.setAligned(true);
    		Robot.turretTilt.setPower(0);
    		Robot.turretSpin.setPower(0);
    		if (auton) {
    			(new TrackingOn(false)).start();
    			(new AutoShoot()).start();
    			cancel();
    		}
    		return;
    	} else {
    		SmartDashboard.putString("On goal?", "No!");
    		//sensors.setAlignedToGoal(false);
    	}
    	
    	double vAngle = targetData[1];
    	double tiltSpeed = tiltPID.getCorrection(-vAngle);
    	if (Math.abs(tiltSpeed) > 1) {
    		if (tiltSpeed > 1) tiltSpeed = 1;
    		else tiltSpeed = -1;
    	}
    	tiltHint = NONE;
    	spinHint = NONE;
    
    	Robot.turretTilt.setPower(tiltSpeed);
    	if (tiltSpeed < 0) {
    		tiltHint = DOWN;
    		spinHint = NONE;
    		hintStart = 0;
    		hintTimeOut = 3000;    		
    	} else {
    		tiltHint = NONE;
    		spinHint = NONE;
    		hintStart = 1000;
    		hintTimeOut = 1000;
    	}
    	
    	double hAngle = targetData[0];
    	double spinSpeed = spinPID.getCorrection(hAngle);
    	/* 
    	 * 
    	 * A positive hAngle means we want to rotate counter-clockwise
    	 * 
    	 */
    	
    	if (hAngle > 0) { 
    		Robot.turretSpin.setPower(spinSpeed, RobotMap.COUNTERCLOCKWISE);
    	} else {
    		Robot.turretSpin.setPower(spinSpeed, RobotMap.CLOCKWISE);
    	}
    	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.turretTilt.setPower(0);
    	Robot.turretSpin.setPower(0); // TODO
    	//sensors.setAlignedToGoal(false);
    	tiltHint = NONE;
    	spinHint = NONE;
    	hintTimeOut = 1000;
    	hintStart = 1000;
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
