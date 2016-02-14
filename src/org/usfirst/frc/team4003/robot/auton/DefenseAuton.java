package org.usfirst.frc.team4003.robot.auton;

import org.usfirst.frc.team4003.robot.commands.*;
import org.usfirst.frc.team4003.robot.io.*;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class DefenseAuton extends CommandGroup {
    public DefenseAuton(int defense) {
    	if (defense == Sensors.RAMPART) {
    		addSequential(new RampartAuton()); 
    		return;
    	}
    	addSequential(new DriveToPoint(44, 0, 0.65, 0, false, true));
    	switch(defense) {
    	case Sensors.ROCKWALL:
    		addSequential(new DriveOverDefense(0.5));
    		break;
    	case Sensors.MOAT:
    		addSequential(new DriveOverDefense(.75));
    		break;
    	case Sensors.RAMPART:
    		addSequential(new RotateToHeading(-25, .5, 0));
    		addSequential(new DriveOverDefense(.6));
    	}
    	addSequential(new DriveToPoint(12, 0, 0.65, 0, true, false));
        // Add Commands here:
        // e.g. addSequential(new Command1());
        //      addSequential(new Command2());
        // these will run in order.

        // To run multiple commands at the same time,
        // use addParallel()
        // e.g. addParallel(new Command1());
        //      addSequential(new Command2());
        // Command1 and Command2 will run in parallel.

        // A command group will require all of the subsystems that each member
        // would require.
        // e.g. if Command1 requires chassis, and Command2 requires arm,
        // a CommandGroup containing them would require both the chassis and the
        // arm.
    }
}
