package com.team3925.robot2016;

import com.team3925.robot2016.trajectory.LauncherTrajectoryTable;

/**
 *	A class holding all the constants of the project
 */
public class Constants {
	private Constants() {};
	
	// Loop time of the program
	public static final double DELTA_TIME = 0.020; // 20 ms TODO Will be bigger due to GRIP
	
	public static final boolean DO_LOG_AHRS_VALUES = true;//Robot.prefs.getBoolean("Do Log AHRS Vals", false);
	public static final boolean DO_LOG_PDP_VALUES = false;//Robot.prefs.getBoolean("Do Log PDP Vals", false);
	public static final boolean DO_LOG_GRIP_VALUES = false;//Robot.prefs.getBoolean("Do Log GRIP Vals", false);
	
	public static final double XBOX_AXIS_TOLERANCE = 0.05;
	
	public static final boolean DO_MANUAL_CLIMBER = true;
	
	
	
	//WORLD CONSTANTS
	public static final double GRAVITY = 0;
	
	
	// MECHANICAL CONSTANTS
	public static final double DRIVE_WHEEL_DIAMETER = 6.0; // inches (inflated)
	public static final double LAUNCHER_WHEEL_CIRCUM = 12.5 / 12.0;//feet
	
	
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
	//carpet to pivot 7 in
    //cam to pivot 18 in
    
    
    // CLIMBER CONSTANTS
    public static final double CLIMBER_MAX_VALUE = 20_000; // TODO Get true limit
    public static final double CLIMBER_ACTIVE_TIME = 125d; // the 20 seconds at end of match
//    public static final double CLIMBER_ACTIVE_TIME = 135d; // the 20 seconds at end of match
    
    
    
	// LAUNCHER CONSTANTS
	public static final double LAUNCHER_TESTING_ANGLE = 45d; // degrees
    
	public static final LauncherTrajectoryTable TABLE = new LauncherTrajectoryTable(11, LAUNCHER_TESTING_ANGLE);
	public static final void initLauncherIntakeTable() {
		// TODO Add implementation and get debug values
	}
    public static final double LAUNCHER_GLOBAL_MAX_POWER = 1;//Robot.prefs.getDouble("Max Shooter Pwr", 1);
    public static final double LAUNCHER_AIM_MOTOR_SPEED_MULTIPLIED = 1;
	public static final double LAUNCHER_MAX_INTAKE_SPEED = 25_000; //TODO get actual max speed
	
//	Launcher PID Constants
	public static final double LAUNCHER_MAX_HEIGHT = 700; // in encoder ticks
	public static final double LAUNCHER_MIN_HEIGHT = 50; // in encoder ticks
	
	public static final double LAUNCHER_AIM_TOLERANCE = 3;
	public static final double LAUNCHER_AIM_SLOWDOWN = 40;
	public static final double LAUNCHER_AIM_INCREMENT = 18;
	public static final double LAUNCHER_INTAKE_INCREMENT = 1000;
	//TODO: tune shooter pid
	public static final double LAUNCHER_AIM_KP = /*400d/10_000d;*/   240d/10000d;
	public static final double LAUNCHER_AIM_KI = /*16d/10_000d; */   8d/10000d;
	public static final double LAUNCHER_AIM_KD = /*950d/10_000d;*/   250d/10000d;
	public static final double LAUNCHER_AIM_KF = 0.0;
	public static final double LAUNCHER_AIM_RAMP_RATE = 5;//ramp rate is maximum acceleration in voltage/second
	public static final int LAUNCHER_AIM_IZONE = 0; // izone eliminates
	
	public static final double LAUNCHER_WHEELS_KP = 4e-7;
	public static final double LAUNCHER_WHEELS_KI = 0.000000000;
	public static final double LAUNCHER_WHEELS_KD = 0.00;
	public static final double LAUNCHER_WHEELS_KF = -0.9;
	public static final double LAUNCHER_WHEELS_RAMP_RATE = 1;//ramp rate is maximum acceleration in voltage/second
	public static final int LAUNCHER_WHEELS_IZONE = 0; // izone eliminates
	public static final double LAUNCHER_WHEELS_TOLERANCE = 50;
	
	
	
//	DRIVETRAIN CONSTANTS
	
	public static final double GLOBAL_MAX_DRIVE_TRAIN_PWR = 0.8;//Robot.prefs.getDouble("Max DriveTrain Pwr", 1);
	public static final double DRIVE_TRAIN_VOLTAGE_RAMP_RATE = 1;
	
//	Straight Gyro Drive PID Constants
	//TODO: tune gyro straight drive PID
	public static final double GYRO_DRIVE_KP = 0.1;
	public static final double GYRO_DRIVE_KI = 0;
	public static final double GYRO_DRIVE_KD = 0;
	
	
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
	public static final double GYROTURN_P = 0.03;
	public static final double GYROTURN_I = 0.0;
	public static final double GYROTURN_D = 0.001;
	
    public static final double DRIVETRAIN_ON_TARGET_ERROR = .05;
	
    public static final double GYROTURN_POS_TOLERANCE = 1;
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
