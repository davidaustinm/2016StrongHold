package org.usfirst.frc.team4003.robot.commands;

import org.usfirst.frc.team4003.robot.Robot;
import edu.wpi.first.wpilibj.smartdashboard.*;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class StrongHoldArcade extends Command {

    public StrongHoldArcade() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.strongHoldDrive);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }
    
    double deadBand = 0.1;
    protected double deadBand(double x) {
    	if (Math.abs(x) < deadBand) return 0;
    	return x;
    }

    // Called repeatedly when this Command is scheduled to run
    double inertia = 0.2;
	double inertiam1 = 1-inertia;
	double oldThrottle = 0;
	double oldWheel = 0;
	double maxSpeed = 0.8;
	public void setMaxSpeed(double speed) {
		maxSpeed = speed;
	}
    protected void execute() {
    	double tmpThrottle = -deadBand(Robot.oi.driver.getLeftJoyY());
    	double tmpWheel = deadBand(Robot.oi.driver.getRightJoyX());
    	//SmartDashboard.putNumber("throttle", tmpThrottle);
    	//SmartDashboard.putNumber("wheel", tmpWheel);
    	
    	double throttle = inertiam1 * tmpThrottle + inertia * oldThrottle;
    	double wheel = inertiam1 * tmpWheel + inertia * oldWheel;
    	
    	oldThrottle = tmpThrottle;
    	oldWheel = tmpWheel;
    	
    	double left = throttle + wheel;
    	double right = throttle - wheel;
    	
    	if (Math.abs(left) > 1) {
    		if (left < -1) left = -1;
    		else left = 1;
    	}
    	if (Math.abs(right) > 1) {
    		if (right < -1) right = -1;
    		else right = 1;
    	}
    	
    	left = deadBand(left);
    	right = deadBand(right);
    	double currentMaxSpeed = maxSpeed;
    	if (Robot.oi.driver.getRightTrigger() > 0.4) currentMaxSpeed = 1;
    	Robot.strongHoldDrive.setPower(left * currentMaxSpeed, right * currentMaxSpeed);
    	
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
