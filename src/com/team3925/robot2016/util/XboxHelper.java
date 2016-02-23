package com.team3925.robot2016.util;

import com.team3925.robot2016.Constants;
import com.team3925.robot2016.Robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Joystick.RumbleType;

/**
 * ControllersHelper elimanates the need for classes to create copies of the xboxes.
 * It also automatically creates deadzones for the axes and provides xbox value mapping.
 * @author Bryan
 */
public class XboxHelper {
	private XboxHelper() {}
	
	private static Joystick driver;
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
	
	private static boolean hasInit = false;
	
	public static void init() {
		driver = Robot.oi.xboxDriver;
		shooter = Robot.oi.xboxShooter;
	}
	
	public static double getShooterAxis(int axis) {
		if (!hasInit) {	init();	}
		return Math.abs(shooter.getRawAxis(axis)) > Math.abs(Constants.XBOX_AXIS_TOLERANCE) ? shooter.getRawAxis(axis) : 0;
	}
	
	public static boolean getShooterButton(int button) {
		if (!hasInit) {	init();	}
		return shooter.getRawButton(button);
	}
	
	public static double getShooterPOV() {
		if (!hasInit) {	init();	}
		return shooter.getPOV();
	}
	
	public static double getDriverAxis(int axis) {
		if (!hasInit) {	init();	}
		return Math.abs(driver.getRawAxis(axis)) > Math.abs(Constants.XBOX_AXIS_TOLERANCE) ? driver.getRawAxis(axis) : 0;
	}
	
	public static boolean getDriverButton(int button) {
		if (!hasInit) {	init();	}
		return driver.getRawButton(button);
	}
	
	public static double getDriverPOV() {
		if (!hasInit) {	init();	}
		return driver.getPOV();
	}
	
	public static void setShooterRumble(float magnitude) {
		shooter.setRumble(RumbleType.kLeftRumble, magnitude);
		shooter.setRumble(RumbleType.kRightRumble, magnitude);
	}
	
	public static void setDriverRumble(float magnitude) {
		driver.setRumble(RumbleType.kLeftRumble, magnitude);
		driver.setRumble(RumbleType.kRightRumble, magnitude);
	}
	
}
