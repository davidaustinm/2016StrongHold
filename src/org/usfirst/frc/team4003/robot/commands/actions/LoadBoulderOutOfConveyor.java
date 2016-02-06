package org.usfirst.frc.team4003.robot.commands.actions;

import org.usfirst.frc.team4003.robot.Robot;
import org.usfirst.frc.team4003.robot.commands.IntakeRaiseLower;
import org.usfirst.frc.team4003.robot.commands.RunIntakeAndConveyor;
import org.usfirst.frc.team4003.robot.commands.WaitForIntakeSwitch;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class LoadBoulderOutOfConveyor extends CommandGroup {
    
    public  LoadBoulderOutOfConveyor() {
    	requires(Robot.intakeUpDown);
    	setInterruptible(false);
    	IntakeRaiseLower raiseIntake = new IntakeRaiseLower(false);
    	addSequential(raiseIntake);
    	addSequential(new WaitForIntakeSwitch());
    	addSequential(new RunIntakeAndConveyor(false));
    }
}
