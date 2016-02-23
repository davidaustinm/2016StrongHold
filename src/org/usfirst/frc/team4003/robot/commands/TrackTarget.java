package org.usfirst.frc.team4003.robot.commands;

import org.usfirst.frc.team4003.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team4003.robot.io.*;

/**
 *
 */
public class TrackTarget extends Command {
	Sensors sensors;
	TrisonicsPID tiltPID, spinPID;
	TargetCamera camera;
    public TrackTarget() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.turretTilt);
    	requires(Robot.turretSpin);
    	camera = Robot.targetCamera;
    	sensors = Sensors.getInstance();
    	tiltPID = new TrisonicsPID(0.033, 0.00, 0.001);
    	tiltPID.setTarget(0);
    	spinPID = new TrisonicsPID(0.03, 0.005, 0.001);
    	tiltPID.setTarget(0);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    double htolerance = 5;
    double vtolerance = 5;
    protected void execute() {
    	if (camera.getTargetReady() == false) return;
    	
    	Double[] targetData = sensors.getTargetData();
    	if (targetData == null) return;
    	
    	double hPixelError = targetData[2];
    	double vPixelError = targetData[3];
    	hPixelError = 0;
    	if (hPixelError < htolerance && vPixelError < vtolerance) {
    		sensors.setAlignedToGoal(true);
    		Robot.turretTilt.setPower(0);
    		//Robot.turretSpin.setPower(0);
    		return;
    	} else {
    		sensors.setAlignedToGoal(false);
    	}
    	
    	double vAngle = targetData[1];
    	double tiltSpeed = tiltPID.getCorrection(-vAngle);
    	Robot.turretTilt.setPower(tiltSpeed);
    	
    	double hAngle = targetData[0];
    	double spinSpeed = spinPID.getCorrection(-hAngle);
    	//Robot.turretSpin.setPower(spinSpeed);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.turretTilt.setPower(0);
    	//Robot.turretSpin.setPower(0);
    	sensors.setAlignedToGoal(false);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
