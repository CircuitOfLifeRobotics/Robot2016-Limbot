package com.team3925.robot2016;

/**
 *	A class holding all the constants of the project
 */
public class Constants {
	private Constants() {};
	
	
	// Loop time of the program
	public static final double DELTA_TIME = 0.020; // 20 ms
	
	
	public static enum AutoStartPos {
		COURTYARD, CENTER, DO_NOTHING;
	}

	public static final AutoStartPos AUTO_START_LOCATION = AutoStartPos.CENTER;
	public static final boolean DO_LOG_AHRS_VALUES = false;
	public static final boolean DO_LOG_PDP_VALUES = false;

	public static final double XBOX_AXIS_TOLERANCE = 0.1;

	public static final double GLOBAL_MAX_SHOOTER_PWR = 1;
	public static final double GLOBAL_MAX_DRIVE_TRAIN_PWR = 1;
	public static final double LAUNCHER_AIM_MOTOR_SPEED_MULTIPLIED = 0.05;
	
	
	
	// MECHANICAL CONSTANTS
	public static final double WHEEL_DIAMETER = 6.0; // inches (inflated)
	
	
	
	
	// LAUNCHER CONSTANTS
	
	// Launcher shooting motors PID Constants
	//TODO: tune shooter pid
	public static final double LAUNCHER_SHOOTER_KP = 1;
	public static final double LAUNCHER_SHOOTER_KI = 0;
	public static final double LAUNCHER_SHOOTER_KD = 0;
	public static final double LAUNCHER_SHOOTER_KF = 0;
	public static final double LAUNCHER_SHOOTER_RAMP_RATE = 1;//ramp rate is maximum acceleration in voltage/second
	public static final int LAUNCHER_SHOOTER_IZONE = 0; // izone is limit for integration
	public static final int LAUNCHER_SHOOTER_PROFILE = 0;
	
	// Launcher aim motor constants
	public static final double LAUNCHER_AIM_KP = 1;
	public static final double LAUNCHER_AIM_KI = 0;
	public static final double LAUNCHER_AIM_KD = 0;
	public static final double LAUNCHER_AIM_KF = 0;
	public static final double LAUNCHER_AIM_RAMP_RATE = 1;//ramp rate is maximum acceleration in voltage/second
	public static final int LAUNCHER_AIM_IZONE = 0; // izone eliminates
	public static final int LAUNCHER_AIM_PROFILE = 0;
	
	
	// DRIVETRAIN CONSTANTS
	
	public static final double DRIVETRAIN_ENCODER_FACTOR = 2 * Math.PI * WHEEL_DIAMETER / 256; // TODO Get Encoder Pulses per Revolution
	
	public static final double MAX_ACCEL_M_SEC2 = 0.923;
	public static final double MAX_VEL_M_SEC = 1.973;
	
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
