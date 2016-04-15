package org.usfirst.frc.team4003.robot.commands;

import org.usfirst.frc.team4003.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class CameraToggleNI extends Command {
	final int STOP = 0;
	final int RESTART = 1;
	final int FINISHED = 2;
	int state = STOP;
	long timeOut;
    public CameraToggleNI() {
        // Use requires() here to declare subsystem dependencies
        //requires(Robot.cameras);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	state = STOP;
    }

    // Called repeatedly when this Command is scheduled to run
    final int SLEEP = 250;
    protected void execute() {
    	switch(state) {
    	case STOP:
    		Robot.cameras.stopCamera();
    		Robot.cameras.toggle();
    		timeOut = System.currentTimeMillis() + SLEEP;
    		state = RESTART;
    		break;
    	case RESTART:
    		if (System.currentTimeMillis() > timeOut) {
    			Robot.cameras.restartCamera();
    			timeOut = System.currentTimeMillis() + SLEEP;
    			state = FINISHED;
    			break;
    		}
    		
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return state == FINISHED;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }

}
