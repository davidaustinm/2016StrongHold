package org.usfirst.frc.team4003.robot.commands;

import org.usfirst.frc.team4003.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class RunTurretTilt extends Command {
	long stopTime;
    public RunTurretTilt() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.turretTilt);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	stopTime = System.currentTimeMillis() + 500;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.turretTilt.setPower(0.6);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return System.currentTimeMillis() > stopTime;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.turretTilt.setPower(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
