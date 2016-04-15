package org.usfirst.frc.team4003.robot.commands.actions;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc.team4003.robot.commands.*;

/**
 *
 */
public class AutoShoot extends CommandGroup {
    public  AutoShoot() {
        addSequential(new ShooterCommand(true));
        addSequential(new WaitForTime(2300));
        addSequential(new RunConveyorForTime(true, 3000));
        addSequential(new ShooterCommand(false));
    }
}
