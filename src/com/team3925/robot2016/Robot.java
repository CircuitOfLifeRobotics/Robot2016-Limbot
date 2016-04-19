package com.team3925.robot2016;


import static com.team3925.robot2016.Constants.DO_LOG_AHRS_VALUES;
import static com.team3925.robot2016.Constants.DO_LOG_MOVEMENT_CONSTANTS;
import static com.team3925.robot2016.Constants.DO_LOG_PDP_VALUES;

import com.kauailabs.navx.frc.AHRS;
import com.team3925.robot2016.commands.LaunchBall;
import com.team3925.robot2016.commands.auto.GyroTurn;
import com.team3925.robot2016.subsystems.DriveTrain;
import com.team3925.robot2016.subsystems.Launcher;
import com.team3925.robot2016.util.DriveTrainSignal;
import com.team3925.robot2016.util.SmartdashBoardLoggable;
import com.team3925.robot2016.util.TimeoutAction;
import com.team3925.robot2016.util.hidhelpers.FlightStickHelper;
import com.team3925.robot2016.util.hidhelpers.ThrustmasterHelper;
import com.team3925.robot2016.util.hidhelpers.XboxHelper;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
	
	//Other
	public static AHRS navx = null;
	public static PowerDistributionPanel pdp;
	public static OI oi;
	public static CheesyDriveHelper cdh;
	private static TimeoutAction brakeBeforeMatchEnd = new TimeoutAction();
	
	//Commands
	private CommandGroup autoRoutine;
//	private ManualDrive manualDrive;
//	private ManualIntakeAssist manualIntakeAssist;
	private GyroTurn visionGyroTurn = null;
	private LaunchBall launchBall = null;
	
	//Variables
	public static double deltaTime = 0;
	private static double lastTimestamp = 0;
	private static double lastRotationStamp = 0;
	private static double deltaRotation = 0;
	private static double maxAccel = 0;
	private static double maxVel = 0;
	private static double maxRotationVel = 0;
	private static double maxRotationAccel = 0;
	
	//TODO: remove
	private String tempManualState;
	private boolean buttonWasDown;
	
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
		RobotMap.init();
		
		//Creating Subsystems and Related Processes
		driveTrain = new DriveTrain();
		launcher = new Launcher(RobotMap.launcherMotorArm, RobotMap.launcherMotorFar, RobotMap.launcherMotorNear, RobotMap.launcherPuncherSolenoid, RobotMap.launcherFwdLimitSwitch, RobotMap.launcherRevLimitSwitch);
		pdp = RobotMap.pdp;
		
		// OI must be constructed after subsystems. If the OI creates Commands
		//(which it very likely will), subsystems are not guaranteed to be
		// constructed yet. Thus, their requires() statements may grab null
		// pointers. Bad news. Don't move it.
		oi = new OI();
		XboxHelper.config(oi.shooterXbox);
		FlightStickHelper.config(oi.driverFlightstick);
		ThrustmasterHelper.config(oi.driverWheel);
		cdh = new CheesyDriveHelper(driveTrain);
		
		//Creating Commands
		visionGyroTurn = new GyroTurn(0);
		launchBall = new LaunchBall(60);
		
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
//		launcher.resetPID();
		
		//TODO: remove
		tempManualState = "intake";
		buttonWasDown = false;
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
		autoRoutine = oi.setAutonomous();
		
		driveTrain.setHighGear(false);
		reset();
		
		autoRoutine.start();
		launcher.init();
	}
	
	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
		
		updateSubsystems();
		logData();
	}

	public void teleopInit() {
		if (autoRoutine != null) { autoRoutine.cancel(); }
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		reset();
		
		brakeBeforeMatchEnd.config(135 - Constants.DRIVETRAIN_BREAK_MODE_ENABLE);
		
		// should be handled by default commands
//		manualDrive.start();
//		manualIntakeAssist.start();
		
		launcher.init();
	}

	/**
	 * This function is called periodically during operator control
	 */
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
		
		updateSubsystems();
		
