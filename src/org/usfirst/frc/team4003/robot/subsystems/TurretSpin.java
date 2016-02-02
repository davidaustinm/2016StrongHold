package org.usfirst.frc.team4003.robot.subsystems;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.usfirst.frc.team4003.robot.commands.*;

/**
 *
 */
public class TurretSpin extends Subsystem {
    Talon spin = new Talon(2);
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    	setDefaultCommand(new TurretSpinCommand());
    }
    public void setPower(double power) {
    	spin.set(power);
    }
}

