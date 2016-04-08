package com.team3925.robot2016.util.hidhelpers;

import com.team3925.robot2016.Constants;

import edu.wpi.first.wpilibj.Joystick;

public final class ThrustmasterHelper {

	private ThrustmasterHelper() {}

	private static Joystick wheel;
	private static boolean needsInit = true;

	public static int
	AXIS_WHEEL = 0,
	SQUARE = 1,
	X = 2,
	CIRCLE = 3,
	TRIANGLE = 4,
	PADDLE_LEFT = 5,
	PADDLE_RIGHT = 6,
	L2 = 7,
	R2 = 8,
	SHARE = 9,
	OPTIONS = 10,
	L3 = 11,
	R3 = 12,
	PS = 13;
	
	public static void config(Joystick wheel) {
		ThrustmasterHelper.wheel = wheel;
		needsInit = false;
	}

	public static double getAxis(int axis) {
		if (needsInit) {
			return 0d;
		} else {
			return Math.abs(wheel.getRawAxis(axis)) > Constants.AXIS_TOLERANCE ? wheel.getRawAxis(axis) : 0d;
		}
	}

	public static boolean getButton(int button) {
		if (needsInit) {
			return false;
		} else {
			return wheel.getRawButton(button);
		}
	}
	
}
