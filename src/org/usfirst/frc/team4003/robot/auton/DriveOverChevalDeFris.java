package org.usfirst.frc.team4003.robot.auton;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveOverChevalDeFris extends DriveOverDefense {
	long slowTime;
    public DriveOverChevalDeFris(double speed) {
		super(speed);
		// TODO Auto-generated constructor stub
	}

	// Called just before this Command runs the first time
    protected void initialize() {
    	slowTime = System.currentTimeMillis() + 1000;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if(System.currentTimeMillis() > slowTime) speed = 0.3;
    	super.execute();
    }

    // Make this return true when this Command no longer needs to run execute()
}
