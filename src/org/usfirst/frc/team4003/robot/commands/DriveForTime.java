package org.usfirst.frc.team4003.robot.commands;

import org.usfirst.frc.team4003.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveForTime extends Command {
	long stopTime;
	int time;
	double speed = 0.1;
	public DriveForTime(int time, double speed) {
		this(time);
		this.speed = speed;
	}
    public DriveForTime(int time) {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.strongHoldDrive);
        this.time = time;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	stopTime = System.currentTimeMillis() + time;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.strongHoldDrive.setPower(0.1, 0.1);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return System.currentTimeMillis() >= stopTime;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
