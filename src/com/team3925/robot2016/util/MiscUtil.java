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
    public static double limit(double v, double limit) {
        return (Math.abs(v) < limit) ? v : limit * (v < 0 ? -1 : 1);
    }
	
	public static void putPoseSD(String prefix, Pose pose) {
		SmartDashboard.putNumber(prefix + "LeftDistance", pose.getLeftDistance());
		SmartDashboard.putNumber(prefix + "RightDistance", pose.getRightDistance());
		SmartDashboard.putNumber(prefix + "LeftVelocity", pose.getLeftVelocity());
		SmartDashboard.putNumber(prefix + "RightVelocity", pose.getRightVelocity());
		SmartDashboard.putNumber(prefix + "Heading", pose.getHeading());
		SmartDashboard.putNumber(prefix + "HeadingVelocity", pose.getHeadingVelocity());
	}
	
}
