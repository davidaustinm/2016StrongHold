package org.usfirst.frc.team4003.robot.triggers;

import edu.wpi.first.wpilibj.buttons.Trigger;
import org.usfirst.frc.team4003.robot.*;

/**
 *
 */
public class IntakeTrigger extends Trigger {
	boolean raise;
	public IntakeTrigger(boolean raise) {
		this.raise = raise;
	}
    
    public boolean get() {
    	if (Robot.oi == null || Robot.oi.operator == null |
    			Robot.oi.raiseIntake == null || Robot.oi.lowerIntake == null) return false;
    	if (raise) {
    		return (Robot.oi.operator.getRightTrigger() > 0.2) || Robot.oi.raiseIntake.get();
    	}
        return (Robot.oi.operator.getLeftTrigger() > 0.2) || Robot.oi.lowerIntake.get();
    }
}
