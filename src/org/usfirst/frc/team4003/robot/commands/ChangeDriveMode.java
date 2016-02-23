package org.usfirst.frc.team4003.robot.commands;

import org.usfirst.frc.team4003.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ChangeDriveMode extends Command {
	public static final int TANK = 0;
	public static final int ARCADE = 1;
	int mode;
    public ChangeDriveMode(int mode) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	this.mode = mode;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	if (mode == TANK) {
    		Robot.arcadeDrive.cancel();
    		Robot.tankDrive.start();
    	} else {
    		Robot.tankDrive.cancel();
    		Robot.arcadeDrive.start();
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
