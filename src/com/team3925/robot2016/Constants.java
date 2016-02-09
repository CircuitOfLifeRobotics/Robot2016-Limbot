package com.team3925.robot2016;

/**
 *	A class holding all the constants of the project
 */
public class Constants {
	private Constants() {};
	
	public static enum AutoStartPos {
		COURTYARD, CENTER, DO_NOTHING;
	}

	public static final AutoStartPos AUTO_START_LOCATION = AutoStartPos.CENTER;
	public static final boolean DO_LOG_AHRS_VALUES = true;
	public static final boolean DO_LOG_PDP_VALUES = false;

	public static final double XBOX_AXIS_TOLERANCE = 0.1;

	public static final double GLOBAL_MAX_SHOOTER_PWR = 1;
	public static final double LAUNCHER_AIM_MOTOR_SPEED_MULTIPLIED = 0.6;
	
	public static final double MAX_ACCEL_M_SEC2 = 0.923;
	public static final double MAX_VEL_M_SEC = 1.973;
	
	
//	DRIVETRAIN CONSTANTS
	public static final double GLOBAL_MAX_DRIVE_TRAIN_PWR = 1;
	
//	Straight Gyro Drive PID Constants
	//TODO: tune gyro straight drive pid
	public static final double GYRO_DRIVE_KP = 1;
	public static final double GYRO_DRIVE_KI = 0;
	public static final double GYRO_DRIVE_KD = 0;
	
//	Launcher PID Constants
	//TODO: tune shooter pid
	public static final double LAUNCHER_AIM_KP = 1;
	public static final double LAUNCHER_AIM_KI = 0;
	public static final double LAUNCHER_AIM_KD = 0;
	public static final double LAUNCHER_AIM_KF = 0;
	public static final double LAUNCHER_AIM_RAMP_RATE = 1;//ramp rate is maximum acceleration in voltage/second
	public static final int LAUNCHER_AIM_IZONE = 0; // izone eliminates
	public static final int LAUNCHER_AIM_PROFILE = 0;
	
	public static final double LAUNCHER_WHEELS_KP = 1;
	public static final double LAUNCHER_WHEELS_KI = 0;
	public static final double LAUNCHER_WHEELS_KD = 0;
	public static final double LAUNCHER_WHEELS_KF = 0;
	public static final double LAUNCHER_WHEELS_RAMP_RATE = 1;//ramp rate is maximum acceleration in voltage/second
	public static final int LAUNCHER_WHEELS_IZONE = 0; // izone eliminates
	public static final int LAUNCHER_WHEELS_PROFILE = 0;
	
//	Drivetrain PID Constants
	//TODO: tune drivetrain pid
	public static final double DRIVE_TRAIN_KP = 1;
	public static final double DRIVE_TRAIN_KI = 0;
	public static final double DRIVE_TRAIN_KD = 0;
	
}
