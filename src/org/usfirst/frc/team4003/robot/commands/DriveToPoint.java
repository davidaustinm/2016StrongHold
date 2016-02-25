package org.usfirst.frc.team4003.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.*;
import org.usfirst.frc.team4003.robot.*;
import org.usfirst.frc.team4003.robot.io.*;

/**
 *
 */
public class DriveToPoint extends Command {
	double targetX, targetY;
	double speed;
	double heading;
	Sensors sensors;
	TrisonicsPID angle, distance, coordinate;
	long stopTime;
	boolean reset = false;
	boolean coast = false;
	public DriveToPoint(double x, double y, double speed, double heading,boolean reset, boolean coast) {
		this(x,y , speed, heading);
		this.reset = reset;
		this.coast = coast;
	}

    public DriveToPoint(double x, double y, double speed, double heading) {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.strongHoldDrive);
        targetX = x;
        targetY = y;
        this.speed = speed;
        this.heading = heading;
        angle = new TrisonicsPID(0.02, 0, 0);
        angle.setTarget(heading);
        distance = new TrisonicsPID(0.2, 0, 0);
        distance.setTarget(targetX);
        coordinate = new TrisonicsPID(0.05, 0, 0);
        coordinate.setTarget(targetY);
        sensors = Sensors.getInstance();
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	stopTime = System.currentTimeMillis() + 5000;
    	if (reset) sensors.resetPosition();
    }

    double deadBand = 0.1;
    protected double deadBand(double x) {
    	if (Math.abs(x) < deadBand) return 0;
    	return x;
    }
    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double correction = angle.getCorrection(sensors.getYaw()) +
    			coordinate.getCorrection(sensors.getPositionY());
    	double ramp = distance.getCorrection(sensors.getPositionX());
    	if (ramp > 1) ramp = 1;
    	double left = (speed - correction)*ramp;
    	double right = (speed + correction)*ramp;
    	left = deadBand(left);
    	right = deadBand(right);
    	Robot.strongHoldDrive.setPower(left, right);
    	//SmartDashboard.putNumber("Robot X", sensors.getPositionX());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return targetX - sensors.getPositionX() < 1;
    }

    // Called once after isFinished returns true
    protected void end() {
    	if(coast==false) Robot.strongHoldDrive.setPower(0,0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
