package com.team3925.robot2016;

/**
 *	A class holding all the constants of the project
 */
public class Constants {
	private Constants() {};
	
	
//	Loop time of the program
	public static final double DELTA_TIME = 0.020; // 20 ms
	
	
	public static enum AutoStartPos {
		COURTYARD, CENTER, DO_NOTHING;
	}

	public static final AutoStartPos AUTO_START_LOCATION = AutoStartPos.CENTER;
	public static final boolean DO_LOG_AHRS_VALUES = false;

	public static final double XBOX_AXIS_TOLERANCE = 0.1;

	public static final double GLOBAL_MAX_SHOOTER_PWR = 1;
	public static final double LAUNCHER_AIM_MOTOR_SPEED_MULTIPLIED = 0.05;
	
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
	public static final double LAUNCHER_KP = 1;
	public static final double LAUNCHER_KI = 0;
	public static final double LAUNCHER_KD = 0;
	public static final double LAUNCHER_KF = 0;
	public static final double LAUNCHER_RAMP_RATE = 1;//ramp rate is maximum acceleration in voltage/second
	public static final int LAUNCHER_IZONE = 0; // izone eliminates
	public static final int LAUNCHER_PROFILE = 0;
	
//	Drivetrain PID Constants
	//TODO: tune drivetrain pid
	public static final double DRIVE_TRAIN_KP = 1;
	public static final double DRIVE_TRAIN_KI = 0;
	public static final double DRIVE_TRAIN_KD = 0;
	
	
	
//	Trajectory Follow Constants
	public static final double TRAJECTORY_FOLLOWER_KP = 1;
	public static final double TRAJECTORY_FOLLOWER_KI = 0;
	public static final double TRAJECTORY_FOLLOWER_KD = 0;
	public static final double TRAJECTORY_FOLLOWER_KV = 0;
	public static final double TRAJECTORY_FOLLOWER_KA = 0;
	public static final double TRAJECTORY_FOLLOWER_ERROR = 1;
	
	
	// Temporary values for controllers
    // DriveStraightController gains
    public static double kDriveMaxSpeedInchesPerSec = 77.677;
    public static double kDriveMaxAccelInchesPerSec2 = 36.34;
    public static double kDrivePositionKp = 0.7;
    public static double kDrivePositionKi = 0;
    public static double kDrivePositionKd = 0;
    public static double kDriveStraightKp = 3.0;
    public static double kDriveStraightKi = 0;
    public static double kDriveStraightKd = 0;
    public static double kDrivePositionKv = 0.008;
    public static double kDrivePositionKa = 0.0017;
    public static double kDriveOnTargetError = 0.75;
    public static double kDrivePathHeadingFollowKp = 0.01;
    // TurnInPlaceController gains
    public static double kTurnMaxSpeedRadsPerSec = 5.25;
    public static double kTurnMaxAccelRadsPerSec2 = 5.25;
    public static double kTurnKp = 3.0;
    public static double kTurnKi = 0.18;
    public static double kTurnKd = 0.23;
    public static double kTurnKv = 0.085;
    public static double kTurnKa = 0.075;
    public static double kTurnOnTargetError = 0.0225;
    
}
