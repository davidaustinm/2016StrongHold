package org.usfirst.frc.team4003.robot.subsystems;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;

import org.usfirst.frc.team4003.robot.RobotMap;
import org.usfirst.frc.team4003.robot.commands.*;

import edu.wpi.first.wpilibj.*;

/**
 *
 */
public class StrongHoldDrive extends Subsystem {
    CANTalon leftFront = new CANTalon(RobotMap.LEFTFRONTDRIVEMOTOR);
    CANTalon leftBack = new CANTalon(RobotMap.LEFTBACKDRIVEMOTOR);
    CANTalon rightFront = new CANTalon(RobotMap.RIGHTFRONTDRIVEMOTOR);
    CANTalon rightBack = new CANTalon(RobotMap.RIGHTBACKDRIVEMOTOR);
    
    public StrongHoldDrive() {
    	rightFront.setInverted(true);
    	rightBack.setInverted(true);
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    	//setDefaultCommand(new StrongHoldArcade());
    }
    Command defaultCommand;
    
    public void setDefault(Command command) {
    	setDefaultCommand(command);
    	defaultCommand = command;
    }
    
    public void setPower(double leftPower, double rightPower) {
    	//System.out.println("drive: " + leftPower + " " + rightPower);
    	leftFront.set(leftPower);
    	leftBack.set(leftPower);
    	rightFront.set(rightPower);
    	rightBack.set(rightPower);
    }
}

