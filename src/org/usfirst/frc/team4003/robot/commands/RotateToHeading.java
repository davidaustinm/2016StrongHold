package org.usfirst.frc.team4003.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team4003.robot.*;
import org.usfirst.frc.team4003.robot.io.*;
/**
 *
 */
public class RotateToHeading extends Command {
	Sensors sensors;
	TrisonicsPID pid;
	double heading, leftPower, rightPower;
	long stopTime;
    public RotateToHeading(double heading, double leftPower, double rightPower) {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.strongHoldDrive);
        sensors = Sensors.getInstance();
        this.heading = heading;
        this.leftPower = leftPower;
        this.rightPower = rightPower;
        pid = new TrisonicsPID(0.1, 0, 0);
        pid.setTarget(heading);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	stopTime = System.currentTimeMillis() + 3000;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double correction = pid.getCorrection(sensors.getYaw());
    	if (correction > 1) correction = 1;
    	if (correction < -1) correction = -1;
    	Robot.strongHoldDrive.setPower(-leftPower*correction, rightPower*correction);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Math.abs(sensors.getYaw() - heading) < 5 || 
        		System.currentTimeMillis() > stopTime;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.strongHoldDrive.setPower(0,0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
