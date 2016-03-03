package org.usfirst.frc.team4003.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team4003.robot.io.*;
/**
 *
 */
public class WaitUntilAligned extends Command {
	Sensors sensors;
    public WaitUntilAligned() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	//sensors = Sensors.getInstance();
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	sensors = Sensors.getInstance();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	SmartDashboard.putBoolean("aligned?", sensors.getAlignedToGoal());
        return sensors.getAlignedToGoal();
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
