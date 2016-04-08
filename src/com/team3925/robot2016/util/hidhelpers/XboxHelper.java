package com.team3925.robot2016.util.hidhelpers;

import com.team3925.robot2016.Constants;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Joystick.RumbleType;

/**
 * ControllersHelper elimanates the need for classes to create copies of the xboxes.
 * It also automatically creates deadzones for the axes and provides xbox value mapping.
 * @author Bryan
 */
public class XboxHelper {
	private XboxHelper() {}
	
	private static Joystick shooter;
	
	public static final int
	A = 1,
	B = 2,
	X = 3,
	Y = 4,
	TRIGGER_LT = 5,
	TRIGGER_RT = 6,
	BACK = 7,
	START = 8,
	STICK_LEFT = 9,
	STICK_RIGHT = 10,
	
	AXIS_LEFT_Y = 1,
	AXIS_LEFT_X = 0,
	AXIS_RIGHT_Y = 5,
	AXIS_RIGHT_X = 4,
	AXIS_TRIGGER_LEFT = 2,
	AXIS_TRIGGER_RIGHT = 3;
	
	private static boolean needsInit = true;
	
	public static void config(Joystick xbox) {
		shooter = xbox;
		needsInit = false;
	}
	
	public static double getShooterAxis(int axis) {
		if (needsInit) {
			return 0d;
		} else {
			return Math.abs(shooter.getRawAxis(axis)) > Math.abs(Constants.AXIS_TOLERANCE) ? shooter.getRawAxis(axis) : 0;
		}
	}
	
	public static boolean getShooterButton(int button) {
		if (needsInit) {
			return false;
		} else {
			return shooter.getRawButton(button);
		}
	}
	
	public static double getShooterPOV() {
		if (needsInit) {
			return 0d;
		} else {
			return shooter.getPOV();
		}
	}
	
	public static void setShooterRumble(float magnitude) {
		shooter.setRumble(RumbleType.kLeftRumble, magnitude);
		shooter.setRumble(RumbleType.kRightRumble, magnitude);
	}
	
}
