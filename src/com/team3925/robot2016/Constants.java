package com.team3925.robot2016;

/**
 *	A class holding all the constants of the project
 */
public class Constants {
	private Constants() {};

	// Loop time of the program
	public static final double DELTA_TIME = 0.020; // 20 ms TODO Re-test

	public static final boolean DO_LOG_AHRS_VALUES = false;
	public static final boolean DO_LOG_PDP_VALUES = false;
	public static final boolean DO_LOG_MOVEMENT_CONSTANTS = false;

	public static final double AXIS_TOLERANCE = 0.05;



	// ELECTRICAL CONSTANTS
	public static final int DRIVETRAIN_MOTOR_LEFT_A = 19;
	public static final int DRIVETRAIN_MOTOR_LEFT_B = 18;
	public static final int DRIVETRAIN_MOTOR_RIGHT_A = 17;
	public static final int DRIVETRAIN_MOTOR_RIGHT_B = 16;
	public static final int DRIVETRAIN_SOLENOID_FORWARD = 4;
	public static final int DRIVETRAIN_SOLENOID_REVERSE = 5;

	public static final int LAUNCHER_MOTOR_NEAR = 12;
	public static final int LAUNCHER_MOTOR_FAR = 14;
	public static final int LAUNCHER_MOTOR_ARM = 13;
	public static final int LAUNCHER_SOLENOID_PUNCHER_FORWARD = 2;
	public static final int LAUNCHER_SOLENOID_PUNCHER_REVERSE = 3;
	public static final int LAUNCHER_LIMIT_SWITCH_REVERSE = 1;
	public static final int LAUNCHER_LIMIT_SWITCH_FORWARD = 0;
	public static final int LAUNCHER_ULTRASONIC = 0;

	public static final int PLEXIARMS_SOLENOID_LEFT_A = 0;
	public static final int PLEXIARMS_SOLENOID_LEFT_B = 1;

	


	// MECHANICAL CONSTANTS
	public static final double DRIVE_WHEEL_DIAMETER = 6.0; // inches (inflated)


	// CAMERA CONSTANTS

	public static final String AXIS_CAMERA_IP = "192.168.0.90";
	public static final double CAMERA_AIMED_X = 159;
	public static final double CAMERA_FOV_DEG = 45.134;
	public static final double CAMERA_FOV_PIX = 320;
	public static final double CAMERA_DEGS_PER_PX = CAMERA_FOV_DEG/CAMERA_FOV_PIX;
	public static final double CAMERA_TARGET_WIDTH = 5d/3d;//in feet
	public static final double CAMERA_TARGET_HEIGHT_GROUND = 83.5d;
	public static final double CAMERA_PIVOT_HEIGHT_GROUND = (7d / 12d);
	public static final double CAMERA_PIVOT_DIST = (18d / 12d);
	public static final double CAMERA_MID_OFFSET = (6.5d / 12d);
	public static final double MAX_BALL_EXIT_VELOCITY = 7.7;//meters / second
	public static final double CAMERA_X_DEG_OFFSET_TOL = 1;
	public static final double CAMERA_MAX_DATA_AGE = 50;
	//carpet to pivot 7 in
	//cam to pivot 18 in


	// CLIMBER CONSTANTS
	public static final double CLIMBER_MAX_VALUE = 20_000; // TODO Get true limit
	//    public static final double CLIMBER_ACTIVE_TIME = 125d; // the 20 seconds at end of match
	public static final double CLIMBER_ACTIVE_TIME = 30; // calculated from end of match
	public static final boolean DO_MANUAL_CLIMBER = true;




	// LAUNCHER CONSTANTS

	public static final double LAUNCHER_GLOBAL_POWER = 1d;
	public static final double LAUNCHER_MAX_ARM_ANGLE = 90d; // in degrees
	public static final double LAUNCHER_ENCODER_SCALE_FACTOR = -0.0032873376623377/*(-9d/3200d)*/;
	public static final double LAUNCHER_ZERO_COMMAND_WAIT = 1; // entries
	public static final double LAUNCHER_ENCODER_WATCHER_TOLERANCE = 3; //degrees TODO tune
	public static final long LAUNCHER_ENCODER_WATCHER_PERIOD = 20; //ms
	public static final int LAUNCHER_ENCODER_WATCHER_DATA_CACHE_SIZE = 4; // entries
    public static final double[] LAUNCHER_SETPOINTS = {0,26,35,29,39,43,45,47,48,49,50,41}; //This is the launcher setpoints

    public static final double LAUNCHER_COLLECT_BALL_WAIT = 0.5;
    
	public static final double LAUNCHER_REDUCTION_ANGLE = 20;
	public static final double LAUNCHER_REDUCTION_MULTIPLIER = .3;

	public static final double LAUNCHER_LAUNCH_BALL_MIDZONE_ANGLE = 53;

	public static final double LAUNCHER_LAUNCHER_BALL_HIGH_ANGLE = 85;
	public static final double LAUNCHER_LAUNCHER_BALL_LOW_ANGLE = 53;
	public static final double LAUNCHER_LAUNCHER_BALL_SIDE_ANGLE = 50;

