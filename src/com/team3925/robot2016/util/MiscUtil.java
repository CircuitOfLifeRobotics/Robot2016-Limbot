package com.team3925.robot2016.util;

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
	
}
