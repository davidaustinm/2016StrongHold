package org.usfirst.frc.team4003.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team4003.robot.*;
import org.usfirst.frc.team4003.robot.io.*;
/**
 *
 */
public class PIDHold extends Command {
	Sensors sensors = Sensors.getInstance();
	TrisonicsPID pid;
    public PIDHold() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.strongHoldDrive);
        pid = new TrisonicsPID(0.4, 0, 0);
        pid.setTarget(0);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	sensors.resetYaw();
    	sensors.resetPosition();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double speed = pid.getCorrection(sensors.getPositionX());
    	Robot.strongHoldDrive.setPower(speed, speed);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
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
