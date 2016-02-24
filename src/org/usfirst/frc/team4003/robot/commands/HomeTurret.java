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
    public HomeTurret() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.turretSpin);
    	requires(Robot.turretTilt);
    	spinPID = new TrisonicsPID(0.001, 0, 0);
    	spinPID.setTarget(TurretSpin.SPINHALFREV);
    	sensors = Sensors.getInstance();
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.turretTilt.setPower(-0.5);
    	double spinSpeed = spinPID.getCorrection(Robot.turretSpin.getPosition());
    	if (Math.abs(spinSpeed) > 1) {
    		if (spinSpeed > 1) spinSpeed = 1;
    		else spinSpeed = -1;
    	}
    	Robot.turretSpin.setPower(spinSpeed);
    }
    double tolerance = 300;
    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return sensors.getTurretResetSwitch() &&
        		Math.abs(Robot.turretSpin.getPosition() - TurretSpin.SPINHALFREV) < tolerance;
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
