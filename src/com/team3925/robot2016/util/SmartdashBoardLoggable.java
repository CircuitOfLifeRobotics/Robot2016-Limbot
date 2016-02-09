package com.team3925.robot2016.util;

import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * 	An interface for classes that put values on SmartDashboard
 * 
 * @author Bryan
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
	
}
