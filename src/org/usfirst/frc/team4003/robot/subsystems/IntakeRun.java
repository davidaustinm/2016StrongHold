package org.usfirst.frc.team4003.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.*;
import org.usfirst.frc.team4003.robot.commands.*;
public class IntakeRun extends Subsystem {
    Talon intake0 = new Talon(0);
    Talon intake1 = new Talon(1);
    
    public IntakeRun() {
    	intake1.setInverted(true);
    }
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    	setDefaultCommand(new IntakeRunCommand());
    }
    
    public void setPower(double power) {
    	intake0.set(power);
    	intake1.set(power);
    }
}

