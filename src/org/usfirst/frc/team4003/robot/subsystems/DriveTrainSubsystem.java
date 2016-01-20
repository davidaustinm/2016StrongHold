package org.usfirst.frc.team4003.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.*;
import org.usfirst.frc.team4003.robot.commands.*;
/**
 *
 */
public class DriveTrainSubsystem extends Subsystem {
    RobotDrive chassis = new RobotDrive(0,1);
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    public void setPower(double left, double right) {
    	chassis.tankDrive(left, right);
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new DriveTrainCommand());
    }
}

