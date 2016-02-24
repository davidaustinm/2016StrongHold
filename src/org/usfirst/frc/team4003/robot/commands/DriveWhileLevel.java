package org.usfirst.frc.team4003.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team4003.robot.io.*;
import org.usfirst.frc.team4003.robot.*;

/**
 *
 */
public class DriveWhileLevel extends Command {
	double speed;
	TrisonicsPID angle, coordinate;
	Sensors sensors;
	final double ROLLLIMIT = 1.8;
	
    public DriveWhileLevel(double speed) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.strongHoldDrive);
    	this.speed = speed;
    	angle = new TrisonicsPID(0.02, 0, 0);
        angle.setTarget(0);
        coordinate = new TrisonicsPID(0.05, 0, 0);
        coordinate.setTarget(0);
        sensors = Sensors.getInstance();
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
    protected void execute() {
    	double correction = angle.getCorrection(sensors.getYaw()) +
    			coordinate.getCorrection(sensors.getPositionY());
    	double left = (speed - correction);
    	double right = (speed + correction);
    	left = deadBand(left);
    	right = deadBand(right);
    	Robot.strongHoldDrive.setPower(left, right);
    	SmartDashboard.putNumber("Robot X", sensors.getPositionX());
    	sensors.displayOrientation();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return sensors.getRoll() > sensors.getRollBaseLine() + ROLLLIMIT &&
        		sensors.getPositionX() > 46;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.strongHoldDrive.setPower(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
