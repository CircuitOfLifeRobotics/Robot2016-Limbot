package com.team3925.robot2016.util;

public class MiscUtil {

	/**
	 * A logical AND gate that is resizable at runtime
	 * @param val1 first boolean
	 * @param valn the other inputs
	 * @return a logical AND of the input
	 * @author Bryan
	 */
	public static boolean gateAND(boolean val1, boolean... valn) {
		boolean tmpVal = val1;
		for (int i = 0; i < valn.length; i++) {
			tmpVal = tmpVal && valn[i];
		}
		return tmpVal;
	}
	
	
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
	
	/**
	 * Convert {@link SmartdashBoardLoggable} <code>getFormattedName()<code> to a normal name without underscore at end
	 * <p>
	 * TODO Refactor {@link SmartdashBoardLoggable} to not need this helper method
	 * 
	 * @param formattedName String returned by <code>SmartDashboardLoggable.getFormattedName()</code>
	 * @return String without the '_' at end
	 */
	public static String formattedNameToNormalName(String formattedName) {
		return formattedName.substring(0, formattedName.length()-1);
	}
	
}
