package org.usfirst.frc.team4003.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.*;
import org.usfirst.frc.team4003.robot.commands.*;
/**
 *
 */
public class BoulderConveyor extends Subsystem {
    Talon bc0 = new Talon(4);
    Talon bc1 = new Talon(5);
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    public BoulderConveyor() {
    	bc1.setInverted(true);
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    	setDefaultCommand(new BoulderConveyorCommand());
    }
    
}

