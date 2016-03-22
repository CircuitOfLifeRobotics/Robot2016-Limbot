package com.team3925.robot2016.util;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class MiscUtil {
	
	/**
	 * Limits an input to a value between 1 and -1
	 * @param input Value to be limited
	 * @return A double between -1 and 1
	 */
	public static double limit(double input) {
		if (input > 1.0) {
			return 1.0;
		} else if (input < -1.0) {
			return -1.0;
		} else {
			return input;
		}
	}
	
    /**
     * Limits the given input to the given magnitude.
     */
    public static double limit(double value, double limit) {
        return (Math.abs(value) < limit) ? value : limit * (value < 0 ? -1 : 1);
    }
	
	public static void putPoseSD(String prefix, DrivetrainPose pose) {
		SmartDashboard.putNumber(prefix + "LeftDistance", pose.getLeftDistance());
		SmartDashboard.putNumber(prefix + "RightDistance", pose.getRightDistance());
		SmartDashboard.putNumber(prefix + "LeftVelocity", pose.getLeftVelocity());
		SmartDashboard.putNumber(prefix + "RightVelocity", pose.getRightVelocity());
		SmartDashboard.putNumber(prefix + "Heading", pose.getHeading());
		SmartDashboard.putNumber(prefix + "HeadingVelocity", pose.getHeadingVelocity());
	}
	
	/**
	 * Get the angle setpoint from joystick value
	 * (assuming 1 on joystick is full height on launcher,
	 * and 0 on joystick is flat on launcher)
	 * 
	 * @param joystickVal
	 * @return angle
	 */
	public static double joystickToDegrees(double joystickVal) {
		return (Math.abs(joystickVal)*78)-3;
	}
	
	/**
	 * Get the rotation count of an intake wheel from the encoder output
	 * 
	 * @param encoderTicks
	 * @return rotations
	 */
	public static double intakeEncoderTicksToRotations(double encoderTicks) {
		return encoderTicks/4096 * 3;
		// gearing:	3:1
		// encoder:	
	}
	
	/**
	 * Get the angle of the launcher from the encoder output
	 * 
	 * @param enocderTicks
	 * @return degrees
	 */
	public static double aimEncoderTicksToDegrees(double encoderTicks) {
//		return (encoderTicks + 36)/11.377777778;
//		return (encoderTicks/8) + 5;
		return encoderTicks * (60d/765d);
	}
	
}
