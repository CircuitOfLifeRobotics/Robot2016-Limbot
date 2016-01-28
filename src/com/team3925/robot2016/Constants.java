package com.team3925.robot2016;

/**
 *	A class holding all the constants of the project
 */
public class Constants {
	private Constants() {};

	public static final boolean AUTO_START_IN_CENTER = true;
	public static final boolean DO_LOG_AHRS_VALUES = false;


	public static final double XBOX_AXIS_TOLERANCE = 0.1;
	
//	Trajectory Follow Constants
	public static final double TRAJECTORY_FOLLOWER_KP = 1;
	public static final double TRAJECTORY_FOLLOWER_KI = 0;
	public static final double TRAJECTORY_FOLLOWER_KD = 0;
	public static final double TRAJECTORY_FOLLOWER_KV = 0;
	public static final double TRAJECTORY_FOLLOWER_KA = 0;
	public static final double TRAJECTORY_FOLLOWER_ERROR = 1;
	
}
