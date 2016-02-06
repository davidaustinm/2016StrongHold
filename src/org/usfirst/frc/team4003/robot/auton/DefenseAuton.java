package org.usfirst.frc.team4003.robot.auton;

import org.usfirst.frc.team4003.robot.commands.*;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class DefenseAuton extends CommandGroup {
    public static final int ROCKWALL = 0;
    public static final int MOAT = 1;
    public static final int ROUGHTERRAIN = 2;
    public static final int RAMPART = 3;
    public DefenseAuton(int defense) {
    	addSequential(new DriveToPoint(44, 0, 0.5, 0));
    	switch(defense) {
    	case ROCKWALL:
    		addSequential(new DriveOverDefense(0.5));
    	}
    	addSequential(new DriveToPoint(12, 0, 0.5, 0, true));
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