//		if (oi.shooterXbox.getRawButton(XboxHelper.X)) {
//			launcherNew.startZeroCommand();
//		}
		if (oi.shooterXbox.getRawButton(XboxHelper.A) && !launchBall.isRunning()) {
			launchBall.start();
		}
		if (oi.getCommandCancel()) {
			launchBall.cancel();
		}
				
		
		// Driver Vision Control
		
//		if (oi.getVisionShoot_GyroTurnEnable()) {
//			if (!visionGyroTurn.isRunning()) {
//				if (Double.isNaN(launcher.getTurnAngle())) {
//					visionGyroTurn.setSetpointRelative(launcher.getTurnAngle());
//					visionGyroTurn.start();
//				} else {
//					DriverStation.reportWarning("Camera cannot detect object! Thou shall not runneth or beginneth a GyroDrive!", false);
//				}
//			}
//		} else {
//			if (visionGyroTurn.isRunning()) {
//				visionGyroTurn.cancel();
//			}
//		}


		if (brakeBeforeMatchEnd.isFinished()) {
			driveTrain.setBrakeMode(true);
		}
		
		logData();
	}
	
	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic() {
		LiveWindow.run();
		
		//TESTING MANUAL LAUNCHER TODO: remove
//		boolean buttonDown = Robot.oi.shooterXbox.getRawButton(XboxHelper.A);
//		if (buttonDown) {
//			if (!buttonWasDown) tempManualState = tempManualState.equals("intake") ? "wheels": tempManualState.equals("wheels") ? "shooting" : "intake";
//			buttonWasDown = true;
//		} else
//			buttonWasDown = false;
//		if (tempManualState.equals("intake")) {
//			launcherNew.setFlywheelFarSetpoint(0);
//			launcherNew.setFlywheelNearSetpoint(0);
//			launcherNew.setPuncherSolenoid(false);
//		}else if (tempManualState.equals("wheels")) {
//			launcherNew.setFlywheelFarSetpoint(-1);
//			launcherNew.setFlywheelNearSetpoint(-1);
//		}else if (tempManualState.equals("shooting")) {
//			launcherNew.setPuncherSolenoid(true);
//		}
//		System.out.println("state = "+tempManualState);
//		
//		launcherNew.update();
	}
	
	private void updateSubsystems() {
		driveTrain.update();
		launcher.update();
	}
	
	@Override
	public void logData() {
//		driveTrain.logData();
//		launcher.logData();
//		intakeAssist.logData();
		
		double now = Timer.getFPGATimestamp();
		deltaTime = now - lastTimestamp;
		lastTimestamp = now;
		
		

		
		
		putNumberSD("DeltaTime", deltaTime);
		
		
		if (DO_LOG_AHRS_VALUES) {
			if (navx != null) {
				logNavXData();
			}
		}
		
		if (DO_LOG_PDP_VALUES) {
			if (pdp != null) {
				putDataSD("PDP", pdp);
			}
		}
		
		if (DO_LOG_MOVEMENT_CONSTANTS) {
			if (pdp != null) {
				calcMovementConstants();
			}
		}
		
	}
	
	@Override
	public String getFormattedName() {
		return "Robot_";
	}
	
	private void calcMovementConstants() {
		double curVel = Math.toRadians(navx.getRate());
		deltaRotation = curVel - lastRotationStamp;
		lastRotationStamp = curVel;
		
		
		maxAccel = Math.max(maxAccel, navx.getWorldLinearAccelX());
		maxVel = Math.max(maxVel, navx.getVelocityX());
		
		maxRotationVel = Math.max(maxRotationVel, Math.toRadians(navx.getRate()));
		maxRotationAccel = Math.max(maxRotationAccel, deltaRotation / deltaTime );
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
	
}
