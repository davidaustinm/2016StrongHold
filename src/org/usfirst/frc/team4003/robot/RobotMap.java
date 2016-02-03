package org.usfirst.frc.team4003.robot;
/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	// TalonSRX, these are CAN ids
	public static final int LEFTFRONTDRIVEMOTOR = 1;
	public static final int LEFTBACKDRIVEMOTOR = 2;
	public static final int RIGHTFRONTDRIVEMOTOR = 3;
	public static final int RIGHTBACKDRIVEMOTOR = 4;
	public static final int SHOOTER0 = 5;
	public static final int SHOOTER1 = 6;
	
	// Talons, PWM
	public static final int INTAKEMOTOR0 = 0;
	public static final int INTAKEMOTOR1 = 1;
	public static final int TURRETSPIN = 2;
	public static final int TURRETTILT = 3;
	public static final int CONVEYOR0 = 4;
	public static final int CONVEYOR1 = 5;
	public static final int HANGING = 6;
	
	// Solenoids
	public static final int INTAKEDOWN = 0;
	public static final int SHIFTER = 1;
	
	// Digital Inputs
	public static final int LEFTDRIVEENCODERA = 0;
	public static final int LEFTDRIVEENCODERB = 1;
	public static final int RIGHTDRIVEENCODERA = 2;
	public static final int RIGHTDRIVEENCODERB = 3;
	public static final int SHOOTER0ENCODERA = 4;
	public static final int SHOOTER0ENCODERB = 5;
	public static final int SHOOTER1ENCODERA = 6;
	public static final int SHOOTER1ENCODERB = 7;
	public static final int TURRETENCODERA = 8;
	public static final int TURRETENCODERB = 9;
	public static final int INTAKESWITCH = 10;
	public static final int CONVEYORSWITCH = 11;
	public static final int TURRETRESETSWITCH = 12;
	public static final int COMPRESSOR = 13;
	
	// Analog inputs
	public static final int TILTPOT = 0;

}
