package org.usfirst.frc.team4003.robot;
/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	// TalonSRX, these are CAN ids
	public static final int LEFTFRONTDRIVEMOTOR = 13;
	public static final int LEFTBACKDRIVEMOTOR = 16;
	public static final int RIGHTFRONTDRIVEMOTOR = 2;
	public static final int RIGHTBACKDRIVEMOTOR = 14;
	public static final int SHOOTER0 = 15; // top shooter
	public static final int SHOOTER1 = 3; // bottom shooter
	public static final int TURRETSPIN = 11;
	public static final int TURRETTILT = 12;
	
	// Talons, PWM
	public static final int INTAKEMOTOR = 2;
	public static final int CONVEYOR = 1;
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
	
	public static final int POSITIONAUTON0 = 14;
	public static final int POSITIONAUTON1 = 15;
	public static final int POSITIONAUTON2 = 16;
	public static final int DEFENSEAUTON0 = 17;
	public static final int DEFENSEAUTON1 = 18;
	public static final int DEFENSEAUTON2 = 19;
	
	// Analog inputs
	public static final int TILTPOT = 0;
	
	// Cameras
	public static final int TARGET_CAMERA = 0; // This one is stuck in 1280x720 mode
	public static final int DRIVER_CAMERA = 1; // This one is stuck in 160x120 mode
	
	public static final int TARGET_CAMERA_W = 320;
	public static final int TARGET_CAMERA_H = 180;

	public static final int DRIVER_CAMERA_W = 320;
	public static final int DRIVER_CAMERA_H = 180;
	
	public static final double TARGET_TAPE_HEIGHT = 12.0; // In inches
	public static final double TARGET_TAPE_WIDTH = 20.0; // In inches
	
	

}
