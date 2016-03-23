package org.usfirst.frc.team4003.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team4003.robot.*;
import org.usfirst.frc.team4003.robot.io.*;
/**
 *
 */
public class PIDHold extends Command {
	Sensors sensors = Sensors.getInstance();
	double x = 0;
	double lastLeft, lastRight;
	double yawOffset;
	long timeOut;
	TrisonicsPID pid;
    public PIDHold() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.strongHoldDrive);
        pid = new TrisonicsPID(0.4, 0, 0);
        pid.setTarget(0);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	x = 0;
    	lastLeft = sensors.getLeftDriveEncoder();
    	lastRight = sensors.getRightDriveEncoder();
    	yawOffset = sensors.getYaw();
    	timeOut = System.currentTimeMillis() + 5000;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double left = sensors.getLeftDriveEncoder();
    	double right = sensors.getRightDriveEncoder();
    	double distance = (left + right - lastLeft - lastRight)/2.0;
    	lastLeft = left;
    	lastRight = right;
    	x += distance * Math.cos(Math.toRadians(sensors.getYaw() - yawOffset));
    	double speed = pid.getCorrection(x);
    	if (Math.abs(speed) > 1) {
    		if (speed > 1) speed = 1;
    		else speed = -1;
    	}
    	Robot.strongHoldDrive.setPower(speed, speed);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return System.currentTimeMillis() > timeOut;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.strongHoldDrive.setPower(0,0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
