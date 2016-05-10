package org.usfirst.frc.team4003.robot.commands.actions;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc.team4003.robot.io.*;
import org.usfirst.frc.team4003.robot.commands.*;
import org.usfirst.frc.team4003.robot.*;
/**
 *
 */
public class AlignAndShoot extends CommandGroup {
    
    public  AlignAndShoot() {
    	Sensors sensors = Sensors.getInstance();
    	addSequential(new IntakeRaiseLower(true));
    	addSequential(new WaitForTime(500));
    	addSequential(new RunTurretTilt());
    	if (sensors.getPosition() == 5) {
    		//addSequential(new RunTurretSpinForTime(450, RobotMap.COUNTERCLOCKWISE));
    		addSequential(new RotateTurretByAngle(Math.atan(0.4), RobotMap.COUNTERCLOCKWISE)); // was 0.4 with 12"
    	}
    	if (sensors.getPosition() == 2) {
    		//addSequential(new RunTurretSpinForTime(450, RobotMap.CLOCKWISE));
    		addSequential(new RotateTurretByAngle(Math.atan(0.7), RobotMap.CLOCKWISE));
    	}
    	
    	// reenable this on 4/15
    	addSequential(new TrackingOn(true));
    	
    	/*
    	addSequential(new WaitUntilAligned());
    	addSequential(new TrackingOn(false));
    	addSequential(new AutoShoot());
    	*/
    	
    	//addSequential(new IntakeUpDownCommand(false));
    	
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
