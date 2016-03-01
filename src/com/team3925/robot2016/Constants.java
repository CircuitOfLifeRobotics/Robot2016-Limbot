package com.team3925.robot2016;

/**
 *	A class holding all the constants of the project
 */
public class Constants {
	private Constants() {};
	
	// Loop time of the program
	public static final double DELTA_TIME = 0.020; // 20 ms
	
	public static final String AXIS_CAMERA_IP = "192.168.0.90";
	
	public static final boolean DO_LOG_AHRS_VALUES = false;//Robot.prefs.getBoolean("Do Log AHRS Vals", false);
	public static final boolean DO_LOG_PDP_VALUES = false;//Robot.prefs.getBoolean("Do Log PDP Vals", false);
	public static final boolean DO_LOG_GRIP_VALUES = false;//Robot.prefs.getBoolean("Do Log GRIP Vals", false);
	
	public static final double XBOX_AXIS_TOLERANCE = 0.05;
	
	public static final double GLOBAL_MAX_SHOOTER_PWR = 1;//Robot.prefs.getDouble("Max Shooter Pwr", 1);
	public static final double LAUNCHER_AIM_MOTOR_SPEED_MULTIPLIED = 1;
	
	public static final double MAX_ACCEL_M_SEC2 = 0.923;
	public static final double MAX_VEL_M_SEC = 1.973;
	
	// MECHANICAL CONSTANTS
	public static final double WHEEL_DIAMETER = 6.0; // inches (inflated)
	
	
	
//	DRIVETRAIN CONSTANTS
	public static final double GLOBAL_MAX_DRIVE_TRAIN_PWR = 0.8;//Robot.prefs.getDouble("Max DriveTrain Pwr", 1);
	public static final double DRIVE_TRAIN_VOLTAGE_RAMP_RATE = 1;
	
//	Straight Gyro Drive PID Constants
	//TODO: tune gyro straight drive pid
	public static final double GYRO_DRIVE_KP = 0.1;
	public static final double GYRO_DRIVE_KI = 0;
	public static final double GYRO_DRIVE_KD = 0;
	
	
	
	public static final double MAX_INTAKE_SPEED = 25000; //TODO get actual max speed
	
//	Launcher PID Constants
	public static final double LAUNCHER_MAX_HEIGHT = 700; // in encoder ticks
	public static final double LAUNCHER_MIN_HEIGHT = 50; // in encoder ticks
	
	public static final double LAUNCHER_AIM_TOLERANCE = 10;
	public static final double LAUNCHER_AIM_SLOWDOWN = 40;
	public static final double LAUNCHER_AIM_INCREMENT = 6;
	public static final double LAUNCHER_INTAKE_INCREMENT = 1000;
	//TODO: tune shooter pid
	public static final double LAUNCHER_AIM_KP = 1400d/10000d;   //200d/10000d
	public static final double LAUNCHER_AIM_KI = 16d/10000d;     //8d/10000d; 
	public static final double LAUNCHER_AIM_KD = 950d/10000d;   //250d/10000d
	public static final double LAUNCHER_AIM_KF = 0.0;
	public static final double LAUNCHER_AIM_RAMP_RATE = 5;//ramp rate is maximum acceleration in voltage/second
	public static final int LAUNCHER_AIM_IZONE = 0; // izone eliminates
	
	public static final double LAUNCHER_WHEELS_KP = 0.06;
	public static final double LAUNCHER_WHEELS_KI = 0.000000;
	public static final double LAUNCHER_WHEELS_KD = 0.6;
	public static final double LAUNCHER_WHEELS_KF = 0/*0.04092*/;
	public static final double LAUNCHER_WHEELS_RAMP_RATE = 1;//ramp rate is maximum acceleration in voltage/second
	public static final int LAUNCHER_WHEELS_IZONE = 0; // izone eliminates
	public static final double LAUNCHER_WHEELS_TOLERANCE = 50;
	
	
	
//	Drivetrain PID Constants
	// DRIVETRAIN CONSTANTS
	public static final double MAX_DRIVETRAIN_ACCEL_PWR_PER_TICK = 1;
	
	private static final int DRIVETRAIN_ENCODER_TICKS = 128 * 4;
	public static final double DRIVETRAIN_ENCODER_FACTOR = Math.PI * WHEEL_DIAMETER / DRIVETRAIN_ENCODER_TICKS * (97d/77d);
	private static final double MAX_VALUE_MULTIPLIER = 0.8;
	
	public static final double MAX_DRIVETRAIN_VEL_M_PER_SEC = 2.6 * MAX_VALUE_MULTIPLIER;
	public static final double MAX_DRIVETRAIN_ACCEL_M_PER_SEC2 = 2.5 * MAX_VALUE_MULTIPLIER;
	public static final double MAX_ROTATION_VELOCITY_RADS_PER_SEC = 6.3 * MAX_VALUE_MULTIPLIER;
	public static final double MAX_ROTATION_ACCEL_RADS_PER_SEC2 = 570 * MAX_VALUE_MULTIPLIER;
	
    public static double kDriveMaxSpeedInchesPerSec = 82;
    public static double kDriveMaxAccelInchesPerSec2 = 79;
	
    // Left PID constants
	public static final double DRIVETRAIN_LEFT_KP = .35;
	public static final double DRIVETRAIN_LEFT_KI = .005;
	public static final double DRIVETRAIN_LEFT_KD = 0;
	
	// Right PID Constants
	public static final double DRIVETRAIN_RIGHT_KP = .35;
	public static final double DRIVETRAIN_RIGHT_KI = 0.005;
	public static final double DRIVETRAIN_RIGHT_KD = 0;
    
	//Gyro turn constants
	public static final double GYROTURN_P = 0.05;
	public static final double GYROTURN_I = 0.0;
	public static final double GYROTURN_D = 0.001;
	
    public static final double DRIVETRAIN_ON_TARGET_ERROR = .05;
	
    //Camera constants
    public static final double CAMERA_AIMED_X = 159;
    public static final double CAMERA_FOV_DEG = 45.134;
    public static final double CAMERA_FOV_PIX = 320;
    public static final double CAMERA_DEGS_PER_PX = CAMERA_FOV_DEG/CAMERA_FOV_PIX;
    public static final double CAMERA_TARGET_WIDTH = 5d/3d;//in feet
    
    public static final double GYROTURN_POS_TOLERANCE = 3;
    public static final double GYROTURN_RATE_TOLERANCE = 0.1;
    
    //Default Motion Profiles
    //{Position (rotations), Velocity (RPM), Duration (ms)}
    public static final double[][] MOTION_PROFILE_HOLD = {
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    		{0,	0,	10},
    };
}
