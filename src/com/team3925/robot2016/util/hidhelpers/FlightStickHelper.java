package com.team3925.robot2016.util.hidhelpers;

import com.team3925.robot2016.Constants;

import edu.wpi.first.wpilibj.Joystick;

/**
 * Helper class for flight sticks
 * <p>
 * Not as useful as some of the other helper classes, but
 * it's better to have one than not.
 */
public final class FlightStickHelper {

	private FlightStickHelper() {}

	private static Joystick stick;
	private static boolean needsInit = true;

	public static int
	AXIS_X = 	0,
	AXIS_Y = 	1,
	AXIS_KNOB =	2,
	
	TRIGGER = 				1,
	TOP_DOWN = 				2,
	TOP_UP = 				3,
	TOP_LEFT = 				4,
	TOP_RIGHT =				5,
	BOTTOM_LEFT_UP =		6,
	BOTTOM_LEFT_DOWN =		7,
	BOTTOM_CENTER_LEFT =	8,
	BOTTOM_CENTER_RIGHT =	9,
	BOTTOM_RIGHT_UP =		11,
	BOTTOM_RIGHT_DOWN =		10;
	
	public static void config(Joystick stick) {
		FlightStickHelper.stick = stick;
	}

	public static double getAxis(int axis) {
		if (needsInit) {
			return 0d;
		} else {
			return Math.abs(stick.getRawAxis(axis)) > Constants.AXIS_TOLERANCE ? stick.getRawAxis(axis) : 0d;
		}
	}

	public static boolean getButton(int button) {
		if (needsInit) {
			return false;
		} else {
			return stick.getRawButton(button);
		}
	}
	
}
