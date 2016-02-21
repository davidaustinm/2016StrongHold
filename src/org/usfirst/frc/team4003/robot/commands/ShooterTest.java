package org.usfirst.frc.team4003.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team4003.robot.*;

/**
 *
 */
public class ShooterTest extends Command {

    public ShooterTest() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.shooter);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double power = -Robot.oi.operator.getLeftJoyY();
    	Robot.shooter.setPower(power);
    	SmartDashboard.putNumber("power",  power);
    	SmartDashboard.putNumber("Shooter0Speed", Robot.shooter.getShooter0Speed());
    	SmartDashboard.putNumber("Shooter1Speed", Robot.shooter.getShooter1Speed());
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
