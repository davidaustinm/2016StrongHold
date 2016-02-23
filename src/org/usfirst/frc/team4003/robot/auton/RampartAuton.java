package org.usfirst.frc.team4003.robot.auton;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc.team4003.robot.commands.actions.*;
import org.usfirst.frc.team4003.robot.commands.*;

/**
 *
 */
public class RampartAuton extends CommandGroup {
    
    public  RampartAuton() {
    	addSequential(new DriveToPoint(50, 0, .5, 0, false, true));
    	double heading = -25;
    	addSequential(new RotateToHeading(heading, 0.4, 0.4));
    	addSequential(new DriveOnHeading(heading, 12, 0.4, false));
    	addSequential(new RotateToHeading(0, .4, .8));
    	addSequential(new DriveOverDefense(.5));
    	addSequential(new DriveToPoint(12, 0, 0.65, 0, true, false));
    	addSequential(new AlignAndShoot());
    }
}
