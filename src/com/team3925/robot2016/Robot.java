package com.team3925.robot2016;

//import static com.team3925.robot2016.Constants.AUTO_START_LOCATION;
import static com.team3925.robot2016.Constants.DO_LOG_AHRS_VALUES;
import static com.team3925.robot2016.Constants.DO_LOG_GRIP_VALUES;
import static com.team3925.robot2016.Constants.DO_LOG_PDP_VALUES;

import com.kauailabs.navx.frc.AHRS;
import com.team3925.robot2016.commands.AutoRoutineCenter;
import com.team3925.robot2016.commands.AutoRoutineCourtyard;
import com.team3925.robot2016.commands.AutoRoutineDoNothing;
import com.team3925.robot2016.commands.CandyCane;
import com.team3925.robot2016.commands.CollectBall;
import com.team3925.robot2016.commands.JankyLauncher;
import com.team3925.robot2016.commands.LaunchBallHigh;
import com.team3925.robot2016.commands.LaunchBallLow;
import com.team3925.robot2016.commands.LauncherPID;
import com.team3925.robot2016.commands.ManualDrive;
import com.team3925.robot2016.commands.TrapzoidalMotionTest;
import com.team3925.robot2016.subsystems.Arms;
import com.team3925.robot2016.subsystems.DriveTrain;
import com.team3925.robot2016.subsystems.Launcher;
import com.team3925.robot2016.util.DriveTrainSignal;
import com.team3925.robot2016.util.SmartdashBoardLoggable;
import com.team3925.robot2016.util.TimeoutAction;
import com.team3925.robot2016.util.XboxHelper;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot implements SmartdashBoardLoggable {
	
	//Other
	public static AHRS navx = null;
	public static PowerDistributionPanel pdp;
	public static NetworkTable table;
	public static OI oi;
	public static Preferences prefs;
	private TimeoutAction candyCaneWait = new TimeoutAction();
	
	//Subsystems
	public static DriveTrain driveTrain;
	public static Launcher launcher;
	public static Arms arms;
	
	//Commands
	public static Command launcherPID;
	Command autoCommandGroup;
	Command collectBall;
//	Command launchBall;
	Command manualDrive;
	Command trapMotionTest;
	Command jankyLauncher;
	Command candyCaneRun;
	SendableChooser autoChooser;
	
	//Variables
	public static double deltaTime = 0;
	private static double lastTimestamp = 0;
	private static double lastRotationStamp = 0;
	private static double deltaRotation = 0;
	private static double maxAccel = 0;
	private static double maxVel = 0;
	private static double maxRotationVel = 0;
	private static double maxRotationAccel = 0;
	private double[] defaultVal;
	
	private static TimeoutAction autoWait = new TimeoutAction();
	

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
		launcher = new Launcher();
		arms = new Arms();
		Preferences.getInstance();
		pdp = RobotMap.pdp;
		
		table = NetworkTable.getTable("GRIP/contourReport");
		try {
			new ProcessBuilder("/home/lvuser/grip").inheritIO().start();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		// OI must be constructed after subsystems. If the OI creates Commands
		//(which it very likely will), subsystems are not guaranteed to be
		// constructed yet. Thus, their requires() statements may grab null
		// pointers. Bad news. Don't move it.
		oi = new OI();
		XboxHelper.init();
		reset();
		
//		//	Switch for current start position.
//		switch (AUTO_START_LOCATION) {
//		case CENTER:
//			autoCommandGroup = new AutoRoutineCenter();
//			break;
//		case COURTYARD:
//			autoCommandGroup = new AutoRoutineCourtyard();
//			break;
//		case DO_NOTHING:
//			autoCommandGroup = new AutoRoutineDoNothing();
//			break;
//
//		default:
//			DriverStation.reportError("Was unable to select Auto Routine! \nDefault routine selected.", false);
//			autoCommandGroup = new AutoRoutineDoNothing();
//			break;
//		}
		//Creating Autonomous
		autoChooser = new SendableChooser();
		autoChooser.addDefault("Nothing Auto", new AutoRoutineDoNothing());
		autoChooser.addObject("Center Auto", new AutoRoutineCenter());
		autoChooser.addObject("Courtyard Auto", new AutoRoutineCourtyard());
		
		//Creating Commands
		collectBall = new CollectBall();
//		launchBall = new LaunchBallHigh();
		manualDrive = new ManualDrive();
		trapMotionTest = new TrapzoidalMotionTest();
		jankyLauncher = new JankyLauncher();
		launcherPID = new LauncherPID(Constants.LAUNCHER_AIM_KP_UP, Constants.LAUNCHER_AIM_KI_UP, Constants.LAUNCHER_AIM_KD_UP, 0d);
		candyCaneRun = new CandyCane();
	}
	
	/**
	 * Resets lastTimestamp, the IMU, max unit testers and encoders
	 */
	private void reset() {
		driveTrain.resetEncoders();
		lastTimestamp = Timer.getFPGATimestamp();
		lastRotationStamp = navx.getRate();
		navx.reset();
		navx.resetDisplacement();
		maxAccel = 0;
		maxVel = 0;
		maxRotationVel = 0;
		maxRotationAccel = 0;
	}
	
	/**
	 * This function is called when the disabled button is hit.
	 * You can use it to reset subsystems before shutting down.
	 */
	public void disabledInit(){
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
		// schedule the autonomous command (example)
		if (autoCommandGroup != null) autoCommandGroup.start();
		
		driveTrain.setHighGear(false);
		reset();
		autoWait.config(1);
		
//		launchBall.start();
	}
	
	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
		logData();
		
//		if (autoWait.isFinished()) {
//			trapMotionTest.start();
//		}
		
	}

	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (autoCommandGroup != null) autoCommandGroup.cancel();
		
		launcherPID.start();
