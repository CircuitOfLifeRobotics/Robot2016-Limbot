package com.team3925.robot2016.util;

/**
 *	Holds two DriveTrain values
 */
public class DriveTrainSignal {
	public double left;
	public double right;
	
	public DriveTrainSignal(double leftSpeed, double rightSpeed) {
		left = leftSpeed;
		right = rightSpeed;
	}
	
	/**
	 * A DriveTrainSignal with motor speeds of 0.0
	 */
	public static DriveTrainSignal NEUTRAL = new DriveTrainSignal(0.0, 0.0);
	
}
