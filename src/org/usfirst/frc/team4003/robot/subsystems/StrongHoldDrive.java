package org.usfirst.frc.team4003.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import org.usfirst.frc.team4003.robot.commands.*;
import edu.wpi.first.wpilibj.*;

/**
 *
 */
public class StrongHoldDrive extends Subsystem {
    TalonSRX leftFront = new TalonSRX(1);
    TalonSRX leftBack = new TalonSRX(2);
    TalonSRX rightFront = new TalonSRX(3);
    TalonSRX rightBack = new TalonSRX(4);
    
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

