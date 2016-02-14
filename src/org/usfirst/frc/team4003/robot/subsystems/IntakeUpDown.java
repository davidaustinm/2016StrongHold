package org.usfirst.frc.team4003.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.*;

import org.usfirst.frc.team4003.robot.RobotMap;
import org.usfirst.frc.team4003.robot.commands.*;
/**
 *
 */
public class IntakeUpDown extends Subsystem {
    //Solenoid intakeDown = new Solenoid(20, RobotMap.INTAKEDOWN);
	Solenoid intakeDown = new Solenoid(20, 0);
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    	//setDefaultCommand(new IntakeUpDownCommand());
    }
    public void down(boolean down) {
    	intakeDown.set(down);
    }
}

