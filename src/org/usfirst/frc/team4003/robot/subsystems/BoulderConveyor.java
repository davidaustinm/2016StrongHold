package org.usfirst.frc.team4003.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.*;

import org.usfirst.frc.team4003.robot.RobotMap;
import org.usfirst.frc.team4003.robot.commands.*;
/**
 *
 */
public class BoulderConveyor extends Subsystem {
    Talon bc0 = new Talon(RobotMap.CONVEYOR0);
    Talon bc1 = new Talon(RobotMap.CONVEYOR1);
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    public BoulderConveyor() {
    	bc1.setInverted(true);
    }
    public void setPower(double power) {
    	bc0.set(power);
    	bc1.set(power);
    }
    public void initDefaultCommand() {
        // Set the default command for a subsystem hered.
        //setDefaultCommand(new MySpecialCommand());
    	setDefaultCommand(new BoulderConveyorCommand());
    }
    
}

