package org.usfirst.frc.team4003.robot.subsystems;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.command.Subsystem;

import org.usfirst.frc.team4003.robot.RobotMap;
import org.usfirst.frc.team4003.robot.commands.*;

/**
 *
 */
public class TurretSpin extends Subsystem {
    CANTalon spin = new CANTalon(RobotMap.TURRETSPIN);
	//CANTalon spin = new CANTalon(16);
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    CANTalon.TalonControlMode defaultMode;
    
    public static final int SPINHALFREV = 19000;
    final double ENCODERCOUNTSPERDEGREE = SPINHALFREV/180.0;
    final double UPPERLIMIT = 3*SPINHALFREV;
    final double LOWERLIMIT = -SPINHALFREV;
    
    public TurretSpin() {
    	spin.setInverted(true);
    	spin.reverseSensor(true);;
    	spin.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
    	defaultMode = spin.getControlMode();
    	spin.setProfile(0);
		spin.setP(0.01);
    }
    public void resetPosition() {
    	spin.setPosition(0);
    }
    public double getPosition() {
    	return spin.getPosition();
    }
    public void setPositionMode() {
    	spin.changeControlMode(CANTalon.TalonControlMode.Position);
    }
    public void home() {
    	setPositionMode();
    	spin.set(0);
    }
    public void resetControlMode() {
    	spin.changeControlMode(defaultMode);
    	spin.set(0);
    }
    public void initDefaultCommand() {
    	//setDefaultCommand(new TurretSpinCommand()); // TODO
    }
    double maxSpeed = 1;
    public void setMaxSpeed(double speed) {
    	maxSpeed = speed;
    }
    
    public void setPower(double power) {
    	double position = getPosition();
    	if (power > 0 && position > UPPERLIMIT) power = 0;
    	if (power < 0 && position < LOWERLIMIT) power = 0;
    	spin.set(power*maxSpeed);
    }
    
    public void setPower(double power, int direction) {
    	setPower(Math.abs(power) * direction);
    }
    public void rotateBy(double angle) {
    	setPositionMode();
    	spin.set(spin.getPosition() + angle*ENCODERCOUNTSPERDEGREE);
    }
}

