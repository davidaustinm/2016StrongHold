package org.usfirst.frc.team4003.robot.subsystems;

import org.usfirst.frc.team4003.robot.RobotMap;
import org.usfirst.frc.team4003.robot.io.*;
import org.usfirst.frc.team4003.robot.commands.TurretTiltCommand;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class TurretTilt extends Subsystem {
	CANTalon tilt = new CANTalon(RobotMap.TURRETTILT);
	//CANTalon tilt = new CANTalon(16);
	final double ENCODERCOUNTSPERDEGREE = 3;
	public final double UPPERLIMIT = 2600;
	double upperLimit = UPPERLIMIT;
	Sensors sensors;
	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
		tilt.setInverted(true);
		tilt.reverseSensor(true);
		setDefaultCommand(new TurretTiltCommand());
	}

	CANTalon.TalonControlMode defaultMode;

	public TurretTilt() {
		sensors = Sensors.getInstance();
		tilt.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		defaultMode = tilt.getControlMode();
		tilt.setProfile(0);
		tilt.setP(0.01);
	}

	public void resetPosition() {
		tilt.setPosition(0);
	}
	public double getPosition() {
		return tilt.getPosition();
	}
	public void setPower(double power) {
		if (sensors.getTurretResetSwitch()) {
			if (power < 0) power = 0;
			upperLimit = tilt.getPosition() + UPPERLIMIT;
		}
		if (power > 0 && tilt.getPosition() >= upperLimit) power = 0;
		tilt.set(power);
	}
	public void setPositionMode() {
		tilt.changeControlMode(CANTalon.TalonControlMode.Position);
	}
	public void home() {
		setPositionMode();
		tilt.set(0);
	}

	public void resetControlMode() {
		tilt.changeControlMode(defaultMode);
		tilt.set(0);
	}
	public void rotateBy(double angle) {
		setPositionMode();
		tilt.set(tilt.getPosition() + angle * ENCODERCOUNTSPERDEGREE);
	}
}

