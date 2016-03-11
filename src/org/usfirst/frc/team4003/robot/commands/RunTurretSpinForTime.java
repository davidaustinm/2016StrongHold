package org.usfirst.frc.team4003.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team4003.robot.*;
/**
 *
 */
public class RunTurretSpinForTime extends Command {
	long stopTime;
	int time;
	int direction;
    public RunTurretSpinForTime(int time, int direction) {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.turretSpin);
        this.time = time;
        this.direction = direction;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	stopTime = System.currentTimeMillis() + time;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.turretSpin.setPower(0.5, direction);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return System.currentTimeMillis() > stopTime;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