	public static final double LAUNCHER_COLLECT_BALL_FAR = -0.5;
	public static final double LAUNCHER_COLLECT_BALL_NEAR = 1;

	public static final double LAUNCHER_SETPOINT_REACH_WAIT = 3;
	public static final int LAUNCHER_RESTING_ANGLE = 30;

	//TODO TUNE
	public static final double LAUNCHER_PID_K_P = 0.038;
	public static final double LAUNCHER_PID_K_I = 0.003; //0.0015 old val
	public static final double LAUNCHER_PID_K_D = 0.02;
	public static final double LAUNCHER_ARM_TOLERANCE = .5d;
	public static final double LAUNCHER_PID_VELOCITY_TOLERANCE = 100; //TODO Tune and reimplement



	//	DRIVETRAIN CONSTANTS
	public static double kDriveSensitivity = 0.75;
	public static final double GLOBAL_MAX_DRIVETRAIN_PWR = 0.8;
	public static final double DRIVETRAIN_VOLTAGE_RAMP_RATE = 1;
	public static final double DRIVETRAIN_BREAK_MODE_ENABLE = 0.25; // calculated from end of the match

	//	Straight Gyro Drive PID Constants
	//TODO: tune gyro straight drive PID
	public static final double GYRO_DRIVE_KP = 0.1;
	public static final double GYRO_DRIVE_KI = 0;
	public static final double GYRO_DRIVE_KD = 0;
	public static final double GYRO_DRIVE_ON_TARGET_ERROR_INCHES = 5;


	//	Drivetrain PID Constants
	// DRIVETRAIN CONSTANTS
	public static final double MAX_DRIVETRAIN_ACCEL_PWR_PER_TICK = 1;
	private static final double MAX_VALUE_MULTIPLIER = 0.8;

	private static final int DRIVETRAIN_ENCODER_TICKS = 128 * 4;
	public static final double DRIVETRAIN_ENCODER_FACTOR = Math.PI * DRIVE_WHEEL_DIAMETER / DRIVETRAIN_ENCODER_TICKS * (97d/77d);


	public static final double MAX_ACCEL_M_SEC2 = 0.923;
	public static final double MAX_VEL_M_SEC = 1.973;

	public static final double MAX_DRIVETRAIN_VEL_M_PER_SEC = 2.6 * MAX_VALUE_MULTIPLIER;
	public static final double MAX_DRIVETRAIN_ACCEL_M_PER_SEC2 = 2.5 * MAX_VALUE_MULTIPLIER;
	public static final double MAX_ROTATION_VELOCITY_RADS_PER_SEC = 6.3 * MAX_VALUE_MULTIPLIER;
	public static final double MAX_ROTATION_ACCEL_RADS_PER_SEC2 = 570 * MAX_VALUE_MULTIPLIER;

	public static final double DRIVETRAIN_MAX_SPEED_INCHES_PER_SEC = 82;
	public static final double DRIVETRAIN_MAX_ACCEL_INCHES_PER_SEC2 = 79;


	// Left PID constants
	public static final double DRIVETRAIN_LEFT_KP = .35;
	public static final double DRIVETRAIN_LEFT_KI = .005;
	public static final double DRIVETRAIN_LEFT_KD = 0;

	// Right PID Constants
	public static final double DRIVETRAIN_RIGHT_KP = .35;
	public static final double DRIVETRAIN_RIGHT_KI = 0.005;
	public static final double DRIVETRAIN_RIGHT_KD = 0;

	//Gyro turn constants
	public static final double GYROTURN_P = 0.04;
	public static final double GYROTURN_I = 0.0000;
	public static final double GYROTURN_D = 0.07;

	public static final double DRIVETRAIN_ON_TARGET_ERROR = .05;

	public static final double GYROTURN_POS_TOLERANCE = 1;

	
	
	public static final double COMMAND_RESET_ANGLE = LAUNCHER_MAX_ARM_ANGLE;


	
	// AUTONOMOUS CONSTANTS
	public static final String AUTONOMOUS_CHOOSER_NAME = "Autonomous Routine Chooser";
	public static final String AUTONOMOUS_POSITION_CHOOSER_NAME = "Position Chooser";
	
	public static final double AUTONOMOUS_SHOOT_ANGLE = 45; // TODO TUNE
	public static final double AUTONOMOUS_CROSS_DEFENSE_DRIVE_TIME = 2.5d;
	public static final double AUTONOMOUS_WAIT_FOR_DOWN = 0.5d;
	
	public static final double AUTONOMOUS_DEFENSECROSS_CHEVAL_DRIVE_TIME = 0.5;
	public static final double AUTONOMOUS_DEFENSECROSS_CHEVAL_DRIVE_TIME_2 = 1.5;
	public static final double AUTONOMOUS_DEFENSECROSS_CHEVAL_WAIT_TIME = 1.5;
	


}
