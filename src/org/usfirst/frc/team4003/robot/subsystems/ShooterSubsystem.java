package org.usfirst.frc.team4003.robot.subsystems;

import org.usfirst.frc.team4003.robot.RobotMap;
import org.usfirst.frc.team4003.robot.commands.*;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class ShooterSubsystem extends Subsystem {
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	CANTalon shooter0 = new CANTalon(RobotMap.SHOOTER0);
	//CANTalon shooter0 = new CANTalon(16);
	double shooter0Target = 4300;
	double shooter1Target = 4100;  //3800
	double slow0 = 3400;
	double slow1 = slow0;
	//double shooter0Target = 3500;
	//double shooter1Target = 3500;
	CANTalon shooter1 = new CANTalon(RobotMap.SHOOTER1);
	CANTalon.TalonControlMode defaultMode;
	public ShooterSubsystem() {
		defaultMode = shooter0.getControlMode();
        shooter0.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
        shooter0.setInverted(true);
        shooter0.reverseSensor(false);
        shooter0.configNominalOutputVoltage(0, 0);
        shooter0.configPeakOutputVoltage(12, -12);
        shooter0.setProfile(0);
        shooter0.setF(0.025);
        shooter0.setI(0);
        shooter0.setP(0.02);
        shooter0.setD(0);
        shooter0.changeControlMode(TalonControlMode.Speed);
       
        //shooter1.setInverted(true);
        shooter1.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
        shooter1.setInverted(true);
        shooter1.reverseSensor(false);
        shooter1.configNominalOutputVoltage(0, 0);
        shooter1.configPeakOutputVoltage(12, -12);
        shooter1.setProfile(0);
        shooter1.setF(0.025);
        shooter1.setI(0);
        shooter1.setP(0.02);
        shooter1.setD(0);
        shooter1.changeControlMode(TalonControlMode.Speed);

	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new ShooterTest());
    }
    
    public void setOn(boolean on, boolean slow) {
    	if (slow) {
    		setOn(on, slow0, slow1);
    	} else {
    		setOn(on, shooter0Target, shooter1Target);
    	}
    }
    
    public void setOn(boolean on, double speed0, double speed1) {
    	if (on) {
    		shooter0.set(speed0);
    		shooter1.set(speed1);
    	}
    	else {
    		shooter0.set(0);
    		shooter1.set(0);
    	}
    }
    public void setPower(double power) {
    	shooter0.changeControlMode(defaultMode);
    	shooter1.changeControlMode(defaultMode);
    	shooter0.set(power);
    	shooter1.set(power);
    }
    public double getShooter0Speed() {
    	return shooter0.getSpeed();
    }
    public double getShooter1Speed() {
    	return shooter1.getSpeed();
    }
}

