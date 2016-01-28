package com.team3925.robot2016.util;

import java.util.HashMap;
import java.util.Map;

import com.team3925.robot2016.Constants;
import com.team3925.robot2016.Robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;

/**
 * ControllersHelper elimanates the need for classes to create copies of the controllers.
 * It also automatically creates deadzones for the axes and provides controller value mapping.
 * @author Bryan
 */
public class ControllersHelper {
	private ControllersHelper() {}
	
	public static enum CurrentController {
		DRIVER, SHOOTER, LAUNCHPAD;
	}
	
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
	AXIS_LEFT_X = 2,
	AXIS_RIGHT_Y = 3,
	AXIS_RIGHT_X = 4,
	AXIS_TRIGGER_LEFT = 5,
	AXIS_TRIGGER_RIGHT = 6;
	
//	TODO Get helper values for LaunchPad
	
	private static boolean hasInit = false;
	private static Map<CurrentController, Joystick> controllers = new HashMap<>();
	
	public static void init() {
		if (!hasInit) {
			controllers.put(CurrentController.DRIVER, Robot.oi.xboxDriver);
			controllers.put(CurrentController.SHOOTER, Robot.oi.xboxShooter);
			controllers.put(CurrentController.LAUNCHPAD, Robot.oi.launchPad);
		}
	}
	
	public static double getAxis(CurrentController currentController, int axis) {
		if (!hasInit) {	init();	}
		return Math.abs(controllers.get(currentController).getRawAxis(axis)) > Constants.XBOX_AXIS_TOLERANCE ? controllers.get(currentController).getRawAxis(axis) : 0;
	}
	
	public static boolean getButton(CurrentController currentController, int button) {
		if (!hasInit) {	init();	}
		return controllers.get(currentController).getRawButton(button);
	}
	
	public static double getPOV(CurrentController xbox) {
		if (!hasInit) {	init();	}
		if (xbox == CurrentController.LAUNCHPAD) {
			DriverStation.reportError("Tried to get POV on launchpad!", false);
			return 0;
		}
		return controllers.get(xbox).getPOV();
	}
	
}