//		jankyLauncher.start();
		
		reset();
		
		manualDrive.start();
		System.out.println("Robot has init! (Said through System.out.println)");
		driveTrain.setPIDEnabled(false);
		
		candyCaneWait.config(55d);
	}

	/**
	 * This function is called periodically during operator control
	 */
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
		
		if (XboxHelper.getShooterButton(XboxHelper.START)) {
			if (candyCaneWait.isFinished() && !candyCaneRun.isRunning()) {
				candyCaneRun.start();
			} else {
				XboxHelper.setShooterRumble(1f);
			}
		}else {
			XboxHelper.setShooterRumble(0);
		}
		
		//TODO: move into command
		arms.setArm(XboxHelper.getDriverAxis(2)>0.5);
		logData();
	}
	
	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic() {
		LiveWindow.run();
		
		launcher.liveWindow();
	}
	
	@Override
	public void logData() {
		driveTrain.logData();
		launcher.logData();
		
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
		
		
		putNumberSD("MaxAcceleration", maxAccel);
		putNumberSD("MaxVelocity", maxVel);
		
		putNumberSD("MaxRotationVelocity", maxRotationVel);
		putNumberSD("MaxRotationAccel", maxRotationAccel);
		
		putNumberSD("CurrentTime", Timer.getFPGATimestamp());
		putNumberSD("DeltaTime", deltaTime);
		
		putDataSD("Autonomous Chooser", autoChooser);
		putNamedDataSD(Scheduler.getInstance());
		
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
		
		if (DO_LOG_GRIP_VALUES) {
			if (table.isConnected()) {
				logGRIPData();
			}else {
				putStringSD("GRIPLogger", "Cannot log GRIP values while unconnected!");
			}
		}
	}

	@Override
	public String getFormattedName() {
		return "Robot_";
	}
	
	private void logGRIPData() {
		double[] area = table.getNumberArray("area", defaultVal);
		double[] width = table.getNumberArray("width", defaultVal);
		double[] height = table.getNumberArray("height", defaultVal);
		double[] solidity = table.getNumberArray("solidity", defaultVal);
		double[] centerX = table.getNumberArray("centerX", defaultVal);
		double[] centerY = table.getNumberArray("centerY", defaultVal);
		int areasCount = area.length;
		putNumberSD("NetworkTables_Count", areasCount);
		for (int i=0; i<1; i++) {
			putNumberSD("NetworkTables_Area_"+i, area[i]);
			putNumberSD("NetworkTables_Width_"+i, width[i]);
			putNumberSD("NetworkTables_Height_"+i, height[i]);
			putNumberSD("NetworkTables_Solidity_"+i, solidity[i]);
			putNumberSD("NetworkTables_CenterX_"+i, centerX[i]);
			putNumberSD("NetworkTables_CenterY_"+i, centerY[i]);
		}
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

		SmartDashboard.putNumber(   "Velocity_X",           navx.getVelocityX());
		SmartDashboard.putNumber(   "Velocity_Y",           navx.getVelocityY());
		SmartDashboard.putNumber(   "Displacement_X",       navx.getDisplacementX());
		SmartDashboard.putNumber(   "Displacement_Y",       navx.getDisplacementY());

		/* Display Raw Gyro/Accelerometer/Magnetometer Values                       */
		/* NOTE:  These values are not normally necessary, but are made available   */
		/* for advanced users.  Before using this data, please consider whether     */
		/* the processed data (see above) will suit your needs.                     */

		SmartDashboard.putNumber(   "RawGyro_X",            navx.getRawGyroX());
		SmartDashboard.putNumber(   "RawGyro_Y",            navx.getRawGyroY());
		SmartDashboard.putNumber(   "RawGyro_Z",            navx.getRawGyroZ());
		SmartDashboard.putNumber(   "RawAccel_X",           navx.getRawAccelX());
		SmartDashboard.putNumber(   "RawAccel_Y",           navx.getRawAccelY());
		SmartDashboard.putNumber(   "RawAccel_Z",           navx.getRawAccelZ());
		SmartDashboard.putNumber(   "RawMag_X",             navx.getRawMagX());
		SmartDashboard.putNumber(   "RawMag_Y",             navx.getRawMagY());
		SmartDashboard.putNumber(   "RawMag_Z",             navx.getRawMagZ());
		SmartDashboard.putNumber(   "IMU_Temp_C",           navx.getTempC());

		/* Omnimount Yaw Axis Information                                           */
		/* For more info, see http://navx-mxp.kauailabs.com/installation/omnimount  */
		AHRS.BoardYawAxis yaw_axis = navx.getBoardYawAxis();
		SmartDashboard.putString(   "YawAxisDirection",     yaw_axis.up ? "Up" : "Down" );
		SmartDashboard.putNumber(   "YawAxis",              yaw_axis.board_axis.getValue() );

		/* Sensor Board Information                                                 */
		SmartDashboard.putString(   "FirmwareVersion",      navx.getFirmwareVersion());

		/* Quaternion Data                                                          */
		/* Quaternions are fascinating, and are the most compact representation of  */
		/* orientation data.  All of the Yaw, Pitch and Roll Values can be derived  */
		/* from the Quaternions.  If interested in motion processing, knowledge of  */
		/* Quaternions is highly recommended.                                       */
		SmartDashboard.putNumber(   "QuaternionW",          navx.getQuaternionW());
		SmartDashboard.putNumber(   "QuaternionX",          navx.getQuaternionX());
		SmartDashboard.putNumber(   "QuaternionY",          navx.getQuaternionY());
		SmartDashboard.putNumber(   "QuaternionZ",          navx.getQuaternionZ());

		/* Connectivity Debugging Support                                           */
		SmartDashboard.putNumber(   "IMU_Byte_Count",       navx.getByteCount());
		SmartDashboard.putNumber(   "IMU_Update_Count",     navx.getUpdateCount());
	}
	
	private void logPDPData() {
		SmartDashboard.putData("PDP", pdp);
		SmartDashboard.putNumber("PDP_Temperature", pdp.getTemperature());
//		SmartDashboard.putNumber("PDP_Total_Current", pdp.getTotalCurrent());
		SmartDashboard.putNumber("PDP_Total_Energy", pdp.getTotalEnergy()); // in milliJoules
		SmartDashboard.putNumber("PDP_Total_Power", pdp.getTotalPower());
//		SmartDashboard.putNumber("PDP_Voltage", pdp.getVoltage());
	}
	
}
