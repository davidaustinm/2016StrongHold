package org.usfirst.frc.team4003.robot.commands;

import org.usfirst.frc.team4003.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class RunConveyorForTime extends Command {
	int time;
	long stopTime;
	boolean in;
    public RunConveyorForTime(boolean in, int time) {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.boulderConveyor);
        this.time = time;
        this.in = in;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	stopTime = System.currentTimeMillis() + time;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if (in) Robot.boulderConveyor.setPower(1);
    	else Robot.boulderConveyor.setPower(-1);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return System.currentTimeMillis() >= stopTime;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.boulderConveyor.setPower(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
