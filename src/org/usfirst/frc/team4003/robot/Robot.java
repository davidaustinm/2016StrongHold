package org.usfirst.frc.team4003.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.*;

import org.usfirst.frc.team4003.robot.commands.actions.*;
import org.usfirst.frc.team4003.robot.io.*;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

import org.usfirst.frc.team4003.robot.subsystems.*;
import org.usfirst.frc.team4003.robot.commands.*;
import org.usfirst.frc.team4003.robot.auton.*;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.vision.*;
/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	public static DriveTrainSubsystem driveTrain;
	public static StrongHoldDrive strongHoldDrive;
	public static IntakeRun intakeRun;
	public static IntakeUpDown intakeUpDown;
	public static Shifter shifter;
	public static TurretSpin turretSpin;
	public static TurretTilt turretTilt;
	public static BoulderConveyor boulderConveyor;
	public static ShooterSubsystem shooter;
	public static Cameras cameras;
	public static OI oi;
	
	public static StrongHoldTank tankDrive;
	public static StrongHoldArcade arcadeDrive;
	
	public static boolean NIVision = true;
	
	Sensors sensors;

    Command autonomousCommand;
    SendableChooser chooser;
    public static TargetCamera targetCamera;
    public static DriverCamera driverCamera;
    public static DashboardMatProvider activeCamera = null;

    protected Thread targetThread;
    protected Thread driverThread;
    protected Thread cameraServerThread;
    
    // Making this volatile is good enough, no need to sync.  A single dirty read isn't worth the overhead of synch.
    protected static volatile boolean enableTargetTracking = false;
    static CamerasCommand cameraCommand;
    
    static {
    	 //System.load("/usr/local/lib/opencv310/libopencv_java310.so");
    	 if (SubsystemLoad.DRIVETRAIN) driveTrain = new DriveTrainSubsystem();
    	 if (SubsystemLoad.STRONGHOLDDRIVE) {
    		 strongHoldDrive = new StrongHoldDrive();
    		 tankDrive = new StrongHoldTank();
        	 arcadeDrive = new StrongHoldArcade();
        	 //arcadeDrive.start();
    	 }
    	 
    	 if (SubsystemLoad.INTAKERUN) intakeRun = new IntakeRun();
    	 if (SubsystemLoad.INTAKEUPDOWN) intakeUpDown = new IntakeUpDown();
    	 //if (SubsystemLoad.SHIFTER) shifter = new Shifter();
    	 if (SubsystemLoad.TURRETSPIN) turretSpin = new TurretSpin();
    	 if (SubsystemLoad.TURRETTILT) turretTilt = new TurretTilt();
    	 if (SubsystemLoad.BOULDERCONVEYOR) boulderConveyor = new BoulderConveyor(); 
    	 if (SubsystemLoad.SHOOTER) shooter = new ShooterSubsystem();    
    	 
    	 if (NIVision) cameras = new Cameras();
    	 
    	 
    }
    
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */

    
    public void robotInit() {
    	oi = new OI();
		sensors = Sensors.getInstance();
		
		if (NIVision == false) {
			targetCamera = new TargetCamera();
			targetThread = new Thread(targetCamera);
			targetThread.start();
        
			driverCamera = new DriverCamera();
			driverThread = new Thread(driverCamera);
			driverThread.start();
        
			activeCamera = targetCamera;
        
			cameraServerThread = new Thread(new TSCameraServer());
			cameraServerThread.start();
		}
		
		
		cameraCommand = new CamerasCommand();
	
        
        /*
        Compressor compressor = new Compressor(20);
        compressor.setClosedLoopControl(true);
        compressor.start();
        */
    }
    
    public static void enableTargetTracking() {
    	enableTargetTracking = true;
    }
    
    public static void disableTargetTracking() {
    	enableTargetTracking = false;
    }
    
    public static boolean isTargetTracking() {
    	return enableTargetTracking;
    }

	/**
     * This function is called once each time the robot enters Disabled mode.
     * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
     */
    public void disabledInit(){

    }
	
    boolean first = true;
    boolean cameraStarted = false;
    long startCameraTime;
	public void disabledPeriodic() {
		if (cameraStarted) {
			cameraCommand.exec();
		}
		if (first) {
			startCameraTime = System.currentTimeMillis() + 2000;
			first = false;
		} else {
			if (!cameraStarted && System.currentTimeMillis() > startCameraTime) {
				cameraCommand.init();
				cameras.restartCamera();
				cameraStarted = true;
			}
		}
		
		Scheduler.getInstance().run();
		
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select between different autonomous modes
	 * using the dashboard. The sendable chooser code works with the Java SmartDashboard. If you prefer the LabVIEW
	 * Dashboard, remove all of the chooser code and uncomment the getString code to get the auto name from the text box
	 * below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the chooser code above (like the commented example)
	 * or additional comparisons to the switch structure below with additional strings & commands.
	 */
	String delayString = "Delay (seconds)";
    public void autonomousInit() {
        //autonomousCommand = (Command) chooser.getSelected();
    	
    	TrackTarget.setAuton(true);
    	
    	
    	
    	Robot.turretTilt.resetPosition();
        Robot.turretSpin.resetPosition();
        //sensors.resetYaw();
        sensors.resetEncoders();
        sensors.setBaseLines();
        sensors.setYawOffset();
        
        int defense = sensors.getDefense();
        int position = sensors.getPosition();
       
        
        //position = 4;
        /*
        if (position == 2 || position == 3) {
        	TrackTarget.setSpinHint(TrackTarget.RIGHT);
        }
        if (position == 5) {
        	TrackTarget.setSpinHint(TrackTarget.LEFT);
        }
        */
        if (position == Sensors.SPYBOT) {
        	autonomousCommand = new SpybotAuton();
        } else {
        	autonomousCommand = new DefenseAuton(defense);
        }
        
        SmartDashboard.putNumber("Defense",  defense);
        SmartDashboard.putNumber("Position", position);
        /*
        autonomousCommand = new ChevalDeFrisAuton();
        autonomousCommand = new AlignAndShoot();
        autonomousCommand = new DefenseAuton(Sensors.RAMPART);
        autonomousCommand = new SpybotAuton();
        */
        /*
        int delay = (int) SmartDashboard.getNumber(delayString, 0);
        CommandGroup auton = new CommandGroup();
        if (delay > 0) auton.addSequential(new WaitForTime(delay * 1000));
        auton.addSequential(autonomousCommand);
        */
        //autonomousCommand = new ChevalDeFrisAuton();
       
        //autonomousCommand = new AlignAndShoot();
        //autonomousCommand = new ChevalDeFrisAuton();
        if (autonomousCommand != null) autonomousCommand.start();
        
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    	sensors.updatePosition();
    	sensors.displayOrientation();
        Scheduler.getInstance().run();
        cameraCommand.exec();
    }

    public void teleopInit() {
        if (autonomousCommand != null) autonomousCommand.cancel();
        TrackTarget.setAuton(false);
        arcadeDrive.start();
        SmartDashboard.putNumber(delayString, 0);
    }

    /**
     * This function is called periodically during operator control
     */
    
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        //sensors.displayOrientation();
        cameraCommand.exec();
        sensors.displayShooterSpeeds();
        /*
        SmartDashboard.putNumber("Position", sensors.getPosition());
        SmartDashboard.putNumber("Defense", sensors.getDefense());
        //sensors.displaySwitches();
        
        //sensors.displayShooterSpeeds();
        
        
        sensors.displaySwitches();
        sensors.displayAutonSwitches();
        sensors.displayTurretEncoders();
        sensors.displayDriveEncoders();
        */
        //sensors.displayDriveEncoders();
        //sensors.displayAutonSwitches();
        SmartDashboard.putNumber("Position", sensors.getPosition());
        SmartDashboard.putNumber("Defense", sensors.getDefense());
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
    }
}
