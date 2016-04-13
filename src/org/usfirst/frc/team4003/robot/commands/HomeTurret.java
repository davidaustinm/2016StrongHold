package org.usfirst.frc.team4003.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team4003.robot.io.*;
import org.usfirst.frc.team4003.robot.subsystems.*;
import org.usfirst.frc.team4003.robot.*;
/**
 *
 */
public class HomeTurret extends Command {
	
	TrisonicsPID spinPID;
	Sensors sensors;
	double target;
	long stopTime;
    public HomeTurret() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.turretSpin);
    	requires(Robot.turretTilt);
    	target = TurretSpin.SPINHALFREV;
    	spinPID = new TrisonicsPID(0.0005, 0, 0.0001);
    	spinPID.setTarget(target);
    	sensors = Sensors.getInstance();
    }
    
    public HomeTurret(double target) {
    	this.target = target;
    	requires(Robot.turretSpin);
    	requires(Robot.turretTilt);
    	//spinPID = new TrisonicsPID(0.001, 0, 0);
    	spinPID = new TrisonicsPID(0.0005, 0, 0);
    	spinPID.setTarget(target);
    	sensors = Sensors.getInstance();
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	stopTime = System.currentTimeMillis() + 4000;
    	
    }

    // Called repeatedly when this Command is scheduled to run
    int tiltCutoff = 0; // was 500
    protected void execute() {
    	if (Robot.turretTilt.getPosition() > tiltCutoff) Robot.turretTilt.setPower(-0.5);
    	else Robot.turretTilt.setPower(0);
    	double spinSpeed = spinPID.getCorrection(Robot.turretSpin.getPosition());
    	if (Math.abs(spinSpeed) > 1) {
    		if (spinSpeed > 1) spinSpeed = 1;
    		else spinSpeed = -1;
    	}
    	Robot.turretSpin.setPower(spinSpeed);
    }
    double tolerance = 100;
    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return System.currentTimeMillis() >= stopTime ||
        		Math.abs(Robot.turretSpin.getPosition() - target) < tolerance;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.turretSpin.setPower(0);
    	Robot.turretTilt.setPower(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
