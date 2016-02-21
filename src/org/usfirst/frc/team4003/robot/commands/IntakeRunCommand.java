package org.usfirst.frc.team4003.robot.commands;

import org.usfirst.frc.team4003.robot.Robot;
import org.usfirst.frc.team4003.robot.io.*;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class IntakeRunCommand extends Command {
	Sensors sensors;
    public IntakeRunCommand() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.intakeRun);
    	sensors = Sensors.getInstance();
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double power = Robot.oi.driver.getRightTrigger()-
    			Robot.oi.driver.getLeftTrigger();
    	if(Math.abs(power)<.1)power = 0;
    	if (sensors.getIntakeSwitch()) power = 0;
    	Robot.intakeRun.setPower(power);
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
