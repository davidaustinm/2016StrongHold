package org.usfirst.frc.team4003.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;

import org.usfirst.frc.team4003.robot.RobotMap;
import org.usfirst.frc.team4003.robot.commands.*;

import edu.wpi.first.wpilibj.*;

/**
 *
 */
public class StrongHoldDrive extends Subsystem {
    TalonSRX leftFront = new TalonSRX(RobotMap.LEFTFRONTDRIVEMOTOR);
    TalonSRX leftBack = new TalonSRX(RobotMap.LEFTBACKDRIVEMOTOR);
    TalonSRX rightFront = new TalonSRX(RobotMap.RIGHTFRONTDRIVEMOTOR);
    TalonSRX rightBack = new TalonSRX(RobotMap.RIGHTBACKDRIVEMOTOR);
    
    public StrongHoldDrive() {
    	leftFront.setInverted(true);
    	leftBack.setInverted(true);
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    	setDefaultCommand(new StrongHoldTank());
    }
    
    public void setPower(double leftPower, double rightPower) {
    	leftFront.set(leftPower);
    	leftBack.set(leftPower);
    	rightFront.set(rightPower);
    	rightBack.set(rightPower);
    }
}

