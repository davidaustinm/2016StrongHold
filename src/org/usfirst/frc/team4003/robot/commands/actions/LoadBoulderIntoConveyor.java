package org.usfirst.frc.team4003.robot.commands.actions;

import edu.wpi.first.wpilibj.command.CommandGroup;

import org.usfirst.frc.team4003.robot.Robot;
import org.usfirst.frc.team4003.robot.commands.*;
/**
 *
 */ 
public class LoadBoulderIntoConveyor extends CommandGroup {
    
    public  LoadBoulderIntoConveyor() {
    	requires(Robot.intakeUpDown);
    	setInterruptible(false);
    	IntakeRaiseLower raiseIntake = new IntakeRaiseLower(false);
    	addSequential(raiseIntake);
    	addSequential(new WaitForIntakeSwitch());
    	addSequential(new RunIntakeAndConveyor(true));
    }
}
