package org.usfirst.frc.team4003.robot.commands;

import org.usfirst.frc.team4003.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team4003.robot.io.*;

/**
 *
 */
public class TrackTarget extends Command {
	Sensors sensors;
	TrisonicsPID tiltPID;
	TargetCamera camera;
    public TrackTarget() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.turretTilt);
    	requires(Robot.turretSpin);
    	camera = Robot.targetCamera;
    	sensors = Sensors.getInstance();
    	tiltPID = new TrisonicsPID(.03, 0, 0);
    	tiltPID.setTarget(0);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if (camera.getTargetReady() == false) return;
    	Double vAngle = sensors.getTargetVAngle();
    	Double hAngle = sensors.getTargetAngle();
    	if (vAngle == null) return;
    	double tiltSpeed = tiltPID.getCorrection(-vAngle);
    	Robot.turretTilt.setPower(tiltSpeed);
    	//Robot.turretSpin.rotateBy(hAngle);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.turretTilt.setPower(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
