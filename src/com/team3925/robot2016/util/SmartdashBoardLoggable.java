package com.team3925.robot2016.util;

import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * 	A mixin for classes that put values on SmartDashboard.
 * 	Used to reinforce the source of information.
 *  <p>
 *  Subsytems that use this mixin should put their logData inside Robot's logData() 
 */
public interface SmartdashBoardLoggable {
	
	public void logData();
	public String getFormattedName();
	
	
	default void putNumberSD(String key, double value) {
		SmartDashboard.putNumber(getFormattedName() + key, value);
	}
	
	default void putBooleanSD(String key, boolean value) {
		SmartDashboard.putBoolean(getFormattedName() + key, value);
	}
	
	default void putStringSD(String key, String value) {
		SmartDashboard.putString(getFormattedName() + key, value);
	}
	
	default void putDataSD(String key, Sendable data) {
		SmartDashboard.putData(getFormattedName() + key, data);
	}
	
	default void putDriveTrainPoseSD(DriveTrainPose pose) {
		SmartDashboard.putNumber(getFormattedName() + "LeftDistance", pose.getLeftDistance());
		SmartDashboard.putNumber(getFormattedName() + "RightDistance", pose.getRightDistance());
		SmartDashboard.putNumber(getFormattedName() + "LeftVelocity", pose.getLeftVelocity());
		SmartDashboard.putNumber(getFormattedName() + "RightVelocity", pose.getRightVelocity());
		SmartDashboard.putNumber(getFormattedName() + "Heading", pose.getHeading());
		SmartDashboard.putNumber(getFormattedName() + "HeadingVelocity", pose.getHeadingVelocity());
	}
	
}
