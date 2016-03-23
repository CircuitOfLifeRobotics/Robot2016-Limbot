package com.team3925.robot2016;


import static com.team3925.robot2016.Constants.DO_LOG_AHRS_VALUES;
import static com.team3925.robot2016.Constants.DO_LOG_PDP_VALUES;

import com.kauailabs.navx.frc.AHRS;
import com.team3925.robot2016.commands.Climb;
import com.team3925.robot2016.commands.GyroDrive;
import com.team3925.robot2016.commands.GyroTurn;
import com.team3925.robot2016.commands.LaunchBallHigh;
import com.team3925.robot2016.commands.ManualDrive;
import com.team3925.robot2016.commands.ManualPlexiArms;
import com.team3925.robot2016.commands.TrapzoidalMotionTest;
import com.team3925.robot2016.commands.defensecommands.CrossDefault;
import com.team3925.robot2016.subsystems.Climber;
import com.team3925.robot2016.subsystems.DriveTrain;
import com.team3925.robot2016.subsystems.IntakeAssist;
import com.team3925.robot2016.subsystems.Launcher;
import com.team3925.robot2016.subsystems.PlexiArms;
import com.team3925.robot2016.util.DriveTrainSignal;
import com.team3925.robot2016.util.SmartdashBoardLoggable;
import com.team3925.robot2016.util.TimeoutAction;
import com.team3925.robot2016.util.XboxHelper;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.USBCamera;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot implements SmartdashBoardLoggable {
	
	//Subsystems
	public static DriveTrain driveTrain;
	public static Launcher launcher;
	public static Climber candyCanes;
	public static PlexiArms plexiArms;
	public static IntakeAssist intakeAssist;
	
	//Other
//	public static Preferences prefs;
	public static AHRS navx = null;
	public static PowerDistributionPanel pdp;
	public static OI oi;
	public static CheesyDriveHelper cdh;
	public static USBCamera usbCamera;
	public static CameraServer cameraServer;
	private static TimeoutAction brakeBeforeMatchEnd = new TimeoutAction();
	
	//Commands
	CommandGroup autoRoutine;
	Command trapMotionTest;
	Command manualDrive;
	Command manualCandyCanes;
	Command manualArms;
	Command visionTest;
	Command launchBallHigh;
	Command gyroTurn;
	Command gyroDrive;
	
	
	//Variables
	public static double deltaTime = 0;
	private static double lastTimestamp = 0;
	private static double lastRotationStamp = 0;
	private static double deltaRotation = 0;
	private static double maxAccel = 0;
	private static double maxVel = 0;
	private static double maxRotationVel = 0;
	private static double maxRotationAccel = 0;
//	private double[] defaultVal  = new double[0];
	
	public Robot() {
		try {
			//Alternatively:  I2C.Port.kMXP, SerialPort.Port.kMXP or SerialPort.Port.kUSB
			navx = new AHRS(SPI.Port.kMXP);
		} catch (RuntimeException e) {
			DriverStation.reportError("There was an error instantiating the NavxMXP!\n" + e.getMessage(), true);
		}
	}

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit() {
		Constants.initLauncherIntakeTable();
		RobotMap.init();
		
		//Creating Subsystems and Related Processes
		driveTrain = new DriveTrain();
		launcher = new Launcher();
		candyCanes = new Climber();
		plexiArms = new PlexiArms();
		intakeAssist = new IntakeAssist();
//		prefs = Preferences.getInstance();
		pdp = RobotMap.pdp;
		
		// OI must be constructed after subsystems. If the OI creates Commands
		//(which it very likely will), subsystems are not guaranteed to be
		// constructed yet. Thus, their requires() statements may grab null
		// pointers. Bad news. Don't move it.
		oi = new OI();
		XboxHelper.init();
		cdh = new CheesyDriveHelper(driveTrain);
		
		usbCamera = new USBCamera("cam0");
		usbCamera.setBrightness(0);
		usbCamera.updateSettings();
		cameraServer = CameraServer.getInstance();
		cameraServer.startAutomaticCapture(usbCamera);
		
		//Creating Commands
		manualArms = new ManualPlexiArms();
		manualDrive = new ManualDrive();
		trapMotionTest = new TrapzoidalMotionTest();
		manualCandyCanes = new Climb();
		launchBallHigh = new LaunchBallHigh();
		gyroTurn = new GyroTurn(45);
		gyroDrive = new GyroDrive();
		
		
		reset();
	}
	
	/**
	 * Resets lastTimestamp, the IMU, max unit testers, encoders, and launcherPID
	 */
	private void reset() {
		driveTrain.setBrakeMode(false);
		driveTrain.resetEncoders();
		lastTimestamp = Timer.getFPGATimestamp();
		lastRotationStamp = navx.getRate();
		navx.reset();
		navx.resetDisplacement();
		maxAccel = 0;
		maxVel = 0;
		maxRotationVel = 0;
		maxRotationAccel = 0; 
//		launcherPID.reset();
	}
	
	/**
	 * This function is called when the disabled button is hit.
	 * You can use it to reset subsystems before shutting down.
	 */
	public void disabledInit(){
		if (autoRoutine != null) { autoRoutine.cancel(); }
		driveTrain.setMotorSpeeds(DriveTrainSignal.NEUTRAL);
//		launcher.setIntakeSpeeds(0);
		
		reset();
	}
	
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
		driveTrain.setMotorSpeeds(DriveTrainSignal.NEUTRAL);
//		launcher.setIntakeSpeeds(0);
	}
	
	public void autonomousInit() {
//		autoRoutine = oi.setAutonomous();
		
		driveTrain.setHighGear(false);
		reset();
		
//		launchBallTest.start();
//		launchBallHigh.start();
//		gyroTurn.start();
//		gyroDrive.start();
		
		launcher.init();
		
//		autoRoutine.start();
		Command tmpAuto = new GyroDrive(0, true, 2);
		tmpAuto.start();
	}
	
	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
		logData();
		
		launcher.update();
		intakeAssist.setArmPosition(false);
	}

	public void teleopInit() {
		if (autoRoutine != null) { autoRoutine.cancel(); }
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		reset();
		
		candyCanes.startTimeOut();
		brakeBeforeMatchEnd.config(135 - Constants.DRIVETRAIN_BREAK_MODE_ENABLE);
		intakeAssist.setArmEncPos(0);
		
		
//		visionTest.start();
		manualDrive.start();
		manualArms.start();
		
		launcher.init();
	}

	/**
	 * This function is called periodically during operator control
	 */
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
//		if (XboxHelper.getShooterButton(XboxHelper.STICK_RIGHT)) {
//			Object selected = oi.throwBallTesting.getSelected();
//			if (selected instanceof Command) {
//				Command c = (Command) selected;
//				c.start();
//			} else {
//				DriverStation.reportError("Tried to start a throwballtest but it could not be cast to a command!", true);
//			}
//		}
		
		launcher.update();
		
		// too lazy to make a new command just for this
		intakeAssist.setArmPosition(oi.getIntakeAssist_ArmValue());
		
		if (brakeBeforeMatchEnd.isFinished()) {
			driveTrain.setBrakeMode(true);
			DriverStation.reportError("BrakeModeEnabled!", false);
		}
		
		logData();
	}
	
	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic() {
		LiveWindow.run();
	}
	
	@Override
	public void logData() {
//		driveTrain.logData();
		launcher.logData();
		plexiArms.logData();
//		candyCanes.logData();
		intakeAssist.logData();
		
		double now = Timer.getFPGATimestamp();
		deltaTime = now - lastTimestamp;
		lastTimestamp = now;
		
		
		double curVel = Math.toRadians(navx.getRate());
		deltaRotation = curVel - lastRotationStamp;
		lastRotationStamp = curVel;
		
		
		maxAccel = Math.max(maxAccel, navx.getWorldLinearAccelX());
		maxVel = Math.max(maxVel, navx.getVelocityX());
		
		maxRotationVel = Math.max(maxRotationVel, Math.toRadians(navx.getRate()));
		maxRotationAccel = Math.max(maxRotationAccel, deltaRotation / deltaTime );
		
		
//		putNumberSD("MaxAcceleration", maxAccel);
//		putNumberSD("MaxVelocity", maxVel);
//		
//		putNumberSD("MaxRotationVelocity", maxRotationVel);
//		putNumberSD("MaxRotationAccel", maxRotationAccel);
//		
//		putNumberSD("CurrentTime", Timer.getFPGATimestamp());
		putNumberSD("DeltaTime", deltaTime);
//		
//		putDataSD("Autonomous Chooser", autoChooser);
//		putNamedDataSD(Scheduler.getInstance());
		
    	SmartDashboard.putData("Autonomous Routing Chooser", oi.autoChooser);
//    	SmartDashboard.putData("Throw Ball Testing", oi.throwBallTesting);
    	SmartDashboard.putData("Autonomous Position Chooser", oi.positionChooser);
    	
		if (DO_LOG_AHRS_VALUES) {
			if (navx != null) {
				logNavXData();
			} else {
				putStringSD("NavXLogger", "Cannot log NavX values while null!");
			}
		}
		
		if (DO_LOG_PDP_VALUES) {
			if (pdp != null) {
				logPDPData();
			} else {
				putStringSD("PDPLogger", "Cannot log PDP values while null!");
			}
		}
		
	}

	@Override
	public String getFormattedName() {
		return "Robot_";
	}
	
	private void logNavXData() {
		//	Copied from navXMXP Data Monitor Project

		/* Display 6-axis Processed Angle Data                                      */
		SmartDashboard.putBoolean(  "IMU_Connected",        navx.isConnected());
		SmartDashboard.putBoolean(  "IMU_IsCalibrating",    navx.isCalibrating());
		SmartDashboard.putNumber(   "IMU_Yaw",              navx.getYaw());
		SmartDashboard.putNumber(   "IMU_Pitch",            navx.getPitch());
		SmartDashboard.putNumber(   "IMU_Roll",             navx.getRoll());

		/* Display tilt-corrected, Magnetometer-based heading (requires             */
		/* magnetometer calibration to be useful)                                   */

		SmartDashboard.putNumber(   "IMU_CompassHeading",   navx.getCompassHeading());

		/* Display 9-axis Heading (requires magnetometer calibration to be useful)  */
		SmartDashboard.putNumber(   "IMU_FusedHeading",     navx.getFusedHeading());

		/* These functions are compatible w/the WPI Gyro Class, providing a simple  */
		/* path for upgrading from the Kit-of-Parts gyro to the navx MXP            */

		SmartDashboard.putNumber(   "IMU_TotalYaw",         navx.getAngle());
		SmartDashboard.putNumber(   "IMU_YawRateDPS",       navx.getRate());

		/* Display Processed Acceleration Data (Linear Acceleration, Motion Detect) */

		SmartDashboard.putNumber(   "IMU_Accel_X",          navx.getWorldLinearAccelX());
		SmartDashboard.putNumber(   "IMU_Accel_Y",          navx.getWorldLinearAccelY());
		SmartDashboard.putBoolean(  "IMU_IsMoving",         navx.isMoving());
		SmartDashboard.putBoolean(  "IMU_IsRotating",       navx.isRotating());

		/* Display estimates of velocity/displacement.  Note that these values are  */
		/* not expected to be accurate enough for estimating robot position on a    */
		/* FIRST FRC Robotics Field, due to accelerometer noise and the compounding */
		/* of these errors due to single (velocity) integration and especially      */
		/* double (displacement) integration.                                       */

		SmartDashboard.putNumber(   "IMU_Velocity_X",           navx.getVelocityX());
		SmartDashboard.putNumber(   "IMU_Velocity_Y",           navx.getVelocityY());
		SmartDashboard.putNumber(   "IMU_Displacement_X",       navx.getDisplacementX());
		SmartDashboard.putNumber(   "IMU_Displacement_Y",       navx.getDisplacementY());

		/* Display Raw Gyro/Accelerometer/Magnetometer Values                       */
		/* NOTE:  These values are not normally necessary, but are made available   */
		/* for advanced users.  Before using this data, please consider whether     */
		/* the processed data (see above) will suit your needs.                     */

		SmartDashboard.putNumber(   "IMU_RawGyro_X",            navx.getRawGyroX());
		SmartDashboard.putNumber(   "IMU_RawGyro_Y",            navx.getRawGyroY());
		SmartDashboard.putNumber(   "IMU_RawGyro_Z",            navx.getRawGyroZ());
		SmartDashboard.putNumber(   "IMU_RawAccel_X",           navx.getRawAccelX());
		SmartDashboard.putNumber(   "IMU_RawAccel_Y",           navx.getRawAccelY());
		SmartDashboard.putNumber(   "IMU_RawAccel_Z",           navx.getRawAccelZ());
		SmartDashboard.putNumber(   "IMU_RawMag_X",             navx.getRawMagX());
		SmartDashboard.putNumber(   "IMU_RawMag_Y",             navx.getRawMagY());
		SmartDashboard.putNumber(   "IMU_RawMag_Z",             navx.getRawMagZ());
		SmartDashboard.putNumber(   "IMU_Temp_C",           navx.getTempC());

		/* Omnimount Yaw Axis Information                                           */
		/* For more info, see http://navx-mxp.kauailabs.com/installation/omnimount  */
		AHRS.BoardYawAxis yaw_axis = navx.getBoardYawAxis();
		SmartDashboard.putString(   "IMU_YawAxisDirection",     yaw_axis.up ? "Up" : "Down" );
		SmartDashboard.putNumber(   "IMU_YawAxis",              yaw_axis.board_axis.getValue() );

		/* Sensor Board Information                                                 */
		SmartDashboard.putString(   "IMU_FirmwareVersion",      navx.getFirmwareVersion());

		/* Quaternion Data                                                          */
		/* Quaternions are fascinating, and are the most compact representation of  */
		/* orientation data.  All of the Yaw, Pitch and Roll Values can be derived  */
		/* from the Quaternions.  If interested in motion processing, knowledge of  */
		/* Quaternions is highly recommended.                                       */
		SmartDashboard.putNumber(   "IMU_QuaternionW",          navx.getQuaternionW());
		SmartDashboard.putNumber(   "IMU_QuaternionX",          navx.getQuaternionX());
		SmartDashboard.putNumber(   "IMU_QuaternionY",          navx.getQuaternionY());
		SmartDashboard.putNumber(   "IMU_QuaternionZ",          navx.getQuaternionZ());

		/* Connectivity Debugging Support                                           */
		SmartDashboard.putNumber(   "IMU_Byte_Count",       navx.getByteCount());
		SmartDashboard.putNumber(   "IMU_Update_Count",     navx.getUpdateCount());
	}
	
	private void logPDPData() {
		SmartDashboard.putData("PDP", pdp);
		/*
		SmartDashboard.putNumber("PDP_Temperature", pdp.getTemperature());
//		SmartDashboard.putNumber("PDP_Total_Current", pdp.getTotalCurrent());
		SmartDashboard.putNumber("PDP_Total_Energy", pdp.getTotalEnergy()); // in milliJoules
		SmartDashboard.putNumber("PDP_Total_Power", pdp.getTotalPower());
//		SmartDashboard.putNumber("PDP_Voltage", pdp.getVoltage()); */
	}
	
}
