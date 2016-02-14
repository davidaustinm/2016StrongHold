package org.usfirst.frc.team4003.robot.subsystems;

import org.usfirst.frc.team4003.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.*;
/**
 *
 */
public class Shifter extends Subsystem {
    Solenoid shifter = new Solenoid(20, RobotMap.SHIFTER);
	//Solenoid shifter = new Solenoid(20, 0);
	// Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    public void setLow(boolean low) {
    	shifter.set(low);
    }
    public boolean getStatus() {
    	return shifter.get();
    }
}

