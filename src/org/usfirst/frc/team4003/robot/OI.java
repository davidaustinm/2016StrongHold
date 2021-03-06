package org.usfirst.frc.team4003.robot;

import edu.wpi.first.wpilibj.buttons.Button;
import org.usfirst.frc.team4003.robot.triggers.*;

import org.usfirst.frc.team4003.robot.commands.*;
import org.usfirst.frc.team4003.robot.io.*;
import org.usfirst.frc.team4003.robot.commands.actions.*;
import org.usfirst.frc.team4003.robot.commands.actions.FinishShooting;
/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	public Xbox driver = new Xbox(0);
	public Xbox operator = new Xbox(1);
	public XboxButton interrupt = new XboxButton(driver, XboxButton.BUTTONB);
	public XboxButton pidHold = new XboxButton(driver, XboxButton.BUTTONX);
	public XboxButton raiseIntake = new XboxButton(driver, XboxButton.BUTTONRB);
	public XboxButton lowerIntake = new XboxButton(driver, XboxButton.BUTTONLB);
	public XboxButton loadBoulder = new XboxButton(driver, XboxButton.BUTTONY);
	public XboxButton unloadBoulder = new XboxButton(driver, XboxButton.BUTTONA);
	
	public XboxButton cameraToggle = new XboxButton(operator,XboxButton.BUTTONA);
	public XboxButton homeTurret = new XboxButton(operator,XboxButton.BUTTONY);
	public XboxButton trackingOn = new XboxButton(operator,XboxButton.BUTTONX);
	//public XboxButton trackingOff = new XboxButton(operator,XboxButton.BUTTONB);
	public XboxButton resetTurret = new XboxButton(operator,XboxButton.BUTTONB);
	public XboxButton shooterOn = new XboxButton(operator,XboxButton.BUTTONRB);
	public XboxButton shooterOff = new XboxButton(operator,XboxButton.BUTTONLB);
	
	public XboxButton operatorLoadBoulder = new XboxButton(operator, XboxButton.DPADUP);
	public XboxButton operatorUnloadBoulder = new XboxButton(operator, XboxButton.DPADDOWN);
	
	
	public XboxButton tankDrive = new XboxButton(driver,XboxButton.DPADUP);
	public XboxButton arcadeDrive = new XboxButton(driver,XboxButton.DPADDOWN);
	public XboxButton slowDrive = new XboxButton(driver,XboxButton.DPADRIGHT);
	public XboxButton fastDrive = new XboxButton(driver,XboxButton.DPADLEFT);
	
	public XboxButton slowShooter = new XboxButton(operator, XboxButton.BUTTONSTART);
	public XboxButton slowTurret = new XboxButton(operator,XboxButton.DPADRIGHT);
	public XboxButton fastTurret = new XboxButton(operator,XboxButton.DPADLEFT);
	public XboxButton turretConveyorSwitchOverride = new XboxButton(operator, XboxButton.DPADNW);
	public XboxButton intakeSwitchOverride = new XboxButton(driver, XboxButton.DPADNW);
	

	public OI() {
		//shiftHigh.whenPressed(new ShifterCommand(false));
		//shiftLow.whenPressed(new ShifterCommand(true));
		
		//pidHold.whileHeld(new PIDHold());
		
		
		//raiseIntake.whileHeld(new IntakeUpDownCommand(false));
		//lowerIntake.whileHeld(new IntakeUpDownCommand(true));
		
		(new IntakeTrigger(true)).whileActive(new IntakeUpDownCommand(false));;
		(new IntakeTrigger(false)).whileActive(new IntakeUpDownCommand(true));
		
		//loadBoulder.whenPressed(new RunIntakeAndConveyor(true));
		//unloadBoulder.whenPressed(new RunIntakeAndConveyor(false));
		interrupt.whenPressed(new InterruptCommand());
		
		//operatorLoadBoulder.whenPressed(new RunIntakeAndConveyor(true));
		//operatorUnloadBoulder.whenPressed(new RunIntakeAndConveyor(false));
		
		/*
		if (Robot.NIVision) {
			cameraToggle.whenPressed(new CameraToggleNI());
			//trackingOn.whenPressed(new TrackingOnNI(true));
			//trackingOff.whenPressed(new TrackingOnNI(false));
		} else {
			cameraToggle.whenPressed(new CameraToggle());
		}
		*/
		
		//trackingOn.whenPressed(new TrackingOn(true));
		trackingOn.toggleWhenPressed(new TrackTarget());
		//trackingOff.whenPressed(new TrackingOn(false));
		
		
		//homeTurret.whenPressed(new HomeTurret());
		homeTurret.whenPressed(new FinishShooting());
		resetTurret.whenPressed(new HomeTurret(0));
		shooterOn.whenPressed(new ShooterCommand(true));
		shooterOff.whenPressed(new ShooterCommand(false));
		slowShooter.whenPressed(new ShooterCommand(true, true));
		
		tankDrive.whenPressed(new ChangeDriveMode(ChangeDriveMode.TANK));
		arcadeDrive.whenPressed(new ChangeDriveMode(ChangeDriveMode.ARCADE));
		slowDrive.whenPressed(new ChangeDriveSpeed(0.6));
		fastDrive.whenPressed(new ChangeDriveSpeed(0.8));
		
		slowTurret.whenPressed(new ChangeTurretSpeed(0.15));
		fastTurret.whenPressed(new ChangeTurretSpeed(1));
		
	}

    //// CREATING BUTTONS
    // One type of button is a joystick button which is any button on a joystick.
    // You create one by telling it which joystick it's on and which button
    // number it is.
    // Joystick stick = new Joystick(port);
    // Button button = new JoystickButton(stick, buttonNumber);
    
    // There are a few additional built in buttons you can use. Additionally,
    // by subclassing Button you can create custom triggers and bind those to
    // commands the same as any other Button.
    
    //// TRIGGERING COMMANDS WITH BUTTONS
    // Once you have a button, it's trivial to bind it to a button in one of
    // three ways:
    
    // Start the command when the button is pressed and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenPressed(new ExampleCommand());
    
    // Run the command while the button is being held down and interrupt it once
    // the button is released.
    // button.whileHeld(new ExampleCommand());
    
    // Start the command when the button is released  and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenReleased(new ExampleCommand());
}

