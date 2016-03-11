package org.usfirst.frc.team4003.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team4003.robot.*;

/**
 *
 */
public class RotateTurretByAngle extends Command {
	double distance;
	int direction;
	double initialEncoder;
    public RotateTurretByAngle(double angle, int direction) {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.turretSpin);
        distance = Math.toDegrees(angle) * Robot.turretSpin.ENCODERCOUNTSPERDEGREE;
        this.direction = direction;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	initialEncoder = Robot.turretSpin.getPosition();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.turretSpin.setPower(0.5, direction);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Math.abs(Robot.turretSpin.getPosition() - initialEncoder) > distance;
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
