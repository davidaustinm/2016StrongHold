package org.usfirst.frc.team4003.robot.commands;

import org.usfirst.frc.team4003.robot.Robot;
import org.usfirst.frc.team4003.robot.io.Sensors;
import edu.wpi.first.wpilibj.smartdashboard.*;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AlignRobotToGoal extends Command {
	Sensors sensors;
	TrisonicsPID pid = new TrisonicsPID(0.065,0,0);
	Double target;
	double current,speed;
    public AlignRobotToGoal(double speed) {
        requires(Robot.driveTrain);
        sensors = Sensors.getInstance();
        this.speed = speed;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	target = sensors.getTargetAngle();
    	if (target == null) return;
    	pid.setTarget(target.doubleValue());
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if (target == null) return;
        current = sensors.getYaw();
    	double correction = pid.getCorrection(current);
    	if (correction>1) correction =1;
    	if (correction<-1) correction =-1;
    	//SmartDashboard.putNumber("correction",correction);
    	//SmartDashboard.putNumber("target", target.doubleValue());
    	//SmartDashboard.putNumber("current", current);
    	Robot.driveTrain.setPower(-speed*correction, speed*correction);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return target == null || Math.abs(current-target)<3;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.driveTrain.setPower(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
