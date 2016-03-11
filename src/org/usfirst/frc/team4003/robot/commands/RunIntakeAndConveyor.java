package org.usfirst.frc.team4003.robot.commands;

import org.usfirst.frc.team4003.robot.Robot;
import org.usfirst.frc.team4003.robot.io.Sensors;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class RunIntakeAndConveyor extends Command {
	Sensors sensors = Sensors.getInstance();
	boolean in;
	long stopTime;
    public RunIntakeAndConveyor(boolean in) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.boulderConveyor);
    	requires(Robot.intakeRun);
    	this.in = in;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	stopTime = 0;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double power = 0.7;
    	if (in == false) power *= -1;
    	Robot.intakeRun.setPower(power);
    	Robot.boulderConveyor.setPower(-power);
    	if (!in && sensors.getIntakeSwitch() && stopTime == 0) {
    		stopTime = System.currentTimeMillis() + 250;
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	if (in) return sensors.getConveyorSwitch();
    	return sensors.getIntakeSwitch() && System.currentTimeMillis() > stopTime;
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
