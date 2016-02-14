package org.usfirst.frc.team4003.robot.auton;

import org.usfirst.frc.team4003.robot.Robot;
import org.usfirst.frc.team4003.robot.commands.TrisonicsPID;
import org.usfirst.frc.team4003.robot.io.Sensors;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DriveOnHeading extends Command {
	double encoderTarget, driveDistance;
	double speed;
	double heading;
	Sensors sensors;
	TrisonicsPID angle, distance;
	long stopTime;
	boolean coast = false;
	public DriveOnHeading(double heading, double driveDistance, double speed, boolean coast) {
		this(heading, driveDistance, speed);
		this.coast = coast;
	}

    public DriveOnHeading(double heading, double driveDistance, double speed) {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.strongHoldDrive);
        this.speed = speed;
        this.heading = heading;
        angle = new TrisonicsPID(0.02, 0, 0);
        angle.setTarget(heading);
        distance = new TrisonicsPID(20, 0, 0);
        this.driveDistance = driveDistance;
        sensors = Sensors.getInstance();
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	stopTime = System.currentTimeMillis() + 5000;
    	encoderTarget = sensors.getAverageEncoder() + 
    			driveDistance * sensors.ENCODERCOUNTSPERINCH;
    	distance.setTarget(encoderTarget);
    }

    double deadBand = 0.1;
    protected double deadBand(double x) {
    	if (Math.abs(x) < deadBand) return 0;
    	return x;
    }
    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double correction = angle.getCorrection(sensors.getYaw());
    	double ramp = distance.getCorrection(sensors.getAverageEncoder());
    	if (ramp > 1) ramp = 1;
    	double left = (speed - correction)*ramp;
    	double right = (speed + correction)*ramp;
    	left = deadBand(left);
    	right = deadBand(right);
    	Robot.strongHoldDrive.setPower(left, right);
    	SmartDashboard.putNumber("Robot X", sensors.getPositionX());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return encoderTarget - sensors.getAverageEncoder() < 1;
    }

    // Called once after isFinished returns true
    protected void end() {
    	if(coast==false) Robot.strongHoldDrive.setPower(0,0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
 
}
