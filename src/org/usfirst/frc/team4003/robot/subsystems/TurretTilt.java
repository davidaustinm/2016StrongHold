package org.usfirst.frc.team4003.robot.subsystems;

import org.usfirst.frc.team4003.robot.commands.TurretTiltCommand;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
   public class TurretTilt extends Subsystem {
    	 Talon tilt = new Talon(3);
    	 
    	 public void initDefaultCommand() {
    	        // Set the default command for a subsystem here.
    	        //setDefaultCommand(new MySpecialCommand());
    	    	setDefaultCommand(new TurretTiltCommand());
    }
    	 public void setPower(double power) {
    	    	tilt.set(power);
    }
}

