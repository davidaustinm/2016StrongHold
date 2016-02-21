package org.usfirst.frc.team4003.robot.commands;

import org.usfirst.frc.team4003.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class CameraToggle extends Command {

    public CameraToggle() {
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	synchronized(Robot.activeCamera) {
			if (Robot.activeCamera == Robot.targetCamera) {
				//Robot.activeCamera = Robot.driverCamera;
			} else {
				Robot.activeCamera = Robot.targetCamera;
			}
    	}
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
