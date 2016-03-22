package com.team3925.robot2016.subsystems;

import com.team3925.robot2016.RobotMap;
import com.team3925.robot2016.util.SmartdashBoardLoggable;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.Subsystem;

public class IntakeAssist extends Subsystem implements SmartdashBoardLoggable {
	
	private final CANTalon wheels = RobotMap.intakeAssistWheels;
	private final CANTalon armLeft = RobotMap.intakeAssistArmLeft;
	private final CANTalon armRight = RobotMap.intakeAssistArmRight;
	
	/**
	 * In PercentVBus
	 * @param speed double from -1 to 1
	 */
	public void setWheelSpeeds(double speed) {
		wheels.set(speed);
	}
	
	/**
	 * In PercentVBus <p>
	 * <strong> <tbody> #So Janky </tbody> </strong>
	 * @param speed double from -1 to 1
	 */
	public void setArmSpeeds(double speed) {
		armLeft.set(speed);
		armRight.set(speed);
	}
	
	@Override
	public void logData() {
		putNumberSD("WheelSpeed", wheels.getSpeed());
	}

	@Override
	protected void initDefaultCommand() {
	}

	@Override
	public String getFormattedName() {
		return "IntakeAssist_";
	}
	
	
	
	
}
