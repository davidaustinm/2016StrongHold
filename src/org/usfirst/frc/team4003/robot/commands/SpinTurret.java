package org.usfirst.frc.team4003.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc.team4003.robot.*;
import org.usfirst.frc.team4003.robot.subsystems.TurretSpin;

/**
 *
 */
public class SpinTurret extends Command {
	TrisonicsPID pid;
	
	double target;
	double tolerance = 300;
    public SpinTurret(double revolution, int direction) {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.turretSpin);
        pid = new TrisonicsPID(0.001, 0, 0);
        target = direction * revolution * 2 * TurretSpin.SPINHALFREV;
        pid.setTarget(target);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double speed = pid.getCorrection(Robot.turretSpin.getPosition());
    	if (Math.abs(speed)> 1){
    		if (speed > 1) speed = 1;
    		else speed = -1;
    	}
    	Robot.turretSpin.setPower(speed);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Math.abs(Robot.turretSpin.getPosition() - target) < tolerance;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.turretSpin.setPower(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
