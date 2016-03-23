package org.usfirst.frc.team4003.robot.auton;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc.team4003.robot.commands.actions.*;
import org.usfirst.frc.team4003.robot.commands.*;
import org.usfirst.frc.team4003.robot.io.*;
/**
 *
 */
public class ChevalDeFrisAuton extends CommandGroup {
    
    public  ChevalDeFrisAuton() {
    	addSequential(new DriveWhileLevel(.5));
    	addSequential(new IntakeRaiseLower(true));
    	addSequential(new WaitForTime(1500));
    	
    	CommandGroup group = new CommandGroup();
    	group.addSequential(new WaitForTime(2000)); //was 500
    	group.addSequential(new IntakeRaiseLower(false));
    	
    	//addParallel(group);
    	addParallel(new DriveOverChevalDeFris(0.6));
    	addSequential(group);
    	addSequential(new DriveToPoint(Sensors.getInstance().getFinalDrive(), 0, 0.65, 0, true, false));
    	addSequential(new AlignAndShoot());
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
