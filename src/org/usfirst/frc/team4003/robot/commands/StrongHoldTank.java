package org.usfirst.frc.team4003.robot.commands;

import org.usfirst.frc.team4003.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class StrongHoldTank extends Command {

    public StrongHoldTank() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.strongHoldDrive);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    double deadBand = 0.1;
    protected double deadBand(double x) {
    	if (Math.abs(x) < deadBand) return 0;
    	return x;
    }
    double maxSpeed = 0.6;
    public void setMaxSpeed(double speed) {
    	maxSpeed = speed;
    }
    protected void execute() {
    	double left = deadBand(-Robot.oi.driver.getLeftJoyY());
    	double right = deadBand(-Robot.oi.driver.getRightJoyY());
    	Robot.strongHoldDrive.setPower(left * maxSpeed, right * maxSpeed);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
