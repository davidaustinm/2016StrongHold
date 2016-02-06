package org.usfirst.frc.team4003.robot.commands;

import org.usfirst.frc.team4003.robot.Robot;
import org.usfirst.frc.team4003.robot.io.Sensors;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class RunIntakeAndConveyor extends Command {
	Sensors sensors = Sensors.getInstance();
    public RunIntakeAndConveyor() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.boulderConveyor);
    	requires(Robot.intakeRun);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.intakeRun.setPower(1);
    	Robot.boulderConveyor.setPower(1);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return sensors.getConveyorSwitch();
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.intakeRun.setPower(0);
    	Robot.boulderConveyor.setPower(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
