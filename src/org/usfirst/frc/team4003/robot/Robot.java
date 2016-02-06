
package org.usfirst.frc.team4003.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import org.usfirst.frc.team4003.robot.io.*;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

import org.usfirst.frc.team4003.robot.subsystems.*;

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
	public static OI oi;

    Command autonomousCommand;
    SendableChooser chooser;
    public static Camera camera;
    protected Thread targetThread;
    protected Thread cameraServerThread;
    
    static {
    	 System.load("/usr/local/lib/lib_OpenCV/java/libopencv_java2410.so");
    	 if (SubsystemLoad.DRIVETRAIN) driveTrain = new DriveTrainSubsystem();
    	 if (SubsystemLoad.STRONGHOLDDRIVE) strongHoldDrive = new StrongHoldDrive();
    	 if (SubsystemLoad.INTAKERUN) intakeRun = new IntakeRun();
    	 if (SubsystemLoad.INTAKEUPDOWN) intakeUpDown = new IntakeUpDown();
    	 if (SubsystemLoad.SHIFTER) shifter = new Shifter();
    	 if (SubsystemLoad.TURRETSPIN) turretSpin = new TurretSpin();
    	 if (SubsystemLoad.TURRETTILT) turretTilt = new TurretTilt();
    	 if (SubsystemLoad.BOULDERCONVEYOR) boulderConveyor = new BoulderConveyor();
    	 
    }
    
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */

    public void robotInit() {
		oi = new OI();
        chooser = new SendableChooser();
        //chooser.addDefault("Default Auto", new ExampleCommand());
        //chooser.addObject("My Auto", new MyAutoCommand());
        SmartDashboard.putData("Auto mode", chooser);
        camera = new Camera();
        targetThread = new Thread(camera);
        targetThread.start();
        cameraServerThread = new Thread(new TSCameraServer());
        cameraServerThread.start();
    }
	
	/**
     * This function is called once each time the robot enters Disabled mode.
     * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
     */
    public void disabledInit(){

    }
	
	public void disabledPeriodic() {
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
    public void autonomousInit() {
        autonomousCommand = (Command) chooser.getSelected();
        if (autonomousCommand != null) autonomousCommand.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
        Double targetAngle =Sensors.getInstance().getTargetAngle();
        if (targetAngle == null) return;
        SmartDashboard.putNumber("targetAngle", targetAngle.doubleValue());
        SmartDashboard.putNumber("yaw", Sensors.getInstance().getAngle());
    }

    public void teleopInit() {
		// This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to 
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (autonomousCommand != null) autonomousCommand.cancel();
        Sensors.getInstance().resetYaw();
    }

    /**
     * This function is called periodically during operator control
     */
    
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        SmartDashboard.putNumber("yaw", Sensors.getInstance().getYaw());
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
    }
}
